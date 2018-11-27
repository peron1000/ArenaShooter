package arenashooter.entities;

import arenashooter.engine.Input;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2d;

public class Character extends Spatial {
	int pv;
	Vec2d vel;
	Collider collider;
	boolean isOnGround;

	public Character() {
		pv = 10;
		position = new Vec2d();
		rotation = 0;
		collider = new Collider(new Vec2d(160, 160));
	}
	
	public void jump() {
		if (isOnGround)
			vel.y = -800;
		// TODO: collider
	}

	public void attack() {
		// TODO: attac
	}

	
	@Override
	public void step(double d) {
		isOnGround = true;

		vel.x = Utils.lerpD(vel.x, Input.getAxis("moveX") * 500, d * 5);

		if (Input.actionPressed("jump"))
			jump();
		if (Input.actionPressed("attac"))
			attack();
		if (isOnGround)
			vel.y += 9.807 * 200 * d;

		position.add(Vec2d.multiply(vel, d));
		//position.y = Math.min(450, position.y);

		position.add(vel);
		super.step(d);
	}
}
