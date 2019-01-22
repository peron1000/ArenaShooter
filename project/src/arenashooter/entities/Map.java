package arenashooter.entities;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.Physic;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.Disk;
import arenashooter.engine.physic.shapes.Rectangle;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Gun;

public class Map extends Entity {
	/** Spawn points */
	public ArrayList<Vec2f> spawn;
	/** World gravity vector */
	public Vec2f gravity = new Vec2f(0);

	public Physic physic;

	private int dernierspawn = -1;

	/**
	 * Character spawns
	 */
	public ArrayList<Vec2f> spawnch = new ArrayList<>();
	/** World bounds (min x, min y, max x, max y) */
	public Vec4f cameraBounds;

//	public Map(int nbPlayer) {
//		physic = new Physic(this);
//		
//		spawn = new ArrayList<>(nbPlayer);
//		creationPlateforme();
//		creationSpawn(nbPlayer);
//
//		Mesh testMesh = new Mesh(new Vec3f(0, -1000, -2000), new Quat(), new Vec3f(2000), "data/meshes/Suzanne.obj");
//		testMesh.attachToParent(this, "aaMesh_Suzanne");
//		Mesh arrows = new Mesh(new Vec3f(-500, 500, 200), new Quat(), new Vec3f(200), "data/meshes/arrows.obj");
//		arrows.attachToParent(this, "Mesh Arrows");
//
//		cameraBounds = new Vec4f(-5000, -1000, 5000, 1000);
//	}

	public Map() {
		physic = new Physic(this);
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
	public Vec2f GetRandomRespawn() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, spawn.size());
		while (dernierspawn == randomNum) {
			randomNum = ThreadLocalRandom.current().nextInt(0, spawn.size());
		}
		dernierspawn = randomNum;
		return spawn.get(randomNum);
	}

	/**
	 * @author Shervin Donne un vecteur/spawn qui n'est utilisé par aucun joueur
	 *         (random)<br/>
	 *         Utiliser pour donner un spawn aleatoire different a chaque joueur
	 * @return Vec2f
	 */
	public Vec2f GetRandomRespawnch() {
		int rand = ThreadLocalRandom.current().nextInt(spawn.size());
		if (!spawnch.isEmpty()) {
			while (spawnch.contains(spawn.get(rand))) {
				rand = ThreadLocalRandom.current().nextInt(0, spawn.size());
				spawn.get(rand);
			}
		}
		if (!spawnch.contains(spawn.get(rand))) {
			spawnch.add(spawn.get(rand));
		}
		return spawn.get(rand);
	}

	/**
	 * Create entities to test physics engine
	 */
	private void testPhysics() {
		// Rigid body 1
		Vec2f position = new Vec2f(-450, -500);
		RigidBody body = new RigidBody(new Rectangle(new Vec2f(100, 50)), position, .5, 500);
		RigidBodyContainer rb = new RigidBodyContainer(position, body);
		Sprite rbSprite = new Sprite(new Vec2f(), "data/default_texture.png");
		rbSprite.size = new Vec2f(200, 100);
		rb.attachToParent(this, "Rigid Body test");
		rbSprite.attachToParent(rb, "Sprite");

		// Rigid body 2
		position = new Vec2f(-400, -675);
		body = new RigidBody(new Disk(50), position, 0, 100);
		rb = new RigidBodyContainer(position, body);
		rbSprite = new Sprite(new Vec2f(), "data/sprites/UnMoineHD.png");
		rbSprite.size = new Vec2f(100, 100);
		rb.attachToParent(this, "Rigid Body test 2");
		rbSprite.attachToParent(rb, "Sprite");
	}

	// déso, ces lignes sont temporaires
	public void init() {
		testPhysics();
		Gun gun2 = new Gun(new Vec2f(300, 350), Item.SpritePath.assault);
		// public static CloseWeapon sword1 = new CloseWeapon(new Vec2f(750, 350),
		// Item.SpritePath.sword);
		Gun gun3 = new Gun(new Vec2f(-250, 1050), Item.SpritePath.minigun);
		Gun gun4 = new Gun(new Vec2f(1000, 350), Item.SpritePath.minigun);
		Gun gun5 = new Gun(new Vec2f(1000, 1050), Item.SpritePath.minigun);
		// sword1.attachToParent(this, "Item_Weapon"+genName());
		gun2.attachToParent(this, "Item_Weapon" + genName());
		gun3.attachToParent(this, "Item_Weapon" + genName());
		gun4.attachToParent(this, "Item_Weapon" + genName());
		gun5.attachToParent(this, "Item_Weapon" + genName());
	}
}
