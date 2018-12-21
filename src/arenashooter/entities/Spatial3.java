package arenashooter.entities;

import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;

public class Spatial3 extends Entity {
	public Vec3f position = new Vec3f();
	public Quat rotation = Quat.fromAngle(0);
}
