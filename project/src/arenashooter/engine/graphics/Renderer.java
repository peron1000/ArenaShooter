package arenashooter.engine.graphics;

import java.util.List;

import org.apache.logging.log4j.Logger;

import arenashooter.engine.math.Mat4fi;
import arenashooter.entities.spatials.Camera;

public interface Renderer {
	/**
	 * Initialize this renderer and create game window.<br/>
	 * @param windowWidth
	 * @param windowHeight
	 * @param windowTtitle
	 */
	public void init(int windowWidth, int windowHeight, boolean fullscreen, float resolutionScale, String windowTtitle);
	
	/**
	 * Destroy the window and terminates the renderer
	 */
	public void destroy();
	
	public Logger getLogger();
	
	/**
	 * @return User tries to close the window
	 */
	public boolean requestedClose();
	
	/**
	 * Begin a frame, this will update Input and prepare the window for rendering
	 */
	public void beginFrame();
	
	/**
	 * End a frame, this will swap framebuffers
	 */
	public void endFrame();
	
	public void beginTransparency();
	
	public void endTransparency();
	
	public void beginUi();
	
	public void endUi();
	
	/**
	 * Add a scissor box to the stack
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void stackScissor(float x, float y, float width, float height);
	
	/**
	 * Pop the last scissor box from the stack
	 */
	public void popScissor();

	public PostProcess getPostProcess();
	
	public void setPostProcess(PostProcess postProcess);
	
	/**
	 * @return current resolution scale
	 */
	public float getResScale();
	
	/**
	 * Change resolution scale
	 * @param newScale
	 */
	public void setResScale(float newScale);
	
	/**
	 * Change the window size or fullscreen resolution
	 * @param newWidth
	 * @param newHeight
	 */
	public void resize(int newWidth, int newHeight);
	
	public boolean isFullscreen();
	
	public void setFullscreen(boolean fullscreen);
	
	/**
	 * Change the window size, resolution scale and set fullscreen mode
	 * @param newWidth
	 * @param newHeight
	 * @param fullscreen
	 * @param resolutionScale
	 */
	public void resize(int newWidth, int newHeight, boolean fullscreen, float resolutionScale);
	
	/**
	 * @return window's width
	 */
	public int getWidth();

	/**
	 * @return window's height
	 */
	public int getHeight();
	
	/**
	 * Get all available resolutions for primary monitor
	 * @return sorted list of int[] in {width, height} format
	 */
	public List<int[]> getAvailableResolutions();
	
	/**
	 * Get screen aspect ratio (width/height)
	 */
	public float getRatio();
	
	/**
	 * @return current view matrix
	 */
	public Mat4fi getView();
	
	/**
	 * @return perspective projection matrix
	 */
	public Mat4fi getProj();
	
	/**
	 * @return orthogonal projection matrix
	 */
	public Mat4fi getProjOrtho();
	
	/**
	 * Create the projection matrix based on window size
	 */
	public void createProjectionMatrix();
	
	/**
	 * Set the window title
	 * @param title new title
	 */
	public void setTitle(String title);
	
	/**
	 * Enable or disable vertical synchronization
	 * @param enable new vSync state
	 */
	public void setVsync(boolean enable);
	
	/**
	 * @return current camera
	 */
	public Camera getCamera();
	
	/**
	 * Change the current camera, affecting view and projection matrices
	 * @param newCam
	 */
	public void setCamera(Camera newCam);
	
	public void setCurorVisibility(boolean visibility);
	
	public Texture loadTexture(String path);
	
	public Texture getDefaultTexture();
	
	public Material loadMaterial(String path);
}
