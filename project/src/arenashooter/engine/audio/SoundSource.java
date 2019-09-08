package arenashooter.engine.audio;

import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3fi;

public interface SoundSource {
	/**
	 * Destroy this source and unregister it from Audio manager
	 */
	public void destroy();
	
	public boolean isPlaying();
	
	public void play();
	
	public void pause();
	
	public void stop();
	
	public void setPosition2D(Vec2fi position);
	
	public void setPosition3D(Vec3fi position);
	
	public AudioChannel getChannel();
	
	public float getVolume();
	
	public void setVolume(float newVolume);
	
	public float getPitch();
	
	public void setPitch(float newPitch);
	
	public boolean isLooping();
	
	public void setLooping(boolean isLooping);
	
	public void setBuffer(SoundBuffer buffer);
}
