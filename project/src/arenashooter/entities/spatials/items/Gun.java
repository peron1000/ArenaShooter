package arenashooter.entities.spatials.items;

import arenashooter.engine.audio.SoundSourceSingle;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.spatials.Bullet;
import arenashooter.game.GameMaster;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CircleBullet;
import arenashooter.entities.spatials.Particles;

public class Gun extends Melee {
	public float recoil = 0.4f;// High
	public float thrust = 0;//
	public double cannonLength = 1.0;
	public int bulletType = 0;
	public double bulletSpeed = 1.0;
	public double waitCharge;// Temps depuis lequel la gachette est enclenchee

	private SoundEffect sndCharge = null;
	private float sndChargeVol, sndChargePitch;

	private int nbAmmo = 15;

	public Gun(Vec2f position, String itemSprite, String bangSound, String chargeSound, String noAmmoSound,
			double fireRate, int bulletType, float bulletSpeed, float damage, double cannonLength, double recoil,
			double thrust, double tpsCharge, double size) {
		super(position, itemSprite, damage, fireRate,"GunCock1" );

		this.bulletType = bulletType;
		this.bulletSpeed = bulletSpeed;
		this.cannonLength = cannonLength;
		this.recoil = (float) recoil;
		this.thrust = (float) thrust;

		SoundEffect bang = new SoundEffect(this.position, "data/sound/" + bangSound + ".ogg", 2, 0.85f, 1.15f);
		bang.setVolume(0.25f);
		bang.attachToParent(this, "snd_Bang");

		SoundEffect charge = new SoundEffect(this.position, "data/sound/" + chargeSound + ".ogg", -1, 1, 1);
		charge.setVolume(0.35f);
		sndCharge = charge;

		SoundEffect noAmmo = new SoundEffect(this.position, "data/sound/" + noAmmoSound + ".ogg", 2, 0.85f, 1.15f);
		noAmmo.setVolume(0.25f);
		noAmmo.attachToParent(this, "snd_NoAmmo");

		Entity particleContainer = new Entity();
		particleContainer.attachToParent(this, "particle_container");

		timerAttack.setProcessing(false);

	}

	public Gun(Vec2f position) {
		super(position, "data/weapons/Assaut_1.png", 1, 0.15 , "data/sound/GunCock1.ogg");

		thrust = 500;
		recoil = 0.5f;
		bulletSpeed = 4000;
		cannonLength = 40.0;

		SoundEffect bang = new SoundEffect(this.position, "data/sound/Bang1.ogg", 2, 0.9f, 1.1f);
		bang.setVolume(0.35f);
		bang.attachToParent(this, "snd_Bang");

		SoundEffect pickup = new SoundEffect(this.position, "data/sound/GunCock1.ogg", 2, 0.9f, 1.1f);
		pickup.setVolume(0.35f);
		pickup.attachToParent(this, "snd_Pickup");

		SoundEffect charge = null;

		SoundEffect noAmmo = new SoundEffect(this.position, "data/sound/slap.ogg", 2, 0.9f, 1.1f);
		noAmmo.setVolume(0.35f);
		noAmmo.attachToParent(this, "snd_NoAmmo");

		Entity particleContainer = new Entity();
		particleContainer.attachToParent(this, "particle_container");
	}

	// public Gun(Vec2f position, SpritePath itemSprite) {
	// super(position, itemSprite);
	// coll = new Collider(position, new Vec2f(40, 40));
	// switch (itemSprite) {
	// case assault:
	// fire = new Timer(0.10);
	// fire.attachToParent(this, "attack timer");
	//
	// thrust = 500;
	// recoil = 0.5f;
	// damage = 25f;
	// bulletSpeed = 4000;
	// cannonLength = 40.0;
	//
	// tpscharge = 0;
	// chargeInertia = 0;
	//
	// SoundEffect bangSound1 = new SoundEffect(this.position,
	// "data/sound/Bang1.ogg", 2);
	// bangSound1.setVolume(3f);
	// bangSound1.attachToParent(this, "snd_Bang");
	// break;
	// case minigun:
	// fire = new Timer(0.03);
	// fire.attachToParent(this, "attack timer");
	//
	// thrust = 500;
	// recoil = 0.30f;
	// damage = 1f;
	// bulletSpeed = 4000;
	// cannonLength = 65.0;
	// tpscharge = 0.8;
	// chargeInertia = 0.2;
	//
	// SoundEffect bangSound2 = new SoundEffect(this.position,
	// "data/sound/Bang2.ogg", 2);
	// bangSound2.setVolume(3f);
	// bangSound2.attachToParent(this, "snd_Bang");
	// break;
	// case iongun:
	// fire = new Timer(0.10);
	// fire.attachToParent(this, "attack timer");
	//
	// sndCharge = new SoundEffect(this.position, "data/sound/IonChargeV2_3.ogg",
	// -1, 1, 1);
	// sndCharge.attachToParent(this, "snd_charge");
	//
	// thrust = 100;
	// recoil = 0.4f;
	// damage = 8f;
	// bulletSpeed = 2500;
	// cannonLength = 55.0;
	//
	// tpscharge = 0.6;
	// chargeInertia = 0;
	//
	// SoundEffect bangSound3 = new SoundEffect(this.position,
	// "data/sound/BangIonGun2.ogg", 2, 0.9f, 1.1f);
	// bangSound3.setVolume(0.35f);
	// bangSound3.attachToParent(this, "snd_Bang");
	//
	// handPosL = new Vec2f(8, 20);
	//
	// handPosR = new Vec2f(-15, 20);
	// break;
	// default:
	// break;
	// }
	//
	// SoundEffect pickup = new SoundEffect(this.position,
	// "data/sound/GunCock1.ogg", 1);
	// pickup.setVolume(0.5f);
	// pickup.attachToParent(this, "snd_Pickup");
	//
	// Entity particleContainer = new Entity();
	// particleContainer.attachToParent(this, "particle_container");
	// }

