package arenashooter.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.TreeMap;

import org.lwjgl.glfw.GLFWGamepadState;

public enum Device {
	CONTROLLER01(GLFW_JOYSTICK_1), 
	CONTROLLER02(GLFW_JOYSTICK_2), 
	CONTROLLER03(GLFW_JOYSTICK_3), 
	CONTROLLER04(GLFW_JOYSTICK_4), 
	CONTROLLER05(GLFW_JOYSTICK_5), 
	CONTROLLER06(GLFW_JOYSTICK_6), 
	CONTROLLER07(GLFW_JOYSTICK_7), 
	CONTROLLER08(GLFW_JOYSTICK_8), 
	CONTROLLER09(GLFW_JOYSTICK_9), 
	CONTROLLER10(GLFW_JOYSTICK_10), 
	CONTROLLER11(GLFW_JOYSTICK_11), 
	CONTROLLER12(GLFW_JOYSTICK_12), 
	CONTROLLER13(GLFW_JOYSTICK_13), 
	CONTROLLER14(GLFW_JOYSTICK_14), 
	CONTROLLER15(GLFW_JOYSTICK_15), 
	CONTROLLER16(GLFW_JOYSTICK_16), 
	KEYBOARD(GLFW_JOYSTICK_LAST+1);

	/** GLFW Joystick ID or GLFW_JOYSTICK_LAST+1 for keyboard */
	public final int id;
	private static final float deadZone = 0.3f;
	
	private TreeMap<ActionTest, ActionState> actions = new TreeMap<>();
	private TreeMap<AxisTest, Float> axis = new TreeMap<>();
	
	private Device( int id ) {
		this.id = id;
		for (ActionTest a : ActionTest.values()) {
			actions.put(a, ActionState.RELEASED);
		}
		for (AxisTest a : AxisTest.values()) {
			axis.put(a, Float.valueOf(0f));
		}
	}
	
	public ActionState getActionState(ActionTest a) {
		return actions.getOrDefault(a, ActionState.RELEASED);
	}
	
	public float getAxisFloat(AxisTest a) {
		return axis.getOrDefault(a, Float.valueOf(0f)).floatValue();
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
	
	public void update(long window , GLFWGamepadState gamePad) {
		if(this == Device.KEYBOARD) {
			for (ActionTest a : actions.keySet()) {
				boolean isPressed = a.isKeyPressed(window);
				ActionState nouveau = getActionState(actions.get(a), isPressed);
				actions.put(a, nouveau);
			}
		} else if(glfwJoystickIsGamepad(id)) {
			if(glfwGetGamepadState(id, gamePad)) {
				
				// Actions
				for (ActionTest a : actions.keySet()) {
					boolean isPressed = a.isButtonPressed(gamePad);
					ActionState nouveau = getActionState(actions.get(a), isPressed);
					actions.put(a, nouveau);
				}
				
				// Axis
				for (AxisTest a : axis.keySet()) {
					float f = a.getFloat(gamePad);
					if(Math.abs(f) >= deadZone) {
						axis.put(a, Float.valueOf(f));
					}
				}
			}
		}
	}
}
