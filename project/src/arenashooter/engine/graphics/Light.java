package arenashooter.engine.graphics;

import arenashooter.engine.math.Vec3f;

public class Light {
	Vec3f position = new Vec3f();
	float radius;
	Vec3f color = new Vec3f();
	
	public Light() {
	}
	
	/**
	 * Set this light's position (or direction for a directional light)
	 * @param position
	 */
	public void setPosition(Vec3f position) {
		this.position.set(position);
	}
	
	/**
	 * Set this light's radius<br/>
	 * > 1 for a point light<br/>
	 * = 0 to disable this light<br/>
	 * < 0 for a directional light<br/>
	 * @param radius
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void setColor(Vec3f color) {
		this.color.set(color);
	}

}
