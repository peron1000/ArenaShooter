package arenashooter.entities;

import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.engine.Input.Axis;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class Character extends Spatial {
	int pv;
	Vec2f vel = new Vec2f();
	Collider collider;
	boolean isOnGround = true;

	public Character() {
		pv = 10;
		position = new Vec2f(800, 500);
		rotation = 0;
		vel.y = -30;
		collider = new Collider(new Vec2f(160, 160));
		Sprite body = new Sprite("data/UnMoineHD.png");
		body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
		body.attachToParent(this, "body_texture");
	}

	public void jump(int saut) {
		vel.y = -saut;
		isOnGround = false;
		// TODO: collider
	}

	public void attack() {
		// TODO: attac SPRITE CHANGE
	}

	@Override
	public void step(double d) {
//		System.out.println(System.nanoTime());
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
		if (position.y < 500)
			isOnGround = false;
		else
			isOnGround = true;
		vel.x = (float)Utils.lerpD(vel.x, Input.getAxis(Axis.MOVE_X) * 500, d * 5);

		if (Input.actionPressed(Action.JUMP)&&isOnGround)
			jump(25);
		if (Input.actionPressed(Action.ATTACK))
			attack();
		if (!isOnGround) {
			vel.y += 9.807 * 10 * d;
		} else
			vel.y = 0;

		position.add(Vec2f.multiply(vel, (float)d));
		// position.y = Math.min(450, position.y);

		position.add(vel);
		((Sprite) children.get("body_texture")).position.x = position.x;
		((Sprite) children.get("body_texture")).position.y = position.y;
		super.step(d);
	}
}
