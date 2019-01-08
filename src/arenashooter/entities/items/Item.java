package arenashooter.entities.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Impact;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Plateform;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;

public class Item extends Spatial {

	Vec2f vel = new Vec2f();
	Collider collider;
	boolean isOnGround = true;
	public float movementInput = 0;

	public Item(Vec2f position) {
		super(position);

		Sprite core = new Sprite(position, "data/weapons/Minigun_1.png");
		core.size = new Vec2f(core.tex.getWidth(), core.tex.getHeight());
		core.attachToParent(this, "item_Sprite"); 
		
		collider = new Collider(position, new Vec2f(core.tex.getWidth(), core.tex.getHeight()));
		collider.attachToParent(this, "coll_item");

		

	}

	@Override
	public void step(double d) {

		vel.x = (float) Utils.lerpD(vel.x, movementInput * 1500, d * (isOnGround ? 10 : 2));
		vel.y += 9.807 * 800 * d;

		isOnGround = false;
		for (Entity plat : getParent().children.values()) {
			if (plat instanceof Plateform) {
				for (Entity coll : ((Plateform) plat).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						Impact impact = new Impact(collider, c, Vec2f.multiply(vel, (float) d));
						vel.x = vel.x * impact.getVelMod().x;
						vel.y = vel.y * impact.getVelMod().y;
						if (collider.getYBottom() + (vel.y * d) >= c.getYTop()
								&& collider.getYBottom() + (vel.y * d) < c.getYBottom()
								&& Collider.isX1IncluedInX2(collider, c))
							isOnGround = true;
					}
				}
			}
		}
		super.step(d);
	}
}