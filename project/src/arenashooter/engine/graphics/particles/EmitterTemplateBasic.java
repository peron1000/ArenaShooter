package arenashooter.engine.graphics.particles;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec4f;

public class EmitterTemplateBasic extends EmitterTemplate {	
	float sizeInitial, sizeEnd;
	
	/**
	 * Create a basic particle emitter
	 * @param owner particle system owning this emitter
	 * @param texture texture used for rendering
	 * @param duration duration of the emitter, in seconds, 0 for single burst, -1 for infinite
	 * @param delay delay before activating this emitter, in seconds
	 * @param rate spawn rate in particles per second or burst size if duration = 0
	 * @param lifetimeMin minimum lifetime of a particle
	 * @param lifetimeMax maximum lifetime of a particle
	 * @param colorStart particle color at spawn
	 * @param colorEnd particle color at death
	 * @param angleMin minimum spawn angle
	 * @param angleMax maximum spawn angle
	 * @param velocityMin minimum spawn velocity
	 * @param velocityMax maximum spawn velocity
	 */
	public EmitterTemplateBasic( Texture texture, float duration, float delay, float rate, 
			float lifetimeMin, float lifetimeMax, 
			Vec4f colorStart, Vec4f colorEnd, 
			float angleMin, float angleMax, 
			float velocityMin, float velocityMax,
			float sizeInitial, float sizeEnd, 
			float gravityScale) {
		
		super(texture, duration, delay, rate, 
				lifetimeMin, lifetimeMax, 
				colorStart, colorEnd, 
				angleMin, angleMax, 
				velocityMin, velocityMax, 
				gravityScale);
		
		this.sizeInitial = sizeInitial;
		this.sizeEnd = sizeEnd;
		
//		shader = Shader.loadShader("data/shaders/particle_simple");
	}
}
