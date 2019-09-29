package arenashooter.entities.spatials.items;

import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.entities.Entity;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CircleBullet;
import arenashooter.entities.spatials.Grenade;
import arenashooter.entities.spatials.Particles;
import arenashooter.entities.spatials.SoundEffect;
import arenashooter.entities.spatials.StarBullet;
import arenashooter.game.Main;

public class Gun extends Usable {
	protected float recoil = 0.4f;// High
	protected float thrust = 0;//
	protected double cannonLength = 1.0;
	protected int bulletType = 0;
	protected float bulletSpeed = 10;
	protected float damage = 0f;
	protected String soundNoAmmo = "data/sound/no_ammo_01.ogg";
	/** Time before the first bullet is fired */
	protected Timer timerWarmup = new Timer(warmup);
	protected SoundEffect sndWarmup = null;
	protected float sndChargeVol;
	protected float sndChargePitch;

	private Gun() {
		super(Main.getRenderer().getDefaultTexture().getPath());
	}
	
	/**
	 * 
	 * @param localPosition
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
	public Gun(Vec2fi localPosition, String name, double weight, String pathSprite, Vec2fi handPosL, Vec2fi handPosR,
			Vec2fi extent, String soundPickup, double cooldown, int uses, String animPath, double warmupDuration,
			String soundWarmup, String soundFire, String soundNoAmmo, int bulletType, float bulletSpeed, float damage,
			double cannonLength, double recoil, double thrust) {

		super(localPosition, name, weight, pathSprite, handPosL, handPosR, extent, soundPickup, cooldown, uses,
				animPath, warmupDuration, soundWarmup, soundFire);

		this.bulletType = bulletType;
		this.bulletSpeed = bulletSpeed;
		this.cannonLength = cannonLength;
		this.damage = damage;
		this.recoil = (float) recoil;
		this.thrust = (float) thrust;
		this.soundNoAmmo = soundNoAmmo;
		this.extent.set(extent);

		if (soundWarmup == null || soundWarmup.isEmpty()) {
			sndWarmup = null;
		} else {
			sndWarmup = new SoundEffect(new Vec2f(), soundWarmup, AudioChannel.SFX, 0, 1, 1, true);
			sndWarmup.attachToParent(this, "snd_Warmup");
		}

		// Warmup
		setTimerWarmup(new Timer(warmupDuration));

		Entity particleContainer = new Entity();
		particleContainer.attachToParent(this, "particle_container");
	}

	/**
	 * Constructor for the Editor to avoid a new Item creation for each change state
	 * 
	 * @author Nathan
	 * @param sprite
	 */
	public Gun(String sprite) {
		super(sprite);
		// Warmup
		setTimerWarmup(new Timer(warmup));

		Entity particleContainer = new Entity();
		particleContainer.attachToParent(this, "particle_container");
	}

	public Timer getTimerWarmup() {
		return timerWarmup;
	}

	public void setTimerWarmup(Timer timerWarmup) {
		this.timerWarmup = timerWarmup;
		this.timerWarmup.setIncreasing(false);
		this.timerWarmup.setProcessing(true);
		this.timerWarmup.attachToParent(this, "timer_warmup");
	}

	public void setRecoil(float recoil) {
		this.recoil = recoil;
	}

	public void setThrust(float thrust) {
		this.thrust = thrust;
	}

	public void setCannonLength(double cannonLength) {
		this.cannonLength = cannonLength;
	}

	public void setBulletType(int bulletType) {
		this.bulletType = bulletType;
	}

	public void setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setSoundNoAmmo(String soundNoAmmo) {
		this.soundNoAmmo = soundNoAmmo;
	}

	public void setExtent(Vec2fi extent) {
		this.extent.set(extent);
	}

	public void setSndWarmup(SoundEffect sndWarmup) {
		this.sndWarmup = sndWarmup;
		sndWarmup.attachToParent(this, "snd_Warmup");
	}

	/**
	 * @return the recoil
	 */
	public float getRecoil() {
		return recoil;
	}

	/**
	 * @return the thrust
	 */
	public float getThrust() {
		return thrust;
	}

	/**
	 * @return the cannonLength
	 */
	public double getCannonLength() {
		return cannonLength;
	}

	/**
	 * @return the bulletType
	 */
	public int getBulletType() {
		return bulletType;
	}

	/**
	 * @return the bulletSpeed
	 */
	public float getBulletSpeed() {
		return bulletSpeed;
	}

	/**
	 * @return the damage
	 */
	public float getDamage() {
		return damage;
	}

	/**
	 * @return the warmupDuration
	 */
	public double getWarmupDuration() {
		return warmup;
	}

	/**
	 * @return the soundNoAmmo
	 */
	public String getSoundNoAmmo() {
		return soundNoAmmo;
	}

	/**
	 * @return the size
	 */
	public Vec2f getExtent() {
		return extent;
	}

	/**
	 * @return the timerCooldown
	 */
	public Timer getTimerCooldown() {
		return timerCooldown;
	}

	/**
	 * @return the sndWarmup
	 */
	public SoundEffect getSndWarmup() {
		return sndWarmup;
	}

	/**
	 * @return the sndChargeVol
	 */
	public float getSndChargeVol() {
		return sndChargeVol;
	}

	/**
	 * @return the sndChargePitch
	 */
	public float getSndChargePitch() {
		return sndChargePitch;
	}

	/**
	 * @return the nbAmmo
	 */
	public int getNbAmmo() {
		return uses;
	}

	@Override
	public void attackStart(boolean demo) {
		this.demo = demo;
		timerWarmup.setIncreasing(true);
		if (sndWarmup != null && !sndWarmup.isPlaying())
			sndWarmup.play();
	}

	@Override
	public void attackStop() {
		timerWarmup.setIncreasing(false);
	}

