package arenashooter.engine.events.input;

import arenashooter.engine.events.Event;
import arenashooter.engine.input.AxisTest;
import arenashooter.engine.input.Device;

public class InputAxisEvent extends Event {
	private Device device;
	private AxisTest axis;
	private float value;
	
	public InputAxisEvent(Device device , AxisTest axis , float value) {
		this.axis = axis;
		this.device = device;
		this.value = value;
	}

	public Device getDevice() {
		return device;
	}

	public AxisTest getAxis() {
		return axis;
	}

	public float getValue() {
		return value;
	}
}
