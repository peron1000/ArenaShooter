package arenashooter.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	
	private static final Texture default_tex = loadTexture( "data/default_texture.png" );;
	
	private final int id;
	private final String file;
	private final int width, height;
	
	private Texture( String path, int id, int width, int height ) {
		file = path;
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Load a texture from a file
	 * @param path image file
	 * @return texture object (with filtering enabled) or the default texture if an error occurred
	 */
	public static Texture loadTexture( String path ) {
		Image img = Image.loadImage(path);
		
		if( img == null ) {
			System.err.println( "Render - Cannot load texture : "+path );
			return default_tex;
		}
		
		int channels = img.channels;
		
		int pixelFormat;
		
		switch(channels) {
//		case 1:
//			pixelFormat = GL_RED;
//			break;
		case 3:
			pixelFormat = GL_RGB;
			break;
		case 4:
			pixelFormat = GL_RGBA;
			break;
		default:
			System.err.println("Render - Unsupported channel count ("+channels+") for texture : "+path);
			return default_tex;
		}
		
		int width = img.width;
		int height = img.height;
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, pixelFormat, width, height, 0, pixelFormat, GL_UNSIGNED_BYTE, img.buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		unbind();		
		
		return new Texture(path, id, width, height);
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
	
	/**
	 * Set filtering on this texture (nearest or linear)
	 * @param val enable linear filtering
	 */
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
