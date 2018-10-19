package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

/**
 * @author Argan Loisel
 */

public class Window {

	private long window;
	
	public Window(int width, int height, String title) {
		if(!glfwInit()) System.err.println("Can't create window !");

		//Interdire le redimensionnement manuel
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		//Masquer la fenetre jusqu'a ce qu'elle soit entierement creee
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

		window = glfwCreateWindow(width, height, title, NULL, NULL);

		if (window == NULL) System.err.println("Can't create window !");

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		//Centrer la fenetre
		glfwSetWindowPos(window, ( vidmode.width()/2 )-( width/2 ), ( vidmode.height()/2 )-( height/2 ));
		
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
	
	public void update() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glfwSwapBuffers(window);
		
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
	public void resize(int newWidth, int newHeight) {
		//TODO: faire ca
	}
}
