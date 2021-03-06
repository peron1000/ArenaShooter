package arenashooter.entities.spatials;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import arenashooter.engine.math.Vec2fi;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
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
	
	private Sprite editorView;
	
	private boolean playerSpawn = false;
	
	private Spawner() {
		super();
	}
	
	public Spawner(Vec2f localPosition, double cooldown) {
		super(localPosition);
		setCooldown(cooldown);
		editorView = new Sprite(localPosition , "data/weapons/alien.png");
		editorView.size.set(editorView.getTexture().getSize()).multiply(0.03f);
	}
	
	@Override
	protected void recursiveAttach(Entity newParent) {
		super.recursiveAttach(newParent);
		
		refreshPlayerSpawn();
	}

	@Override
	protected void recursiveDetach(Arena oldArena) {
		super.recursiveDetach(oldArena);
		if(oldArena != null) oldArena.playerSpawns.remove(this);
	}
	
	/**
	 * @return if this spawn is used to spawn player characters
	 */
	public boolean isPlayerSpawn() { return playerSpawn; }
	
	public void setPlayerSpawn(boolean playerSpawn) {
		this.playerSpawn = playerSpawn;
		
		refreshPlayerSpawn();
	}
	
	private void refreshPlayerSpawn() {
		if(getArena() != null) {
			if(playerSpawn) {
				if( !getArena().playerSpawns.contains(this) )
					getArena().playerSpawns.add(this);
			} else
				getArena().playerSpawns.remove(this);
		}
	}
	
	private void setCooldown(double cooldown) {
		timerWarmup = new Timer(cooldown);
		timerWarmup.attachToParent(this, "timer_spawn");
	}

	/**
	 * @return the cooldown
	 */
	public double getCooldown() {
		return timerWarmup.getMax();
	}

	/**
	 * Add an item to the spawn list
	 * @param item
	 * @param proba
	 */
	public void addItem(String item, int proba) {
		if(availableItems.containsKey(item)) {
			Main.log.warn("Item \""+item+"\" is already present in spawner");
			return;
		}
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
				break;
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
	public void editorAddPosition(Vec2fi position) {
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
	
	
	/*
	 * JSON
	 */
	
	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();
		
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return new JsonObject(availableItems);
			}
			@Override
			public String getKey() {
				return "available items";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				Map<String, Number> items = json.getMap(this);
				availableItems.clear();
				for(Entry<String, Number> e : items.entrySet())
					addItem(e.getKey(), e.getValue().intValue());
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return getCooldown();
			}
			@Override
			public String getKey() {
				return "cooldown";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				setCooldown(json.getFloat(this));
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return isPlayerSpawn();
			}
			@Override
			public String getKey() {
				return "player spawn";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				setPlayerSpawn(json.getBoolean(this));
			}
		});
		return set;
	}
	
	public static Spawner fromJson(JsonObject json) throws Exception {
		Spawner s = new Spawner();
		useKeys(s, json);
		return s;
	}
}
