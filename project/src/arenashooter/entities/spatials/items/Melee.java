package arenashooter.entities.spatials.items;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.items.Item.SpritePath;
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
		SoundEffect pickup = new SoundEffect(this.position, "data/sound/swordraw.ogg", 1);
		pickup.setVolume(0.5f);
		pickup.attachToParent(this, "snd_Pickup");
	}

	@Override
	public void attack() {
		if (fire.isOver()) {
			fire.restart();
			CharacterSprite skeleton = ((CharacterSprite) children.get("skeleton"));
			if (skeleton != null)
				System.out.println("lol");

			for (Entity entity : GameMaster.gm.getEntities()) {
				if (entity instanceof Character && entity != this) {
					Character c = (Character) entity;

					boolean isInFrontOfMe = false;
					if (skeleton != null) {
						if ((lookRight && collider.getXRight() < (c.collider.getXRight() + 40))
								|| (!lookRight && collider.getXLeft() > (c.collider.getXLeft() - 40))) {
							isInFrontOfMe = true;
						}
					}

					if (isInFrontOfMe) {
						float xDiff = Math.abs(position.x - c.position.x);
						float yDiff = Math.abs(position.y - c.position.y);
						if (xDiff < 175 && yDiff < 175) {
							c.takeDamage(5, lookRight);
							((SoundEffect) children.get("snd_Punch_Hit")).play();
						}
					}

				}
			}
		}

	}
}
