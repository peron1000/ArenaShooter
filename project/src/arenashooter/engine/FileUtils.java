package arenashooter.engine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import arenashooter.game.Main;

public final class FileUtils {
	//This class cannot be instantiated
	private FileUtils() {}
	
	/**
	 * Read a file as a string
	 * @param path
	 * @return content of the file or "" if an error occurred
	 */
	public static String resToString(String path) {
		String res = "";
		
		try {
			InputStream in = new FileInputStream(new File(path));
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inReader);
			
			String line = "";
			while( (line = reader.readLine()) != null )
					res += line+"\n"; //Include line breaks
			
			reader.close();
			inReader.close();
			in.close();
		} catch(Exception e) {
			Main.log.error("Cannot read file: "+path);
		}
		
		return res;
	}
	
	public static ByteBuffer resToByteBuffer(String path) {
		ByteBuffer res = null;
		
		InputStream in;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			in = new FileInputStream(new File(path));

			int val = in.read();
			while( val != -1 ) {
				out.write(val);
				val = in.read();
			}
			
			in.close();
		} catch (Exception e) {
			Main.log.error( "Cannot load file : "+path );
			e.printStackTrace();
			return null;
		}
		
		byte[] array = out.toByteArray();
		res = BufferUtils.createByteBuffer(array.length);
		
		for( int i=0; i<array.length; i++ )
			res.put(array[i]);
		
		res.flip();
		
		return res;
	}
	
}
