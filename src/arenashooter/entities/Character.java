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
		position = new Vec2f(300, 0);
		rotation = 0;
		vel.y = -3;
		collider = new Collider(position, new Vec2f(50, 110));
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
		chevre.setFilter(false);
		Sprite body = ((Sprite) children.get("body_Sprite"));
		((Sprite) children.get("body_Sprite")).tex = chevre;
		body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
		collider.extent.y = body.size.y / 2;
		collider.extent.x = (body.size.x / 2) - 20;
		// TODO: attac
	}

	@Override
	public void step(double d) {
		isOnGround = false;
		vel.x = (float) Utils.lerpD(vel.x, Input.getAxis(Axis.MOVE_X) * 20, d * 8);
		vel.y += 9.807 * 5 * d;
		for (Entity plat : getParent().children.values()) {
			if (plat instanceof Plateform) {
				for (Entity coll : ((Plateform) plat).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						Impact impact = new Impact(collider, c, vel);
						vel.x = vel.x * impact.getResponse().x;
						vel.y = vel.y * impact.getResponse().y;
						if (collider.getYBottom() >= c.getYTop() && collider.getYBottom() < c.getYBottom()
								&& Collider.isX1IncluedInX2(collider, c))
							isOnGround = true;
					}
				}
			}
		}
		if (Input.actionPressed(Action.JUMP) && isOnGround)
			jump(35);
		if (Input.actionPressed(Action.ATTACK))
			attack();

		if (Input.getAxis(Axis.MOVE_X) > 0)
			((Sprite) children.get("body_Sprite")).flipX = false;
		else if (Input.getAxis(Axis.MOVE_X) < 0)
			((Sprite) children.get("body_Sprite")).flipX = true;

		position.add(vel);
//		position.add(Vec2f.multiply(vel, (float) d));
		((Sprite) children.get("body_Sprite")).position.x = position.x;
		((Sprite) children.get("body_Sprite")).position.y = position.y;
		super.step(d);
	}
}
