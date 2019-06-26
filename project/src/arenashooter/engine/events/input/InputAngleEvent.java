package arenashooter.engine.events.input;

import arenashooter.engine.events.Event;
import arenashooter.engine.input.Device;

public class InputAngleEvent extends Event {
	
	private Device device;
	private double angle;
	private boolean aim;

	public InputAngleEvent(Device device , double angle , boolean aim) {
		this.device = device;
		this.angle = angle;
		this.aim = aim;
	}

	public Device getDevice() {
		return device;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public boolean isAim() {
		return aim;
	}

}
