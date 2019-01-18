package arenashooter.engine.graphics.particles;

import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec4f;

public abstract class EmitterTemplate {
	///Emitter parameters
	/** Total duration of emission, 0 for single burst, -1 for infinite */
	final float duration;
	/** Delay before starting the emitter */
	float delay;
	/** Spawn rate, in particles per second or particle count for burst emitter */
	final float rate;
	/** Remaining number of particles to spawn */
	int remaining;
	/** Particles to spawn this frame */
	double particlesToSpawn = 0;
	//Visuals
	Shader shader;
	Texture tex;
	
	///Particles parameters
	//Lifetime values of a particle
	float lifetimeMin, lifetimeMax;
	//Color
	final Vec4f colorStart, colorEnd;
	//Initial angle
	final float angleMin, angleMax;
	//Initial velocity
	final float velocityMin, velocityMax;
	
	EmitterTemplate( Texture texture, float duration, float delay, float rate,
			float lifetimeMin, float lifetimeMax, 
			Vec4f colorStart, Vec4f colorEnd, 
			float angleMin, float angleMax, 
			float velocityMin, float velocityMax ) {
		tex = texture;
		this.duration = duration;
		this.delay = delay;
		this.rate = rate;
		if( duration == 0 ) //Burst
			remaining = (int) rate;
		else if( duration < 0 ) //Infinite
			remaining = -1;
		else
			remaining = (int) Math.max(1, rate*duration);
		
		//Lifetime
		this.lifetimeMin = lifetimeMin;
		this.lifetimeMax = lifetimeMax;
		
		//Colors
		this.colorStart = colorStart;
		this.colorEnd = colorEnd;
		
		//Angle
		this.angleMin = angleMin;
		this.angleMax = angleMax;
		
		//Initial velocity
		this.velocityMin = velocityMin;
		this.velocityMax = velocityMax;
	}
}
