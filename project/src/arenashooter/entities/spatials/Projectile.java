package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;

public class Projectile extends Spatial {

	public Character shooter;

	boolean disp = false;
	Vec2f vel = new Vec2f();
	Collider collider;
	float damage;// Un projectile n'a pas forc�ment de damage.

	public Projectile() {
		super();
	}

	public boolean isShooter(Character dude) {
		if (shooter != null) {
			return dude == shooter;
		} else
			return false;
	}

	public Projectile(Vec2f position) {
		super(position);
	}

}
