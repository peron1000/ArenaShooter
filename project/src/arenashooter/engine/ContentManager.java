package arenashooter.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import arenashooter.game.Main;

/**
 * Manages game content and mods
 */
public final class ContentManager {
	private static final Logger log = LogManager.getLogger("Content");
	
	private static final int MAX_MOD_DISCOVERY_DEPTH = 32;
	
	private static final String MOD_FILE = "blepmod.json";
	private static final String INVALID_MOD_ID = "!!!INVALID ID!!!";
	
	private static final String loadOrderHeader = "# This file lists mods with descending priority.\r\n"
	+ "# This means that the highest mod will override content from the lower ones.\r\n"
	+ "# If a mod is not listed here, the game will add it to the top of the list.\r\n"
	+ "# If a mod from this list does not exist, it will not be removed from the file.";

	private ContentManager() {}
	
	private static List<String> loadOrder = new ArrayList<>();
	
	/** Mods found in user directory */
	private static Map<String, ModDescriptor> mods = new HashMap<>();
	
	private static Map<String, String> contentOverride = new HashMap<>();

	public static void scanMods() {
		Path modsDirP = FileUtils.getUserDir().resolve("mods");
		
		try {
			Files.createDirectories(modsDirP);
		} catch (IOException e) {
			Main.log.fatal("Could not create mods directory", e);
			return;
		}

		// Not allowed to read in mods dir
		if( !Files.isReadable(modsDirP) ) {
			Main.log.error("Cannot read in mods directory");
			return;
		}
		
		// Read or create load order file
		readLoadOrderFromDisk();
		
		log.info("Discovering mods...");
		
		mods.clear();
		try( DirectoryStream<Path> dirs = Files.newDirectoryStream(modsDirP) ) {
			dirs.forEach( (p) -> searchMods(p, 0) );
		} catch (IOException e) {
			log.error(e);
		}
		
		loadMods();

		saveLoadOrderToDisk();
	}
	
	/**
	 * Get the real path to a resource, using content override. 
	 * If the path is overridden by a mod, it will return its global path. 
	 * Otherwise, it will be returned unchanged
	 * @param path
	 * @return
	 */
	public static String transformPath(String path) {
		return contentOverride.getOrDefault(path, path);
	}
	
	/**
	 * Get a game resource as an InputStream. 
	 * This will take content override (mods) in account
	 * @param path resource path
	 * @return an InputStream or null is something went wrong
	 */
	public static InputStream getRes(String path) {
		String moddedPath = contentOverride.get(path);
		try {
			InputStream res = null;
			if(moddedPath == null)
				res = ClassLoader.getSystemResourceAsStream(path);
			else
				res = new FileInputStream(new File(moddedPath));
			return res;
		} catch(Exception e) {
			Main.log.error(e);
			return null;
		}
	}
	
	/**
	 * Tests if a file exists as a game resource
	 * @param path
	 * @return
	 */
	public static boolean resExists(String path) {
		String moddedPath = contentOverride.get(path);
		if(moddedPath == null)
			return ClassLoader.getSystemResource(path) != null;
		else
			return Files.isRegularFile(Paths.get(path));
	}
	
	public static List<String> listRes(String folderPath) {
		List<String> res = new ArrayList<>();
		
		String[] javaRes = FileUtils.listJavaRes(folderPath); // TODO: Look in mods too
		for(String f : javaRes) {
			res.add( folderPath+"/"+f );
		}
		
		return res;
	}
	
	/**
	 * Test if the provided path is a mod, 
	 * if it is, load it, 
	 * otherwhise look if it contains mods
	 * @param dir directory to explore
	 * @param depth current exploration depth
	 */
	private static void searchMods(Path dir, int depth) {
		if(depth > MAX_MOD_DISCOVERY_DEPTH) {
			log.warn("Max directory depth exceeded while searching for mods!\n"
					+ "The content of "+dir+" will be ignored");
			return;
		}
		if( !Files.isDirectory(dir) ) return;
		
		Path modP = dir.resolve(MOD_FILE);
		
		if( Files.isRegularFile(modP) ) { // This directory is a mod
			ModDescriptor modDesc = readModFile(modP);
			if(modDesc == null) return;
			
			log.info("Found mod \""+modDesc.name+"\" v"+modDesc.version+" by "+modDesc.author+" in "+modDesc.path);
			
			// Name id conflict
			if(mods.containsKey(modDesc.id)) {
				log.error( "Conflicting mod IDs: "+ mods.get(modDesc.id).path + " and " + modDesc.path
						+ "\n" + modDesc.path + " will not be loaded. Please contact the authors of both mods about this.");
				return;
			}
			
			mods.put(modDesc.id, modDesc);
			
		} else { // This directory is not a mod, keep looking
			try( DirectoryStream<Path> dirs = Files.newDirectoryStream(dir) ) {
				dirs.forEach( (p) -> searchMods(p, depth+1) );
			} catch (IOException e) {
				log.error(e);
			}
		}
	}
	
