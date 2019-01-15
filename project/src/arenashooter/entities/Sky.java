package arenashooter.entities;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.math.Vec3f;

/**
 * World sky<br/>
 * This entity will fill the background with a vertical linear gradient when drawn
 */
public class Sky extends Entity {
	
	private static Model quad = null;
	private static Shader shader = null;
	private Vec3f colorBot = new Vec3f(), colorTop = new Vec3f();

	public Sky(Vec3f colorBot, Vec3f colorTop) {
		if(quad == null) quad = Model.loadQuad();
		if(shader == null) shader = new Shader("data/shaders/sky");
		setColors(colorBot, colorTop);
	}
	
	public void setColors(Vec3f newColorBot, Vec3f newColorTop) {
		colorBot.set(newColorBot);
		colorTop.set(newColorTop);
		
		shader.bind();
		shader.setUniformV3("colorBot", colorBot);
		shader.setUniformV3("colorTop", colorTop);
	}
	
	public void setColorBot(Vec3f newColorBot) {
		colorBot.set(newColorBot);

		shader.bind();
		shader.setUniformV3("colorBot", colorBot);
	}
	
	public void setColorTop(Vec3f newColorTop) {
		colorTop.set(newColorTop);

		shader.bind();
		shader.setUniformV3("colorTop", colorTop);
	}

	@Override
	public void draw() {
		super.draw();
		
		Profiler.startTimer(Profiler.SPRITES);
		
		shader.bind();
		quad.bindToShader(shader);
		quad.bind();
		quad.draw();
		
		Profiler.endTimer(Profiler.SPRITES);
	}
	
}
