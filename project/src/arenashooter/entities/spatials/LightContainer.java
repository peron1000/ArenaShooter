package arenashooter.entities.spatials;

import arenashooter.engine.graphics.Light;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;

public class LightContainer extends Spatial3 {
	private final Light light;

	public LightContainer(Light light) {
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
}
