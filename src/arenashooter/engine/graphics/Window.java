package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import arenashooter.engine.math.Vec2d;

/**
 * Game window
 */

public class Window {
	private static final int WIDTH_MIN = 640, HEIGHT_MIN = 480;
	
	private long window;
	private GLFWVidMode vidmode;
	
	public Window(int width, int height, String title) {
		if(!glfwInit()) System.err.println("Can't initialize GLFW !");
		
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		//On s'assure que la fenetre respecte les tailles minimales et maximales
		width = Math.max(WIDTH_MIN, Math.min(width, vidmode.width()));
		height = Math.max(HEIGHT_MIN, Math.min(height, vidmode.height()));

		//Interdire le redimensionnement manuel
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		//Masquer la fenetre jusqu'a ce qu'elle soit entierement creee
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

		window = glfwCreateWindow(width, height, title, NULL, NULL);

		if (window == NULL) System.err.println("Can't create window !");
		
		//Centrer la fenetre
		glfwSetWindowPos(window, (vidmode.width()-width)/2, (vidmode.height()-height)/2 );
		
		//Definir le contexte de la fenetre
		glfwMakeContextCurrent(window);
		
		//Lier la fenetre a OpenGL
		GL.createCapabilities();
		
		//Definit la couleur de fond de la fenetre
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		//Afficher la fenetre
		glfwShowWindow(window);
	}
	
	/**
	 * @return Si l'utilisateur demande la fermeture de la fenetre
	 */
	public boolean requestClose() {
		return glfwWindowShouldClose(window);
	}
	
	//Carré de test
	Vec2d pos = new Vec2d();
	double size = 200;
	
	public void update() {
		FloatBuffer pad1joys = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
		
		float moveX = 0, moveY = 0;
		
		if(pad1joys != null) {
			float deadzone = .2f;
			if( Math.abs(pad1joys.get(0)) > deadzone || Math.abs(pad1joys.get(1)) > deadzone ) {
				moveX = pad1joys.get(0);
				moveY = pad1joys.get(1);
			}
		}
		
		if( glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS )
			moveX=-1;
		if( glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS )
			moveX=1;
		if( glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS )
			moveY=-1;
		if( glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS )
			moveY=1;
		
		pos.x += moveX*3;
		pos.y += moveY*3;
		
		glLoadIdentity();
		glOrtho(0, 1280, 720, 0, 10, -10);
		glBegin(GL_QUADS);
		glColor3d(1, 0, 0);
		glVertex3d(pos.x+size, pos.y, 0);
		glColor3d(0, 1, 0);
		glVertex3d(pos.x, pos.y, 0);
		glColor3d(1, 1, 0);
		glVertex3d(pos.x, pos.y+size, 0);
		glColor3d(0, 0, 1);
		glVertex3d(pos.x+size, pos.y+size, 0);
		glEnd();
		
		glfwSwapBuffers(window);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glfwPollEvents();
	}
	
	/**
	 * Detruit la fenetre
	 */
	public void destroy() {
		glfwDestroyWindow(window);
	}
	
	/**
	 * Modifie la taille de la fenetre
	 * @param newWidth
	 * @param newHeight
	 */
	public void resize(int width, int height) { //TODO: A tester
		width = Math.max(WIDTH_MIN, Math.min(width, vidmode.width()));
		height = Math.max(HEIGHT_MIN, Math.min(height, vidmode.height()));
		glfwSetWindowSize(window, width, height);
	}
	
	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}
}
