package arenashooter.entities;

/**
 * @author Nathan
 * Timer until a given integer<br/>
 * To change the border integer, you have to create a new Timer
 */
public class Timer extends Entity {
	
	private final double max;
	private double current = 0;
	private boolean over = false;
	private boolean inProcess = true;

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
	
	public void breakTimer() {
		over=false;
		current = 0;
		inProcess = false;
	}
	
	@Override
	public void step(double d) {
		if(inProcess) {
			current += d;
			if(current > max)over = true;
		}
		
		super.step(d);
	}

}
