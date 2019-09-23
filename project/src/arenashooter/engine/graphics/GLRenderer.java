package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

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
import arenashooter.engine.math.Mat4fi;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4i;
import arenashooter.entities.spatials.Camera;

/**
 * Game window using OpenGL
 */
public final class GLRenderer implements Renderer {
	private static final int WIDTH_MIN = 640, HEIGHT_MIN = 480;
	
	static final Logger log = LogManager.getLogger("Render");
	
	private long window;
	private GLFWVidMode vidmode;

	/** Window width or x res for fullscreen */
	private int width;
	/** Window height or y res for fullscreen */
	private int height;
	private float ratio;
	private boolean fullscreen = false;
	
	/** Current Window state is Transparency */
	private boolean stateTransparency = false;
	/** Current Window state is UI */
	private boolean stateUi = false;
	
	//Projection
	/** Perspective projection matrix */
	private Mat4f proj = new Mat4f();
	private float fov = 70;
	private static final float CLIP_NEAR = 1.0f, CLIP_FAR = 4500;
	/** Orthographic projection matrix */
	private Mat4f projOrtho = new Mat4f();
	
	//View
	private Camera camera = new Camera(new Vec3f(0, 0, 450));
	
	//Post processing
	/** Current post processing settings */
	private PostProcess postProcess;
	private GLModel quad;

	//Framebuffers
	/** Main framebuffer object */
	private int fbo;
	/** Scene color texture */
	private int renderTarget;
	private int colorRenderBuffer, depthRenderBuffer;
	//Internal rendering resolution
	private int resX, resY;
	private float resolutionScale = 1;
	
	//Scissor
	private final Stack<Vec4i> scissorStack = new Stack<>();
	
	//Callbacks
	private GLFWErrorCallback callbackError;
	
	/**
	 * Initialize OpenGL and create game window.<br/>
	 * Make sure to call this before anything relying on OpenGL
	 * @param windowWidth
	 * @param windowHeight
	 * @param windowTtitle
	 */
	@Override
	public void init(int windowWidth, int windowHeight, boolean fullscreen, float resolutionScale, String windowTtitle) {
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
		resize(windowWidth, windowHeight, fullscreen, resolutionScale);
		
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
		quad = GLModel.loadQuad();
		
		//Load post-processing
		postProcess = new PostProcess("data/shaders/post_process/pp_default.frag");

		//Create projection matrix
		createProjectionMatrix();
		
		//Show the window
		glfwShowWindow(window);
		
		//Cursor
		setCursor("data/cursor.png");
		setCurorVisibility(false);
	}
	
	@Override
	public Logger getLogger() {
		return log;
	}
	
	@Override
	public boolean requestedClose() {
		return glfwWindowShouldClose(window);
	}
	
