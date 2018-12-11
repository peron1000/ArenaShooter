package arenashooter.engine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class FileUtils {
	
	/**
	 * Read a resource file as a string
	 * @param path resource path
	 * @return content of the resource or "" if an error occured
	 */
	public static String resToString(String path) {
		String res = "";
		
		InputStream in = ClassLoader.getSystemResourceAsStream(path);
		
		if( in != null) {
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inReader);
			
			String line = "";
			try {
				while( (line = reader.readLine()) != null ) {
					res += line+"\n"; //Include line breaks
//					res += line; //Ignore line breaks
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				reader.close();
				inReader.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Can't find resource: "+path);
		}
		
		return res;
	}
	
	public static ByteBuffer resToByteBuffer(String path) { //TODO: Test this
		ByteBuffer res = null;
		
		InputStream in;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			in = ClassLoader.getSystemResourceAsStream(path);

			if( in == null ) {
				throw new IOException();
			}
			
			int val = in.read();
			while( val != -1 ) {
				out.write(val);
				val = in.read();
			}
			
			in.close();
		} catch (Exception e) {
			System.err.println( "Can't load file : "+path );
			e.printStackTrace();
		}
		
		byte[] array = out.toByteArray();
		res = BufferUtils.createByteBuffer(array.length);
		
		for( int i=0; i<array.length; i++ )
			res.put(array[i]);
		
		res.flip();
		
		return res;
	}
	
}
