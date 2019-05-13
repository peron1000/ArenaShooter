package arenashooter.entities.spatials;

import java.util.List;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.xmlReaders.MapXmlReader;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Gun;

public class Spawner extends Spatial {
	// Timer de respawn
	protected Timer timerWarmup = null;
	protected List<Tuple<String, Integer>> test = null;

	// Item collection
	public Spawner(Vec2f position, Double cooldown, List<Tuple<String, Integer>> table) {
		super(position);
		this.timerWarmup = new Timer(cooldown);
		this.test = table;
	}

	public void spawnWeapon() {
		
	}

	public void print() {
		for (Tuple<String, Integer> e : this.test)
			System.err.println(e.x + " : " + e.y);
	}

	@Override
	public void step(double d) {
		print();
		if (timerWarmup.isOver()) {
			timerWarmup.restart();
			spawnWeapon();
		}
		super.step(d);
	}
}
