package arenashooter.engine.graphics;

import arenashooter.engine.math.Vec3f;

public class Light {
	public static enum LightType { POINT, DIRECTIONAL };
	
	/** Position of this light */
	public final Vec3f position = new Vec3f();
	/** Direction of this light */
	public final Vec3f direction = new Vec3f();
	/** Radius of this light, <0 means this is a directional light */
	public float radius;
	public final Vec3f color = new Vec3f();
	
	public Light() { }
	
	/**
	 * @return type of light (point or directional)
	 */
	public LightType getType() {
		if(radius < 0)
			return LightType.DIRECTIONAL;
		return LightType.POINT;
	}
	
	/**
	 * Set the type of this light (changes radius)
	 * @param type
	 */
	public void setType(LightType type) {
		switch(type) {
		case POINT:
			radius = Math.max(0, radius);
			break;
		case DIRECTIONAL:
			radius = -1;
			break;
		default:
			break;
		}
	}
}
