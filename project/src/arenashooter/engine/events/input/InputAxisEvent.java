package arenashooter.engine.events.input;

import arenashooter.engine.events.Event;
import arenashooter.engine.input.AxisV2;
import arenashooter.engine.input.Device;

/**
 * @author Nathan
 * Package of information inherit for a Axis Event
 */
public class InputAxisEvent extends Event {
	private Device device;
	private AxisV2 axis;
	private float value;
	
	public InputAxisEvent(Device device , AxisV2 axis , float value) {
		this.axis = axis;
		this.device = device;
		this.value = value;
	}

	public Device getDevice() {
		return device;
	}

	public AxisV2 getAxis() {
		return axis;
	}

	public float getValue() {
		return value;
	}
}
