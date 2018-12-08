package arenashooter.engine;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_2;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_3;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_4;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_5;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_6;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_7;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_8;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_9;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_10;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_11;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_12;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_13;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_14;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_15;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_16;

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
	KEYBOARD(16);

	protected final int id;
	
	private Device( int id ) {
		this.id = id;
	}
}
