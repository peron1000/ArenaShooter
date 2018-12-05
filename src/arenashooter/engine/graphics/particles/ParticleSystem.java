package arenashooter.engine.graphics.particles;

import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec3f;

public class ParticleSystem {
	private List<Emitter> emitters;
	public Vec3f position;
	
	public ParticleSystem( Vec3f position ) {
		this.position = position;
		emitters = new ArrayList<Emitter>();
		
		//TODO: temp emitter
		emitters.add(new EmitterBasic(this, new Texture("data/test.png"), -1, .1f, 1500, 1, 2));
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
