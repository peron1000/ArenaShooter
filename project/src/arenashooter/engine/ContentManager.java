package arenashooter.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private static Map<String, ModDescriptor> mods = new HashMap<>();

	public static void scanMods() {
		Path modsDirP = FileUtils.getUserDirPath().resolve("mods");
		
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
	}
	
	/**
	 * Test if the provided path is a mod, 
	 * otherwhise look if it contains a mod
	 * @param dir directory to explore
	 * @param depth current exploration depth
	 */
	private static void searchMods(Path dir, int depth) {
		if(depth > MAX_MOD_DISCOVERY_DEPTH) return;
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
	
	private static void readLoadOrderFromDisk() {
		log.info("Loading mods load order...");
		
		Path loadOrderFP = FileUtils.getUserDirPath().resolve("modsLoadOrder.txt");
		if( Files.isRegularFile(loadOrderFP) ) {
			if( Files.isReadable(loadOrderFP) ) {
				try( BufferedReader reader = Files.newBufferedReader(loadOrderFP, StandardCharsets.UTF_8) ) {
					loadOrder.clear();
					String line;
					while( (line = reader.readLine()) != null) {
						if( !line.startsWith("#") )
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
