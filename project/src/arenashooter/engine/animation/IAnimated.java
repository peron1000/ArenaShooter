package arenashooter.engine.animation;

/**
 * Animation capabilities
 */
public interface IAnimated {
	
	/**
	 * Set current animation
	 * @param anim new animation
	 */
	public void setAnim(Animation anim);
	
	/**
	 * Play current animation
	 */
	public void playAnim();
	
	/**
	 * Stop current animation
	 */
	public void stopAnim();
	
	/**
	 * Set current animation time to its end
	 */
	public void animJumpToEnd();
	
	/**
	 * @return current animation
	 */
	public Animation getAnim();
	
	/**
	 * Set animation playback speed
	 * @param speed new playback speed
	 */
	public void setAnimSpeed(double speed);
	
	/**
	 * @return current playback speed
	 */
	public double getAnimSpeed();
	
}
