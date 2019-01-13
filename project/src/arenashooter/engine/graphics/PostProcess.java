package arenashooter.engine.graphics;

/**
 * Post processing settings
 */
public class PostProcess {
	
	private float vignetteIntensity = 1.4f;
	private float chromaAbbIntensity = 0;

	public PostProcess() {
	}
	
	public void setVignetteIntensity(float value) {
		value = Math.max(value, 0);
		
		if(vignetteIntensity != value) {
			vignetteIntensity = value;
			Window.postProcessShader.bind();
			Window.postProcessShader.setUniformF("vignetteIntensity", vignetteIntensity);
		}
	}
	
	public void setChromaAbbIntensity(float value) {
		value = Math.max(value, 0);
		
		if(chromaAbbIntensity != value) {
			chromaAbbIntensity = value;
			Window.postProcessShader.bind();
			Window.postProcessShader.setUniformF("chromaAbbIntensity", chromaAbbIntensity);
		}
	}

}
