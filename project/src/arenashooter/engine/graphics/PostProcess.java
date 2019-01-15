package arenashooter.engine.graphics;

/**
 * Post processing settings
 */
public class PostProcess {
	private Shader shader;
	private float vignetteIntensity = 1.4f;
	private float chromaAbbIntensity = 0;

	public PostProcess(String shader) {
		this.shader = new Shader(shader);
	}
	
	public float getVignetteIntensity() { return vignetteIntensity; }
	
	public void setVignetteIntensity(float value) {
		value = Math.max(value, 0);
		
		if(vignetteIntensity != value) {
			vignetteIntensity = value;
			shader.bind();
			shader.setUniformF("vignetteIntensity", vignetteIntensity);
		}
	}
	
	public float getChromaAbbIntensity() { return chromaAbbIntensity; }
	
	public void setChromaAbbIntensity(float value) {
		value = Math.max(value, 0);
		
		if(chromaAbbIntensity != value) {
			chromaAbbIntensity = value;
			shader.bind();
			shader.setUniformF("chromaAbbIntensity", chromaAbbIntensity);
		}
	}
	
	Shader getShader() { return shader; }

}
