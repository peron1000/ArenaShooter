package arenashooter.game.gameStates;

import java.util.HashSet;
import java.util.Set;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.json.JsonTransformer;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Camera;
import arenashooter.game.Main;

public abstract class GameState {
	protected Arena current;

	private Thread arenaLoading;
	
	public GameState() {
		current = new Arena();
		init();
	}

	public GameState(String arenaPath) {
		arenaLoading = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					current = JsonTransformer.importArena(arenaPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		arenaLoading.start();
	}

	/**
	 * Call this once the arena is loaded
	 */
	public void init() {
		// Camera
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default.frag");
		Camera cam = new Camera(current.cameraBasePos);
		cam.attachToParent(current, "camera");
		Window.setCamera(cam);

		Window.setCurorVisibility(false);
	}

	/**
	 * Called by GameMaster on state change
	 */
	public void destroy() {
		Set<String> entities = new HashSet<>(current.getChildren().keySet());
		for (String e : entities)
			current.getChild(e).detach();
	}

	public Camera getCamera() {
		return (Camera) current.getChild("camera");
	}

	public void update(double delta) {
		if(!isReady()) {
			Exception e= new Exception("This gameState is updated before the arena had been loaded");
			e.printStackTrace();
			return;
		}
		current.step(delta);
	}

	public void draw() {
		current.renderFirstPass();

		if (!Main.skipTransparency) {
			Window.beginTransparency();
			for (Entity e : current.transparent)
				e.draw(true);
			current.transparent.clear();
			Window.endTransparency();
		}
	}

	public Arena getMap() {
		return current;
	}

	/**
	 * @return <code>true</code> if the arena has been loaded and init method has
	 *         been called or <code>false</code> instead
	 */
	public boolean isReady() {
		return !arenaLoading.isAlive();
	}
}
