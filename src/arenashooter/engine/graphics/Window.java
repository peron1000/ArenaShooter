package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import arenashooter.engine.Input;
import arenashooter.engine.math.Utils;
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
		
		//Activation des textures
		glEnable(GL_TEXTURE_2D);
		
		//Activation de la transparence
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		//Definit la couleur de fond de la fenetre
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		//Afficher la fenetre
		glfwShowWindow(window);
		
		Input.setWindow(window);
		
		tex = new Texture("data/test.png"); //Texture de test
	}
	
	/**
	 * @return Si l'utilisateur demande la fermeture de la fenetre
	 */
	public boolean requestClose() {
		return glfwWindowShouldClose(window);
	}
	
	//Boule magique
	Vec2d pos = new Vec2d();
	Vec2d vel = new Vec2d();
	double size = 200;
	Texture tex;
	
	public void update( double delta ) {
		//Physique et controles de la boule magique
		vel.x = Utils.lerpD(vel.x, Input.getAxis("moveX")*500, delta*5);
		
		if( Input.actionPressed("jump") )
			if( pos.y == 450 )
				vel.y = -800;
		
		if(pos.y < 450)
			vel.y += 9.807*200*delta;

		pos.add(Vec2d.multiply(vel, delta));
		
		pos.y = Math.min(450, pos.y);
		
		glLoadIdentity();
		glOrtho(0, 1280, 720, 0, 10, -10);
		
		//Ciel
		glBindTexture(GL_TEXTURE_2D, 0);
		glBegin(GL_QUADS);
		glColor3d(.8, .8, 1);
		glVertex3d(1280, 0, 10);
		
		glColor3d(.8, .8, 1);
		glVertex3d(0, 0, 10);
		
		glColor3d(.5, .5, 1);
		glVertex3d(0, 720, 10);
		
		glColor3d(.5, .5, 1);
		glVertex3d(1280, 720, 10);
		glEnd();
		
		tex.bind(); //On bind la texture
		glBegin(GL_QUADS);

		double color1 = (Math.sin( System.currentTimeMillis()*.02 )/2)+.5;
		double color2 = (Math.sin( (System.currentTimeMillis()*.02)+2 )/2)+.5;
		
		glColor3d(color2, color1, color1);
		glTexCoord2i(0, 0);
		glVertex3d(pos.x+size, pos.y, 0);
		
		glColor3d(color1, color2, color1);
		glTexCoord2i(1, 0);
		glVertex3d(pos.x, pos.y, 0);
		
		glColor3d(color2, color2, color1);
		glTexCoord2i(1, 1);
		glVertex3d(pos.x, pos.y+size, 0);
		
		glColor3d(color1, color1, color2);
		glTexCoord2i(0, 1);
		glVertex3d(pos.x+size, pos.y+size, 0);
		
		glEnd(); //Fin de la boule magique
		
		glfwSwapBuffers(window);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glfwPollEvents();
		Input.update();
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