	@Override
	public void attackStart() {
		if (nbAmmo > 0) {
			timerAttack.setIncreasing(true);
			timerAttack.setProcessing(true);
		} else {
			timerAttack.setValue(0.05);
			super.attackStart();
		}
	}

	@Override
	public void attackStop() {
		timerAttack.setIncreasing(false);
	}

	/* (non-Javadoc)
	 * @see arenashooter.entities.spatials.items.Weapon#step(double)
	 */
	@Override
	public void step(double d) {

		boolean charge = timerAttack.isOver();

		if (isEquipped()) {
			Vec2f targetOffSet = Vec2f.rotate(new Vec2f(50, 0), rotation);

			localPosition.x = (float) Utils.lerpD((double) localPosition.x, targetOffSet.x, Math.min(1, d * 55));
			localPosition.y = (float) Utils.lerpD((double) localPosition.y, targetOffSet.y, Math.min(1, d * 55));
			rotation = Utils.lerpAngle(rotation, ((Character) parent).aimInput, Math.min(1, d * 17));
		}

		if (sndCharge != null) {
			if (charge) {
				sndChargeVol = Utils.lerpF(sndChargeVol, 0.20f, d * 15);
				sndChargePitch = Utils.lerpF(sndChargePitch, 3.5f, d * 4.5);
			} else {
				sndChargeVol = Utils.lerpF(sndChargeVol, 0, d * .04);
				sndChargePitch = Utils.lerpF(sndChargePitch, .01f, d * 2.5);
			}
			sndCharge.setVolume(sndChargeVol);
			if (sndCharge.getSound() instanceof SoundSourceSingle)
				((SoundSourceSingle) sndCharge.getSound()).setPitch(sndChargePitch);
		}

		if (charge) {
			timerAttack.restart();

			Vec2f aim = Vec2f.fromAngle(rotation);

			Vec2f bulSpeed = Vec2f.multiply(aim, bulletSpeed);
			Vec2f bulletPos = pos();
			bulletPos.add(Vec2f.multiply(aim, cannonLength));

			Particles flash;
			
			if (nbAmmo > 0) {
				nbAmmo--;
				
				switch (bulletType) {
				case 0:
					Bullet bul = new Bullet(bulletPos, bulSpeed, damage);
					bul.attachToParent(GameMaster.gm.getMap(), ("bullet_" + bul.genName()));
					if (isEquipped())
						bul.shooter = ((Character) parent);
					flash = new Particles(bulletPos, "data/particles/flash_01.xml");
					flash.attachToParent(children.get("particle_container"), "particles_flash");
					break;

				case 1:
					flash = new Particles(bulletPos, "data/particles/flash_02.xml");
					flash.attachToParent(children.get("particle_container"), "particles_flash");

					CircleBullet bull = new CircleBullet(bulletPos, bulSpeed, damage, false);
					CircleBullet bull2 = new CircleBullet(bulletPos, bulSpeed, damage, true);

					if (isEquipped()) {
						bull.shooter = ((Character) parent);
						bull2.shooter = ((Character) parent);
					}
					bull.attachToParent(GameMaster.gm.getMap(), ("bullet_" + bull.genName()));
					bull2.attachToParent(GameMaster.gm.getMap(), ("bullet_" + bull2.genName()));
					break;

				default:

					Bullet bul1 = new Bullet(bulletPos, bulSpeed, damage);
					bul1.attachToParent(GameMaster.gm.getMap(), ("bullet_" + bul1.genName()));
					if (isEquipped())
						bul1.shooter = ((Character) parent);
					flash = new Particles(bulletPos, "data/particles/flash_01.xml");
					flash.attachToParent(children.get("particle_container"), "particles_flash");

					break;
				}
				if (isEquipped()) {
					getVel().add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), recoil * 5000));
					((Character) parent).vel.add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust));
				} else {
					getVel().add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust / 10));
				}

				((SoundEffect) children.get("snd_Bang")).play();

				Particles shell = new Particles(bulletPos, "data/particles/shell_01.xml");
				shell.selfDestruct = true;
				shell.attachToParent(this, shell.genName());

				rotation += ((Math.random()) - 0.5) * recoil;

				// Add camera shake
				Window.getCamera().setCameraShake(2.8f);
			}

		}

		getSprite().rotation = rotation;

		super.step(d);
	}

	@Override
	protected void setLocalPositionOfSprite() {
		localPosition = Vec2f.rotate(new Vec2f(20, 0), rotation);
	}
}
