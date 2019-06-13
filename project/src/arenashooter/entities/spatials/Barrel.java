package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;

public class Barrel extends RigidBodyContainer{
	private float damage = 75;

	public Barrel(Vec2f position) {
		super(new RigidBody(new ShapeBox(new Vec2f(.25, .15)), position, 0, CollisionFlags.PROJ, 1, 1));
		
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.06, sprite.getTexture().getHeight()*.06);
		sprite.getTexture().setFilter(false);
		sprite.attachToParent(this, "barrel_Sprite");
	}

}
