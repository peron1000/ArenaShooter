package arenashooter.engine.graphics;

/**
 * Contains geometry data for a single model 
 * Doesn't have any shader or texture information
 */
public interface Model {
	/**
	 * Send this model's data to a shader
	 * @param shader
	 */
	public void bindToShader( Shader shader );
	
	/**
	 * Bind this model for drawing. Only one bind() is required if you want to draw this model multiple times.
	 */
	public void bind();
	
	/**
	 * Draw this model. Make sure bind() was called before.
	 */
	public void draw();
	
	/**
	 * Draw this model. Make sure bind() was called before.
	 * @param wireframe draw this model as wireframe
	 */
	public void draw(boolean wireframe);
	
}
