package arenashooter.entities.spatials;

import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.GameMaster;

public class Spawner extends Spatial {
	// Timer de respawn
	private Timer timerWarmup = null;
	private List<Tuple<Item, Integer>> itemList = new ArrayList<>();
	private double probaTotal = 0;
	private Vec2f position = null;
	private Item itemCourant = null;
	private Vec2f pos = null;

	public Spawner(Vec2f position, Double cooldown) {
		super(position);
		this.position = position;
		this.timerWarmup = new Timer(cooldown);
		timerWarmup.attachToParent(this, timerWarmup.genName());
	}

	public void addItem(Item item, int proba) {
		Tuple<Item, Integer> tuple = new Tuple<Item, Integer>(item, proba);
		itemList.add(tuple);
		probaTotal += tuple.y;
	}

	public void spawnWeapon() {
		if (!itemList.isEmpty()) {
			Item weaponToSpawn = get().clone(position);
			
			// Variables pour posdiff
			itemCourant = weaponToSpawn;
			pos = weaponToSpawn.getWorldPos();
			
			GameMaster.gm.getMap().items.add(weaponToSpawn);
			weaponToSpawn.attachToParent(GameMaster.gm.getMap(), genName());
			timerWarmup.reset();
		}
	}

	public Item get() {
		double random = Math.random() * probaTotal;
		double counter = 0;
		Item chosenOne = null;
		for (Tuple<Item, Integer> tuple : itemList) {
			counter += tuple.y;
			if (chosenOne == null && counter >= random) {
				chosenOne = tuple.x;
			}
		}
		return chosenOne;
	}

	@Override
	public void step(double d) {
		if (posDiff()) {
			timerWarmup.setProcessing(true);
			if (timerWarmup.isOver())
				spawnWeapon();
		}
		super.step(d);
	}

	public boolean posDiff() {
		boolean res = false;
		if ((itemCourant == null && pos == null) || (itemCourant.getWorldPos().x != pos.x && itemCourant.getWorldPos().y != pos.y)) {
			res = true;
		}
		return res;
	}
}
