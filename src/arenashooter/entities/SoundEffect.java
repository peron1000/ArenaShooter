package arenashooter.entities;

import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class SoundEffect extends Spatial {
	private SoundSource sound;

	public SoundEffect( Vec2f position, String path ) {
		sound = new SoundSource(path, 4, .8f, 1.2f, true);
	}
	
	public void play() {
		sound.play();
	}
	
	public boolean isPlaying() {
		return sound.isPlaying();
	}
	
	public void stop() {
		sound.stop();
	}

	@Override
	public void step(double d) {
		sound.setPosition( new Vec3f( position.x, position.y, 0 ) );

		super.step(d);
	}
}
