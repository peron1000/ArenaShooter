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

	private MapXmlReader mapXmlReader;

	private Loading() { 
		super(1);
	}

	public void init() {
		current = new Map();

		Window.postProcess = new PostProcess("data/shaders/post_process/pp_loading");

		ArrayList<Entity> entities = new ArrayList<>();

		//Camera
		Camera cam = new Camera(new Vec3f(0, 0, 8));
		cam.setFOV(90);
		entities.add(cam);
		Window.setCamera(cam);

		float startX = -(GameMaster.gm.controllers.size() * 1.28f) / 2f;
		int i = 0;
		for (Controller c : GameMaster.gm.controllers) {
			float x = startX + (1.28f * i);

			entities.add(new CharacterSprite(new Vec2f(x, -.52), c.info));
			entities.add(new LoadingFloor(new Vec2f(x, .9)));
			i++;
		}
		if (GameMaster.gm.controllers.size() == 0) { // No controllers, just place a floor
			entities.add(new LoadingFloor(new Vec2f(0, .9)));
		}

		for (Entity entity : entities) {
			entity.attachToParent(current, entity.genName());
		}
		
	}

	/**
	 * Set loading target
	 * 
	 * @param next
	 * @param mapPath
	 */
	public void setNextState(GameState next, String... mapPath) {
		if(mapPath.length < next.maps.length) {
			Exception e = new Exception("Not enough map Path given");
			e.printStackTrace();
			for (int i = 0; i < next.maps.length; i++) {
				if(i < mapPath.length) {
					mapXmlReader = new MapXmlReader(mapPath[i]);
				} else {
					mapXmlReader = new MapXmlReader(mapPath[mapPath.length-1]);
				}
				mapXmlReader.load(next.maps[i]);
			}
		} else {
			for (int i = 0; i < next.maps.length; i++) {
				String string = mapPath[i];
				mapXmlReader = new MapXmlReader(string);
				mapXmlReader.load(next.maps[i]);
			}
		}
		this.next = next;
	}

	public GameState getNextState() {
		return next;
	}

	@Override
	public void update(double delta) {
		current.step(delta);
		if(mapXmlReader.isDone()) {
			stopLoading();
		}
	}
	
	public void stopLoading() {
		next.init();
		GameMaster.gm.requestNextState(next,"data/mapXML/empty.xml");
	}
}
