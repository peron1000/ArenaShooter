package arenashooter.engine;

import static org.lwjgl.glfw.GLFW.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import arenashooter.engine.math.Vec2f;

public final class Input {
	private static Vec2f mousePos = new Vec2f();
	private static DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);// Buffers to stock cursor coordinates;
	private static DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);

	private static FloatBuffer[] joyAxis = new FloatBuffer[16];
	private static ByteBuffer[] joyButtons = new ByteBuffer[16];
	private static long window;

	private static float[] axisMoveX = new float[17], axisMoveY = new float[17], axisAimX = new float[17],
			axisAimY = new float[17];
	private static ActionState[] actionJump = new ActionState[17], actionAttack = new ActionState[17],
			actionGetItem = new ActionState[17], actionDropItem = new ActionState[17],
			actionUiLeft = new ActionState[17], actionUiRight = new ActionState[17];

	// This class cannot be instantiated
	private Input() {
	}
	
	/**
	 * Initialize Input<br/>
	 * Do not call this before Window.init()
	 */
	public static void init(long window) {
		Input.window = window;
		
		//Initialize actions
		for (int i = 0; i < 17; i++) {
			actionJump[i] = ActionState.RELEASED;
			actionAttack[i] = ActionState.RELEASED;
			actionGetItem[i] = ActionState.RELEASED;
			actionDropItem[i] = ActionState.RELEASED;
			actionUiLeft[i] = ActionState.RELEASED;
			actionUiRight[i] = ActionState.RELEASED;
		}
		
		//Load gamepad mappings
		try {
			InputStream in = new FileInputStream(new File("data/gamecontrollerdb.txt"));
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inReader);
			
			String line = "";
			while( (line = reader.readLine()) != null )
					if(!glfwUpdateGamepadMappings(line))
						System.err.println("Error reading gamepad mapping: "+line);
		
			reader.close();
			inReader.close();
			in.close();
		} catch(Exception e) {
			System.err.println("Can't load gamepad mappings!");
		}
	}

	public enum ActionState {
		RELEASED, JUST_PRESSED, PRESSED, JUST_RELEASED;
	}

	public enum Action {
		JUMP, ATTACK, GET_ITEM, DROP_ITEM, UI_LEFT, UI_RIGHT;
	}

	public enum Axis {
		MOVE_X, MOVE_Y, AIM_X, AIM_Y;
	}

	public static float getAxis(Device device, Axis axis) {
		switch (axis) {
		case MOVE_X:
			return axisMoveX[device.id];
		case MOVE_Y:
			return axisMoveY[device.id];
		case AIM_X:
			return axisAimX[device.id];
		case AIM_Y:
			return axisAimY[device.id];
		default:
			return 0;
		}
	}

	/**
	 * 
	 * @param device
	 * @param action
	 * @return the state of an action
	 */
	public static ActionState getActionState(Device device, Action action) {
		switch (action) {
		case JUMP:
			return actionJump[device.id];
		case ATTACK:
			return actionAttack[device.id];
		case GET_ITEM:
			return actionGetItem[device.id];
		case DROP_ITEM:
			return actionDropItem[device.id];
		case UI_LEFT:
			return actionUiLeft[device.id];
		case UI_RIGHT:
			return actionUiRight[device.id];
		default:
			return ActionState.RELEASED;
		}
	}

	/**
	 * 
	 * @param device
	 * @param action
	 * @return is an action currently pressed on a device
	 */
	public static boolean actionPressed(Device device, Action action) {
		ActionState state = getActionState(device, action);
		return state == ActionState.JUST_PRESSED || state == ActionState.PRESSED;
	}

	/**
	 * 
	 * @param device
	 * @param action
	 * @return was an action just pressed on a device
	 */
	public static boolean actionJustPressed(Device device, Action action) {
		return getActionState(device, action) == ActionState.JUST_PRESSED;
	}

	public static void update() {
		for (int i = 0; i < 17; i++) {
			axisMoveX[i] = 0;
			axisMoveY[i] = 0;
			axisAimX[i] = 0;
			axisAimY[i] = 0;

			if (i == Device.KEYBOARD.id) {// Mouse and Keyboard

				glfwGetCursorPos(window, xBuffer, yBuffer);
				mousePos.x = (float) xBuffer.get(0);
				mousePos.y = (float) yBuffer.get(0);

				if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
					axisMoveX[i] -= 1;
				if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
					axisMoveX[i] += 1;
				if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
					axisMoveY[i] -= 1;
				if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
					axisMoveY[i] += 1;

				actionJump[i] = getActionState(actionJump[i], glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS);
				actionAttack[i] = getActionState(actionAttack[i], glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS);
				actionGetItem[i] = getActionState(actionGetItem[i], glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS);
				actionDropItem[i] = getActionState(actionDropItem[i], glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS);

			} else { // Controller

				joyAxis[i] = glfwGetJoystickAxes(i);
				joyButtons[i] = glfwGetJoystickButtons(i);

				if (joyAxis[i] != null) {
					float deadzone = .3f;

					if (Math.abs(joyAxis[i].get(0)) >= deadzone || Math.abs(joyAxis[i].get(1)) >= deadzone) {
						axisMoveX[i] = joyAxis[i].get(0);
						axisMoveY[i] = joyAxis[i].get(1);
					}

					if (Math.abs(joyAxis[i].get(2)) >= deadzone || Math.abs(joyAxis[i].get(3)) >= deadzone) {
						// If AimInput<deadzone, aim=move direction.
						axisAimX[i] = joyAxis[i].get(2);
						axisAimY[i] = joyAxis[i].get(3);
					} else if (axisMoveX[i] != 0 && axisMoveY[i] != 0) {
						axisAimX[i] = axisMoveX[i];
						axisAimY[i] = axisMoveY[i];
				}

				if (joyButtons[i] != null) {
					actionJump[i] = getActionState(actionJump[i], joyButtons[i].get(0) == 1);
					actionAttack[i] = getActionState(actionAttack[i], joyButtons[i].get(2) == 1);
					actionGetItem[i] = getActionState(actionGetItem[i], joyButtons[i].get(1) == 1);
					actionDropItem[i] = getActionState(actionDropItem[i], joyButtons[i].get(3) == 1);
				}
			}
		}

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

	/**
	 * Use this to see the id of every buttons/axes
	 * 
	 * @param id
	 */
	static void printController(int id) {
		if (joyAxis[id] == null)
			return;
		String res = "Controller " + id + " axis :";
		for (int i = 0; i < joyAxis[id].capacity(); i++)
			res += "\n " + i + " : " + joyAxis[id].get(i);
		System.out.println(res);
	}
}
