package arenashooter.game.gameStates;

import java.util.HashSet;
import java.util.Set;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.entities.Arena;
import arenashooter.entities.spatials.Camera;

public abstract class GameState {
	protected Arena[] maps;
	protected Arena current;

	public GameState(int nbMap) {
		if(nbMap < 1) {
			Exception e = new Exception("Not enough arenas selected (at least one required)");
			e.printStackTrace();
			nbMap = 1;
		}
		maps = new Arena[nbMap];
		for (int i = 0; i < maps.length; i++) {
			maps[i] = new Arena();
		}
		current = maps[0];
	}

	/**
	 * Call this once the map is ready
	 */
	public void init() {
		// Camera
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default.frag");
		Camera cam = new Camera(current.cameraBasePos);
		cam.attachToParent(current, "camera");
		Window.setCamera(cam);
	}
	
	/**
	 * Called by GameMaster on state change
	 */
	public void destroy() {
		Set<String> entities = new HashSet<>(current.getChildren().keySet());
		for(String e : entities)
			current.getChild(e).detach();
	}
	
	public Camera getCamera() {
		return (Camera) current.getChild("camera");
	}

	public void update(double delta) {
		current.step(delta);
	}

	public void draw() {
		current.drawSelfAndChildren();

		Window.endTransparency(); // Make sure to end transparency
	}

	public Arena getMap() {
		return current;
	}
}
