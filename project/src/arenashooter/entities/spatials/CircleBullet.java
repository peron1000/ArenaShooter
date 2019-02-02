package arenashooter.entities.spatials;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class CircleBullet extends Bullet {

	private double movementTime = 0;

	public CircleBullet(Vec2f position, Vec2f vel, float damage) {
		super(position, vel, damage);
	}

	@Override
	public void step(double d) {

		movementTime += d;

		double sin = Math.sin(movementTime * .05);
		double cos = Math.cos(movementTime * .05);
	}
}
