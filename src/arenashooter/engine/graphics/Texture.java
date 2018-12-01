package arenashooter.engine.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Texture {
	
	private int id;
	private String file;
	private int width, height;
	
	public Texture( String path ) {
		file = path;
		loadTexture( path );
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public String getPath() {
		return file;
	}
	
	/**
	 * @return the texture's width in pixels
	 */
	public int getWidth() { return width; }
	
	/**
	 * @return the texture's height in pixels
	 */
	public int getHeight() { return height; }
	
	private void loadTexture( String path ) {
		InputStream in = null;
		
		try {
			in = ClassLoader.getSystemResourceAsStream( path );
			PNGDecoder decoder = new PNGDecoder(in);

			ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
			decoder.decode(buf, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
			buf.flip();

			width = decoder.getWidth();
			height = decoder.getHeight();
			
			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			in.close();
		} catch (Exception e) {
			System.err.println( "Can't load texture: "+path );
			e.printStackTrace();
		}

		if( in != null )
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		unbind();
	}
	
	public void setFilter(boolean val) {
		glBindTexture(GL_TEXTURE_2D, id);
		if(val) {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		} else {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}
	}
}
