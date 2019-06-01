package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH24_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
//import static org.lwjgl.opengl.GL30.GL_RGB16F;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVidMode.Buffer;
import org.lwjgl.opengl.GL;

import arenashooter.engine.Profiler;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.spatials.Camera;

/**
 * Game window
 */
public final class Window {
	private static final int WIDTH_MIN = 640, HEIGHT_MIN = 480;
	
	public static final Logger log = LogManager.getLogger("Render");
	
	private static long window;
	private static GLFWVidMode vidmode;
	private static int width, height;
	private static float ratio;
	
	/** Current Window state is Transparency */
	private static boolean stateTransparency = false;
	/** Current Window state is UI */
	private static boolean stateUi = false;
	
	//Projection
	/** Perspective projection matrix */
	public static Mat4f proj;
	private static float fov = 70;
	private static final float CLIP_NEAR = 2, CLIP_FAR = 4500;
	/** Orthographic projection matrix */
	public static Mat4f projOrtho;
	
	//View
	private static Camera camera = new Camera(new Vec3f(0, 0, 450));
	
	//Post processing
	/** Current post processing settings */
	public static PostProcess postProcess;
	private static Model quad;

	//Framebuffers
	/** Main framebuffer object */
	private static int fbo;
	/** Scene color texture */
	private static int renderTarget;
	private static int colorRenderBuffer, depthRenderBuffer;
	//Internal rendering resolution
	private static int resX, resY;
	public static float resolutionScale = 1;
	
	//Callbacks
	private static GLFWErrorCallback callbackError;
	
	//This class cannot be instantiated
	private Window() {}
	
	/**
	 * Initialize OpenGL and create game window.<br/>
	 * Make sure to call this before anything relying on OpenGL
	 * @param windowWidth
	 * @param windowHeight
	 * @param windowTtitle
	 */
	public static void init(int windowWidth, int windowHeight, String windowTtitle) {
		log.info("Initializing");
		
		callbackError = GLFWErrorCallback.createPrint(System.err);
		glfwSetErrorCallback(callbackError);
		
		if(!glfwInit()) {
			log.error("Cannot initialize GLFW");
			System.exit(1);
		}
		
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
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
		
		window = glfwCreateWindow(WIDTH_MIN, HEIGHT_MIN, windowTtitle, NULL, NULL);

		if (window == NULL) {
			log.fatal("Cannot create window");
			System.exit(1);
		}
		
		setIcon( new String[] {"data/icon_32.png", "data/icon_64.png", "data/icon_128.png"} );
		
		//Attach an openGL context to the window
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		//Set window size, create projection matrix, create framebuffers etc
		resize(windowWidth, windowHeight);
		
		//Center window
		glfwSetWindowPos(window, (vidmode.width()-width)/2, (vidmode.height()-height)/2 );
		
		//Enable textures
		glEnable(GL_TEXTURE_2D);
		
		//Set transparency blend function
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		//Enable depth sorting
		glEnable(GL_DEPTH_TEST);
	    glDepthFunc(GL_LEQUAL);
		
		//Set the clear color to black
		glClearColor(0, 0, 0, 0);
		
		//Initialize Input and link keyboard input to the window
		Input.init(window);
		
		//Load default quad
		quad = Model.loadQuad();
		
		//Load post-processing
		postProcess = new PostProcess("data/shaders/post_process/pp_default");

		//Create projection matrix
		createProjectionMatrix();
		
		//Show the window
		glfwShowWindow(window);
	}
	
	/**
	 * @return User tries to close the window
	 */
	public static boolean requestClose() {
		return glfwWindowShouldClose(window);
	}
	
	/**
	 * Begin a frame, this will update Input and prepare the window for rendering
	 */
	public static void beginFrame() {
		glfwPollEvents();
		Input.update();
		
		//Bind framebuffer object
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glViewport(0, 0, resX, resY);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_DEPTH_TEST);
	}
	
	/**
	 * End a frame, this will swap framebuffers
	 */
	public static void endFrame() {
		Profiler.startTimer(Profiler.POSTPROCESS);
		
		//Bind default framebuffer
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, width, height);
		
		glDisable(GL_DEPTH_TEST);

		//Render full-screen quad for post processing
		postProcess.bind();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, renderTarget);
