package arenashooter.entities;

import java.util.ArrayList;

import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.entities.spatials.AnimationTester;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Shotgun;

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

	/**
	 * Create entities to test stuff
	 */
	private void createTestEntities() {
		// Rigid body 1
		Vec2f position = new Vec2f(9.6, -7);
		RigidBody body = new RigidBody(new ShapeBox(new Vec2f(.5, .5)), position, .5, 1, .3f);
		RigidBodyContainer rb = new RigidBodyContainer(position, body);
		rb.attachToParent(this, "Rigid Body test");
		Mesh rbMesh = new Mesh(new Vec3f(), new Quat(), "data/meshes/crate/crate_01.obj");
		rbMesh.rotationFromParent = true;
		rbMesh.attachToParent(rb, "mesh");

		// Rigid body 2
		position = new Vec2f(-7, 0);
		body = new RigidBody(new ShapeDisk(.5), position, 0, 1, .3f);
		rb = new RigidBodyContainer(position, body);
		Sprite rbSprite = new Sprite(new Vec2f(), "data/sprites/UnMoineHD.png");
		rbSprite.size = new Vec2f(1, 1);
		rb.attachToParent(this, "Rigid Body test 2");
		rbSprite.rotationFromParent = true;
		rbSprite.attachToParent(rb, "Sprite");

		// Rigid body 3
		position = new Vec2f(-7.5, -1);
		body = new RigidBody(new ShapeDisk(1), position, 0, 1, .3f);
		rb = new RigidBodyContainer(position, body);
		rbSprite = new Sprite(new Vec2f(), "data/sprites/UnMoineHD.png");
		rbSprite.size = new Vec2f(2, 2);
		rb.attachToParent(this, "Rigid Body test 3");
		rbSprite.rotationFromParent = true;
		rbSprite.attachToParent(rb, "Sprite");

		// Rigid body 4
		position = new Vec2f(10, -9);
		body = new RigidBody(new ShapeBox(new Vec2f(1, .5)), position, -.5, 1, .3f);
		rb = new RigidBodyContainer(position, body);
		rb.attachToParent(this, "Rigid Body test 4");
		rbMesh = new Mesh(new Vec3f(), new Quat(), "data/meshes/crate/crate_01.obj");
		rbMesh.rotationFromParent = true;
		rbMesh.scale.x = 2;
		rbMesh.attachToParent(rb, "mesh");
		
		// Rigid body 5
		position = new Vec2f(10, -12);
		body = new RigidBody(new ShapeBox(new Vec2f(1, .5)), position, 1.56, 1, .3f);
		rb = new RigidBodyContainer(position, body);
		rb.attachToParent(this, "Rigid Body test 5");
		rbMesh = new Mesh(new Vec3f(), new Quat(), "data/meshes/crate/crate_01.obj");
		rbMesh.rotationFromParent = true;
		rbMesh.scale.x = 2;
		rbMesh.attachToParent(rb, "mesh");
		
		AnimationTester animTester = new AnimationTester(new Vec2f(5, 0));
		animTester.attachToParent(this, "anim tester 1");
	}
}
