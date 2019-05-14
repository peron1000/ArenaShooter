package arenashooter.entities.spatials;

import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.xmlReaders.MapXmlReader;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Usable;

public class Spawner extends Spatial {
	// Timer de respawn
	private Timer timerWarmup = null;
	private List<Tuple<Item, Integer>> itemList = new ArrayList<>();
	private double probaTotal = 0;

	// Item collection
	public Spawner(Vec2f position, Double cooldown) {
		super(position);
		this.timerWarmup = new Timer(cooldown);
		timerWarmup.attachToParent(this, timerWarmup.genName());
	}

	public void addItem(Item item, int proba) {
		Tuple<Item, Integer> tuple = new Tuple<Item, Integer>(item, proba);
		itemList.add(tuple);
	}

	public void spawnWeapon() {
		if (!itemList.isEmpty()) {
			Item test = itemList.get(0).x;
			test.attachToParent(getParent(), "test");
		}
	}

	@Override
	public void step(double d) {
		if (timerWarmup.isOver()) {
			spawnWeapon();
		}
		super.step(d);
	}
}
