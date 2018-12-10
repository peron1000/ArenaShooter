package arenashooter.engine.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Image {

	public final String file;
	public final int width, height;
	public final ByteBuffer buffer;
	
	private Image(String path, int width, int height, ByteBuffer buffer) {
		this.file = path;
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}
	
	public static Image loadImage(String path) { //TODO: replace PNGDecoder with STBImage
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
