package arenashooter.engine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;

import arenashooter.game.Main;

public final class FileUtils {
	//This class cannot be instantiated
	private FileUtils() {}
	
	private static final String userHome = System.getProperty("user.home");
	
	private static File userDir;
	private static Path userDirPath;

	static {
		// Initialize user directory to the default one
		setUserDir( getDefaultUserDir() );
	}
	
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

	public static File getUserDir() {
		return userDir;
	}
	
	public static Path getUserDirPath() {
		return userDirPath;
	}

	/**
	 * Read a file as a string
	 * @param path
	 * @return content of the file or "" if an error occurred
	 */
	public static String resToString(String path) {
		String res = "";

		try( InputStream in = new FileInputStream(new File(path)) ) {
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

		try( InputStream in = new FileInputStream(new File(path)) ) {
			int val = in.read();
			while( val != -1 ) {
				out.write(val);
				val = in.read();
			}
		} catch (Exception e) {
			Main.log.error( "Cannot load file : "+path );
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
