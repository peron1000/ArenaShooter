package arenashooter.game.gameStates;

import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.ui.Trigger;
import arenashooter.entities.Entity;
import arenashooter.entities.Arena;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.LoadingFloor;
import arenashooter.game.ControllerPlayer;
import arenashooter.game.Main;
import arenashooter.game.gameStates.loading.LoadingArena;

public class Loading extends GameState {
	private static boolean isLoading = false;

	public static Loading loading = new Loading();

	private static LoadingArena loadingThread = new LoadingArena();

	private static boolean firstStep = true;

	private static Trigger onFinish = new Trigger() {

		@Override
		public void make() {
			// Nothing by default
		}
	};

	protected Loading() {
	}

	public static boolean isLoading() {
		return isLoading;
	}

	public static void loadingStep() {
		if (firstStep) {
			firstStep = false;
			return;
		}
		
		if (loadingThread.isAlive()) {
			return;
		} else {
			isLoading = false;
			onFinish.make();
		}
	}

	public void setOnFinish(Trigger t) {
		onFinish = t;
	}

	public void init() {
		current = new Arena();
		firstStep = true;

		Window.postProcess = new PostProcess("data/shaders/post_process/pp_loading.frag");

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
	public void setNextState(GameState next, String mapPath) {
		isLoading = true;
		
		while (loadingThread.isAlive()) {
			Main.log.error("Previous LoadingThread stil running");
		}
		
		loadingThread = new LoadingArena(next.current, mapPath);
		loadingThread.start();
	}

}
