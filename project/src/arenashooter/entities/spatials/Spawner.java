package arenashooter.entities.spatials;

import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.util.Tuple;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.GameMaster;

public class Spawner extends Spatial {
	/** Spawn timer */
	private Timer timerWarmup = null;
	/** List of all items available in this spawner, linked to their probability */
	private List<Tuple<Item, Integer>> itemList = new ArrayList<>();
	/** Sum of probabilities */
	private double probaTotal = 0;
	/** Last spawned item */
	private Item currentItem = null;

	public Spawner(Vec2f position, double cooldown) {
		super(position);
		this.timerWarmup = new Timer(cooldown);
		timerWarmup.attachToParent(this, "timer_spawn");
	}

	/**
	 * Add an item to the spawn list
	 * @param item
	 * @param proba
	 */
	public void addItem(Item item, int proba) {
		Tuple<Item, Integer> tuple = new Tuple<Item, Integer>(item, proba);
		itemList.add(tuple);
		probaTotal += tuple.y;
	}

	/**
	 * Spawn a random item and reset timer
	 */
	private void spawnItem() {
		if (!itemList.isEmpty()) {
			Item itemToSpawn = getRandomItem().clone(getWorldPos());
			GameMaster.gm.getMap().items.add(itemToSpawn);
			itemToSpawn.attachToParent(GameMaster.gm.getMap(), genName());

			currentItem = itemToSpawn;
			timerWarmup.reset();
		}
	}

	/**
	 * @return random item to spawn based on their probabilities
	 */
	private Item getRandomItem() {
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
		if(posDiff()) {
			timerWarmup.setProcessing(true);
			if (timerWarmup.isOver())
				spawnItem();
		}
		super.step(d);
	}

	/**
	 * 
	 * @return <b>true</b> if last spawned item has been moved out of the spawner or if no item has been spawned yet
	 */
	private boolean posDiff() {
		if (currentItem == null)
			return true;

		return !currentItem.getWorldPos().equals(getWorldPos(), 1f);
	}
}
