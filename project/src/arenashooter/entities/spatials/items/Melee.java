package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;
import arenashooter.game.GameMaster;

public class Melee extends Weapon {

	private Timer fire = new Timer(0.15);
	public boolean lookRight = true;
	Collider collider;
	private float damage = 10f;

	public Melee(Vec2f position, SpritePath itemSprite) {
		super(position, itemSprite);
		collider = new Collider(position, new Vec2f(40, 40));
		if (itemSprite == SpritePath.katana) {
			fire = new Timer(0.10);
			fire.attachToParent(this, "attack timer");
//			SoundEffect bangSound = new SoundEffect(this.position, "data/sound/Bang1.ogg", 2);
//			bangSound.setVolume(3f);
//			bangSound.attachToParent(this, "snd_Bang");
		}
		SoundEffect pickup = new SoundEffect(this.position, "data/sound/swordraw.ogg", 2);
		pickup.setVolume(0.5f);
		pickup.attachToParent(this, "snd_Pickup");
	}

	@Override
	public void attackStart() {
		if (fire.isOver()) {
			fire.restart();
			for (Entity path : GameMaster.gm.getEntities()) {
				if (path instanceof Character) {
					for (Entity coll : ((Character) path).children.values()) {
						if (coll instanceof Collider) {
							Collider c = (Collider) coll;
							if (c.isColliding(collider)) {
								((Character) path).takeDamage(damage, true);
								break;
							}
						}
					}
				}
			}
		}
	}
}
