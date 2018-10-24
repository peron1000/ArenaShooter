package arenashooter.engine.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Texture {
	
	private int id;
	private String file;
	
	public Texture( String path ) {
		id = loadTexture( path );
		file = path;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public String getPath() {
		return file;
	}
	
	private static int loadTexture( String path ) {
		int texture = 0;
		InputStream in = null;
		
		try {
			in = ClassLoader.getSystemResourceAsStream( path );
			PNGDecoder decoder = new PNGDecoder(in);

			ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
			decoder.decode(buf, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
			buf.flip();

			texture = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texture);
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
		
		return texture;
	}
}
