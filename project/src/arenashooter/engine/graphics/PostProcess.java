package arenashooter.engine.graphics;

import arenashooter.engine.graphics.openGL.GLRenderer;
import arenashooter.engine.graphics.openGL.GLShader;
import arenashooter.game.Main;

/**
 * Post processing settings for openGL
 */
public class PostProcess {
	private static final String vertexShader = "data/shaders/post_process/pp.vert";
	
	private GLShader shader;
	public float vignetteIntensity = 1.4f;
	public float chromaAbbIntensity = 0;
	public float fadeToBlack = 0;

	public PostProcess(String framgnetShader) {
		if(Main.getRenderer() instanceof GLRenderer) //TODO: Remove this
			this.shader = GLShader.loadShader(vertexShader, framgnetShader);
	}
	
	public Shader getShader() { return shader; }
	
	/**
	 * Bind post process shader and set parameters
	 */
	public void bind() {
		shader.bind();
		shader.setUniformF("vignetteIntensity", vignetteIntensity);
		shader.setUniformF("chromaAbbIntensity", chromaAbbIntensity);
		shader.setUniformF("fadeToBlack", fadeToBlack);
	}

}