	private static ModDescriptor readModFile(Path file) {
		String modId, modName, modAuthor, modDesc, modVer;

		try( BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8) ) {
			Object obj = Jsoner.deserialize(reader);
			if( !(obj instanceof JsonObject) ) {
				log.error( "Malformed mod descriptor: "+file.toString() + ", ignoring this mod" );
				return null;
			}
			
			JsonObject json = (JsonObject)obj;
			
			modId = json.getStringOrDefault(modDescId);
			if(modId.equals(INVALID_MOD_ID)) {
				log.error("Invalid or missing id for mod "+file.toString());
				return null;
			}
			
			if( modId.startsWith("#") ) {
				log.error("Invalid mod id: "+modId+" (id cannot start with a '#')");
				return null;
			}
			
			modName = json.getStringOrDefault(modDescName);
			modAuthor = json.getStringOrDefault(modDescAuthor);
			modDesc = json.getStringOrDefault(modDescDescription);
			modVer = json.getStringOrDefault(modDescVersion);
			
		} catch (IOException e) {
			log.error(e);
			return null;
		} catch (JsonException e1) {
			log.error("Cannot parse mod descriptor "+file+":\n"+e1);
			return null;
		}
		
		return new ModDescriptor( modId, file.getParent(), modName, modDesc, modAuthor, modVer );
	}
	
	/**
	 * Load all mods contained in the mods Map, filling the content override map according to the load order. 
	 * If a mod is missing from the load order, it will be added at its top. 
	 * If a mod is present in the load order but not in the mod Map, it is left where it is. 
	 */
	private static void loadMods() {
		contentOverride.clear();
		
		Set<String> remaining = new HashSet<>(mods.keySet());
		
		// Iterate backwards to ensure correct load order
		for(int i=loadOrder.size()-1; i>=0; i--) {
			if(mods.containsKey(loadOrder.get(i))) {
				addContentOverride( mods.get(loadOrder.get(i)).path, contentOverride );
				remaining.remove(loadOrder.get(i));
			}
		}
		
		// Add missing mods to load order and load them
		for(String mod : remaining) {
			addContentOverride( mods.get(mod).path, contentOverride );
			loadOrder.add(0, mod);
		}
	}
	
	/**
	 * Fills a content override map with data from a given mod
	 * @param modPath path to this mod
	 * @param current current content override map
	 */
	private static void addContentOverride(Path modPath, Map<String, String> currentOverride) {
		try {
			Files.walkFileTree(modPath, new FileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if(!file.getFileName().toString().equals(MOD_FILE))
						currentOverride.put( modPath.relativize(file).toString(), file.toString() );
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					log.error("Error accessing mod file"+file.toString(), exc);
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	private static void readLoadOrderFromDisk() {
		log.info("Loading mods load order...");
		
		Path loadOrderFP = FileUtils.getUserDir().resolve("modsLoadOrder.txt");
		if( Files.isRegularFile(loadOrderFP) ) {
			if( Files.isReadable(loadOrderFP) ) {
				try( BufferedReader reader = Files.newBufferedReader(loadOrderFP, StandardCharsets.UTF_8) ) {
					loadOrder.clear();
					String line;
					while( (line = reader.readLine()) != null) {
						if( !line.startsWith("#") && !line.isEmpty() )
							loadOrder.add(line);
					}
				} catch (IOException e) {
					log.error(e);
				}
			} else {
				log.error("Cannot read load order file.");
				return;
			}
		} else {
			if( Files.notExists(loadOrderFP) ) {
				try {
					log.info("Load order file does not exist and will be created.");
					Files.createFile(loadOrderFP);
					Files.write(loadOrderFP, loadOrderHeader.getBytes(StandardCharsets.UTF_8));
				} catch (IOException e) {
					log.error(e);
				}
			} else {
				log.error("Load order file is not a file.");
			}
		}
	}
	
	private static void saveLoadOrderToDisk() {
		log.info("Saving mods load order...");
		
		Path loadOrderFP = FileUtils.getUserDir().resolve("modsLoadOrder.txt");
		
		try(BufferedWriter writer = Files.newBufferedWriter(loadOrderFP, StandardCharsets.UTF_8)) {
			writer.append(loadOrderHeader);
			for(String mod : loadOrder)
				writer.append("\r\n"+mod);
			writer.append("\r\n");
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	private static final JsonKey modDescId = new JsonKey(){
		@Override
		public String getKey(){
			return "id";
		}
		@Override
		public String getValue(){
			return INVALID_MOD_ID;
		}
	};
	private static final JsonKey modDescName = new JsonKey(){
		@Override
		public String getKey(){
			return "name";
		}
		@Override
		public String getValue(){
			return "-Untitled mod-";
		}
	};
	private static final JsonKey modDescAuthor = new JsonKey(){
		@Override
		public String getKey(){
			return "author";
		}
		@Override
		public String getValue(){
			return "-Anon-";
		}
	};
	private static final JsonKey modDescDescription = new JsonKey(){
		@Override
		public String getKey(){
			return "description";
		}
		@Override
		public String getValue(){
			return "-No description-";
		}
	};
	private static final JsonKey modDescVersion = new JsonKey(){
		@Override
		public String getKey(){
			return "version";
		}
		@Override
		public String getValue(){
			return "-Unknown version-";
		}
	};
	
	public static class ModDescriptor {
		final String id, name, desc, author, version;
		final Path path;
		
		private ModDescriptor(String id, Path path, String name, String desc, String author, String version) {
			this.id = id;
			this.path = path;
			this.name = name;
			this.desc = desc;
			this.author = author;
			this.version = version;
		}
	}
}
