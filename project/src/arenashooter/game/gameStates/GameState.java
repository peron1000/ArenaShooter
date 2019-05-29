package arenashooter.game.gameStates;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Arena;
import arenashooter.entities.spatials.Camera;

public abstract class GameState {
	protected Arena[] maps;
	protected Arena current;

	public GameState(int nbMap) {
		if(nbMap < 1) {
			Exception e = new Exception("Nombre de Map insuffisant (inferieur a 1)");
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
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		Camera cam = new Camera(current.cameraBasePos);
		cam.attachToParent(current, "camera");
		Window.setCamera(cam);
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
