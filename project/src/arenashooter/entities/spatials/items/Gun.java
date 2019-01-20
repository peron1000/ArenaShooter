package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.game.Game;
import arenashooter.game.GameMaster;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Sprite;

public class Gun extends Item {
	private Timer fire = new Timer(0.15);
	Collider coll;
	private float recoil = 0.4f;// High
	private float thrust = 0;//
	private float damage = 1f;
	public double cannonLength = 1.0;
	public double bulletSpeed = 1.0;

	public Gun(Vec2f position, SpritePath itemSprite) {
		super(position, itemSprite);
		coll = new Collider(position, new Vec2f(40, 40));
		if (itemSprite == SpritePath.assault) {
			fire = new Timer(0.10);
			fire.attachToParent(this, "attack timer");

			thrust = 500;
			recoil = 0.5f;
			damage = 5f;
			bulletSpeed = 3000;
			cannonLength = 50.0;

			SoundEffect bangSound = new SoundEffect(this.position, "data/sound/Bang1.ogg", 2);
			bangSound.setVolume(3f);
			bangSound.attachToParent(this, "snd_Bang");

		} else if (itemSprite == SpritePath.minigun) {
			fire = new Timer(0.03);
			fire.attachToParent(this, "attack timer");

			thrust = 500;
			recoil = 0.30f;
			damage = 0f;
			bulletSpeed = 3000;
			cannonLength = 75.0;

			SoundEffect bangSound = new SoundEffect(this.position, "data/sound/Bang2.ogg", 2);
			bangSound.setVolume(3f);
			bangSound.attachToParent(this, "snd_Bang");
		}

		SoundEffect pickup = new SoundEffect(this.position, "data/sound/GunCock1.ogg", 1);
		pickup.setVolume(0.5f);
		pickup.attachToParent(this, "snd_Pickup");
	}
	//
	// public void fire(boolean lookRight) {
	// if (fire.isOver()) {
	// float pX = 0;
	// float vX = 0;
	// if (parent instanceof Character) {
	// if (lookRight) {
	// pX = position.x + 50;
	// vX = 5000;
	// ((Character) parent).vel.x -= 250;
	// } else {
	// pX = position.x - 50;
	// vX = -3000;
	// ((Character) parent).vel.x += 250;
	// }
	// }
	// fire.restart();
	//
	// double coeff = (2 * Math.random()) - 1;
	//
	// Vec2f bulletSpeed = Vec2f.rotate(new Vec2f(vX, 0), coeff);
	// bulletSpeed.x += ((Character) parent).vel.x / 4;
	// bulletSpeed.y += ((Character) parent).vel.y / 4;
	//
	// Bullet bul = new Bullet(new Vec2f(pX, position.y), bulletSpeed, damage);
	// bul.attachToParent(GameMaster.gm.getMap(), ("bullet" + bul.genName()));
	//
	// vel.add(Vec2f.multiply(Vec2f.normalize(Vec2f.rotate(bulletSpeed, Math.PI)),
	// recoil * 5000));
	// ((SoundEffect) children.get("snd_Bang")).play();
	//
	// ((Sprite) children.get("Item_Sprite")).rotation += ((Math.random()) - 0.5) *
	// recoil;
	//
	// // Add camera shake
	// Game.camera.setCameraShake(.8f);
	// }
	// }

	public void fire() { // Visée par vecteur
		if (fire.isOver()) {
			fire.restart();

			Vec2f aim = Vec2f.fromAngle(rotation);

			Vec2f bulSpeed = Vec2f.multiply(aim, bulletSpeed);
			Vec2f bulletPos = position.clone();
			bulletPos.add(Vec2f.multiply(aim, cannonLength));

			Bullet bul = new Bullet(bulletPos, bulSpeed, damage);

			bul.attachToParent(GameMaster.gm.getMap(), ("bullet_" + bul.genName()));

			if (isEquipped()) {
				vel.add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), recoil * 5000));
				((Character) parent).vel.add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust));
			} else
				vel.add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust));

			((SoundEffect) children.get("snd_Bang")).play();

			rotation += ((Math.random()) - 0.5) * recoil;

			// Add camera shake
			Game.camera.setCameraShake(2.8f);
		}
	}

	public void step(double d) {
		if (isEquipped()) {
			rotation = Utils.normalizeAngle(rotation);
			Sprite image = ((Sprite) children.get("Item_Sprite"));
			if (image != null) {
				image.rotation = rotation;
				if (rotation < Math.PI / 2 && rotation > -Math.PI / 2)
					image.flipY = false;
				else
					image.flipY = true;
				vel.x = Utils.lerpF(vel.x, 0, Utils.clampD(d * 50, 0, 1));
				vel.y = Utils.lerpF(vel.y, 0, Utils.clampD(d * 50, 0, 1));
			}
		}

		super.step(d);
	}
}
