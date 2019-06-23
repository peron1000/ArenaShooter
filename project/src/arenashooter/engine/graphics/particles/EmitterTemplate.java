package arenashooter.engine.graphics.particles;

import java.util.LinkedList;
import java.util.List;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.particles.modules.ParticleModule;

public class EmitterTemplate {
	final Texture tex;
	
	final float lifetimeMin, lifetimeMax;

	/** Total duration of emission, 0 for single burst, -1 for infinite */
	final float duration;
	/** Delay before starting the emitter */
	final float delay;
	/** Spawn rate, in particles per second or particle count for burst emitter */
	final float rate;
	
	final float initialRotMin, initialRotMax;
	
	final List<ParticleModule> modules;

	public EmitterTemplate(Texture tex, float delay, float duration, float rate, float lifetimeMin, float lifetimeMax, float initialRotMin, float initialRotMax, List<ParticleModule> modules) {
		this.tex = tex;
		this.delay = delay;
		this.duration = duration;
		this.rate = rate;
		this.lifetimeMin = lifetimeMin;
		this.lifetimeMax = lifetimeMax;
		this.initialRotMin = initialRotMin;
		this.initialRotMax = initialRotMax;
		this.modules = new LinkedList<>(modules);
	}

}
