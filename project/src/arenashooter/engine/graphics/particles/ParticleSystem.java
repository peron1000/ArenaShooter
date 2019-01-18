package arenashooter.engine.graphics.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.xmlReaders.ParticlesXmlReader;

public class ParticleSystem {
	private static HashMap<String, EmitterTemplate[]> cache = new HashMap<>();
	
	private List<Emitter> emitters;
	public Vec3f position;
	
	private ParticleSystem( EmitterTemplate[] data ) {
		this.position = new Vec3f();
		emitters = new ArrayList<Emitter>();
		
		for( EmitterTemplate emitterData : data ) {
			if(emitterData instanceof EmitterTemplateBasic)
				emitters.add(new EmitterBasic(this, (EmitterTemplateBasic)emitterData));
			else if(emitterData instanceof EmitterTemplateSparks)
				emitters.add(new EmitterSparks(this, (EmitterTemplateSparks)emitterData));
		}
	}
	
	public static ParticleSystem load(String path) {
		EmitterTemplate[] emitters = cache.get(path);
		
		if(emitters != null) return new ParticleSystem( emitters );
		
		emitters = ParticlesXmlReader.read(path);
		
		cache.put(path, emitters);
		
		return new ParticleSystem( emitters );
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
