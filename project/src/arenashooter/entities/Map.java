package arenashooter.entities;

import java.util.ArrayList;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.Physic;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.Disk;
import arenashooter.engine.physic.shapes.Rectangle;
import arenashooter.entities.spatials.AnimationTester;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Shotgun;

public class Map extends Entity {
	/** Spawn points */
	public ArrayList<Vec2f> spawn;
	/** World gravity vector */
	public Vec2f gravity = new Vec2f(0);

	public Physic physic;

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
		physic = new Physic(this);
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

	/**
	 * Create entities to test stuf
	 */
	private void createTestEntities() {
		// Rigid body 1
		Vec2f position = new Vec2f(000, -700);
		RigidBody body = new RigidBody(new Rectangle(new Vec2f(100, 50)), position, .5, 500);
		RigidBodyContainer rb = new RigidBodyContainer(position, body);
		Sprite rbSprite = new Sprite(new Vec2f(), "data/default_texture.png");
		rbSprite.size = new Vec2f(200, 100);
		rb.attachToParent(this, "Rigid Body test");
		rbSprite.attachToParent(rb, "Sprite");

		// Rigid body 2
		position = new Vec2f(-700, -500);
		body = new RigidBody(new Disk(50), position, 0, 100);
		rb = new RigidBodyContainer(position, body);
		rbSprite = new Sprite(new Vec2f(), "data/sprites/UnMoineHD.png");
		rbSprite.size = new Vec2f(100, 100);
		rb.attachToParent(this, "Rigid Body test 2");
		rbSprite.attachToParent(rb, "Sprite");

		// Rigid body 3
		position = new Vec2f(-750, -600);
		body = new RigidBody(new Disk(45), position, 0, 75);
		rb = new RigidBodyContainer(position, body);
		rbSprite = new Sprite(new Vec2f(), "data/sprites/UnMoineHD.png");
		rbSprite.size = new Vec2f(90, 90);
		rb.attachToParent(this, "Rigid Body test 3");
		rbSprite.attachToParent(rb, "Sprite");
		
		AnimationTester animTester = new AnimationTester(new Vec2f(500, 0));
		animTester.attachToParent(this, "anim tester 1");
		
		Shotgun shot = new Shotgun(new Vec2f(),"shotgun",100,"data/weapons/shotgun1.png", new Vec2f(15, 0), new Vec2f(5, 0),"GunCock_02", 1.2, 6, "", 0, "","bang_shotgun_01","no_ammo_01",10, 0.3,0,2500,3,60,2,2500,2);
		shot.attachToParent(this, "test_Shotgun");
	}
}
