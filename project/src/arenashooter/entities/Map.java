package arenashooter.entities;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.Main;

public class Map extends Entity {
	/** All spawn points (items and characters) */
	public ArrayList<Vec2f> spawn;
	/** Character spawn points */
	public ArrayList<Vec2f> spawnperso;
	
	/** World gravity vector */
	public Vec2f gravity = new Vec2f(0);

	public PhysicWorld physic;
	
	/** Character spawns that have been used */
	public ArrayList<Vec2f> usedSpawns = new ArrayList<>();
	
	/** All items currently on the map */
	public ArrayList<Item> items = new ArrayList<>();
	
	/** World bounds (min x, min y, max x, max y) */
	public Vec4f cameraBounds;

	Timer spawnWeapon = new Timer(4);

	public Map() {
		physic = new PhysicWorld(this);
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
			int rand = ThreadLocalRandom.current().nextInt(0,spawnperso.size());

			randi = spawnperso.get(rand);
			int max = 100;
			int etapes = 0;
			while (usedSpawns.contains(randi) && etapes < max) {
				rand = ThreadLocalRandom.current().nextInt(spawnperso.size());
				randi = spawnperso.get(rand);
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
