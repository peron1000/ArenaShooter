package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import arenashooter.engine.Input;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2d;
import arenashooter.engine.math.Vec3f;

/**
 * Game window
 */

public class Window {
	private static final int WIDTH_MIN = 640, HEIGHT_MIN = 480;
	
	private long window;
	private GLFWVidMode vidmode;
	private int width, height;
	
	public Window(int width, int height, String title) {
		if(!glfwInit()) System.err.println("Can't initialize GLFW !");
		
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		//On s'assure que la fenetre respecte les tailles minimales et maximales
		this.width = Math.max(WIDTH_MIN, Math.min(width, vidmode.width()));
		this.height = Math.max(HEIGHT_MIN, Math.min(height, vidmode.height()));

		glfwDefaultWindowHints();
		
		//Interdire le redimensionnement manuel
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		//Masquer la fenetre jusqu'a ce qu'elle soit entierement creee
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		//Set openGL version to 3.2
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		
		window = glfwCreateWindow(this.width, this.height, title, NULL, NULL);

		if (window == NULL) System.err.println("Can't create window !");
		
		//Center window
		glfwSetWindowPos(window, (vidmode.width()-this.width)/2, (vidmode.height()-this.height)/2 );
		
		//Definir le contexte de la fenetre
		glfwMakeContextCurrent(window);
		
		//Lier la fenetre a OpenGL
		GL.createCapabilities();
		
		//Enable textures
		glEnable(GL_TEXTURE_2D);
		
		//Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		//Enable depth sorting
		glEnable(GL_DEPTH_TEST);
	    glDepthFunc(GL_LEQUAL);
		
		//Set the clear color to black
		glClearColor(0, 0, 0, 0);
		
		//Show the window
		glfwShowWindow(window);
		
		//Link keyboard input to the window
		Input.setWindow(window);

		//Create projection matrix
		createProjectionMatrix();
		
		//TODO: Temp test stuff
		tex = new Texture("data/test.png");
		shaderBouleMagique = new Shader("data/shaders/test_boule_magique");
		shaderSky = new Shader("data/shaders/test_sky");
		quad = Model.loadQuad();
	}
	
	/**
	 * @return User tries to close the window
	 */
	public boolean requestClose() {
		return glfwWindowShouldClose(window);
	}
	
	//TODO: Temp variables
	Model quad;
	Vec2d pos = new Vec2d();
	Vec2d vel = new Vec2d();
	double size = 200;
	Texture tex;
	Shader shaderBouleMagique, shaderSky;
	Mat4f proj;
	
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
		
		//Sky
		drawSky();
		
		//Boule magique
		drawBouleMagique();
		
		glfwSwapBuffers(window);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glfwPollEvents();
		Input.update();
	}
	
	void drawBouleMagique() { //TODO: Remove this temp function
		shaderBouleMagique.bind();
		
		//Create matrices
		Vec3f pos3f = new Vec3f( (float)pos.x, (float)pos.y, -1 );
		Quat rot = Quat.fromAngle(0);
		Vec3f scale = new Vec3f( (float)size, (float)size, (float)size );
		Mat4f model = Mat4f.transform(pos3f, rot, scale);
		shaderBouleMagique.setUniformM4("model", model);
		shaderBouleMagique.setUniformM4("view", Mat4f.identity());
		shaderBouleMagique.setUniformM4("projection", proj);
		
		quad.bindToShader(shaderBouleMagique);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		shaderBouleMagique.setUniformI("baseColor", GL_TEXTURE0);
		
		//Color change
		shaderBouleMagique.setUniformF("colorMod", (float)(Math.sin(System.currentTimeMillis()/100d)+1d)/2f);
		
		quad.bind();
		quad.draw();
		
		Model.unbind();
		Shader.unbind();
		Texture.unbind();
	}
	
	void drawSky() { //TODO: Remove this temp function
		shaderSky.bind();
		
		quad.bindToShader(shaderSky);
		
		quad.bind();
		quad.draw();
		
		Model.unbind();
		Shader.unbind();
	}
	
	/**
	 * Destroy the fenetre
	 */
	public void destroy() {
		glfwDestroyWindow(window);
	}
	
	/**
	 * Change the window size
	 * @param newWidth
	 * @param newHeight
	 */
	public void resize(int newWidth, int newHeight) {
		width = Math.max(WIDTH_MIN, Math.min(newWidth, vidmode.width()));
		height = Math.max(HEIGHT_MIN, Math.min(newHeight, vidmode.height()));
		
		glfwSetWindowSize(window, width, height);
		glViewport(0, 0, width, height);

		//Recreate projection matrix
		createProjectionMatrix();
	}
	
	/**
	 * Create the projection matrix based on window size
	 */
	private void createProjectionMatrix() {
//		proj = Mat4f.perspective(0.1f, 10, 90, (float)width/(float)height);
		proj = Mat4f.ortho(0.1f, 10, 0, height, width, 0);
	}
	
	/**
	 * Set the window title
	 * @param title new title
	 */
	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}
	
	/**
	 * Enable or disable vertical synchronization
	 * @param enable new vSync state
	 */
	public void setVsync(boolean enable) {
		glfwSwapInterval( enable ? 1 : 0 );
	}
	
}
