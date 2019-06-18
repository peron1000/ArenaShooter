package arenashooter.entities.spatials;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;

public class LightContainer extends Spatial3 implements IAnimated {
	private final Light light;
	
	private Mesh editorSprite;
	private Material editorSpriteMat;
	
	private Animation anim;

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
	public void step(double d) {
		//Animation
		if(anim != null) {
			anim.step(d);
			
			if(anim.hasTrackVec3f("pos"))
				localPosition.set( anim.getTrackVec3f("pos") );
			
			if(anim.hasTrackVec3f("rot"))
				Quat.fromEuler( anim.getTrackVec3f("rot"), localRotation );
			
			if(anim.hasTrackD("radius"))
				light.radius = (float) anim.getTrackD("radius");

			if(anim.hasTrackVec3f("color"))
				light.color.set(anim.getTrackVec3f("color"));
		}
		
		super.step(d);
	}
	
	@Override
	public void draw() {
		updateLight();
		super.draw();
	}
	
	@Override
	public void editorDraw() {
		updateLight();
		
		if(editorSprite == null) {
			editorSpriteMat = Material.loadMaterial("data/materials/sprite_simple.xml");
			editorSpriteMat.setParamTex("baseColor", Texture.loadTexture("data/sprites/icon_light.png").setFilter(false));
			editorSprite = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(1), editorSpriteMat);
		}
		editorSpriteMat.setParamVec4f("baseColorMod", new Vec4f(light.color.x,light.color.y,light.color.z,1));
		editorSprite.localPosition.set(getWorldPos());
		editorSprite.localRotation.set(getWorldRot());
		editorSprite.draw();
	}

	@Override
	public void setAnim(Animation anim) {
		this.anim = anim;
	}

	@Override
	public void playAnim() {
		if(anim != null)
			anim.play();
	}

	@Override
	public void stopAnim() {
		if(anim != null)
			anim.stopPlaying();
	}

	@Override
	public void animJumpToEnd() {
		if(anim != null)
			anim.setTime(anim.getLength());
	}

	@Override
	public Animation getAnim() {
		return anim;
	}

	@Override
	public void setAnimSpeed(double speed) {
		// TODO Auto-generated method stub
	}

	@Override
	public double getAnimSpeed() {
		// TODO Auto-generated method stub
		return 1;
	}
}
