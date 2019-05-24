package arenashooter.entities.spatials;

import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.math.Vec2f;

public class Explosion extends Spatial {
	private static SoundSourceMulti sndExplosion = new SoundSourceMulti("data/sound/explosion_01.ogg", 8, .8f, 1.2f, true);

	public Explosion(Vec2f position) {
		super(position);
		Particles particles = new Particles(position, "data/particles/explosion.xml");
		particles.attachToParent(this, "particles");
		
		sndExplosion.play(getWorldPos());
	}

}
