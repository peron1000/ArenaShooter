package arenashooter.entities.spatials;

import arenashooter.engine.graphics.Light;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;

public class LightContainer extends Spatial3 {
	private final Light light;
	
	private Mesh editorSprite;
	private Material editorSpriteMat;

	public LightContainer(Vec3f localPosition, Light light) {
		super(localPosition);
		this.light = light;
	}
	
	public Light getLight() { return light; }
	
	@Override
	protected void recursiveAttach(Entity newParent) {
		super.recursiveAttach(newParent);
		
		if(getArena() != null) getArena().lights.add(light);
	}

	@Override
	protected void recursiveDetach(Arena oldArena) {
		super.recursiveDetach(oldArena);
		if(oldArena != null) oldArena.lights.remove(light);
	}
	
	@Override
	public void updateAttachment() {
		super.updateAttachment();
		updateLight();
	}
	
	@Override
	public void step(double d) {
		super.step(d);
		updateLight();
	}

	private void updateLight() {
		switch(light.getType()) {
		case POINT:
			light.position.set(getWorldPos());
			break;
		case DIRECTIONAL:
			light.position.set(getWorldRot().forward());
			break;
		default:
			break;
		}
	}
	
	@Override
	public void editorDraw() {
		if(editorSprite == null) {
			editorSpriteMat = new Material("data/shaders/sprite_simple");
			editorSpriteMat.setParamTex("baseColor", Texture.loadTexture("data/sprites/icon_light.png").setFilter(false));
			editorSprite = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(1), editorSpriteMat);
		}
		editorSpriteMat.setParamVec4f("baseColorMod", new Vec4f(light.color.x,light.color.y,light.color.z,1));
		editorSprite.localPosition.set(getWorldPos());
		editorSprite.localRotation.set(getWorldRot());
		editorSprite.draw();
	}
}
