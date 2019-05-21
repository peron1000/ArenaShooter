package arenashooter.game.gameStates;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Map;
import arenashooter.entities.spatials.Camera;

public abstract class GameState {
	protected Map[] maps;
	protected Map current;

	public GameState(int nbMap) {
		if(nbMap < 1) {
			Exception e = new Exception("Nombre de Map insuffisant (inferieur a 1)");
			e.printStackTrace();
			nbMap = 1;
		}
		maps = new Map[nbMap];
		for (int i = 0; i < maps.length; i++) {
			maps[i] = new Map();
		}
		current = maps[0];
	}

	public void init() {
		// Camera
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		Camera cam = new Camera(new Vec3f(0, 0, 850));
		cam.attachToParent(current, "camera");
		Window.setCamera(cam);
	}

	public void update(double delta) {
		current.step(delta);
	}

	public void draw() {
		current.drawSelfAndChildren();

		Window.endTransparency(); // Make sure to end transparency
	}

	public Map getMap() {
		return current;
	}
}
