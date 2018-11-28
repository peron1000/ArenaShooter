package arenashooter.engine;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Input {
	private static FloatBuffer[] joyAxis = new FloatBuffer[4];
	private static ByteBuffer[] joyButtons = new ByteBuffer[4];
	private static long window;
	
	private static float axisMoveX, axisMoveY;
	private static boolean actionJump, actionAttack;
	
	public enum Action {
		JUMP, ATTACK;
	}
	
	public enum Axis {
		MOVE_X, MOVE_Y;
	}

	public static float getAxis( Axis axis ) {
		switch( axis ) {
		case MOVE_X: return axisMoveX;
		case MOVE_Y: return axisMoveY;
		default: return 0;			
		}
	}
	
	public static boolean actionPressed( Action action ) {
		switch( action ) {
		case JUMP: return actionJump;
		case ATTACK: return actionAttack;
		default: return false;
		}
	}

	public static void update() {
		joyAxis[0] = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
		joyAxis[1] = glfwGetJoystickAxes(GLFW_JOYSTICK_2);
		joyAxis[2] = glfwGetJoystickAxes(GLFW_JOYSTICK_3);
		joyAxis[3] = glfwGetJoystickAxes(GLFW_JOYSTICK_4);
		
		joyButtons[0] = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
		joyButtons[1] = glfwGetJoystickButtons(GLFW_JOYSTICK_2);
		joyButtons[2] = glfwGetJoystickButtons(GLFW_JOYSTICK_2);
		joyButtons[3] = glfwGetJoystickButtons(GLFW_JOYSTICK_3);
		
		axisMoveX = 0;
		axisMoveY = 0;
		if(joyAxis[0] != null) {
			float deadzone = .2f;
			if( Math.abs(joyAxis[0].get(0)) > deadzone || Math.abs(joyAxis[0].get(1)) > deadzone ) {
				axisMoveX = joyAxis[0].get(0);
				axisMoveY = joyAxis[0].get(1);
			}
		}
		if( glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS )
			axisMoveX-=1;
		if( glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS )
			axisMoveX+=1;
		if( glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS )
			axisMoveY-=1;
		if( glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS )
			axisMoveY+=1;
		
		actionJump = glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS;
		if(joyButtons[0] != null) actionJump = actionJump || (joyButtons[0].get(0) == 1);
		
		actionAttack = glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS;
		if(joyButtons[0] != null) actionAttack = actionAttack || (joyButtons[0].get(1) == 1);
	}
	
	public static void setWindow(long window) {
		Input.window = window;
	}
}
