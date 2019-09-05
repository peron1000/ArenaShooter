package arenashooter.game.gameStates;

import java.util.HashSet;
import java.util.Set;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Camera;
import arenashooter.game.Main;

public abstract class GameState {
	protected Arena current;

	public GameState() {
		current = new Arena();
	}

	/**
	 * Call this once the map is ready
	 */
	public void init() {
		// Camera
		Window.setPostProcess( new PostProcess("data/shaders/post_process/pp_default.frag") );
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
		current.renderFirstPass();

		if(!Main.skipTransparency) {
			Window.beginTransparency();
			for(Entity e : current.transparent)
				e.draw(true);
			current.transparent.clear();
			Window.endTransparency();
		}
	}

	public Arena getMap() {
		return current;
	}
}
