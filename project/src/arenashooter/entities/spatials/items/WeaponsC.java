package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.game.Game;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Sprite;

public class WeaponsC extends Item {

	private double dispersion = 0.15;// la non-précision en radians.
	private Timer fire = new Timer(0.05);
	Collider coll;
	private float recul = 4000;

	public WeaponsC(Vec2f position, ItemSprite itemSprite) {
		super(position, itemSprite);
		fire.attachToParent(this, "attack timer");
		tag = "Arme";
		coll = new Collider(position, new Vec2f(40, 40));

		SoundEffect bangSound = new SoundEffect(this.position, "data/sound/Bang1.ogg");
		bangSound.setVolume(5f);
		bangSound.attachToParent(this, "snd_Bang");
	}

	public void fire(boolean lookRight) { // Visée uniquement droite et gauche pour l'instant. TODO :
		if (fire.isOver()) {
			float pX = 0;
			float vX = 0;
			if (parent instanceof Character) {
				if (lookRight) {
					pX = position.x + 50;
					vX = 2000;
				} else {
					pX = position.x - 50;
					vX = -2000;
				}
			}
			fire.restart();

			double coeff = (2 * Math.random()) - 1;

			Vec2f angle = Vec2f.rotate(new Vec2f(vX, 0), dispersion * coeff);
			angle.x += ((Character) parent).vel.x / 4;
			angle.y += ((Character) parent).vel.y / 4;

			Bullet bul = new Bullet(new Vec2f(pX, position.y), angle);
			bul.attachToParent(Game.game.map, ("bullet" + bul.genName()));

			vel.add(Vec2f.multiply(Vec2f.normalize(Vec2f.rotate(angle, Math.PI)), recul));
			((SoundEffect) children.get("snd_Bang")).play();
		}
	}

	public void step(double d) {
		if (isEquipped()) {
			if (children.get("item_Sprite") instanceof Sprite)
				if (((Character) parent).lookRight)
					((Sprite) children.get("item_Sprite")).flipX = false;
				else
					((Sprite) children.get("item_Sprite")).flipX = true;
			vel.x = (float) Utils.lerpD(vel.x, 0, d * 50);
			vel.y = (float) Utils.lerpD(vel.y, 0, d * 50);
		}

		super.step(d);

	}
}
