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
import org.lwjgl.glfw.GLFWGamepadState;

import arenashooter.engine.math.Vec2f;

public final class Input {
	public static Vec2f mousePos = new Vec2f();
	private static DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);// Buffers to stock cursor coordinates;
	private static DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);

	private static FloatBuffer[] joyAxis = new FloatBuffer[16];
	private static ByteBuffer[] joyButtons = new ByteBuffer[16];
	private static GLFWGamepadState currentGamepad;
	
	/** Game window id, required for keyboard/mouse input */
	private static long window;

	//Valus for axes and actions for each input device
	private static float[][] axes = new float[Axis.values().length][17];
	private static ActionState[][] actions = new ActionState[Action.values().length][17];
	
	//Enums
	/**
	 * Represents the state of an action
	 */
	public enum ActionState {
		/** The action is not pressed */
		RELEASED,
		/** The action has just been pressed */
		JUST_PRESSED, 
		/** The action is currently pressed */
		PRESSED, 
		/** The action has just been released */
		JUST_RELEASED;
	}

	/**
	 * Input actions
	 */
	public enum Action {
		JUMP(0), ATTACK(1), GET_ITEM(2), DROP_ITEM(3), UI_LEFT(4), UI_RIGHT(5), UI_UP(6), UI_DOWN(7), UI_OK(8), UI_BACK(9);
		
		public final int id;
		
		private Action( int id ) {
			this.id = id;
		}
	}

	/**
	 * Input axes
	 */
	public enum Axis {
		MOVE_X(0), MOVE_Y(1), AIM_X(2), AIM_Y(3);
		
		public final int id;
		
		private Axis( int id ) {
			this.id = id;
		}
	}
	
	//This class cannot be instantiated
	private Input() { }
	
	/**
	 * Initialize Input<br/>
	 * This should be called by Window.init()
	 */
	public static void init(long window) {
		Input.window = window;
		
		//Initialize actions
		for(int i=0; i<actions.length; i++)
			for(int j=0; j<actions[0].length; j++)
				actions[i][j] = ActionState.RELEASED;
		
		//Initialize gamepad
		currentGamepad = GLFWGamepadState.create();
		
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

	public static float getAxis(Device device, Axis axis) {
		return axes[axis.id][device.id];
	}

	/**
	 * 
	 * @param device
	 * @param action
	 * @return the state of an action
	 */
	public static ActionState getActionState(Device device, Action action) {
		return actions[action.id][device.id];
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
			axes[Axis.MOVE_X.id][i] = 0;
			axes[Axis.MOVE_Y.id][i] = 0;
			axes[Axis.AIM_X.id][i] = 0;
			axes[Axis.AIM_Y.id][i] = 0;

			if (i == Device.KEYBOARD.id) {// Mouse and Keyboard

				glfwGetCursorPos(window, xBuffer, yBuffer);
				mousePos.x = (float) xBuffer.get(0);
				mousePos.y = (float) yBuffer.get(0);
				axes[Axis.AIM_X.id][i] = mousePos.x;
				axes[Axis.AIM_Y.id][i] = mousePos.y;

				if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
					axes[Axis.MOVE_X.id][i] -= 1;
				if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
					axes[Axis.MOVE_X.id][i] += 1;
				if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
					axes[Axis.MOVE_Y.id][i] -= 1;
				if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
					axes[Axis.MOVE_Y.id][i] += 1;

				actions[Action.JUMP.id][i] = getActionState(actions[Action.JUMP.id][i], glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS);
				actions[Action.ATTACK.id][i] = getActionState(actions[Action.ATTACK.id][i], glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS || glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS);
				actions[Action.GET_ITEM.id][i] = getActionState(actions[Action.GET_ITEM.id][i], glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS);
				actions[Action.DROP_ITEM.id][i] = getActionState(actions[Action.DROP_ITEM.id][i], glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS);
				
				actions[Action.UI_LEFT.id][i] = getActionState(actions[Action.UI_LEFT.id][i], glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS);
				actions[Action.UI_RIGHT.id][i] = getActionState(actions[Action.UI_RIGHT.id][i], glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS);
				actions[Action.UI_UP.id][i] = getActionState(actions[Action.UI_UP.id][i], glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS);
				actions[Action.UI_DOWN.id][i] = getActionState(actions[Action.UI_DOWN.id][i], glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS);
				
				actions[Action.UI_OK.id][i] = getActionState(actions[Action.UI_OK.id][i], glfwGetKey(window, GLFW_KEY_ENTER) == GLFW_PRESS);
				actions[Action.UI_BACK.id][i] = getActionState(actions[Action.UI_BACK.id][i], glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS);

			} else if( glfwJoystickIsGamepad(i) ) { //Gamepad
				
				float deadzone = .3f;

				if(glfwGetGamepadState(i, currentGamepad)) {

					actions[Action.ATTACK.id][i] = getActionState(actions[Action.ATTACK.id][i], currentGamepad.axes(GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER) >= 0);

					if (Math.abs(currentGamepad.axes(GLFW_GAMEPAD_AXIS_LEFT_X)) >= deadzone || Math.abs(currentGamepad.axes(GLFW_GAMEPAD_AXIS_LEFT_Y)) >= deadzone) {
						axes[Axis.MOVE_X.id][i] = currentGamepad.axes(GLFW_GAMEPAD_AXIS_LEFT_X);
						axes[Axis.MOVE_Y.id][i] = currentGamepad.axes(GLFW_GAMEPAD_AXIS_LEFT_Y);
					}

					if (Math.abs(currentGamepad.axes(GLFW_GAMEPAD_AXIS_RIGHT_X)) >= deadzone || Math.abs(currentGamepad.axes(GLFW_GAMEPAD_AXIS_RIGHT_Y)) >= deadzone) {
						// If AimInput<deadzone, aim=move direction.
						axes[Axis.AIM_X.id][i] = currentGamepad.axes(GLFW_GAMEPAD_AXIS_RIGHT_X);
						axes[Axis.AIM_Y.id][i] = currentGamepad.axes(GLFW_GAMEPAD_AXIS_RIGHT_Y);
					} else if (axes[Axis.MOVE_X.id][i] != 0 && axes[Axis.MOVE_Y.id][i] != 0) {
						axes[Axis.AIM_X.id][i] = axes[Axis.MOVE_X.id][i];
						axes[Axis.AIM_Y.id][i] = axes[Axis.MOVE_Y.id][i];
					}

					actions[Action.JUMP.id][i] = getActionState(actions[Action.JUMP.id][i], currentGamepad.buttons(GLFW_GAMEPAD_BUTTON_A) == GLFW_PRESS);
					actions[Action.GET_ITEM.id][i] = getActionState(actions[Action.GET_ITEM.id][i], currentGamepad.buttons(GLFW_GAMEPAD_BUTTON_B) == GLFW_PRESS);
					actions[Action.DROP_ITEM.id][i] = getActionState(actions[Action.DROP_ITEM.id][i], currentGamepad.buttons(GLFW_GAMEPAD_BUTTON_Y) == GLFW_PRESS);

					actions[Action.UI_LEFT.id][i] = getActionState(actions[Action.UI_LEFT.id][i], currentGamepad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_LEFT) == GLFW_PRESS);
					actions[Action.UI_RIGHT.id][i] = getActionState(actions[Action.UI_RIGHT.id][i], currentGamepad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_RIGHT) == GLFW_PRESS);
					actions[Action.UI_UP.id][i] = getActionState(actions[Action.UI_UP.id][i], currentGamepad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_UP) == GLFW_PRESS);
					actions[Action.UI_DOWN.id][i] = getActionState(actions[Action.UI_DOWN.id][i], currentGamepad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_DOWN) == GLFW_PRESS);

					actions[Action.UI_OK.id][i] = getActionState(actions[Action.UI_OK.id][i], currentGamepad.buttons(GLFW_GAMEPAD_BUTTON_A) == GLFW_PRESS);
					actions[Action.UI_BACK.id][i] = getActionState(actions[Action.UI_BACK.id][i], currentGamepad.buttons(GLFW_GAMEPAD_BUTTON_B) == GLFW_PRESS);
				}

			} else { //Non-gamepad joystick

				joyAxis[i] = glfwGetJoystickAxes(i);
				joyButtons[i] = glfwGetJoystickButtons(i);

				if (joyAxis[i] != null) {
					float deadzone = .3f;

					if(  joyAxis[i].capacity()>=6 ) {
						actions[Action.ATTACK.id][i] = getActionState(actions[Action.ATTACK.id][i], joyAxis[i].get(5) >= 0);

						if (Math.abs(joyAxis[i].get(0)) >= deadzone || Math.abs(joyAxis[i].get(1)) >= deadzone) {
							axes[Axis.MOVE_X.id][i] = joyAxis[i].get(0);
							axes[Axis.MOVE_Y.id][i] = joyAxis[i].get(1);
						}

						if (Math.abs(joyAxis[i].get(2)) >= deadzone || Math.abs(joyAxis[i].get(3)) >= deadzone) {
							// If AimInput<deadzone, aim=move direction.
							axes[Axis.AIM_X.id][i] = joyAxis[i].get(2);
							axes[Axis.AIM_Y.id][i] = joyAxis[i].get(3);
						} else if (axes[Axis.MOVE_X.id][i] != 0 && axes[Axis.MOVE_Y.id][i] != 0) {
							axes[Axis.AIM_X.id][i] = axes[Axis.MOVE_X.id][i];
							axes[Axis.AIM_Y.id][i] = axes[Axis.MOVE_Y.id][i];
						}
					}

					if (joyButtons[i] != null && joyButtons[i].capacity()>=4) {
						actions[Action.JUMP.id][i] = getActionState(actions[Action.JUMP.id][i], joyButtons[i].get(0) == GLFW_PRESS);
						actions[Action.GET_ITEM.id][i] = getActionState(actions[Action.GET_ITEM.id][i], joyButtons[i].get(1) == GLFW_PRESS);
						actions[Action.DROP_ITEM.id][i] = getActionState(actions[Action.DROP_ITEM.id][i], joyButtons[i].get(3) == GLFW_PRESS);

						actions[Action.UI_OK.id][i] = getActionState(actions[Action.UI_OK.id][i], joyButtons[i].get(0) == GLFW_PRESS);
						actions[Action.UI_BACK.id][i] = getActionState(actions[Action.UI_BACK.id][i], joyButtons[i].get(1) == GLFW_PRESS);
					}
					
					actions[Action.UI_LEFT.id][i] = getActionState(actions[Action.UI_LEFT.id][i], axes[Axis.MOVE_X.id][i] < 0 );
					actions[Action.UI_RIGHT.id][i] = getActionState(actions[Action.UI_RIGHT.id][i], axes[Axis.MOVE_X.id][i] > 0);
					actions[Action.UI_UP.id][i] = getActionState(actions[Action.UI_UP.id][i], axes[Axis.MOVE_Y.id][i] > 0 );
					actions[Action.UI_DOWN.id][i] = getActionState(actions[Action.UI_DOWN.id][i], axes[Axis.MOVE_Y.id][i] < 0);
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
	
	public static String getDeviceInfo(Device device) {
		String res = "Unavailable device";
		
		if( device == Device.KEYBOARD ) {
			res = "Keyboard / mouse";
		} else if(  glfwJoystickIsGamepad(device.id) ) {
			res = "Gamepad: "+glfwGetGamepadName(device.id);
		} else {
			res = "Joystick (not recognized as a gamepad!): "+glfwGetJoystickName(device.id);
			
			joyAxis[device.id] = glfwGetJoystickAxes(device.id);
			joyButtons[device.id] = glfwGetJoystickButtons(device.id);
			
			res += " ("+joyAxis[device.id].capacity()+" axes, "+joyButtons[device.id].capacity()+" buttons)";
		}
		
		return res;
	}

	/**
	 * Use this to see the id of every buttons/axes
	 * 
	 * @param id
	 */
	static void printJoystick(int id) {
		if (joyAxis[id] == null)
			return;
		String res = "Controller " + id + " axis :";
		for (int i = 0; i < joyAxis[id].capacity(); i++)
			res += "\n " + i + " : " + joyAxis[id].get(i);
		System.out.println(res);
	}
}
