package arenashooter.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.math.Vec2f;

/**
 * Container for OpenGL texture
 */
public class Texture {
	private static Map<String, TextureEntry> textures = new HashMap<String, TextureEntry>();
	
	public static final Texture default_tex = loadTexture( "data/default_texture.png" );
	
	private final int id;
	private final String file;
	private final int width, height;
	
	/** Does this texture use non-1bit transparency */
	public final boolean transparency;
	
	private Texture( String path, int id, int width, int height, boolean transparency ) {
		file = path;
		this.id = id;
		this.width = width;
		this.height = height;
		this.transparency = transparency;
	}
	
	/**
	 * Load a texture from a file
	 * @param path image file
	 * @return texture object (with filtering enabled) or the default texture if an error occurred
	 */
	public static Texture loadTexture( String path ) {
		//Check if the texture has already been loaded
		TextureEntry entry = textures.get(path);
		if( entry != null && entry.texture.get() != null )
			return entry.texture.get();
		
		Image img = Image.loadImage(path);
		
		if( img == null ) {
			Window.log.error("Cannot load texture : "+path);
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
			Window.log.error("Unsupported channel count ("+channels+") for texture : "+path);
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
		
		boolean transparency = img.transparency;
		if( pixelFormat != GL_RGBA ) transparency = false;
		Texture tex = new Texture(path, id, width, height, transparency);
		
		textures.put(path, new TextureEntry(tex));
		
		return tex;
	}
	
	/**
	 * Bind this texture for rendering
	 */
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	/**
	 * Unbind bound texture
	 */
	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * @return path to image file
	 */
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
	 * @return Size in pixels (width, height)
	 */
	public Vec2f getSize() { return new Vec2f(width, height); }
	
	/**
	 * Set filtering on this texture (nearest or linear)
	 * @param val enable linear filtering
	 * @return <i>this</i>
	 */
	public Texture setFilter(boolean val) {
		glBindTexture(GL_TEXTURE_2D, id);
		if(val) {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		} else {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}
		return this;
	}
	
	//
	//Memory management
	//
	
	/**
	 * Remove unused textures from memory
	 */
	public static void cleanTextures() { //TODO: Test
		Window.log.info("Cleaning textures...");
		
		ArrayList<String> toRemove = new ArrayList<String>(0);
		
		Texture.unbind();
		
		for ( TextureEntry entry : textures.values() ) {
		    if( entry.texture.get() == null ) { //Texture has been garbage collected
		    	toRemove.add(entry.file);
		    	
		    	glDeleteTextures(entry.id);
		    }
		}
		
		for( String s : toRemove )
			textures.remove(s);
		
		Window.log.info("Cleaned up "+toRemove.size()+" textures.");
	}
	
	private static class TextureEntry {
		int id;
		String file;
		WeakReference<Texture> texture;
		
		TextureEntry(Texture texture) {
			this.id = texture.id;
			this.file = texture.getPath();
			this.texture = new WeakReference<Texture>(texture);
		}
	}
}
