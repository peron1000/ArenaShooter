package arenashooter.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWGamepadState;

/**
 * Input axes
 */
public enum AxisV2 {
	MOVE_X {

		@Override
		public float getFloat(GLFWGamepadState gamePad) {
			return gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_X);
		}
	}, MOVE_Y {

		@Override
		public float getFloat(GLFWGamepadState gamePad) {
			return gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_Y);
		}
	}, AIM_X {

		@Override
		public float getFloat(GLFWGamepadState gamePad) {
			return gamePad.axes(GLFW_GAMEPAD_AXIS_RIGHT_X);
		}
	}, AIM_Y {

		@Override
		public float getFloat(GLFWGamepadState gamePad) {
			return gamePad.axes(GLFW_GAMEPAD_AXIS_RIGHT_Y);
		}
	};
	
	public abstract float getFloat(GLFWGamepadState gamePad);

}