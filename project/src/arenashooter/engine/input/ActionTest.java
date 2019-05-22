package arenashooter.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWGamepadState;

/**
 * Input actions
 */
public enum ActionTest {
	JUMP {

		@Override
		public boolean isKeyPressed(long window) {
			return glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_A) == GLFW_PRESS;
		}

	},
	ATTACK {

		@Override
		public boolean isKeyPressed(long window) {
			boolean x = glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS;
			boolean mouse = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;
			return x || mouse;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			return gamePad.axes(GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER) == GLFW_PRESS;
		}
	},
	Get_Item {
		@Override
		public boolean isKeyPressed(long window) {
			return glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_X) == GLFW_PRESS;
		}
	},
	Drop_Item {
		@Override
		public boolean isKeyPressed(long window) {
			return glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_Y) == GLFW_PRESS;
		}
	},
	UI_Left {
		@Override
		public boolean isKeyPressed(long window) {
			boolean q = glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS;
			boolean left = glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS;
			return q || left;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_X) <= -0.3f;
			boolean s = gamePad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_LEFT) == GLFW_PRESS;
			return p || s;
		}
	},
	UI_Right {
		@Override
		public boolean isKeyPressed(long window) {
			boolean d = glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS;
			boolean right = glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS;
			return d || right;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_X) >= 0.3f;
			boolean s = gamePad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_RIGHT) == GLFW_PRESS;
			return p || s;
		}
	},
	UI_Up {
		@Override
		public boolean isKeyPressed(long window) {
			boolean z = glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS;
			boolean up = glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS;
			return z || up;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_Y) <= -0.3f;
			boolean s = gamePad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_UP) == GLFW_PRESS;
			return p || s;
		}
	},
	UI_Down {
		@Override
		public boolean isKeyPressed(long window) {
			boolean s = glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS;
			boolean down = glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS;
			return s || down;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			boolean p = gamePad.axes(GLFW_GAMEPAD_AXIS_LEFT_Y) >= 0.3f;
			boolean s = gamePad.buttons(GLFW_GAMEPAD_BUTTON_DPAD_DOWN) == GLFW_PRESS;
			return p || s;
		}
	},
	UI_Ok{
		@Override
		public boolean isKeyPressed(long window) {
			return glfwGetKey(window, GLFW_KEY_ENTER) == GLFW_PRESS;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_A) == GLFW_PRESS;
		}
	},
	UI_Back {
		@Override
		public boolean isKeyPressed(long window) {
			return glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_B) == GLFW_PRESS;
		}
	},
	UI_Pause{
		@Override
		public boolean isKeyPressed(long window) {
			return glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS;
		}

		@Override
		public boolean isButtonPressed(GLFWGamepadState gamePad) {
			return gamePad.buttons(GLFW_GAMEPAD_BUTTON_START) == GLFW_PRESS;
		}
	},
;

	public abstract boolean isKeyPressed(long window);

	public abstract boolean isButtonPressed(GLFWGamepadState gamePad);
}