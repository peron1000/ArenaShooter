package arenashooter.entities.spatials;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;

public class CircleBullet extends Bullet {

	private double movementTime = 0;

	public CircleBullet(Vec2f position, Vec2f vel, float damage) {
		super(position, vel, damage);
		
		Sprite bul = new Sprite(pos(), "data/sprites/Ion_Bullet.png");
		bul.size = new Vec2f(bul.tex.getWidth(), bul.tex.getHeight());
		bul.rotation = rotation;
		bul.attachToParent(this, "bul_Sprite");
	}

	@Override
	public void step(double d) {

		movementTime += d;

		double sin = Math.sin(movementTime * .05);
		double cos = Math.cos(movementTime * .05);
		super.step(d);
	}
}
