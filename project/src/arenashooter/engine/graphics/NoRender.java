package arenashooter.engine.graphics;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.graphics.particles.EmitterTemplate;
import arenashooter.engine.graphics.particles.ParticleSystem;
import arenashooter.engine.json.MaterialJsonReader;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Mat4fi;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.math.Vec4fi;
import arenashooter.entities.spatials.Camera;

public class NoRender implements Renderer {
	static final Logger log = LogManager.getLogger("Render");
	
	private PostProcess postProcess;
	
	private int width, height;
	
	private float resScale = 1;
	
	private boolean fullscreen;

	//Projection
	/** Perspective projection matrix */
	private Mat4f proj = new Mat4f();
	private float fov = 70;
	private static final float CLIP_NEAR = 1.0f, CLIP_FAR = 4500;
	/** Orthographic projection matrix */
	private Mat4f projOrtho = new Mat4f();
	
	//View
	private Camera camera = new Camera(new Vec3f(0, 0, 450));

	@Override
	public void init(int windowWidth, int windowHeight, boolean fullscreen, float resolutionScale, String windowTtitle) {
		log.info("Initializing (no render)");
		
		width = windowWidth;
		height = windowHeight;
		
		this.fullscreen = fullscreen;
		
		resScale = resolutionScale;
	}

	@Override
	public void destroy() {}

	@Override
	public Logger getLogger() {
		return log;
	}

	@Override
	public boolean requestedClose() {
		return false;
	}

	@Override
	public void beginFrame() {}

	@Override
	public void endFrame() {}

	@Override
	public void beginTransparency() {}

	@Override
	public void endTransparency() {}

	@Override
	public void beginUi() {}

	@Override
	public void endUi() {}

	@Override
	public void stackScissor(float x, float y, float width, float height) {}

	@Override
	public void popScissor() {}

	@Override
	public PostProcess getPostProcess() {
		return postProcess;
	}

	@Override
	public void setPostProcess(PostProcess postProcess) {
		this.postProcess = postProcess;
	}

	@Override
	public float getResScale() {
		return resScale;
	}

	@Override
	public void setResScale(float newScale) {
		resScale = newScale;
	}

	@Override
	public void resize(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
	}

