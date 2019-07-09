package arenashooter.entities.spatials;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.json.EntityTypes;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.Main;

public class Spawner extends Spatial {
	/** Spawn timer */
	private Timer timerWarmup = null;
	/** Map of all items available in this spawner, linked to their probability */
	private Map<String, Integer> availableItems = new HashMap<>();
	/** Sum of probabilities */
	private double probaTotal = 0;
	/** Last spawned item */
	private Item currentItem = null;
	/** Timer of respawn*/
	private double cooldown = 0;
	
	private Sprite editorView;

	public Spawner(Vec2f localPosition, double cooldown) {
		super(localPosition);
		this.timerWarmup = new Timer(cooldown);
		timerWarmup.attachToParent(this, "timer_spawn");
		editorView = new Sprite(localPosition , "data/weapons/alien.png");
		editorView.size = editorView.getTexture().getSize().multiply(0.03f);
	}

	/**
	 * @return the cooldown
	 */
	public double getCooldown() {
		return cooldown;
	}

	/**
	 * Add an item to the spawn list
	 * @param item
	 * @param proba
	 */
	public void addItem(String item, int proba) {
		if(availableItems.containsKey(item))
			Main.log.warn("Item \""+item+"\" is already present in spawner");
		availableItems.put(item, proba);
		probaTotal += proba;
	}

	/**
	 * Spawn a random item and reset timer
	 */
	private void spawnItem() {
		if(getArena() == null || availableItems.isEmpty()) return;

		Item itemToSpawn = getRandomItem();
		if(itemToSpawn == null) return;
		itemToSpawn = itemToSpawn.clone();
		//Set item position
		itemToSpawn.localPosition.set(getWorldPos());
		//Slight rotation
		itemToSpawn.localRotation = Math.random()*.1;
		getArena().items.add(itemToSpawn);
		itemToSpawn.attachToParent(getArena(), genName());

		currentItem = itemToSpawn;
		timerWarmup.reset();
	}

	/**
	 * @return random item to spawn based on their probabilities
	 */
	private Item getRandomItem() {
		if(getArena() == null) return null;
		
		double random = Math.random() * probaTotal;
		double counter = 0;
		Item chosenOne = null;
		String itemName = "";
		for(Entry<String, Integer> entry : availableItems.entrySet()) {
			counter += entry.getValue();
			if (chosenOne == null && counter >= random) {
				itemName = entry.getKey();
				chosenOne = getArena().spawnList.get(itemName);
			}
		}
		if(chosenOne == null)
			Main.log.error("Trying to spawn an invalid item: \""+itemName+"\"");
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
	
	/**
	 * Get a map of item names linked to their spawn probability
	 */
	public Map<String, Integer> getAvailableItems() {
		return availableItems;
	}
	
	@Override
	public void editorAddPosition(Vec2f position) {
		editorView.localPosition.add(position);
		super.editorAddPosition(position);
	}
	
	@Override
	public void editorAddRotationZ(double angle) {
		editorView.localRotation += angle;
		super.editorAddRotationZ(angle);
	}
	
	@Override
	public void editorDraw() {
		editorView.draw(false);
	}
	
	@Override
	protected EntityTypes getType() {
		return EntityTypes.SPAWN;
	}
	
	@Override
	protected JsonObject getJson() {
		JsonObject spawner = super.getJson();
		spawner.putChain("cooldown", timerWarmup.getMax());
		return spawner;
	}
}
