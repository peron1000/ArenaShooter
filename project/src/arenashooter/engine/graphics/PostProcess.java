package arenashooter.engine.graphics;

/**
 * Post processing settings
 */
public class PostProcess {
	private static final String vertexShader = "data/shaders/post_process/pp.vert";
	
	private Shader shader;
	public float vignetteIntensity = 1.4f;
	public float chromaAbbIntensity = 0;
	public float fadeToBlack = 0;

	public PostProcess(String framgnetShader) {
		this.shader = Shader.loadShader(vertexShader, framgnetShader);
	}
	
	Shader getShader() { return shader; }
	
	/**
	 * Bind post process shader and set parameters
	 */
	void bind() {
		shader.bind();
		shader.setUniformF("vignetteIntensity", vignetteIntensity);
		shader.setUniformF("chromaAbbIntensity", chromaAbbIntensity);
		shader.setUniformF("fadeToBlack", fadeToBlack);
	}

}
