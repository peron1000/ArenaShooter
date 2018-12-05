package arenashooter.engine.graphics.particles;

import arenashooter.engine.graphics.Texture;

class EmitterSparks extends Emitter {

	protected EmitterSparks( ParticleSystem owner, Texture texture, float duration, float delay, float rate, float lifetimeMin, float lifetimeMax ) {
		super(owner, texture, duration, delay, rate, lifetimeMin, lifetimeMax );
	}
	
	@Override
	boolean update(double delta) {
		super.update(delta);
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	void spawn(int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void draw() {
		// TODO Auto-generated method stub

	}

}
