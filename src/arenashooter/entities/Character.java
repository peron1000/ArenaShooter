package arenashooter.entities;

import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.engine.Input.Axis;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2d;

public class Character extends Spatial {
	int pv;
	Vec2d vel = new Vec2d();
	Collider collider;
	boolean isOnGround =true;

	public Character() {
		pv = 10;
		position = new Vec2d();
		rotation = 0;
		collider = new Collider(new Vec2d(160, 160));
		Sprite body = new Sprite();
		body.tex = new Texture("data/UnMoineHD.png");
		body.attachToParent(body,"body_texture");
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
		for (Entity plat : getParent().children.values()) {
			if (plat instanceof Plateform) {
				for (Entity coll : ((Plateform) plat).children.values()) {
					if (coll instanceof Collider)
						isOnGround |= (position.y + collider.extent.y == ((Plateform) plat).position.y
								- ((Collider) coll).extent.y)
								&& (position.x + collider.extent.x >= ((Plateform) plat).position.x
										- ((Collider) coll).extent.x)
								&& (position.x - collider.extent.x >= ((Plateform) plat).position.x
										+ ((Collider) coll).extent.x);
				}
			}
		}

		vel.x = Utils.lerpD(vel.x, Input.getAxis(Axis.MOVE_X) * 500, d * 5);

		if (Input.actionPressed(Action.JUMP))
			jump();
		if (Input.actionPressed(Action.ATTACK))
			attack();
		if (isOnGround)
			vel.y += 9.807 * 200 * d;

		position.add(Vec2d.multiply(vel, d));
		// position.y = Math.min(450, position.y);

		position.add(vel);
		super.step(d);
	}
}