	@Override
	public void step(double d) {
		if (sndWarmup != null) {
			if (timerWarmup.isIncreasing()) {
				sndChargeVol = Utils.lerpF(sndChargeVol, 0.2f, d * 15);
				sndChargePitch = Utils.lerpF(sndChargePitch, 3.5f, d * 4.5);
			} else {
				sndChargeVol = Utils.lerpF(sndChargeVol, 0, d * .04);
				sndChargePitch = Utils.lerpF(sndChargePitch, .01f, d * 2.5);
			}
			sndWarmup.setVolume(sndChargeVol);
			sndWarmup.setPitch(sndChargePitch);
		}

		// Spawn projectile
		if (timerWarmup.isIncreasing() && timerWarmup.isOver() && timerCooldown.isOver()) {
			timerCooldown.restart();
			if (uses > 0) {
				Vec2f aim = Vec2f.fromAngle(getWorldRot());

				Vec2f bulSpeed = Vec2f.multiply(aim, bulletSpeed);
				Vec2f bulletPos = new Vec2f(getWorldPos());
				bulletPos.add(Vec2f.multiply(aim, cannonLength));

				Particles flash;
				float damage = 0;

				if (!demo) {
					setUses(uses - 1);
					damage = this.damage;
				}

				switch (bulletType) {
				case 0:
					Bullet bul = new Bullet(bulletPos, bulSpeed, damage);
					bul.attachToParent(getArena(), ("bullet_" + bul.genName()));
					if (isEquipped())
						bul.shooter = ((Character) getParent());
					flash = new Particles(bulletPos, "data/particles/flash_01.particles");
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
					bull.attachToParent(getArena(), ("bullet_" + bull.genName()));
					bull2.attachToParent(getArena(), ("bullet_" + bull2.genName()));
					break;

				case 2:
					Grenade bul2 = new Grenade(bulletPos, bulSpeed, damage);
					bul2.attachToParent(getArena(), ("grenade_" + bul2.genName()));
					if (isEquipped())
						bul2.shooter = ((Character) getParent());
//					flash = new Particles(bulletPos, "data/particles/flash_01.particles");
//					flash.attachToParent(getChild("particle_container"), "particles_flash");
					break;

				case 3:
					StarBullet star = new StarBullet(bulletPos, bulSpeed, damage);
					star.attachToParent(getArena(), ("star_" + star.genName()));
					if (isEquipped())
						star.shooter = ((Character) getParent());
					break;

				default:
					Bullet bul1 = new Bullet(bulletPos, bulSpeed, damage);
					bul1.attachToParent(getArena(), ("bullet_" + bul1.genName()));
					if (isEquipped())
						bul1.shooter = ((Character) getParent());
					flash = new Particles(bulletPos, "data/particles/flash_01.particles");
					flash.attachToParent(getChild("particle_container"), "particles_flash");
					break;
				}

				// Recoil
				Vec2f recoilDir = Vec2f.rotate(aim, Math.PI);
				if (isEquipped()) {
					getVel().add(Vec2f.multiply(recoilDir, recoil * 5000));
					Character parent = ((Character) getParent());
					parent.setLinearVelocity(Vec2f.add(parent.getLinearVelocity(), Vec2f.multiply(recoilDir, thrust)));
				} else {
					getVel().add(Vec2f.multiply(recoilDir, thrust / 10));
				}

				Main.getAudioManager().playSound2D(soundFire, AudioChannel.SFX, .25f, Utils.lerpF(.8f, 1.2f, Math.random()),
						getWorldPos());

				Particles shell = new Particles(new Vec2f(), "data/particles/shell_01.xml");
				shell.selfDestruct = true;
				shell.attachToParent(this, shell.genName());

				localRotation += ((Math.random()) - 0.5) * recoil;

				// Add camera shake
				Main.getRenderer().getCamera().setCameraShake(.028f);
			} else {
				Main.getAudioManager().playSound2D(soundNoAmmo, AudioChannel.SFX, .25f, Utils.lerpF(.8f, 1.2f, Math.random()),
						getWorldPos());
			}

		}

//		getSprite().localRotation = getWorldRot();

		super.step(d);
	}

	@Override
	protected void setLocalPositionOfSprite() {
		Vec2f.rotate(new Vec2f(1, 0), getWorldRot(), localPosition);
	}

	@Override
	public Gun clone() {
		Gun gun = new Gun(localPosition, this.genName(), weight, pathSprite, handPosL, handPosR, extent, soundPickup,
				fireRate, uses, animPath, warmup, soundWarmup, soundFire, soundNoAmmo, bulletType, bulletSpeed, damage,
				cannonLength, recoil, thrust);
		return gun;
	}

	
	/*
	 * JSON
	 */
	
	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();
		
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return recoil;
			}
			@Override
			public String getKey() {
				return "recoil";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				recoil = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return thrust;
			}
			@Override
			public String getKey() {
				return "thrust";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				thrust = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return cannonLength;
			}
			@Override
			public String getKey() {
				return "cannonLength";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				cannonLength = json.getDouble(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return soundNoAmmo;
			}
			@Override
			public String getKey() {
				return "sound no ammo";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				soundNoAmmo = json.getString(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return getBulletType();
			}
			@Override
			public String getKey() {
				return "bullet type";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				setBulletType(json.getInteger(this));
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return getBulletSpeed();
			}
			@Override
			public String getKey() {
				return "bullet speed";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				setBulletSpeed(json.getFloat(this));
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return damage;
			}
			@Override
			public String getKey() {
				return "damage";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				damage = json.getFloat(this);
			}
		});
		return set;
	}
	
	public static Gun fromJson(JsonObject json) throws Exception {
		Gun e = new Gun();
		useKeys(e, json);
		return e;
	}
}
