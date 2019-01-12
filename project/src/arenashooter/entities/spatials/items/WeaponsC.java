package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.game.Game;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Sprite;

public class WeaponsC extends Item {

	private double dispersion = 0.05;// la non-précision en radians.
	private Timer fire = new Timer(0.15);
	Collider coll;

	public WeaponsC(Vec2f position, ItemSprite itemSprite) {
		super(position, itemSprite);
		fire.attachToParent(this, "attack timer");
		tag = "Arme";
		coll = new Collider(position, new Vec2f(40, 40));
	}

	public void fire(boolean lookRight) { // Visée uniquement droite et gauche pour l'instant. TODO :
		if (fire.isOver()) {
			float pX = 0;
			float vX = 0;
			if (parent instanceof Character) {
				if (lookRight) {
					pX = position.x + 70;
					vX = 2000;
				} else {
					pX = position.x - 70;
					vX = -2000;
				}
			}
			fire.restart();

			double coeff = (2 * Math.random()) - 1;

			Vec2f angle = Vec2f.rotate(new Vec2f(vX, 0), dispersion * coeff);
			angle.x += ((Character) parent).vel.x/3;
			angle.y += ((Character) parent).vel.y/3;

			Bullet bul = new Bullet(new Vec2f(pX, position.y), angle);
			bul.attachToParent(Game.game.map, ("bullet" + bul.genName()));
		}
	}

	public void step(double d) {
		if (parent != null && parent instanceof Character && children.get("item_Sprite") instanceof Sprite)
			if (((Character) parent).lookRight)
				((Sprite) children.get("item_Sprite")).flipX = false;
			else
				((Sprite) children.get("item_Sprite")).flipX = true;

		super.step(d);

	}
}
