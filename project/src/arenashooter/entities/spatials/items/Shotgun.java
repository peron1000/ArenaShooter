package arenashooter.entities.spatials.items;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Bullet;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CircleBullet;
import arenashooter.entities.spatials.Particles;

public class Shotgun extends Gun {
	private int multiShot = 10;
	private double dispersion = 0.3;

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
	 * @param noAmmoSound
	 * @param multiShot
	 * @param dispersion
	 * @param bulletType
	 * @param bulletSpeed
	 * @param damage
	 * @param cannonLength
	 * @param recoil
	 * @param thrust
	 * @param size
	 */
	public Shotgun(Vec2f position, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			String soundPickup, double cooldown, int uses, String animPath, double warmupDuration, String soundWarmup,
			String soundFire, String noAmmoSound, int multiShot, double dispersion, int bulletType, float bulletSpeed,
			float damage, double cannonLength, double recoil, double thrust, double size) {

		super(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses, animPath,
				warmupDuration, soundWarmup, soundFire, noAmmoSound, bulletType, bulletSpeed, damage, cannonLength,
				recoil, thrust, size);
		this.multiShot = multiShot;
		this.dispersion = dispersion;
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
			sndWarmup.setPitch(sndChargePitch);
		}

		// Spawn projectile
		if (timerWarmup.isIncreasing() && timerWarmup.isOver() && timerCooldown.isOver()) {
			timerCooldown.restart();

			if (nbAmmo > 0) {
				Vec2f aim = Vec2f.fromAngle(getWorldRot());

				Vec2f bulSpeed = Vec2f.multiply(aim, bulletSpeed);
				Vec2f bulletPos = getWorldPos();
				bulletPos.add(Vec2f.multiply(aim, cannonLength));

				Particles flash;

				nbAmmo--;

				switch (bulletType) {
				case 0:
					for (int i = 0; i < multiShot; i++) {

						if (i > 3) {
							double rand = Math.random();
							Bullet bul = new Bullet(
									bulletPos, Vec2f.multiply((Vec2f.rotate(Vec2f.multiply(bulSpeed, 1 + (1 - rand) * 0.2),
															(Math.random()>=0.5 ? 1 : -1)* rand * dispersion)),
													1 + Math.random() / 4),
									damage);
							bul.attachToParent(getArena(), ("bullet_" + bul.genName()));
							if (isEquipped())
								bul.shooter = ((Character) getParent());
						} else {
							Bullet bul = new Bullet(bulletPos,
									Vec2f.multiply((Vec2f.rotate(Vec2f.multiply(bulSpeed,1.2), (Math.random() - 0.5) * 0.5 * dispersion)),
											1 + Math.random() / 4),
									damage);
							bul.attachToParent(getArena(), ("bullet_" + bul.genName()));
							if (isEquipped())
								bul.shooter = ((Character) getParent());
						}
					}
					flash = new Particles(bulletPos, "data/particles/flash_01.xml");
					flash.attachToParent(getChild("particle_container"), "particles_flash");
					break;

				case 1:
					flash = new Particles(bulletPos, "data/particles/flash_02.xml");
					flash.attachToParent(getChild("particle_container"), "particles_flash");

					for (int i = 0; i < multiShot; i++) {
						CircleBullet bull = new CircleBullet(bulletPos,
								Vec2f.multiply((Vec2f.rotate(bulSpeed, (Math.random() - 0.5) * 2 * dispersion)),
										1 + Math.random() / 4),
								damage, false);
						CircleBullet bull2 = new CircleBullet(bulletPos,
								Vec2f.multiply((Vec2f.rotate(bulSpeed, (Math.random() - 0.5) * 2 * dispersion)),
										1 + Math.random() / 4),
								damage, true);

						if (isEquipped()) {
							bull.shooter = ((Character) getParent());
							bull2.shooter = ((Character) getParent());
						}
						bull.attachToParent(getArena(), ("bullet_" + bull.genName()));
						bull2.attachToParent(getArena(), ("bullet_" + bull2.genName()));
					}
					break;

				default:

					Bullet bul1 = new Bullet(bulletPos, bulSpeed, damage);
					bul1.attachToParent(getArena(), ("bullet_" + bul1.genName()));
					if (isEquipped())
						bul1.shooter = ((Character) getParent());
					flash = new Particles(bulletPos, "data/particles/flash_01.xml");
					flash.attachToParent(getChild("particle_container"), "particles_flash");

					break;
				}
				if (isEquipped()) {
					getVel().add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), recoil * 5000));
//					((Character) getParent()).vel.add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust));
					((Character) getParent()).applyImpulse(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust));
				} else {
					getVel().add(Vec2f.multiply(Vec2f.rotate(aim, Math.PI), thrust / 10));
				}

				Audio.playSound2D(soundFire, AudioChannel.SFX, .25f, Utils.lerpF(.8f, 1.2f, Math.random()), getWorldPos());

				Particles shell = new Particles(new Vec2f(), "data/particles/shell_01.xml");
				shell.selfDestruct = true;
				shell.attachToParent(this, shell.genName());

				localRotation += ((Math.random()) - 0.5) * recoil;

				// Add camera shake
				Window.getCamera().setCameraShake(.029f);
			} else {
				Audio.playSound2D(soundNoAmmo, AudioChannel.SFX, .25f, Utils.lerpF(.8f, 1.2f, Math.random()), getWorldPos());
			}

		}

//		getSprite().localRotation = getWorldRot();

		super.step(d);
	}

	@Override
	public Shotgun clone(Vec2f position) {
		Shotgun gun = new Shotgun(position, this.genName(), weight, pathSprite, handPosL, handPosR, soundPickup,
				fireRate, uses, animPath, warmupDuration, soundWarmup, soundFire, soundNoAmmo, multiShot, dispersion,
				bulletType, bulletSpeed, damage, cannonLength, recoil, thrust, size);
		return gun;
	}
}
