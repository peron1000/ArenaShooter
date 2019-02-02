package arenashooter.entities.spatials.items;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.game.GameMaster;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CircleBullet;
import arenashooter.entities.spatials.Particles;
import arenashooter.entities.spatials.Sprite;

public class Gun extends Weapon {
	private Timer fire = new Timer(0.15);
	Collider coll;
	private float recoil = 0.4f;// High
	private float thrust = 0;//
	private float damage = 1f;
	public double cannonLength = 1.0;
	public double bulletSpeed = 1.0;

	private double waitCharge;// Temps a attendre pour charger
	private boolean charge;// La gachette est enclenchee
	private double tpscharge;// Temps depuis lequel la gachette est enclenchee

	private double chargeInertia;// TODO : Temps pour que l'arme commence a se decharger

	public Gun(Vec2f position, SpritePath itemSprite) {
		super(position, itemSprite);
		coll = new Collider(position, new Vec2f(40, 40));
		switch (itemSprite) {
		case assault:
			fire = new Timer(0.10);
			fire.attachToParent(this, "attack timer");

			thrust = 500;
			recoil = 0.5f;
			damage = 25f;
			bulletSpeed = 4000;
			cannonLength = 40.0;

			tpscharge = 0;
			chargeInertia = 0;

			SoundEffect bangSound1 = new SoundEffect(this.position, "data/sound/Bang1.ogg", 2);
			bangSound1.setVolume(3f);
			bangSound1.attachToParent(this, "snd_Bang");
			break;
		case minigun:
			fire = new Timer(0.03);
			fire.attachToParent(this, "attack timer");

			thrust = 500;
			recoil = 0.30f;
			damage = 1f;
			bulletSpeed = 4000;
			cannonLength = 65.0;
			tpscharge = 0.8;
			chargeInertia = 0.2;

			SoundEffect bangSound2 = new SoundEffect(this.position, "data/sound/Bang2.ogg", 2);
			bangSound2.setVolume(3f);
			bangSound2.attachToParent(this, "snd_Bang");
			break;
		case iongun:
			fire = new Timer(0.10);
			fire.attachToParent(this, "attack timer");

			thrust = 200;
			recoil = 0.02f;
			damage = 8f;
			bulletSpeed = 1750;
			cannonLength = 45.0;

			tpscharge = 0.3;
			chargeInertia = 0;

			SoundEffect bangSound3 = new SoundEffect(this.position, "data/sound/BangIonGun.ogg", 2, 0.9f, 1.1f);
			bangSound3.setVolume(3f);
			bangSound3.attachToParent(this, "snd_Bang");
			break;
		default:
			break;
		}

		SoundEffect pickup = new SoundEffect(this.position, "data/sound/GunCock1.ogg", 1);
		pickup.setVolume(0.5f);
		pickup.attachToParent(this, "snd_Pickup");

		Entity particleContainer = new Entity();
		particleContainer.attachToParent(this, "particle_container");
	}

	@Override
	public void attackStart() {
		charge = true;
	}

	@Override
	public void attackStop() {
		charge = false;
	}

	@Override
	public void step(double d) {

		if (isEquipped()) {
			Vec2f targetOffSet = Vec2f.rotate(new Vec2f(50, 0), rotation);

			localPosition.x = (float) Utils.lerpD((double) localPosition.x, targetOffSet.x, Math.min(1, d * 55));
			localPosition.y = (float) Utils.lerpD((double) localPosition.y, targetOffSet.y, Math.min(1, d * 55));
			rotation = Utils.lerpAngle(rotation, ((Character) parent).aimInput, Math.min(1, d * 17));
		}

		if (charge)
			waitCharge = Math.min(waitCharge + d, tpscharge);
		else
			waitCharge = Math.max(waitCharge - d, 0);

		if (charge && waitCharge >= tpscharge && fire.isOver()) {
			fire.restart();

			Vec2f aim = Vec2f.fromAngle(rotation);

			Vec2f bulSpeed = Vec2f.multiply(aim, bulletSpeed);
			Vec2f bulletPos = pos();
			bulletPos.add(Vec2f.multiply(aim, cannonLength));

			if (isIonGun) {

				CircleBullet bul = new CircleBullet(bulletPos, bulSpeed, damage);
				if (isEquipped())
					bul.shooter = ((Character) parent);
				bul.attachToParent(GameMaster.gm.getMap(), ("bullet_" + bul.genName()));

			} else {
				Bullet bul = new Bullet(bulletPos, bulSpeed, damage);
				bul.attachToParent(GameMaster.gm.getMap(), ("bullet_" + bul.genName()));
				if (isEquipped())
					bul.shooter = ((Character) parent);
			}

			if (isEquipped()) {
				getVel().add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), recoil * 5000));
				((Character) parent).vel.add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust));
			} else {
				getVel().add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust / 10));
			}

			((SoundEffect) children.get("snd_Bang")).play();

			if (isIonGun) {
				Particles flash = new Particles(bulletPos, "data/particles/flash_02.xml");
				flash.attachToParent(children.get("particle_container"), "particles_flash");
			} else {
				Particles flash = new Particles(bulletPos, "data/particles/flash_01.xml");
				flash.attachToParent(children.get("particle_container"), "particles_flash");
			}

			Particles shell = new Particles(bulletPos, "data/particles/shell_01.xml");
			shell.selfDestruct = true;
			shell.attachToParent(this, shell.genName());

			rotation += ((Math.random()) - 0.5) * recoil;

			// Add camera shake
			Window.camera.setCameraShake(2.8f);
		}

		super.step(d);
	}
}
