package arenashooter.game.gameStates;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.xmlReaders.reader.MapXmlReader;
import arenashooter.entities.Entity;
import arenashooter.entities.Arena;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.LoadingFloor;
import arenashooter.game.ControllerPlayer;
import arenashooter.game.Main;

public class Loading extends GameState {
	private static GameState next = new Start();

	private static MapXmlReader mapXmlReader;

	private static boolean isLoading = false;

	public static Loading loading = new Loading();

	private static String[] toLoad;
	public static Map<File, Texture> loadTexture = new HashMap<>();
	private static Iterator<File> iterator = loadTexture.keySet().iterator();
	private static int indexLoading = 0;
	private static boolean firstStep = true;

	private static Trigger onFinish = new Trigger() {

		@Override
		public void make() {
			// Nothing by default
		}
	};

	private Loading() {
		super(1);
	}

	public static boolean isLoading() {
		return isLoading;
	}

	public static void loadingStep() {
		if(firstStep) {
			firstStep = false;
			newMapXmlReader();
		} else {
			boolean finish = mapXmlReader.loadNextEntity();
	
			if (finish) {
				if (indexLoading < toLoad.length) {
					newMapXmlReader();
				} else if (iterator.hasNext()) {
					File file = iterator.next();
					Texture texture = Texture.loadTexture(
							"data/MAP_VIS/" + file.getName().substring(0, file.getName().lastIndexOf('.')) + ".png");
					loadTexture.put(file, texture);
				} else {
					onFinish.make();
					loadTexture.clear();
					indexLoading = 0;
					isLoading = false;
				}
			}
		}
	
	}

	private static void newMapXmlReader() {
		mapXmlReader = new MapXmlReader(toLoad[indexLoading]);
		mapXmlReader.load(next.maps[indexLoading]);
		indexLoading++;
	}

	public void setOnFinish(Trigger t) {
		onFinish = t;
	}

	public void init() {
		current = new Arena();
		indexLoading = 0;
		iterator = loadTexture.keySet().iterator();
		firstStep = true;

		Window.postProcess = new PostProcess("data/shaders/post_process/pp_loading");

		List<Entity> entities = new ArrayList<>();

		// Camera
		Camera cam = new Camera(new Vec3f(0, 0, 8));
		cam.setFOV(90);
		entities.add(cam);
		Window.setCamera(cam);

		List<ControllerPlayer> players = Main.getGameMaster().getPlayerControllers();

		float startX = -(players.size() * 1.28f) / 2f;
		int i = 0;
		for (ControllerPlayer c : players) {
			float x = startX + (1.28f * i);

			CharacterSprite sprite = new CharacterSprite(c.info);
			sprite.localPosition.set(x, -.52);
			entities.add(sprite);
			entities.add(new LoadingFloor(new Vec2f(x, .9)));
			i++;
		}

		if (players.size() == 0) // No controllers, just place a floor
			entities.add(new LoadingFloor(new Vec2f(0, .9)));

		for (Entity entity : entities)
			entity.attachToParent(current, entity.genName());
	}

	/**
	 * Set loading target
	 * 
	 * @param next
	 * @param mapPath list of maps to load
	 */
	public void setNextState(GameState next, String... mapPath) {
		isLoading = true;
		if (mapPath.length < next.maps.length) {
			Exception e = new Exception("Not enough map Path given");
			e.printStackTrace();
			toLoad = new String[next.maps.length];
			for (int i = 0; i < toLoad.length; i++) {
				if (i < mapPath.length) {
					toLoad[i] = mapPath[i];
				} else {
					toLoad[i] = mapPath[mapPath.length - 1];
				}
			}
		} else {
			toLoad = mapPath;
		}
		Loading.next = next;
		indexLoading = 0;
	}

	@Override
	public void update(double delta) {
		current.step(delta);
	}

	@Override
	public void draw() {
		super.draw();
	}

}
