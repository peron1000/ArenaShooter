package arenashooter.entities.spatials.items;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.SoundSourceSingle;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CircleBullet;
import arenashooter.entities.spatials.Grenade;
import arenashooter.entities.spatials.Particles;
import arenashooter.entities.spatials.SoundEffect;

public class Gun extends Usable {
	protected float recoil = 0.4f;// High
	protected float thrust = 0;//
	protected double cannonLength = 1.0;
	protected int bulletType = 0;
	protected float bulletSpeed = 1;
	protected float damage = 0f;
	protected double warmupDuration = 0;
	protected String soundNoAmmo = "";
	protected double size = 0;
	protected Timer timerCooldown = null;

	/** Time before the first bullet is fired */
	protected Timer timerWarmup = null;
	protected SoundEffect sndWarmup = null;
	protected float sndChargeVol, sndChargePitch;

	protected int nbAmmo;

	/**
	 * 
	 * @param position
	 * @param name
	 * @param weight
	 * @param pathSprite
	 * @param handPosL
	 * @param handPosR
	 * @param soundPickup
	 * @param cooldown
	 * @param uses
	 * @param animPath
	 * @param warmupDuration
	 * @param soundWarmup
	 * @param soundFire
	 * @param soundNoAmmo
	 * @param bulletType
	 * @param bulletSpeed
	 * @param damage
	 * @param cannonLength
	 * @param recoil
	 * @param thrust
	 * @param size
	 */
	public Gun(Vec2f position, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			String soundPickup, double cooldown, int uses, String animPath, double warmupDuration, String soundWarmup,
			String soundFire, String soundNoAmmo, int bulletType, float bulletSpeed, float damage, double cannonLength,
			double recoil, double thrust, double size) {
		
		super(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses, animPath,
				warmupDuration, soundWarmup, soundFire);

		this.nbAmmo = uses;
		this.bulletType = bulletType;
		this.bulletSpeed = bulletSpeed;
		this.cannonLength = cannonLength;
		this.damage = damage;
		this.recoil = (float) recoil;
		this.thrust = (float) thrust;
		this.warmupDuration = warmupDuration;
		this.soundNoAmmo = soundNoAmmo;
		this.size = size;

		if(soundWarmup == null || soundWarmup.isEmpty()) {
			sndWarmup = null;
		} else {
			sndWarmup = new SoundEffect(new Vec2f(), soundWarmup, -1, 1, 1);
			sndWarmup.setVolume(0);
			sndWarmup.play();
			sndWarmup.attachToParent(this, "snd_Warmup");
		}
		
		//Warmup
		this.timerWarmup = new Timer(warmupDuration);
		this.timerWarmup.setIncreasing(false);
		this.timerWarmup.setProcessing(true);
		this.timerWarmup.attachToParent(this, "timer_warmup");
		
		//Cooldown
		this.timerCooldown = new Timer(fireRate);
		this.timerCooldown.setIncreasing(true);
		this.timerCooldown.setProcessing(true);
		this.timerCooldown.setValue(fireRate);
		this.timerCooldown.attachToParent(this, "timer_cooldown");

//		SoundEffect noAmmo = new SoundEffect(this.parentPosition, noAmmoSound, 1, 0.85f, 1.15f);
//		noAmmo.setVolume(0.25f);
//		noAmmo.attachToParent(this, "snd_NoAmmo");

		Entity particleContainer = new Entity();
		particleContainer.attachToParent(this, "particle_container");
	}

	@Override
	public void attackStart() {
		timerWarmup.setIncreasing(true);
	}

	@Override
	public void attackStop() {
		timerWarmup.setIncreasing(false);
	}