	@Override
	public boolean isFullscreen() {
		return fullscreen;
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	@Override
	public void resize(int newWidth, int newHeight, boolean fullscreen, float resolutionScale) {
		width = newWidth;
		height = newHeight;
		this.fullscreen = fullscreen;
		this.resScale = resolutionScale;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public List<int[]> getAvailableResolutions() {
		List<int[]> res = new ArrayList<>();
		res.add( new int[] {1920, 1080} );
		return res;
	}

	@Override
	public float getRatio() {
		return (float)width/(float)height;
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
		float sizeX = sizeY*getRatio();
		
		Mat4f.ortho(0.01f, 100, -sizeX/2, sizeY/2, sizeX/2, -sizeY/2, projOrtho);
		
		if(getCamera() != null)
			fov = getCamera().getFOV();
		
		Mat4f.perspective(CLIP_NEAR, CLIP_FAR, fov, getRatio(), proj);
	}

	@Override
	public void setTitle(String title) {}

	@Override
	public void setVsync(boolean enable) {}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public void setCamera(Camera newCam) {
		camera = newCam;
	}

	@Override
	public void setCurorVisibility(boolean visibility) { }

	@Override
	public Texture loadTexture(String path) {
		return new NRTexture(path);
	}

	@Override
	public Texture getDefaultTexture() {
		return default_tex;
	}

	@Override
	public Material loadMaterial(String path) {
		return NRMaterial.loadMaterial(path);
	}
	
	@Override
	public Model loadQuad() {
		return new NRModel();
	}
	
	@Override
	public Model loadDisk(int sides) {
		return new NRModel();
	}
	
	@Override
	public Emitter createEmitter(ParticleSystem owner, EmitterTemplate data) {
		return new NREmitter(owner, data);
	}
	
	private final NRTexture default_tex = new NRTexture("data/default_texture.png");
	
	private class NRTexture implements Texture {
		private String path;
		private boolean filtered;
		private int width, height;
		
		private NRTexture(String path) {
			this.path = path;
			
			width = 64;
			height = 46;
		}
		
		@Override
		public void bind() {}

		@Override
		public String getPath() {
			return path;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public Vec2fi getSize() {
			return new Vec2f(width, height);
		}

		@Override
		public Texture setFilter(boolean val) {
			filtered = val;
			return this;
		}

		@Override
		public boolean isFiltered() {
			return filtered;
		}

		@Override
		public boolean isTranslucent() {
			return false;
		}
		
		
		/*
		 * JSON
		 */
		
		@Override
		public String toJson() {
			return new JsonObject().putChain("path", getPath()).putChain("filtered", filtered).toJson();
		}

		@Override
		public void toJson(Writer writable) throws IOException {
			new JsonObject().putChain("path", getPath()).putChain("filtered", filtered).toJson(writable);
		}
		
	}
	
	private static class NRMaterial implements Material {
		private final String name, shaderPathV, shaderPathF;

		/** Should this material be used during transparency pass */
		private boolean transparency = false;

		private Map<String, Integer> paramsI = new HashMap<>();
		private Map<String, Float> paramsF = new HashMap<>();
		private Map<String, Vec2fi> paramsVec2f = new HashMap<>();
		private Map<String, Vec3fi> paramsVec3f = new HashMap<>();
		private Map<String, Vec4fi> paramsVec4f = new HashMap<>();
		private Map<String, Mat4fi> paramsMat4f = new HashMap<>();
		private Map<String, Texture> paramsTex = new HashMap<>();

		private NRMaterial(String name, String shaderPathV, String shaderPathF) {
			this.name = name;
			this.shaderPathV = shaderPathV;
			this.shaderPathF = shaderPathF;
		}

		private static Material loadMaterial(String path) {
			MaterialJsonReader reader = new MaterialJsonReader(path);
			
			NRMaterial res = new NRMaterial(path, reader.vertexShader, reader.fragmentShader);
			res.paramsI.putAll(reader.paramsI);
			res.paramsF.putAll(reader.paramsF);
			res.paramsVec2f.putAll(reader.paramsVec2f);
			res.paramsVec3f.putAll(reader.paramsVec3f);
			res.paramsVec4f.putAll(reader.paramsVec4f);
			res.paramsTex.putAll(reader.paramsTex);
			
			//Add matrices
			res.paramsMat4f.put("model", Mat4f.identity());
			res.paramsMat4f.put("view", Mat4f.identity());
			res.paramsMat4f.put("projection", Mat4f.identity());
			
			return res.clone();
		}

		@Override
		public boolean getTransparency() {
			return transparency;
		}

		@Override
		public void setTransparency(boolean transparency) {
			this.transparency = transparency;
		}

		@Override
		public boolean bind(Model model) {
			return true;
		}

		@Override
		public int getParamI(String name) {
			return paramsI.get(name);
		}

		@Override
		public void setParamI(String name, int value) {
			paramsI.put(name, value);
		}

		@Override
		public float getParamF(String name) {
			return paramsF.get(name);
		}

		@Override
		public void setParamF(String name, float value) {
			paramsF.put(name, value);
		}

		@Override
		public Vec2fi getParamVec2f(String name) {
			return paramsVec2f.get(name);
		}

		@Override
		public void setParamVec2f(String name, Vec2fi value) {
			paramsVec2f.put(name, value);
		}

		@Override
		public Vec3fi getParamVec3f(String name) {
			return paramsVec3f.get(name);
		}

		@Override
		public void setParamVec3f(String name, Vec3fi value) {
			paramsVec3f.put(name, value);
		}

		@Override
		public Vec4fi getParamVec4f(String name) {
			return paramsVec4f.get(name);
		}

		@Override
		public void setParamVec4f(String name, Vec4fi value) {
			paramsVec4f.put(name, value);
		}

		@Override
		public Mat4fi getParamMat4f(String name) {
			return paramsMat4f.get(name);
		}

		@Override
		public void setParamMat4f(String name, Mat4fi value) {
			paramsMat4f.put(name, value);
		}

		@Override
		public Texture getParamTex(String name) {
			return paramsTex.get(name);
		}

		@Override
		public void setParamTex(String name, Texture value) {
			paramsTex.put(name, value);
		}

		@Override
		public void setLights(Set<Light> lights) {}

		@Override
		public Material clone() {
			NRMaterial res = new NRMaterial(name, shaderPathV, shaderPathF);

			res.transparency = transparency;

			for(Entry<String, Integer> entry : paramsI.entrySet())
				res.paramsI.put(entry.getKey(), entry.getValue().intValue());

			for(Entry<String, Float> entry : paramsF.entrySet())
				res.paramsF.put(entry.getKey(), entry.getValue().floatValue());

			for(Entry<String, Vec2fi> entry : paramsVec2f.entrySet())
				res.paramsVec2f.put(entry.getKey(), new Vec2f(entry.getValue()));

			for(Entry<String, Vec3fi> entry : paramsVec3f.entrySet())
				res.paramsVec3f.put(entry.getKey(), new Vec3f(entry.getValue()));

			for(Entry<String, Vec4fi> entry : paramsVec4f.entrySet())
				res.paramsVec4f.put(entry.getKey(), new Vec4f(entry.getValue()));

			for(Entry<String, Mat4fi> entry : paramsMat4f.entrySet())
				res.paramsMat4f.put(entry.getKey(), new Mat4f(entry.getValue()));

			res.paramsTex = new HashMap<>(paramsTex);

			return res;
		}


		/*
		 * JSON
		 */

		private JsonObject toJsonObject() {
			final JsonObject json = new JsonObject();
			json.put("type", "material");
			json.put("shaderVertex", shaderPathV);
			json.put("shaderFragment", shaderPathF);
			json.put("transparent", transparency);

			JsonObject jsonParams = new JsonObject();
			jsonParams.putAll(paramsI);
			jsonParams.putAll(paramsVec2f);
			jsonParams.putAll(paramsVec3f);
			jsonParams.putAll(paramsVec4f);
			jsonParams.putAll(paramsTex);

			//Special case for float parameters which are stored as an array with a single element
			for(Entry<String, Float> entry : paramsF.entrySet())
				jsonParams.put( entry.getKey(), new JsonArray().addChain(entry.getValue()) );

			json.put("params", jsonParams);

			return json;
		}

		@Override
		public String toJson() {
			return toJsonObject().toJson();
		}

		@Override
		public void toJson(Writer writable) throws IOException {
			toJsonObject().toJson(writable);
		}
	}
	
	private class NRModel implements Model {
		@Override
		public void bindToShader(Shader shader) {}

		@Override
		public void bind() {}

		@Override
		public void draw() {}

		@Override
		public void draw(boolean wireframe) {}
	}
	
	private class NREmitter extends Emitter {
		public NREmitter(ParticleSystem owner, EmitterTemplate data) {
			super(owner, data);
		}

		@Override
		public void draw() {}
	}

}
