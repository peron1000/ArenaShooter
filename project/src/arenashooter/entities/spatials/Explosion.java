package arenashooter.entities.spatials;

import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;

public class Explosion extends Spatial {
	private static SoundSourceMulti sndExplosion = new SoundSourceMulti("data/sound/explosion_01.ogg", 8, .8f, 1.2f, true);

	public Explosion(Vec2f position) {
		super(position);
	}
	
	@Override
	public void step(double d) {
		Particles particles = new Particles(getWorldPos(), "data/particles/explosion.xml");
		particles.attachToParent(getMap(), "particles");
		
		sndExplosion.play(getWorldPos());
		
		Window.getCamera().setCameraShake(3);
		
		super.step(d);
		
		detach();
	}

}
