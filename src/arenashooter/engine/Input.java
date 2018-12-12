package arenashooter.engine;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public final class Input {
	private static FloatBuffer[] joyAxis = new FloatBuffer[16];
	private static ByteBuffer[] joyButtons = new ByteBuffer[16];
	private static long window;
	
	private static float[] axisMoveX = new float[17], axisMoveY = new float[17];
	private static boolean[] actionJump = new boolean[17], actionAttack = new boolean[17];
	
	//This class cannot be instantiated
	private Input() {}
	
	public enum Action {
		JUMP, ATTACK;
	}
	
	public enum Axis {
		MOVE_X, MOVE_Y;
	}

	public static float getAxis( Device device, Axis axis ) {
		switch( axis ) {
			case MOVE_X: return axisMoveX[device.id];
			case MOVE_Y: return axisMoveY[device.id];
			default: return 0;	
		}
	}
	
	public static boolean actionPressed( Device device, Action action ) {
		switch( action ) {
			case JUMP: return actionJump[device.id];
			case ATTACK: return actionAttack[device.id];
			default: return false;
		}
	}

	public static void update() {
		for( int i=0; i<17; i++ ) {
			axisMoveX[i] = 0;
			axisMoveY[i] = 0;

			if( i == Device.KEYBOARD.id ) { //Keyboard
				
				if( glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS )
					axisMoveX[i]-=1;
				if( glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS )
					axisMoveX[i]+=1;
				if( glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS )
					axisMoveY[i]-=1;
				if( glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS )
					axisMoveY[i]+=1;

				actionJump[i] = glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS;
				actionAttack[i] = glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS;
				
			} else { //Controller
				
				joyAxis[i] = glfwGetJoystickAxes(i);
				joyButtons[i] = glfwGetJoystickButtons(i);
				
				if(joyAxis[i] != null) {
					float deadzone = .2f;
					if( Math.abs(joyAxis[i].get(0)) >= deadzone )
						axisMoveX[i] = joyAxis[i].get(0);
					if( Math.abs(joyAxis[i].get(1)) >= deadzone )
						axisMoveY[i] = joyAxis[i].get(1);
				}

				if(joyButtons[i] != null) actionJump[i] = joyButtons[i].get(0) == 1;
				if(joyButtons[i] != null) actionAttack[i] = joyButtons[i].get(2) == 1;
				
//				printController(i); //TODO: Remove this
			}
		}
	}
	
	public static void setWindow(long window) {
		Input.window = window;
	}
	
	static void printController(int id) {
		if(joyAxis[id] == null) return;
		String res = "Controller "+id+" axis :";
		for( int i=0; i<joyAxis[id].capacity(); i++ )
			res+="\n "+i+" : "+joyAxis[id].get(i);
		System.out.println(res);
	}
}
