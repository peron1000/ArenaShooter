package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;

public class Spawner extends Spatial {
	// Timer de respawn
	protected Timer timerWarmup = null;
	// Item collection
	//public ItemCollection<ItemConcept> itemCollection = new ItemCollection<ItemConcept>();
	public Spawner (Vec2f position, Double cooldown) {
		super(position);
		this.timerWarmup = new Timer(cooldown);
	}
	
	public void spawnWeapon(){
		
	}
	
	@Override
	public void step(double d) {
		if(timerWarmup.isOver()) {
			timerWarmup.restart();
			spawnWeapon();
		}
		super.step(d);
	}
}
