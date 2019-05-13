package arenashooter.entities.spatials;

import arenashooter.engine.itemCollection.ItemCollection;
import arenashooter.engine.itemCollection.ItemConcept;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.entities.spatials.items.Melee;

public class Spawner extends Spatial {
	// Timer de respawn
	protected Timer timerWarmup = null;
	// Item collection
	public ItemCollection<ItemConcept> itemCollection = new ItemCollection<ItemConcept>();
	public Spawner (Vec2f position, String pathSprite, Double cooldown) {
		super(position);
		this.timerWarmup = new Timer(cooldown);
		Sprite sprite = new Sprite(position, pathSprite);
		sprite.attachToParent(this, "Item_Sprite");
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
