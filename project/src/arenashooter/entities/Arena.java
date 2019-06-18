package arenashooter.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arenashooter.engine.graphics.Light;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.entities.spatials.Barrel;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.Main;

public class Arena extends Entity {
	/** Character spawn points */
	public List<Spawner> playerSpawns = new ArrayList<>();

	/** World gravity vector */
	public Vec2f gravity = new Vec2f(0);

	public Vec3f ambientLight = new Vec3f(.48, .48, .5);
	
	public Vec3f fogColor = new Vec3f(0.929, 0.906, 0.753);
	public float fogDistance = 3000;
	
	//Music
	public String musicPath = "";
	public float musicVolume = 1;
	public float musicPitch = 1;

	public PhysicWorld physic;

	/** Map of all items available to item spawners */
	public Map<String, Item> spawnList = new HashMap<>();

	/** Character spawns that have been used */
	public List<Vec2f> usedSpawns = new ArrayList<>();

	/** All items currently on the map */
	public List<Item> items = new ArrayList<>();

	/** Kill every Spatial that touches these bounds (min X, min Y, max X, max Y) */
	public Vec4f killBound = new Vec4f(-100, -100, 100, 100);

	/**
	 * Base camera position, camera movement will be restricted into this field of
	 * view
	 */
	public Vec3f cameraBasePos = new Vec3f(0, 0, 8);

	public Set<Light> lights = new HashSet<>();

	Timer spawnWeapon = new Timer(4);

	public Arena() {
		physic = new PhysicWorld(this);
		Barrel bb = new Barrel(new Vec2f(0, 0));
		bb.attachToParent(getArena(), "zasqdw");
	}
	

	/**
	 * Get this Arena
	 * 
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
	/*
	 * public Vec2f GetRandomRespawn() { int randomNum =
	 * ThreadLocalRandom.current().nextInt(0, spawn.size()); while (dernierspawn ==
	 * randomNum) { randomNum = ThreadLocalRandom.current().nextInt(0,
	 * spawn.size()); } dernierspawn = randomNum; return spawn.get(randomNum); }
	 */

	/**
	 * Get a spawn location that is not currently occupied by a Character <br/>
	 * Use this to give a random spawn to each Character
	 * 
	 * @author Shervin
	 * @return Vec2f
	 */
	public Vec2f GetRandomRespawnch() {
		/*
		 * int rand = ThreadLocalRandom.current().nextInt(spawn.size()); if
		 * (!spawnch.isEmpty()) { while (spawnch.contains(spawn.get(rand))) { rand =
		 * ThreadLocalRandom.current().nextInt(0, spawn.size()); spawn.get(rand); } } if
		 * (!spawnch.contains(spawn.get(rand))) { spawnch.add(spawn.get(rand)); }
		 */
		Vec2f rand = new Vec2f(0, 0);
		return rand;
	}

	public Vec2f GetRandomRespawnch2() {
		Vec2f randi = new Vec2f();

		try {
			int rand = (int) (Math.random()*playerSpawns.size());
			
			randi = playerSpawns.get(rand).getWorldPos();
			int max = 100;
			int etapes = 0;
			while (usedSpawns.contains(randi) && etapes < max) {
				rand =  (int) (Math.random()*playerSpawns.size());
				randi = playerSpawns.get(rand).getWorldPos();
				etapes++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Main.log.error("Failed to find a spawn point: " + e.getLocalizedMessage());
		}
		usedSpawns.add(randi);
		return randi;

	}
	
	
}