//		glBindTexture(GL_TEXTURE_2D, colorRenderBuffer);
		postProcess.getShader().setUniformI("sceneColor", 0);
		
		quad.bindToShader(postProcess.getShader());
		quad.bind();
		quad.draw();
		Model.unbind();
		Shader.unbind();
		Texture.unbind();

		glfwSwapBuffers(window);
		
		Profiler.endTimer(Profiler.POSTPROCESS);
	}
	
	public static void beginTransparency() {
		Profiler.startTimer(Profiler.TRANSPARENCY);
		if(stateTransparency) return;
		stateTransparency = true;
		glDepthMask(false);
		glEnable(GL_BLEND);
	}
	
	public static void endTransparency() {
		if(!stateTransparency) {
			Profiler.endTimer(Profiler.TRANSPARENCY);
			return;
		} else {
			stateTransparency = false;
			glDepthMask(true);
			glDisable(GL_BLEND);
			Profiler.endTimer(Profiler.TRANSPARENCY);
		}
	}

	public static void beginUi() {
//		Profiler.startTimer(Profiler.TRANSPARENCY);
		if(stateUi) return;
		stateUi = true;
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		glEnable(GL_BLEND);
	}
	
	public static void endUi() {
		if(!stateUi) {
//			Profiler.endTimer(Profiler.TRANSPARENCY);
			return;
		} else {
			stateUi = false;
			glEnable(GL_DEPTH_TEST);
			glDepthMask(true);
			glDisable(GL_BLEND);
//			Profiler.endTimer(Profiler.TRANSPARENCY);
		}
	}
	
	/**
	 * Destroy the window and terminates GLFW
	 */
	public static void destroy() {
		log.info("Stopping");
		
		glfwDestroyWindow(window);
		
		glfwTerminate();
	}
	
	/**
	 * Change the window size
	 * @param newWidth
	 * @param newHeight
	 */
	public static void resize(int newWidth, int newHeight) {
		width = Math.max(WIDTH_MIN, Math.min(newWidth, vidmode.width()));
		height = Math.max(HEIGHT_MIN, Math.min(newHeight, vidmode.height()));
		
		resolutionScale = Utils.clampF(resolutionScale, 0.5f, 2);
		resX = (int)(width*resolutionScale);
		resY = (int)(height*resolutionScale);
		
		ratio = (float)width/(float)height;
		
		glfwSetWindowSize(window, width, height);
		glViewport(0, 0, resX, resY);

		//Update framebuffers
		destroyFramebuffer();
		createFramebuffer();
		
		//Recreate projection matrix
		createProjectionMatrix();
	}
	
	public static int getWidth() { return width; }

	public static int getHeight() { return height; }
	
	/**
	 * Get all available resolutions for primary monitor
	 * @return sorted list of int[] in {width, height} format
	 */
	public static List<int[]> getAvailableResolutions() {
		Buffer modes = glfwGetVideoModes(glfwGetPrimaryMonitor());

		ArrayList<int[]> res = new ArrayList<>();
		
		for ( int i = 0; i < modes.capacity(); i++ ) {
			modes.position(i);
			int w = modes.width();
			int h = modes.height();
			
			if(w < WIDTH_MIN || h < HEIGHT_MIN) continue; //Skip resolutions inferior to the minimum
			
			boolean duplicate = false;
			for(int[] tempReso : res) {
				if(tempReso[0] == w && tempReso[1] == h) {
					duplicate = true;
					break;
				}
			}
			
			if(!duplicate)
				res.add(new int[] {w, h});
		}
		
		res.sort( new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				if(o1[0] > o2[0])
					return 1;
				if(o1[0] < o2[0])
					return -1;
				if(o1[1] > o2[1])
					return 1;
				if(o1[1] < o2[1])
					return -1;
				return 0;
			}
		});

		return res;
	}
	
	/**
	 * Get screen aspect ratio (width/height)
	 */
	public static float getRatio() {
		return ratio;
	}
	
	/**
	 * Set the camera's field of view
	 * @param verticalFOV new FOV
	 */
	private static void setFOV(float verticalFOV) {
		fov = verticalFOV;
		createProjectionMatrix();
	}
	
	/**
	 * @return current view matrix
	 */
	public static Mat4f getView() {
		if(camera == null) return Mat4f.identity();
		else return camera.viewMatrix;
	}
	
	/**
	 * Create the projection matrix based on window size
	 */
	public static void createProjectionMatrix() {
		float sizeY = 100;
		float sizeX = sizeY*ratio;
		projOrtho = Mat4f.ortho(0.01f, 100, -sizeX/2, sizeY/2, sizeX/2, -sizeY/2);
		proj = Mat4f.perspective(CLIP_NEAR, CLIP_FAR, fov, ratio);
	}
	
	private static void createFramebuffer() {
		//Create framebuffer
		fbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		
		//Color
		colorRenderBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, colorRenderBuffer);
//		glRenderbufferStorage(GL_RENDERBUFFER, GL_RGB16F, resX, resY);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_RGB8, resX, resY);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, colorRenderBuffer);

		//Depth
		depthRenderBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthRenderBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, resX, resY);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderBuffer);
		
		//Create texture
		glActiveTexture(GL_TEXTURE0);
		renderTarget = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, renderTarget);
		
		//Create empty image
//		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, resX, resY, 0, GL_RGB, GL_FLOAT, 0);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, resX, resY, 0, GL_RGB, GL_FLOAT, 0);
		
		//Filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		
		//Set wraping to clamp
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		//Attach texture to framebuffer
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, renderTarget, 0);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		
		int fboStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);

		if (fboStatus != GL_FRAMEBUFFER_COMPLETE)
			log.error("Could not create FBO: " + fboStatus);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	private static void destroyFramebuffer() {
		glDeleteRenderbuffers(depthRenderBuffer);
		glDeleteRenderbuffers(colorRenderBuffer);
		glDeleteFramebuffers(fbo);
		glDeleteTextures(renderTarget);
	}
	
	/**
	 * Set the window title
	 * @param title new title
	 */
	public static void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}
	
	/**
	 * Enable or disable vertical synchronization
	 * @param enable new vSync state
	 */
	public static void setVsync(boolean enable) {
		glfwSwapInterval( enable ? 1 : 0 );
	}
	
	/**
	 * @return current camera
	 */
	public static Camera getCamera() { return camera; }
	
	/**
	 * Change the current camera, affecting view and projection matrices
	 * @param newCam
	 */
	public static void setCamera(Camera newCam) {
		camera = newCam;
		setFOV(newCam.getFOV());
	}
	
	private static void setIcon(String[] paths) {
		Image image[] = new Image[paths.length];
		
		for( int i=0; i<paths.length; i++ )
			image[i] = Image.loadImage(paths[i]);
		
		GLFWImage.Buffer images = GLFWImage.malloc(paths.length);
		try {
			for( int i=0; i<paths.length; i++ ) {
				images
					.position(i)
					.width(image[i].width)
					.height(image[i].height)
					.pixels(image[i].buffer);
			}
			
			glfwSetWindowIcon(window, images);
				
		} catch(Exception e) {
			log.error("Error loading window icons");
			e.printStackTrace();
		}
	}
}
