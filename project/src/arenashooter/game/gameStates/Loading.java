package arenashooter.game.gameStates;

import java.util.ArrayList;
import java.util.Iterator;

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
import arenashooter.game.Game;
import arenashooter.game.GameMaster;

public class Loading extends GameState {
	public static final Loading loading = new Loading();

	private GameState next;

	private MapXmlReader mapXmlReader;

	private boolean mapDone = false;

	private Iterator<Entity> iteratorEntities;

	private Loading() {
	}

	public void init() {
		map = new Map();

		Window.postProcess = new PostProcess("data/shaders/post_process/pp_loading");

		ArrayList<Entity> entities = new ArrayList<>();

		Camera cam = new Camera(new Vec3f(0, 0, 600));
		cam.setFOV(90);
		entities.add(cam);
		Window.setCamera(cam);

		float startX = -(GameMaster.gm.controllers.size() * 128f) / 2f;
		int i = 0;
		for (Controller c : GameMaster.gm.controllers) {
			float x = startX + (128f * i);

			entities.add(new CharacterSprite(new Vec2f(x, -30), c.getCharInfo()));
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
		mapDone = false;
		mapXmlReader = new MapXmlReader(mapName);
	}

	public GameState getNextState() {
		return next;
	}

	private boolean createNewMap() {
		iteratorEntities = mapXmlReader.getEntities().iterator();
		long time = System.currentTimeMillis();
		Map m = next.map;
		while (iteratorEntities.hasNext()) {
			Entity entity = iteratorEntities.next();
			entity.attachToParent(m, entity.genName());
			if (System.currentTimeMillis() - time > 0) {
				return false;
			}
		}
		m.spawn = mapXmlReader.getSpawn();
		m.cameraBounds = mapXmlReader.getCameraBounds();
		m.gravity = mapXmlReader.getGravity();
		m.itemCollection = mapXmlReader.getItemCollection();
		if (next instanceof Game)
			m.init();
		return true;
	}

	@Override
	public void update(double delta) {
		long time = System.currentTimeMillis();
		map.step(delta);
		boolean done = loadNextMap(time);
		if (done) {
			next.init();
			GameMaster.gm.requestNextState();
		}
		time = System.currentTimeMillis() - time;
		if(time > 60) {
			System.out.println("Time to load "+next.getClass().getSimpleName()+" too long "+ time+"ms");
		}
	}

	private boolean loadNextMap(long time) {
		final long timeMax = 20;
		if (!mapDone) {
			boolean entitiesLoaded = mapXmlReader.loadNextEntity();
			if (System.currentTimeMillis() - time > timeMax) {
				return false;
			}
			boolean informationsloaded = mapXmlReader.loadNextInformation();
			if (System.currentTimeMillis() - time > timeMax) {
				return false;
			}
			boolean itemsLoaded = mapXmlReader.loadNextItem();
			if (System.currentTimeMillis() - time > timeMax) {
				return false;
			}
			if (entitiesLoaded && informationsloaded && itemsLoaded) {
				mapDone = createNewMap();
			}
		}
		return mapDone;
	}
}
