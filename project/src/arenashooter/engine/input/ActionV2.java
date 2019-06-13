package arenashooter.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWGamepadState;

/**
 * Input actions
 */
public enum ActionV2 {
	JUMP {

		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_A) == GLFW_PRESS;
		}

	},
	ATTACK {

		@Override
		public boolean keyboardInput(long window) {
			boolean x = glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS;
			boolean mouse = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;
			return x || mouse;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.axes(GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER) == GLFW_PRESS;
		}
	},
	GET_ITEM {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_X) == GLFW_PRESS;
		}
	},
	DROP_ITEM {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_Y) == GLFW_PRESS;
		}
	},
	UI_LEFT {
		@Override
		public boolean keyboardInput(long window) {
			boolean q = glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS;
			boolean left = glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS;
			return q || left;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_X) <= -0.3f;
			boolean s = gamePad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_LEFT) == GLFW_PRESS;
			return p || s;
		}
	},
	UI_RIGHT {
		@Override
		public boolean keyboardInput(long window) {
			boolean d = glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS;
			boolean right = glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS;
			return d || right;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_X) >= 0.3f;
			boolean s = gamePad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_RIGHT) == GLFW_PRESS;
			return p || s;
		}
	},
	UI_UP {
		@Override
		public boolean keyboardInput(long window) {
			boolean z = glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS;
			boolean up = glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS;
			return z || up;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_Y) <= -0.3f;
			boolean s = gamePad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_UP) == GLFW_PRESS;
			return p || s;
		}
	},
	UI_DOWN {
		@Override
		public boolean keyboardInput(long window) {
			boolean s = glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS;
			boolean down = glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS;
			return s || down;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_Y) >= 0.3f;
			boolean s = gamePad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_DOWN) == GLFW_PRESS;
			return p || s;
		}
	},
	UI_OK {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS
					&& (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) != GLFW_PRESS
							&& glfwGetKey(window, GLFW_KEY_RIGHT_SHIFT) != GLFW_PRESS);
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_A) == GLFW_PRESS;
		}
	},
	UI_BACK {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_B) == GLFW_PRESS;
		}
	},
	UI_PAUSE {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_START) == GLFW_PRESS;
		}
	},
	UI_CONTINUE {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_ENTER) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_START) == GLFW_PRESS;
		}
	},
	DEBUG_SHOW_COLLISION {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_F1) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return false;
		}
	},
	DEBUG_TOGGLE_PROFILER {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_F2) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return false;
		}
	},
	UI_DOWN2 {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_K) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_RIGHT_Y) >= 0.3f;
			return p;
		}
	},
	UI_UP2 {
		@Override
		public boolean keyboardInput(long window) {
			boolean z = glfwGetKey(window, GLFW_KEY_I) == GLFW_PRESS;
			return z;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_RIGHT_Y) <= -0.3f;
			return p;
		}
	},
	UI_RIGHT2 {
		@Override
		public boolean keyboardInput(long window) {
			boolean d = glfwGetKey(window, GLFW_KEY_L) == GLFW_PRESS;
			return d;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_RIGHT_X) >= 0.3f;
			return p;
		}
	},
	UI_LEFT2 {
		@Override
		public boolean keyboardInput(long window) {
			boolean q = glfwGetKey(window, GLFW_KEY_J) == GLFW_PRESS;
			return q;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_RIGHT_X) <= -0.3f;
			return p;
		}
	},
	UI_CANCEL {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_BACKSPACE) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_X) == GLFW_PRESS;
		}
	},
	UI_CHANGE {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS
					&& (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS
							|| glfwGetKey(window, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS);
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_Y) == GLFW_PRESS;
		}
	},
	UI_ZOOMR {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_KP_ADD) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER) == GLFW_PRESS;
		}
	},
	UI_ZOOML {
		@Override
		public boolean keyboardInput(long window) {
			return glfwGetKey(window, GLFW_KEY_KP_SUBTRACT) == GLFW_PRESS;
		}

		@Override
		public boolean gamepadInput(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_LEFT_BUMPER) == GLFW_PRESS;
		}
	};

	public abstract boolean keyboardInput(long window);

	public abstract boolean gamepadInput(GLFWGamepadState gamePad);
}