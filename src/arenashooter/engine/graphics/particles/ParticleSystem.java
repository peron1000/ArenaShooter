package arenashooter.engine.graphics.particles;

import java.util.ArrayList;
import java.util.List;

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
		emitters.add(new EmitterBasic(this, new Texture("data/test.png"), -1, .1f, 200, 1, 5, 0, 6.28318530718f, new Vec4f(1, 0, 1, 1), new Vec4f(.8f, 1.2f, 0, .75f)));
		emitters.add(new EmitterSparks(this, new Texture("data/particle_glow.png"), 10, 2, 60, 2, 4, new Vec4f(1f, 1f, 8.15f, 1), new Vec4f(.12f, .07f, .18f, .5f)));
	}
	
	public void update(double delta) {
		for( int i=emitters.size()-1; i>=0; i-- ) {
			if( emitters.get(i).update(delta) ) emitters.remove(i);
		}
	}
	
	public void draw() {
		for(Emitter e : emitters)
			e.draw();
	}
}
