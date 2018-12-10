package arenashooter.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import arenashooter.engine.graphics.Image;
import de.matthiasmann.twl.utils.PNGDecoder;

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
	
	public static Image loadImage(String path) {
		Image res = null;
		InputStream in = null;
		ByteBuffer buf = null;
		int width, height;
		
		try {
			in = ClassLoader.getSystemResourceAsStream( path );
			
			if( in == null ) {
				throw new IOException();
			}
			
			PNGDecoder decoder = new PNGDecoder(in);
			
			width = decoder.getWidth();
			height = decoder.getHeight();

			buf = ByteBuffer.allocateDirect(4*width*height);
			decoder.decode(buf, width*4, PNGDecoder.Format.RGBA);
			
			buf.flip();
			
			res = new Image(path, width, height, buf);

		} catch (Exception e) {
			System.err.println( "Render - Can't load image : "+path );
			e.printStackTrace();
		}

		if( in != null ) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
}
