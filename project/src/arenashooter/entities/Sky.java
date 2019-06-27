package arenashooter.entities;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.math.Vec3f;

/**
 * World sky<br/>
 * This entity will fill the background with a vertical linear gradient when drawn
 */
public class Sky extends Entity {
	
	private static Model quad = null;
	public Material material;

	public Sky(Vec3f colorBot, Vec3f colorTop) {
		if(quad == null) quad = Model.loadQuad();
		material = Material.loadMaterial("data/materials/sky.xml");
		setColors(colorBot, colorTop);
		zIndex = -9999;
	}
	
	public Sky(Material material) {
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
	
	public Vec3f getColorBot() {
		return material.getParamVec3f("colorBot");
	}
	
	public void setColorTop(Vec3f newColorTop) {
		material.setParamVec3f("colorTop", newColorTop);
	}
	
	public Vec3f getColorTop() {
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
