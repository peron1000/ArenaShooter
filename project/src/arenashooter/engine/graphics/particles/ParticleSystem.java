package arenashooter.engine.graphics.particles;

import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class ParticleSystem {
	private List<Emitter> emitters;
	public Vec3f position;
	
	public ParticleSystem( Vec3f position ) {
		this.position = position;
		emitters = new ArrayList<Emitter>();
		
		//TODO: temp emitter
		emitters.add(new EmitterBasic( this, Texture.loadTexture("data/test.png"), -1, 0, 200, 
				1, 5, 
				new Vec4f(1, 0, 1, 1), new Vec4f(.8f, 1.2f, 0, .75f), 
				0, (float)(2*Math.PI), 
				100, 200 ));
		emitters.add(new EmitterSparks(this, Texture.loadTexture("data/particle_glow.png"), 20, 2, 60, 
				2, 4, 
				new Vec4f(1f, 1f, 8.15f, 1), new Vec4f(.12f, .07f, .18f, .5f), 
				-.5f, .5f, 
				700, 1200));
	}
	
	public void update(double delta) {
		for( int i=emitters.size()-1; i>=0; i-- ) {
			if( emitters.get(i).update(delta) ) emitters.remove(i);
		}
	}
	
	public void draw() {
		Profiler.startTimer(Profiler.PARTICLES);
		for(Emitter e : emitters)
			e.draw();
		Profiler.endTimer(Profiler.PARTICLES);
	}
}
