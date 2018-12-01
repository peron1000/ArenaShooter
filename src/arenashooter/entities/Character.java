package arenashooter.entities;

import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.engine.Input.Axis;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class Character extends Spatial {
	int pv;
	Vec2f vel = new Vec2f();
	Collider collider;
	boolean isOnGround = true;

	public Character() {
		pv = 10;
		position = new Vec2f(500, 400);
		rotation = 0;
		vel.y = -3;
		collider = new Collider(position, new Vec2f(70, 110));
		Sprite body = new Sprite("data/UnMoineHD.png");
		body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
		body.attachToParent(this, "body_Sprite");
	}

	public void jump(int saut) {
		isOnGround = false;
		vel.y = -saut;
		// TODO: collider
	}

	public void attack() {
		Texture chevre = new Texture("data/Chevre2.png");
		chevre = new Texture("data/Chevre2.png");
		((Sprite) children.get("body_Sprite")).tex = chevre;
		// TODO: attac
	}

	@Override
	public void step(double d) {
		isOnGround = false;
		// System.out.println(System.nanoTime());
		for (Entity plat : getParent().children.values()) {
			if (plat instanceof Plateform) {
				for (Entity coll : ((Plateform) plat).children.values()) {
					if (coll instanceof Collider)
						if (collider.isColliding((Collider) coll)) {
							if ((position.y + collider.extent.y) == (((Collider) coll).position.y
									- ((Collider) coll).extent.y))
								isOnGround = true;
							else {
								position.y = (((Collider) coll).position.y - ((Collider) coll).extent.y)-collider.extent.y;
								isOnGround = true;
							}
						}
				}
			}
		}

		vel.x = (float) Utils.lerpD(vel.x, Input.getAxis(Axis.MOVE_X) * 15, d * 10);

		if (Input.actionPressed(Action.JUMP) && isOnGround)
			jump(25);
		if (Input.actionPressed(Action.ATTACK))
			attack();
		if (!isOnGround) {
			vel.y += 9.807 * 1 * d;
		} else
			vel.y = 0;

		if (Input.getAxis(Axis.MOVE_X) > 0)
			((Sprite) children.get("body_Sprite")).flipX = false;
		else if (Input.getAxis(Axis.MOVE_X) < 0)
			((Sprite) children.get("body_Sprite")).flipX = true;

		position.add(Vec2f.multiply(vel, (float) d));
		// position.y = Math.min(450, position.y);

		position.add(vel);
		((Sprite) children.get("body_Sprite")).position.x = position.x;
		((Sprite) children.get("body_Sprite")).position.y = position.y;
		super.step(d);
	}
}
