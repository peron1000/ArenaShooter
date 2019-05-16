package arenashooter.game.gameStates;

import java.util.ArrayList;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.xmlReaders.MapXmlReader;
import arenashooter.entities.Controller;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.LoadingFloor;
import arenashooter.game.GameMaster;

public class Loading extends GameState {
	public static final Loading loading = new Loading();

	private GameState next;

	private MapXmlReader mapXmlRedader;

	private Loading() { }

	public void init() {
		map = new Map();

		Window.postProcess = new PostProcess("data/shaders/post_process/pp_loading");

		ArrayList<Entity> entities = new ArrayList<>();

		//Camera
		Camera cam = new Camera(new Vec3f(0, 0, 600));
		cam.setFOV(90);
		entities.add(cam);
		Window.setCamera(cam);

		float startX = -(GameMaster.gm.controllers.size() * 128f) / 2f;
		int i = 0;
		for (Controller c : GameMaster.gm.controllers) {
			float x = startX + (128f * i);

			entities.add(new CharacterSprite(new Vec2f(x, -30), c.info));
			entities.add(new LoadingFloor(new Vec2f(x, 80)));
			i++;
		}
		if (GameMaster.gm.controllers.size() == 0) { // No controllers, just place a floor
			entities.add(new LoadingFloor(new Vec2f(0, 80)));
		}

		for (Entity entity : entities) {
			entity.attachToParent(map, entity.genName());
		}
		
	}

	/**
	 * Set loading target
	 * 
	 * @param next
	 * @param mapName
	 */
	public void setNextState(GameState next, String mapName) {
		this.next = next;
		mapXmlRedader = new MapXmlReader(mapName);
		update(0);
		mapXmlRedader.load(next.map);
	}

	public GameState getNextState() {
		return next;
	}

	@Override
	public void update(double delta) {
		map.step(delta);
		if(mapXmlRedader.isDone()) {
			stopLoading();
		}
	}
	
	public void stopLoading() {
		next.init();
		GameMaster.gm.requestNextState(next);
	}
}
