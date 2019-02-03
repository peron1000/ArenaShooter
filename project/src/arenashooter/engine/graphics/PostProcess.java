package arenashooter.engine.graphics;

/**
 * Post processing settings
 */
public class PostProcess {
	private Shader shader;
	public float vignetteIntensity = 1.4f;
	public float chromaAbbIntensity = 0;

	public PostProcess(String shader) {
		this.shader = Shader.loadShader(shader);
	}
	
	Shader getShader() { return shader; }
	
	/**
	 * Bind post process shader and set parameters
	 */
	void bind() {
		shader.bind();
		shader.setUniformF("vignetteIntensity", vignetteIntensity);
		shader.setUniformF("chromaAbbIntensity", chromaAbbIntensity);
	}

}