	@Override
	public void beginFrame() {
		glfwPollEvents();
		Input.update();
		
		//Bind framebuffer object
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glViewport(0, 0, resX, resY);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_DEPTH_TEST);
	}
	
	@Override
	public void endFrame() {
		if(!scissorStack.empty()) {
			log.warn("Scissor stack is not empty!");
			scissorStack.clear();
			glDisable(GL_SCISSOR_TEST);
		}
		
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
		GLModel.unbind();
		GLShader.unbind();
		GLTexture.unbind();

		glfwSwapBuffers(window);
		
		Profiler.endTimer(Profiler.POSTPROCESS);
	}
	
	@Override
	public void beginTransparency() {
		Profiler.startTimer(Profiler.TRANSPARENCY);
		if(stateTransparency) return;
		stateTransparency = true;
		glDepthMask(false);
		glEnable(GL_BLEND);
	}
	
	@Override
	public void endTransparency() {
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

	@Override
	public void beginUi() {
//		Profiler.startTimer(Profiler.TRANSPARENCY);
		if(stateUi) return;
		stateUi = true;
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		glEnable(GL_BLEND);
	}
	
	@Override
	public void endUi() {
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
	
	@Override
	public void stackScissor(float x, float y, float width, float height) {
		int screenX = (int) Utils.lerpF(0, resX, Utils.inverseLerpF(-50*ratio, 50*ratio, x));
		int screenY = (int) Utils.lerpF(0, resY, Utils.inverseLerpF(50, -50, y));
		int screenW = (int) (resX*(width/(100*ratio)));
		int screenH = (int) (resY*(height/100));
		
		if(scissorStack.empty()) glEnable(GL_SCISSOR_TEST);
		glScissor(screenX, screenY, screenW, screenH);
		scissorStack.push(new Vec4i(screenX, screenY, screenW, screenH));
	}
	
	@Override
	public void popScissor() {
		scissorStack.pop();
		if(scissorStack.empty()) {
			glDisable(GL_SCISSOR_TEST);
		} else {
			Vec4i prev = scissorStack.peek();
			glScissor(prev.x, prev.y, prev.z, prev.w);
		}
		
	}
	
	@Override
	public PostProcess getPostProcess() { return postProcess; }
	
	@Override
	public void setPostProcess(PostProcess postProcess) {
		this.postProcess = postProcess;
	}
	
	@Override
	public void destroy() {
		log.info("Stopping");
		
		glfwDestroyWindow(window);
		
		glfwTerminate();
	}
	
	@Override
	public float getResScale() { return resolutionScale; }
	
	@Override
	public void setResScale(float newScale) {
		resize(width, height, fullscreen, newScale);
	}

	@Override
	public void resize(int newWidth, int newHeight) {
		resize(newWidth, newHeight, fullscreen, resolutionScale);
	}
	
	@Override
	public boolean isFullscreen() {
		return fullscreen;
	}
	
	@Override
	public void setFullscreen(boolean fullscreen) {
		resize(width, height, fullscreen, resolutionScale);
	}
	
	@Override
	public void resize(int newWidth, int newHeight, boolean fullscreen, float resolutionScale) {
		int oldW = width, oldH = height;
//		width = Utils.clampI(newWidth, WIDTH_MIN, vidmode.width());
//		height = Utils.clampI(newWidth, HEIGHT_MIN, vidmode.height());
		width = Math.max(newWidth, WIDTH_MIN);
		height = Math.max(newHeight, HEIGHT_MIN);
		
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		if(fullscreen) {
			if(this.fullscreen) { //Change fullscreen res
				if( oldW != width || oldH != height ) //Check that resolution was changed
					glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, width, height, vidmode.refreshRate());
			} else //Switch to fullscreen
				glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, width, height, vidmode.refreshRate());
		} else {
			if(this.fullscreen) { //Switch to windowed mode
				glfwSetWindowMonitor(window, NULL, 0, 0, width, height, vidmode.refreshRate());

				//Center window
				glfwSetWindowPos(window, (vidmode.width()-width)/2, (vidmode.height()-height)/2 );
			} else //Change window size
				glfwSetWindowSize(window, width, height);
		}
		
		this.fullscreen = fullscreen;
		
		glfwFocusWindow(window);
		
		ratio = (float)width/(float)height;
		
		//Update resolution scale
		this.resolutionScale = Utils.clampF(resolutionScale, 0.5f, 2);
		resX = (int)(width*this.resolutionScale);
		resY = (int)(height*this.resolutionScale);
		
		glViewport(0, 0, resX, resY);

		//Update framebuffers
		destroyFramebuffer();
		createFramebuffer();
		
		//Recreate projection matrix
		createProjectionMatrix();
		
		if(this.fullscreen)
			log.info("Set fullscreen resolution to: "+width+"x"+height);
		else
			log.info("Set window size to: "+width+"x"+height);
		
		log.info("Set resolution scale to: "+this.resolutionScale);
	}
	
	@Override
	public int getWidth() { return width; }

	@Override
	public int getHeight() { return height; }
	
	@Override
	public List<int[]> getAvailableResolutions() {
		Buffer modes = glfwGetVideoModes(glfwGetPrimaryMonitor());

		List<int[]> res = new ArrayList<>();
		
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
	
	@Override
	public float getRatio() { return ratio; }
	
	/**
	 * Set the perspective field of view (regenerates matrices)
	 * @param verticalFOV new FOV
	 */
	private void setFOV(float verticalFOV) {
		fov = verticalFOV;
		createProjectionMatrix();
	}
	
	@Override
	public Mat4fi getView() {
		if(camera == null) return Mat4f.identity();
		else return camera.viewMatrix;
	}
	
	@Override
	public Mat4fi getProj() {
		return proj;
	}
	
	@Override
	public Mat4fi getProjOrtho() {
		return projOrtho;
	}

	@Override
	public void createProjectionMatrix() {
		float sizeY = 100;
		float sizeX = sizeY*ratio;
		
		Mat4f.ortho(0.01f, 100, -sizeX/2, sizeY/2, sizeX/2, -sizeY/2, projOrtho);
		
		if(getCamera() != null)
			fov = getCamera().getFOV();
		
		Mat4f.perspective(CLIP_NEAR, CLIP_FAR, fov, ratio, proj);
	}
	
	private void createFramebuffer() {
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
	
	private void destroyFramebuffer() {
		glDeleteRenderbuffers(depthRenderBuffer);
		glDeleteRenderbuffers(colorRenderBuffer);
		glDeleteFramebuffers(fbo);
		glDeleteTextures(renderTarget);
	}
	
	@Override
	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}
	
	@Override
	public void setVsync(boolean enable) {
		glfwSwapInterval( enable ? 1 : 0 );
	}
	
	@Override
	public Camera getCamera() { return camera; }
	
	@Override
	public void setCamera(Camera newCam) {
		camera = newCam;
		setFOV(newCam.getFOV());
	}
	
	private void setIcon(String[] paths) {
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
	
	private void setCursor(String path) {
		Image image = Image.loadImage(path);
		
		GLFWImage glfwImg = GLFWImage.create();
		glfwImg.set(image.width, image.height, image.buffer);
		
		long cursor = glfwCreateCursor(glfwImg, image.width/2, image.height/2);

		glfwSetCursor(window, cursor);
	}
	
	@Override
	public void setCurorVisibility(boolean visibility) {
		if(visibility)
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		else
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
	}
	
	@Override
	public Texture loadTexture(String path) {
		return GLTexture.loadTexture(path);
	}
	
	@Override
	public Texture getDefaultTexture() {
		return GLTexture.default_tex;
	}
	
	@Override
	public Material loadMaterial(String path) {
		return GLMaterial.loadMaterial(path);
	}
	
	@Override
	public Model loadQuad() {
		return GLModel.loadQuad();
	}
	
	@Override
	public Model loadDisk(int sides) {
		return GLModel.loadDisk(sides);
	}
}
