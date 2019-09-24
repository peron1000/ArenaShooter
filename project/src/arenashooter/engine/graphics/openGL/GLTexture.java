package arenashooter.engine.graphics.openGL;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.Image;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;

/**
 * Container for an OpenGL texture
 */
final class GLTexture implements Texture {
	private static Map<String, TextureEntry> textures = new HashMap<String, TextureEntry>();
	
	public static final Texture default_tex = loadTexture( "data/default_texture.png" );
	
	private boolean ready = false;
	private Image img;
	private int pixelFormat;
	
	private int id;
	private final String file;
	private final int width, height;
	private boolean filter = true;
	private boolean filterTarget = true;
	
	/** Does this texture use non-1bit transparency */
	private final boolean transparency;
	
	private GLTexture( String path, int id, int width, int height, boolean transparency ) {
		file = path;
		this.id = id;
		this.width = width;
		this.height = height;
		this.transparency = transparency;
	}
	
	/**
	 * Load a texture from a file <br/>
	 * This is safe to call from any thread
	 * @param path image file
	 * @return texture object (with filtering enabled) or the default texture if an error occurred
	 */
	public static Texture loadTexture( String path ) { //TODO: remove public modifier
		//Check if the texture has already been loaded
		TextureEntry entry = textures.get(path);
		if( entry != null && entry.texture.get() != null )
			return entry.texture.get();
		
		Image img = Image.loadImage(path);
		
		if( img == null ) {
			GLRenderer.log.error("Cannot load texture : "+path);
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
			GLRenderer.log.error("Unsupported channel count ("+channels+") for texture : "+path);
			return default_tex;
		}
		
		int width = img.width;
		int height = img.height;
		
		boolean transparency = img.transparency;
		if( pixelFormat != GL_RGBA ) transparency = false;
		GLTexture tex = new GLTexture(path, -2, width, height, transparency);
		
		tex.img = img;
		tex.pixelFormat = pixelFormat;
		
		textures.put(path, new TextureEntry(tex));
		
		return tex;
	}
	
	/**
	 * Only call this from a thread with an opengl context
	 */
	private void initTexture() {
		if(ready) return;
		ready = true;
		
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, pixelFormat, width, height, 0, pixelFormat, GL_UNSIGNED_BYTE, img.buffer);

		if(filterTarget) {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		} else {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}
		filter = filterTarget;
		
		unbind();
		img = null;
	}
	
	@Override
	public void bind() {
		if(!ready) initTexture();
		glBindTexture(GL_TEXTURE_2D, id);
		
		//Update texture filtering
		if(filter != filterTarget) {
			if(filterTarget) {
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			} else {
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			}
			filter = filterTarget;
		}
	}
	
	/**
	 * Unbind bound texture
	 */
	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	@Override
	public String getPath() {
		return file;
	}
	
	@Override
	public int getWidth() { return width; }
	
	@Override
	public int getHeight() { return height; }
	
	@Override
	public Vec2fi getSize() { return new Vec2f(width, height); }
	
	@Override
	public Texture setFilter(boolean val) {
		filterTarget = val;
		return this;
	}
	
	@Override
	public boolean isFiltered() {
		return filterTarget;
	}

	@Override
	public boolean isTranslucent() {
		return transparency;
	}
	
	//
	//Memory management
	//
	
	/**
	 * Remove unused textures from memory
	 */
	public static void cleanTextures() { //TODO: Test
		GLRenderer.log.info("Cleaning textures...");
		
		List<String> toRemove = new ArrayList<String>(0);
		
		GLTexture.unbind();
		
		for ( TextureEntry entry : textures.values() ) {
		    if( entry.texture.get() == null ) { //Texture has been garbage collected
		    	toRemove.add(entry.file);
		    	
		    	glDeleteTextures(entry.id);
		    }
		}
		
		for( String s : toRemove )
			textures.remove(s);
		
		GLRenderer.log.info("Cleaned up "+toRemove.size()+" textures.");
	}
	
	private static class TextureEntry {
		int id;
		String file;
		WeakReference<GLTexture> texture;
		
		TextureEntry(GLTexture texture) {
			this.id = texture.id;
			this.file = texture.getPath();
			this.texture = new WeakReference<GLTexture>(texture);
		}
	}
	
	
	/*
	 * JSON
	 */
	
	@Override
	public String toJson() {
		return new JsonObject().putChain("path", getPath()).putChain("filtered", filterTarget).toJson();
	}

	@Override
	public void toJson(Writer writable) throws IOException {
		new JsonObject().putChain("path", getPath()).putChain("filtered", filterTarget).toJson(writable);
	}
}
