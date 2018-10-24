package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import org.lwjgl.BufferUtils;
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
		
		//TODO: Temp test stuff
		createVBOs();
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
		
		//Projection orthographique
		glLoadIdentity();
		glOrtho(0, 1280, 720, 0, 10, -10);
		
		//Debut VBOs
		glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
		glVertexPointer(3, GL_FLOAT, 0, 0l);

		glBindBuffer(GL_ARRAY_BUFFER, vboCoords);
		glTexCoordPointer(2, GL_FLOAT, 0, 0l);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		//Ciel
		glColor3d(.8, .8, 1);
		
		glPushMatrix();
		glScaled(1280, 720, 1);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glPopMatrix();
		
		glColor3f(1, 1, 1);
		//Fin du ciel
		
		//Boule magique
		tex.bind();
		
		glPushMatrix();
		glTranslated(pos.x, pos.y, 0);
		glScaled(size, size, 1);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glPopMatrix();
		
		Texture.unbind();
		//Fin de la boule magique
		
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		//Fin VBOs
		
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
	
	int vboVertex, vboCoords;
	private void createVBOs() {
		int vertices = 6;

		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertices * 3);
		vertexData.put(new float[] { 1f, 0f, 0f });
		vertexData.put(new float[] { 0f, 0f, 0f });
		vertexData.put(new float[] { 0f, 1f, 0f });
		
		vertexData.put(new float[] { 0f, 1f, 0f });
		vertexData.put(new float[] { 1f, 1f, 0f });
		vertexData.put(new float[] { 1f, 0f, 0f });
		vertexData.flip();

		FloatBuffer uvData = BufferUtils.createFloatBuffer(vertices * 2);
		uvData.put(new float[] { 1f, 0f });
		uvData.put(new float[] { 0f, 0f });
		uvData.put(new float[] { 0f, 1f });
		
		uvData.put(new float[] { 0f, 1f });
		uvData.put(new float[] { 1f, 1f });
		uvData.put(new float[] { 1f, 0f });
		uvData.flip();

		vboVertex = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboCoords = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboCoords);
		glBufferData(GL_ARRAY_BUFFER, uvData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
}
