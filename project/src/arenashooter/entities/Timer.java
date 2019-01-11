package arenashooter.entities;

/**
 * @author Nathan
 * Timer until an integer given
 * To change the border integer, you have to create a new Timer
 */
public class Timer extends Entity {
	
	private final double max;
	private double current = 0;
	private boolean over = false;

	public Timer(double timer) {
		max = timer;
	}
	
	public boolean isOver() {
		return over;
	}
	
	public void restart() {
		over = false;
		current = 0;
	}
	
	@Override
	public void step(double d) {
		current += d;
		if(current > max)over = true;
		super.step(d);
	}

}
