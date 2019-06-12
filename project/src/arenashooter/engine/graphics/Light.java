package arenashooter.engine.graphics;

import arenashooter.engine.math.Vec3f;

public class Light {
	/** Position of this light, or direction for directional lights */
	public final Vec3f position = new Vec3f();
	/** Radius of this light, <0 means this is a directional light */
	public float radius;
	public final Vec3f color = new Vec3f();
	
	public Light() { }
}
