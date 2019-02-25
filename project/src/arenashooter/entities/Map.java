package arenashooter.entities;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import arenashooter.engine.itemCollection.ItemCollection;
import arenashooter.engine.itemCollection.ItemConcept;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.Physic;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.Disk;
import arenashooter.engine.physic.shapes.Rectangle;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.entities.spatials.items.Melee;
import arenashooter.game.GameMaster;

public class Map extends Entity {
	/** Spawn points */
	public ArrayList<Vec2f> spawn;
	/** World gravity vector */
	public Vec2f gravity = new Vec2f(0);

	public Physic physic;

	private int dernierspawn = -1;

	public ItemCollection<ItemConcept> itemCollection = new ItemCollection<ItemConcept>();

	/**
	 * Character spawns
	 */
	public ArrayList<Vec2f> spawnch = new ArrayList<>();
	/** World bounds (min x, min y, max x, max y) */
	public Vec4f cameraBounds;

	Timer spawnWeapon = new Timer(10);

	public Map() {
		physic = new Physic(this);
	}

	@Override
	public void step(double d) {
		super.step(d);
		if (spawnWeapon.isOver()) {
			spawnWeapon.restart();
			spawnWeapons();
		}
		physic.step(d);
	}

	private void spawnWeapons() {
		ItemConcept ic = itemCollection.get();
		switch (ic.getType()) {
		case "Gun":
			Vec2f position = newPositionWeapons();
			Gun gun = new Gun(position, ic.spritePath, ic.bangSound, ic.pickupSound, ic.chargeSound, ic.noAmmoSound,
					ic.fireRate, ic.bulletType, ic.bulletSpeed, ic.damage, ic.cannonLength, ic.recoil, ic.thrust,
					ic.tpsCharge, ic.getSize());
			if (ic.name == null) {
				gun.attachToParent(this, gun.genName());
			} else {
				gun.attachToParent(this, ic.name);
			}
			break;
		case "Melee":
			position = newPositionWeapons();
			Melee melee = new Melee(position, ic.spritePath, ic.getSize(), ic.fireRate, ic.pickupSound);
			if (ic.name == null) {
				melee.attachToParent(this, melee.genName());
			} else {
				melee.attachToParent(this, ic.name);
			}
			break;
		default:
			break;
		}
	}

	private Vec2f newPositionWeapons() {
		int size = spawn.size();
		double random = Math.random();
		random *= size;
		return spawn.get((int) random);
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
		spawnWeapon.attachToParent(this, "test");

//		Gun gun2 = new Gun(new Vec2f(300, 350));
////		Melee kata = new Melee(new Vec2f(750, 350));
//		Gun gun3 = new Gun(new Vec2f(-250, 1050), "data/weapons/Minigun_1.png", "Bang2", "GunCock1", "GunCock1", "jump",
//				0.03d, 0, 4000f, 5, 65d, 0.3, 500, 1, 1);
//		Gun gun4 = new Gun(new Vec2f(1000, 350), "data/weapons/Minigun_1.png", "Bang2", "GunCock1", "GunCock1", "jump",
//				0.03d, 0, 4000f, 5, 65d, 0.3, 500, 1, 1);
//		Gun gun5 = new Gun(new Vec2f(1000, 1050), "data/weapons/Minigun_1.png", "Bang2", "GunCock1", "GunCock1", "jump",
//				0.03d, 0, 4000f, 5, 65d, 0.3, 500, 1, 1);
//		Gun gun6 = new Gun(new Vec2f(1000, 1450), "data/weapons/RayGun1.png", "BangIonGun2", "GunCock1",
//				"IonChargeV2_3", "jump", 0.10d, 1, 2500f, 5, 55d, 0.4, 200, 0.6, 3);
//		gun6.attachToParent(this, "Item_Weapon" + genName());
////		kata.attachToParent(this, "Item_Weapon"+genName());
//		gun2.attachToParent(this, "Item_Weapon" + genName());
//		gun3.attachToParent(this, "Item_Weapon" + genName());
//		gun4.attachToParent(this, "Item_Weapon" + genName());
//		gun5.attachToParent(this, "Item_Weapon" + genName());

	}
}
