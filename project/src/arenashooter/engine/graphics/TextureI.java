package arenashooter.engine.graphics;

import com.github.cliftonlabs.json_simple.Jsonable;

import arenashooter.engine.math.Vec2fi;

/**
 * Container for a texture
 */
public interface TextureI extends Jsonable {
	/**
	 * Bind this texture for rendering
	 */
	public void bind();
	
	/**
	 * @return path to image file
	 */
	public String getPath();
	
	/**
	 * @return the texture's width in pixels
	 */
	public int getWidth();
	
	/**
	 * @return the texture's height in pixels
	 */
	public int getHeight();
	
	/**
	 * @return Size in pixels (width, height)
	 */
	public Vec2fi getSize();
	
	/**
	 * Set filtering on this texture (nearest or linear)
	 * @param val enable linear filtering
	 * @return <i>this</i>
	 */
	public GLTexture setFilter(boolean val);
	
	/**
	 * @return true if this texture is using filtering
	 */
	public boolean isFiltered();
	
	/**
	 * @return true if this texture uses translucency (non-1-bit transparency)
	 */
	public boolean isTranslucent();
}
