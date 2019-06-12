package arenashooter.entities.spatials;

import arenashooter.engine.graphics.Light;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;

public class LightContainer extends Spatial3 {
	private final Light light;

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
		if(light.radius > 0) { //Point
			light.position.set(getWorldPos());
		} else { //Directional
			light.position.set(getWorldRot().forward());
		}
	}
}
