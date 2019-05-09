package arenashooter.game.gameStates;

import java.util.ArrayList;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Controller;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Character;
import arenashooter.game.GameMaster;

public class Game extends GameState {
	private int nbPlayers = GameMaster.gm.controllers.size();
	private ArrayList<Character> players = new ArrayList<>(nbPlayers);
	private boolean oneLeft;

	/**
	 * Time to count before deciding who wins, or if it's a draw if the last one
	 * dies before the timer expires
	 */
	public Timer chooseWinner = new Timer(3.2);

	/**
	 * Time before switching to next map. After the winner has been found
	 */
	public Timer endGame = new Timer(2);

	private void evalOneLeft() {
		int aliveChars = 0;
		for (Controller controller : GameMaster.gm.controllers) {
			if (!controller.hasDeadChar())
				aliveChars++;
		}
		if (aliveChars <= 1)
			oneLeft = true;
	}

	public Game() {
	}

	@Override
	public void init() {
		chooseWinner.restart();
		endGame.restart();
		chooseWinner.setProcessing(false);
		endGame.setProcessing(false);

		for (Controller controller : GameMaster.gm.controllers) {
			Character character = controller.createNewCharacter(this, map.GetRandomRespawn());
			character.attachToParent(map, character.genName());
		}

		oneLeft = false;

		// Camera
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		Camera cam = new Camera(new Vec3f(0, 0, 450));
		cam.attachToParent(map, "camera");
		Window.setCamera(cam);
	}

	public void registerCharacter(Character character) {
		players.add(character);
	}

	public void characterDeath(Controller controller, Character character) {
		players.remove(character);
		evalOneLeft();
	}

	@Override
	public void update(double d) {
		super.update(d);
		
		if (oneLeft && !chooseWinner.inProcess) {
			System.out.println("melp !");
			chooseWinner.setProcessing(true);
		}
		chooseWinner.step(d);
		if (chooseWinner.isOver() && !endGame.inProcess) {
			endGame.setProcessing(true);
		}
		endGame.step(d);
		if (endGame.isOver()) {
			init();
		}

		if (menu != null)
			return;

		if (Window.getCamera() != null) {
			Window.getCamera().center(players, null, d);
			// Window.getCamera().center(players, map.cameraBounds, d); //TODO: Fix camera
			// bounds and uncomment this
			Audio.setListener(Window.getCamera().pos(), Window.getCamera().rotation);
		} else
			Audio.setListener(new Vec3f(), Quat.fromAngle(0));

		// Update controllers
		for (Controller controller : GameMaster.gm.controllers)
			controller.step(d);

		map.step(d);

	}
}
