package arenashooter.entities.spatials.items;

import arenashooter.engine.audio.SoundSourceSingle;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CircleBullet;
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
	protected String bangSound = "";
	protected String noAmmoSound = "";
	protected double size = 0;
	protected Timer timerCooldown = null;

	/** Time before the first bullet is fired */
	protected Timer timerWarmup = null;
	protected SoundEffect sndWarmup = null;
	protected float sndChargeVol, sndChargePitch;

	private int nbAmmo;

	public Gun(Vec2f position, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			String soundPickup, double cooldown, int uses, String animPath, double warmupDuration, String soundWarmup,
			String bangSound, String noAmmoSound, int bulletType, float bulletSpeed, float damage, double cannonLength,
			double recoil, double thrust, double size) {
		super(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses, animPath,
				warmupDuration, soundWarmup, bangSound);

		this.nbAmmo = uses;
		this.bulletType = bulletType;
		this.bulletSpeed = bulletSpeed;
		this.cannonLength = cannonLength;
		this.damage = damage;
		this.recoil = (float) recoil;
		this.thrust = (float) thrust;
		this.warmupDuration = warmupDuration;
		this.bangSound = bangSound;
		this.noAmmoSound = noAmmoSound;
		this.size = size;

		SoundEffect attack = new SoundEffect(new Vec2f(), "data/sound/" + bangSound + ".ogg", 2, 0.85f, 1.15f);
		attack.setVolume(0.25f);
		attack.attachToParent(this, "snd_Bang");

		if(soundWarmup == null || soundWarmup.isEmpty()) {
			sndWarmup = null;
		} else {
			sndWarmup = new SoundEffect(new Vec2f(), "data/sound/" + soundWarmup + ".ogg", -1, 1, 1);
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

		SoundEffect noAmmo = new SoundEffect(this.parentPosition, "data/sound/" + noAmmoSound + ".ogg", 1, 0.85f, 1.15f);
		noAmmo.setVolume(0.25f);
		noAmmo.attachToParent(this, "snd_NoAmmo");

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
				Vec2f bulletPos = pos();
				bulletPos.add(Vec2f.multiply(aim, cannonLength));

				Particles flash;
				
				nbAmmo--;

				switch (bulletType) {
				case 0:
					Bullet bul = new Bullet(bulletPos, bulSpeed, damage);
					bul.attachToParent(getMap(), ("bullet_" + bul.genName()));
					if (isEquipped())
						bul.shooter = ((Character) parent);
					flash = new Particles(bulletPos, "data/particles/flash_01.xml");
					flash.attachToParent(getChildren().get("particle_container"), "particles_flash");
					break;

				case 1:
					flash = new Particles(bulletPos, "data/particles/flash_02.xml");
					flash.attachToParent(getChildren().get("particle_container"), "particles_flash");

					CircleBullet bull = new CircleBullet(bulletPos, bulSpeed, damage, false);
					CircleBullet bull2 = new CircleBullet(bulletPos, bulSpeed, damage, true);

					if (isEquipped()) {
						bull.shooter = ((Character) parent);
						bull2.shooter = ((Character) parent);
					}
					bull.attachToParent(getMap(), ("bullet_" + bull.genName()));
					bull2.attachToParent(getMap(), ("bullet_" + bull2.genName()));
					break;

				default:

					Bullet bul1 = new Bullet(bulletPos, bulSpeed, damage);
					bul1.attachToParent(getMap(), ("bullet_" + bul1.genName()));
					if (isEquipped())
						bul1.shooter = ((Character) parent);
					flash = new Particles(bulletPos, "data/particles/flash_01.xml");
					flash.attachToParent(getChildren().get("particle_container"), "particles_flash");

					break;
				}
				if (isEquipped()) {
					getVel().add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), recoil * 5000));
					((Character) parent).vel.add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust));
				} else {
					getVel().add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust / 10));
				}

				((SoundEffect) getChildren().get("snd_Bang")).play();

				Particles shell = new Particles(bulletPos, "data/particles/shell_01.xml");
				shell.selfDestruct = true;
				shell.attachToParent(this, shell.genName());

				rotation += ((Math.random()) - 0.5) * recoil;

				// Add camera shake
				Window.getCamera().setCameraShake(2.8f);
			} else {
				((SoundEffect) getChildren().get("snd_NoAmmo")).play();
			}

		}

		getSprite().rotation = rotation;

		super.step(d);
	}

	@Override
	protected void setLocalPositionOfSprite() {
		localPosition = Vec2f.rotate(new Vec2f(20, 0), rotation);
	}
	
	@Override
	public Gun clone(Vec2f position) {
		Gun gun = new Gun(position, this.genName(), weight, pathSprite, handPosL, handPosR, soundPickup, fireRate, uses, animPath, warmupDuration, soundWarmup, bangSound, noAmmoSound, bulletType, bulletSpeed, damage, cannonLength, recoil, thrust, size);
		return gun;
	}
}
