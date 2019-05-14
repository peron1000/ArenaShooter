package arenashooter.entities;

/**
 * @author Nathan Timer until a given integer<br/>
 *         To change the border integer, you have to create a new Timer
 */
public class Timer extends Entity {

	private double max;
	private double current = 0;
	private boolean over = false;
	public boolean inProcess = true;
	private boolean increasing = true;

	public Timer(double timer) {
		max = timer;
	}

	public boolean isOver() {
		return over;
	}

	/**
	 * Reset all variables
	 */
	public void restart() {
		over = false;
		current = 0;
	}
	
	/**
	 * @author Marin C
	 * Stops processing
	 */
	public void stop() {
		inProcess = false;
	}
	
	/**
	 * Stops the timer and reset all his variables
	 */
	public void reset() {
		restart();
		inProcess = false;
	}

	/**
	 * @param process = </br>
	 *                | {@code true} : keeps running or restart to run </br>
	 *                | {@code false} : put on pause
	 */
	public void setProcessing(boolean process) {

		inProcess = process;
	}

	public boolean isIncreasing() {
		return increasing;
	}

	public void setIncreasing(boolean increasing) {
		this.increasing = increasing;
	}
	
	public void setValue(double newValue) {
		this.max = newValue;
	}

	@Override
	public void step(double d) {

		if (inProcess) {
			if (increasing) {
				if (current < max)
					current += d;
			} else {
				if (current > 0)
					current -= d;
			}
			if (current >= max)
				over = true;
		}

		super.step(d);
	}

}
