package arenashooter.entities;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.spatials.AnimationTester;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.items.Item;

public class Map extends Entity {
	/** Spawn points */
	public ArrayList<Vec2f> spawn;
	/** World gravity vector */
	public Vec2f gravity = new Vec2f(0);

	public PhysicWorld physic;
	
	/**
	 * Character spawns
	 */

	public ArrayList<Vec2f> spawnch = new ArrayList<>();
	
	// All items on the map
	public ArrayList<Item> items = new ArrayList<>();
	
	/** World bounds (min x, min y, max x, max y) */
	public Vec4f cameraBounds;

	Timer spawnWeapon = new Timer(4);

	public Map() {
		physic = new PhysicWorld(this);
		createTestEntities();
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
	 * @author Shervin Donne un vecteur/spawn qui n'est utilisé par aucun joueur
	 *         (random)<br/>
	 *         Utiliser pour donner un spawn aleatoire different a chaque joueur
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
		//new Random();
		//System.out.println(" spawn size "+ spawn.size());
		int rand = ThreadLocalRandom.current().nextInt(0,spawn.size());
		//System.out.println(rand);
		
		randi = spawn.get(rand);
		int max = 100;
		int etapes = 0;
		while (spawnch.contains(randi) && etapes < max) {
			rand = ThreadLocalRandom.current().nextInt(spawn.size());
			randi = spawn.get(rand);
			//System.out.println("Vec2f x:"+randi.x+" y:"+randi.y);
			etapes++;
		}
		
	}catch (Exception e) {
		// TODO: handle exception
		System.out.println("fail spawn : "+e);
	}
		spawnch.add(randi);
		return randi;
	
		}

	/**
	 * Create entities to test stuff
	 */
	private void createTestEntities() {
		// Rigid body 1
		Vec2f position = new Vec2f(-7, 0);
		RigidBody body = new RigidBody(new ShapeDisk(.5), position, 0, CollisionFlags.RIGIDBODY, 1, .3f);
		RigidBodyContainer rb = new RigidBodyContainer(position, body);
		Sprite rbSprite = new Sprite(new Vec2f(), "data/test.png");
		rbSprite.size = new Vec2f(1, 1);
		rb.attachToParent(this, "Rigid Body test 1");
		rbSprite.attachToParent(rb, "Sprite");

		// Rigid body 1
		position = new Vec2f(-7.5, -1);
		body = new RigidBody(new ShapeDisk(1), position, 0, CollisionFlags.RIGIDBODY, 1, .3f);
		rb = new RigidBodyContainer(position, body);
		rbSprite = new Sprite(new Vec2f(), "data/sprites/UnMoineHD.png");
		rbSprite.size = new Vec2f(2, 2);
		rb.attachToParent(this, "Rigid Body test 2");
		rbSprite.attachToParent(rb, "Sprite");
		
		AnimationTester animTester = new AnimationTester(new Vec2f(-15, 0));
		animTester.attachToParent(this, "anim tester 1");
	}
}
