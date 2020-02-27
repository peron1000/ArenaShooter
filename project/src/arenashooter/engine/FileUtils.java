package arenashooter.engine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.lwjgl.BufferUtils;

import arenashooter.game.Main;

public final class FileUtils {
	//This class cannot be instantiated
	private FileUtils() {}
	
	private static final String userHome = System.getProperty("user.home");
	
	private static File userDir = null;
	private static Path userDirPath = null;

	public static void setUserDir(Path userDir) {
		try {
			Files.createDirectories(userDir);
		} catch (IOException e) {
			Main.log.fatal("Could not create user directory", e);
			System.exit(1);
		}

		// Not allowed to write in user dir
		if( !Files.isWritable(userDir) ) {
			Main.log.fatal("Cannot write in user directory");
			System.exit(1);
		}

		userDirPath = userDir;
		FileUtils.userDir = userDir.toFile();
		
		Main.log.info("User directory set to: "+userDirPath);
	}
	
	/**
	 * @return default user directory, based on OS
	 */
	public static Path getDefaultUserDir() {
		String userDirName = "super-blep";
		Path userDir;

		switch( OSUtils.os ) {
		case LINUX:
			userDir = Paths.get(userHome, ".config", userDirName);
			break;
		default:
			userDir = Paths.get(userHome, userDirName);
			break;
		}
		return userDir;
	}

	/**
	 * @return current user directory, or null if not set
	 */
	public static Path getUserDir() {
		return userDirPath;
	}
	
	public static File getUserDirFile() {
		return userDir;
	}
	
	public static String getName(String path) {
		return Paths.get(path).getFileName().toString();
	}
	
	public static String[] listJavaRes(String folderPath) { // TODO: Clean this mess
		if(folderPath.startsWith("/"))
			folderPath = folderPath.substring(1);
		if(!folderPath.endsWith("/"))
			folderPath += "/";
		try {
			String[] paths = getResourceListing(folderPath);
			return paths;
		} catch (URISyntaxException e) {
			Main.log.error(e);
		} catch (IOException e) {
			Main.log.error(e);
		}
		
	    Main.log.error("Unable to list java resources in "+folderPath);
	    return new String[0];
	}
	
	/**
	   * List directory contents for a resource folder. Not recursive. 
	   * This is basically a brute-force implementation. 
	   * Works for regular files and also JARs. 
	   * From http://www.uofr.net/~greg/java/get-resource-listing.html
	   * 
	   * @author Greg Briggs
	   * @param path Should end with "/", but not start with one.
	   * @return Just the name of each member item, not the full paths.
	   * @throws URISyntaxException 
	   * @throws IOException 
	   */
	private static String[] getResourceListing(String path) throws URISyntaxException, IOException {
		URL dirURL = FileUtils.class.getClassLoader().getResource(path);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			return new File(dirURL.toURI()).list();
		} 

		if (dirURL == null) {
			/* 
			 * In case of a jar file, we can't actually find a directory.
			 * Have to assume the same jar as clazz.
			 */
			String me = FileUtils.class.getName().replace(".", "/")+".class";
			dirURL = FileUtils.class.getClassLoader().getResource(me);
		}

		if (dirURL.getProtocol().equals("jar")) {
			/* A JAR path */
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
			while(entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(path)) { //filter according to the path
					String entry = name.substring(path.length());
					int checkSubdir = entry.indexOf("/");
					if (checkSubdir >= 0) {
						// if it is a subdirectory, we just return the directory name
						entry = entry.substring(0, checkSubdir);
					}
					result.add(entry);
				}
			}
			jar.close();
			return result.toArray(new String[result.size()]);
		} 

		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	}
	
	/**
	 * Read a file as a string
	 * @param path
	 * @return content of the file or "" if an error occurred
	 */
	public static String resToString(String path) {
		String res = "";

		try( InputStream in = ContentManager.getRes(path) ) {
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inReader);

			String line = "";
			while( (line = reader.readLine()) != null )
				res += line+"\n"; //Include line breaks

			reader.close();
			inReader.close();
		} catch(Exception e) {
			Main.log.error("Cannot read file: "+path);
		}

		return res;
	}
	
	public static ByteBuffer resToByteBuffer(String path) {
		ByteBuffer res = null;

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try( InputStream in = ContentManager.getRes(path) ) {
			int val = in.read();
			while( val != -1 ) {
				out.write(val);
				val = in.read();
			}
		} catch (Exception e) {
			Main.log.error( "Cannot load file : "+path, e );
			e.printStackTrace();
			return null;
		}

		byte[] array = out.toByteArray();
		res = BufferUtils.createByteBuffer(array.length);

		for( int i=0; i<array.length; i++ )
			res.put(array[i]);

		((Buffer)res).flip();

		return res;
	}
	
	/**
	 * Get a list of all files ending with a specific String
	 * @param parent directory used as root
	 * @param endsWith only files ending with this will be returned
	 * @return list of all matches (sorted by path)
	 */
	public static List<File> listFilesByType(File parent, String endsWith) {
		if(parent == null) {
			Main.log.error("Null file, returning an empty list!");
			return Collections.emptyList();
		}
		List<File> res = listFilesByTypeAux(parent, endsWith);

		res.sort(new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.compareTo(o2);
			}
		});

		return res;
	}

	/**
	 * Auxiliary method for listFilesByType()
	 */
	private static List<File> listFilesByTypeAux(File parent, String endsWith) {
		List<File> res = new LinkedList<>();
		for (File file : parent.listFiles()) {
			if (file.isDirectory())
				res.addAll(listFilesByTypeAux(file, endsWith));
			else if (file.getName().endsWith(endsWith))
				res.add(file);
		}
		return res;
	}

}