	@Override
	public void step(double d) {
		if (sndWarmup != null) {
			if (timerWarmup.isIncreasing()) {
				sndChargeVol = Utils.lerpF(sndChargeVol, 0.20f, d * 15);
				sndChargePitch = Utils.lerpF(sndChargePitch, 3.5f, d * 4.5);
			} else {
				sndChargeVol = Utils.lerpF(sndChargeVol, 0, d * .04);
				sndChargePitch = Utils.lerpF(sndChargePitch, .01f, d * 2.5);
			}
			sndWarmup.setVolume(sndChargeVol);
			if (sndWarmup.getSound() instanceof SoundSourceSingle)
				((SoundSourceSingle) sndWarmup.getSound()).setPitch(sndChargePitch);
		}
		
		//Spawn projectile
		if (timerWarmup.isIncreasing() && timerWarmup.isOver() && timerCooldown.isOver()) {
			timerCooldown.restart();

			if (nbAmmo > 0) {
				Vec2f aim = Vec2f.fromAngle(rotation);

				Vec2f bulSpeed = Vec2f.multiply(aim, bulletSpeed);
				Vec2f bulletPos = getWorldPos();
				bulletPos.add(Vec2f.multiply(aim, cannonLength));

				Particles flash;
				
				nbAmmo--;

				switch (bulletType) {
				case 0:
					Bullet bul = new Bullet(bulletPos, bulSpeed, damage);
					bul.attachToParent(getMap(), ("bullet_" + bul.genName()));
					if (isEquipped())
						bul.shooter = ((Character) getParent());
					flash = new Particles(bulletPos, "data/particles/flash_01.xml");
					flash.attachToParent(getChild("particle_container"), "particles_flash");
					break;

				case 1:
					flash = new Particles(bulletPos, "data/particles/flash_02.xml");
					flash.attachToParent(getChild("particle_container"), "particles_flash");

					CircleBullet bull = new CircleBullet(bulletPos, bulSpeed, damage, false);
					CircleBullet bull2 = new CircleBullet(bulletPos, bulSpeed, damage, true);

					if (isEquipped()) {
						bull.shooter = ((Character) getParent());
						bull2.shooter = ((Character) getParent());
					}
					bull.attachToParent(getMap(), ("bullet_" + bull.genName()));
					bull2.attachToParent(getMap(), ("bullet_" + bull2.genName()));
					break;
					
				case 2:
					Grenade bul2 = new Grenade(bulletPos, bulSpeed, damage);
					bul2.attachToParent(getMap(), ("grenade_" + bul2.genName()));
					if (isEquipped())
						bul2.shooter = ((Character) getParent());
//					flash = new Particles(bulletPos, "data/particles/flash_01.xml");
//					flash.attachToParent(getChild("particle_container"), "particles_flash");
					break;

				default:
					Bullet bul1 = new Bullet(bulletPos, bulSpeed, damage);
					bul1.attachToParent(getMap(), ("bullet_" + bul1.genName()));
					if (isEquipped())
						bul1.shooter = ((Character) getParent());
					flash = new Particles(bulletPos, "data/particles/flash_01.xml");
					flash.attachToParent(getChild("particle_container"), "particles_flash");
					break;
				}
				
				//Recoil
				Vec2f recoilDir = Vec2f.rotate(aim, Math.PI);
				if (isEquipped()) {
					getVel().add(Vec2f.multiply(recoilDir, recoil * 5000));
//					((Character) getParent()).vel.add(Vec2f.multiply(recoilDir, thrust));
					((Character) getParent()).applyImpulse(Vec2f.multiply(recoilDir, thrust));
				} else {
					getVel().add(Vec2f.multiply(recoilDir, thrust / 10));
				}

				Audio.playSound2D(soundFire, .25f, Utils.lerpF(.8f, 1.2f, Math.random()), getWorldPos());

				Particles shell = new Particles(bulletPos, "data/particles/shell_01.xml");
				shell.selfDestruct = true;
				shell.attachToParent(this, shell.genName());

				rotation += ((Math.random()) - 0.5) * recoil;

				// Add camera shake
				Window.getCamera().setCameraShake(.028f);
			} else {
				Audio.playSound2D(soundNoAmmo, .25f, Utils.lerpF(.8f, 1.2f, Math.random()), getWorldPos());
			}

		}

		getSprite().rotation = rotation;

		super.step(d);
	}

	@Override
	protected void setLocalPositionOfSprite() {
		Vec2f.rotate(new Vec2f(.5, 0), rotation, localPosition);
	}
	
	@Override
	public Gun clone(Vec2f position) {
		Gun gun = new Gun(position, this.genName(), weight, pathSprite, handPosL, handPosR, soundPickup, fireRate, uses, animPath, warmupDuration, soundWarmup, soundFire, soundNoAmmo, bulletType, bulletSpeed, damage, cannonLength, recoil, thrust, size);
		return gun;
	}
}
