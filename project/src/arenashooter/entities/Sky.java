package arenashooter.entities;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.MaterialI;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;

/**
 * World sky<br/>
 * This entity will fill the background with a vertical linear gradient when drawn
 */
public class Sky extends Entity {
	
	private static Model quad = null;
	public MaterialI material;

	public Sky(Vec3f colorBot, Vec3f colorTop) {
		if(quad == null) quad = Model.loadQuad();
		material = Window.loadMaterial("data/materials/sky.xml");
		setColors(colorBot, colorTop);
		zIndex = -9999;
	}
	
	public Sky(MaterialI material) {
		if(quad == null) quad = Model.loadQuad();
		this.material = material;
		zIndex = -9999;
	}
	
	public void setColors(Vec3f newColorBot, Vec3f newColorTop) {
		setColorBot(newColorBot);
		setColorTop(newColorTop);
	}
	
	public void setColorBot(Vec3f newColorBot) {
		material.setParamVec3f("colorBot", newColorBot);
	}
	
	public Vec3fi getColorBot() {
		return material.getParamVec3f("colorBot");
	}
	
	public void setColorTop(Vec3f newColorTop) {
		material.setParamVec3f("colorTop", newColorTop);
	}
	
	public Vec3fi getColorTop() {
		return material.getParamVec3f("colorTop");
	}

	@Override
	public void draw(boolean transparency) {
		Profiler.startTimer(Profiler.SPRITES);
		
		if(material.bind(quad)) {
			quad.bind();
			quad.draw();
		}
		
		Profiler.endTimer(Profiler.SPRITES);
	}
	
	/**
	 * Creates a clone of this Sky (cloned material)
	 */
	@Override
	public Sky clone() {
		return new Sky(material.clone());
	}
	
}
