package arenashooter.engine.graphics.particles;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Texture;

abstract class Emitter {
	static final protected Model model = Model.loadQuad();
	
	protected final ParticleSystem owner;
	
	///Emitter parameters
	//Total duration of emission, 0 for single burst, -1 for infinite
	final float duration;
	//Delay before starting the emitter
	float delay;
	//Spawn rate, in particles per second or particle count for burst emitter
	final float rate;
	//Remaining number of particles to spawn
	int remaining;
	//Particles to spawn this frame;
	double particlesToSpawn = 0;
	//Visuals
	Shader shader;
	Texture tex;
	
	///Particles parameters
	//Lifetime values of a particle
	float lifetimeMin, lifetimeMax;
	
	Emitter( ParticleSystem owner, Texture texture, float duration, float delay, float rate, float lifetimeMin, float lifetimeMax ) {
		this.owner = owner;
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
		this.lifetimeMin = lifetimeMin;
		this.lifetimeMax = lifetimeMax;
	}
	
	/**
	 * Update this emitter. Handles delay and particle spawning. 
	 * Particle behavior needs to be implemented in override
	 * @param delta frame time in seconds
	 * @return emitter ended and can be destroyed
	 */
	boolean update(double delta) {
		if( delay > 0 ) {
			delay -= delta;
			return false;
		}
		
		if( remaining == -1 ) { //Inifinite
			//Add particle count, depending on framerate
			particlesToSpawn += rate*delta;
			
			//Floor the value to get the number of full particles
			int newParticles = (int)Math.floor(particlesToSpawn);
			
			//Spawn the particles
			spawn(newParticles);
			
			//Update counters
			particlesToSpawn -= newParticles;
			
		} else if( remaining > 0 ) //Need to spawn more particles
			if( duration == 0 ) { //Burst
				spawn(remaining);
				remaining = 0;
			} else { //Spawn over time
				//Add particle count, depending on framerate
				particlesToSpawn += rate*delta;
				
				//Floor the value to get the number of full particles
				int newParticles = (int)Math.floor(particlesToSpawn);
				
				//Make sure we don't spawn more particles than needed
				newParticles = Math.min( newParticles, remaining );
				
				//Spawn the particles
				spawn(newParticles);
				
				//Update counters
				remaining -= newParticles;
				particlesToSpawn -= newParticles;
			}
		
		return false;
	}
	
	abstract void spawn(int count);
	
	abstract void draw();
}
