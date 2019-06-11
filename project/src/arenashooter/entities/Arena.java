package arenashooter.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.Main;

public class Arena extends Entity {
	/** Character spawn points */
	public List<Vec2f> playerSpawns = new ArrayList<>();
	
	/** World gravity vector */
	public Vec2f gravity = new Vec2f(0);

	public PhysicWorld physic;
	
	/** Map of all items available to item spawners */
	public Map<String, Item> spawnList = new HashMap<>();
	
	/** Character spawns that have been used */
	public ArrayList<Vec2f> usedSpawns = new ArrayList<>();
	
	/** All items currently on the map */
	public ArrayList<Item> items = new ArrayList<>();
	
	/** Kill every Spatial that touches these bounds (min X, min Y, max X, max Y) */
	public Vec4f killBound = new Vec4f(-100, -100, 100, 100);
	
	/** Base camera position, camera movement will be restricted into this field of view */
	public Vec3f cameraBasePos = new Vec3f(0, 0, 8);

	Timer spawnWeapon = new Timer(4);

	public Arena() {
		physic = new PhysicWorld(this);
	}
	
	/**
	 * Get this Arena
	 * @return this
	 */
	@Override
	public Arena getArena() {
		return this;
	}

	@Override
	public void step(double d) {
		super.step(d);
		physic.step(d);
	}

	/**
	 * @author SnPop GetRandomRespawn : rend un spawn aleatoire entre 0 inclus et
	 *         taille de spawn exclus<br/>
	 *         Utiliser pour donner un spawn aleatoire a chaque joueur different du
	 *         dernier utilise
	 * @return Vec2f
	 */
	/*public Vec2f GetRandomRespawn() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, spawn.size());
		while (dernierspawn == randomNum) {
			randomNum = ThreadLocalRandom.current().nextInt(0, spawn.size());
		}
		dernierspawn = randomNum;
		return spawn.get(randomNum);
	}*/

	/**
	 * Get a spawn location that is not currently occupied by a Character
	 * <br/> Use this to give a random spawn to each Character
	 * @author Shervin
	 * @return Vec2f
	 */
	public Vec2f GetRandomRespawnch() {
		/*int rand = ThreadLocalRandom.current().nextInt(spawn.size());
		if (!spawnch.isEmpty()) {
			while (spawnch.contains(spawn.get(rand))) {
				rand = ThreadLocalRandom.current().nextInt(0, spawn.size());
				spawn.get(rand);
			}
		}
		if (!spawnch.contains(spawn.get(rand))) {
			spawnch.add(spawn.get(rand));
		}*/
		Vec2f rand = new Vec2f(0,0);
		return rand;
	}

	public Vec2f GetRandomRespawnch2() {
		Vec2f randi = new Vec2f();

		try {
			int rand = ThreadLocalRandom.current().nextInt(0,playerSpawns.size());

			randi = playerSpawns.get(rand);
			int max = 100;
			int etapes = 0;
			while (usedSpawns.contains(randi) && etapes < max) {
				rand = ThreadLocalRandom.current().nextInt(playerSpawns.size());
				randi = playerSpawns.get(rand);
				etapes++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Main.log.error("Failed to find a spawn point: "+e.getLocalizedMessage());
		}
		usedSpawns.add(randi);
		return randi;

	}
}
