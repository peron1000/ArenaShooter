package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;

/**
 * @author Argan Loisel
 */

public class Window {

	private long window;
	
	public Window(int width, int height, String title) {
		if(!glfwInit()) System.err.println("Can't create window !");

		window = glfwCreateWindow(width, height, title, NULL, NULL);

		if (window == NULL) System.err.println("Can't create window !");

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		//Centrer la fenetre
		glfwSetWindowPos(window, ( vidmode.width()/2 )-( width/2 ), ( vidmode.height()/2 )-( height/2 ));
		
		//Definir le contexte de la fenetre
		glfwMakeContextCurrent(window);
		
		//Afficher la fenetre
		glfwShowWindow(window);
	}
	
	/**
	 * @return Si l'utilisateur demande la fermeture de la fenetre
	 */
	public boolean requestClose() {
		return glfwWindowShouldClose(window);
	}
	
	public void update() {
		glfwPollEvents();
		glfwSwapBuffers(window);
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
	public void resize(int newWidth, int newHeight) {
		//TODO: faire ca
	}
}
