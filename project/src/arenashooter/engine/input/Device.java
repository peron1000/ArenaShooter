package arenashooter.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.glfw.GLFWGamepadState;

import arenashooter.engine.math.Vec2f;

public enum Device {
	CONTROLLER01(GLFW_JOYSTICK_1), CONTROLLER02(GLFW_JOYSTICK_2), CONTROLLER03(GLFW_JOYSTICK_3),
	CONTROLLER04(GLFW_JOYSTICK_4), CONTROLLER05(GLFW_JOYSTICK_5), CONTROLLER06(GLFW_JOYSTICK_6),
	CONTROLLER07(GLFW_JOYSTICK_7), CONTROLLER08(GLFW_JOYSTICK_8), CONTROLLER09(GLFW_JOYSTICK_9),
	CONTROLLER10(GLFW_JOYSTICK_10), CONTROLLER11(GLFW_JOYSTICK_11), CONTROLLER12(GLFW_JOYSTICK_12),
	CONTROLLER13(GLFW_JOYSTICK_13), CONTROLLER14(GLFW_JOYSTICK_14), CONTROLLER15(GLFW_JOYSTICK_15),
	CONTROLLER16(GLFW_JOYSTICK_16), KEYBOARD(GLFW_JOYSTICK_LAST + 1);

	/** GLFW Joystick ID or GLFW_JOYSTICK_LAST+1 for keyboard */
	public final int id;
	private static final float deadZone = 0.3f;

	private Map<ActionV2, ActionState> actions = new TreeMap<>();
	private Map<AxisV2, Float> axis = new TreeMap<>();

	private Device(int id) {
		this.id = id;
		for (ActionV2 a : ActionV2.values()) {
			actions.put(a, ActionState.RELEASED);
		}
		for (AxisV2 a : AxisV2.values()) {
			axis.put(a, Float.valueOf(0f));
		}
	}

	public ActionState getActionState(ActionV2 a) {
		return actions.getOrDefault(a, ActionState.RELEASED);
	}

	public float getAxisFloat(AxisV2 a) {
		return axis.getOrDefault(a, Float.valueOf(0f)).floatValue();
	}

	/**
	 * Get value of the axis angle
	 * 
	 * @param aim <code>true</code> if <i>aim</i> axis or <code>false</code> if
	 *            <i>move</i> axis
	 * @return
	 */
	public double getAngle(boolean aim) {
		if (aim) {
			return new Vec2f(getAxisFloat(AxisV2.AIM_X), getAxisFloat(AxisV2.AIM_Y)).angle();
		} else {
			return new Vec2f(getAxisFloat(AxisV2.MOVE_X), getAxisFloat(AxisV2.MOVE_Y)).angle();
		}
	}

	private static ActionState getActionState(ActionState current, boolean isPressed) {
		switch (current) {
		case RELEASED:
			return isPressed ? ActionState.JUST_PRESSED : ActionState.RELEASED;
		case JUST_PRESSED:
			return isPressed ? ActionState.PRESSED : ActionState.JUST_RELEASED;
		case PRESSED:
			return isPressed ? ActionState.PRESSED : ActionState.JUST_RELEASED;
		case JUST_RELEASED:
			return isPressed ? ActionState.JUST_PRESSED : ActionState.RELEASED;
		default:
			return ActionState.RELEASED;
		}
	}

	public void update(long window, GLFWGamepadState gamePad) {
		for (AxisV2 axi : axis.keySet()) {
			axis.put(axi, 0f);
		}
		if (this == Device.KEYBOARD) {
			for (ActionV2 a : actions.keySet()) {
				boolean isPressed = a.keyboardInput(window);
				ActionState nouveau = getActionState(actions.get(a), isPressed);
				actions.put(a, nouveau);
			}
			if (actions.get(ActionV2.UI_LEFT) != ActionState.RELEASED) {
				axis.put(AxisV2.MOVE_X, -1f);
			} else if (actions.get(ActionV2.UI_RIGHT) != ActionState.RELEASED) {
				axis.put(AxisV2.MOVE_X, 1f);
			} else {
				axis.put(AxisV2.MOVE_X, 0f);
			}
			if (actions.get(ActionV2.UI_DOWN) != ActionState.RELEASED) {
				axis.put(AxisV2.MOVE_Y, -1f);
			} else if (actions.get(ActionV2.UI_UP) != ActionState.RELEASED) {
				axis.put(AxisV2.MOVE_Y, 1f);
			} else {
				axis.put(AxisV2.MOVE_Y, 0f);
			}
		} else if (glfwJoystickIsGamepad(id)) {
			if (glfwGetGamepadState(id, gamePad)) {

				// Actions
				for (ActionV2 a : actions.keySet()) {
					boolean isPressed = a.gamepadInput(gamePad);
					ActionState nouveau = getActionState(actions.get(a), isPressed);
					actions.put(a, nouveau);
				}

				// Axis
				for (AxisV2 a : axis.keySet()) {
					float f = a.getFloat(gamePad);
					if (Math.abs(f) >= deadZone) {
						axis.put(a, Float.valueOf(f));
					}
				}
			}
		}
	}
}
