package arenashooter.game.gameStates;

import java.util.ArrayList;
import java.util.HashSet;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.ui.MenuPause;
import arenashooter.entities.Controller;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Character;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.engineParam.GameMode;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Game extends GameState {
	private int nbPlayers = GameMaster.gm.controllers.size();
	private ArrayList<Character> players = new ArrayList<>(nbPlayers);

	int currentRound = 1;
	GameMode gameMode = GameMode.Death_Match;
	int nbRounds = 1;
	boolean teams = false;
	
	//Les teams sont pour l'instant au nombre de 2. On pourra changer l'implementation plus tard en faisant en sorte d'avoir autant de team que voulues.
	HashSet<Controller> team1 = new HashSet<Controller>();
	HashSet<Controller> team2 = new HashSet<Controller>();
	Timer roundTimer;
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

	/**
	 * @author Marin C Evaluates if one character or less is alive. (Trigger this
	 *         function only when a character dies)
	 */
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
		endGame.reset();
		chooseWinner.reset();
		gameMode = GameParam.getGameMode();
		nbRounds = GameParam.getRound();
		GameParam.getTeam();

		for (Controller controller : GameMaster.gm.controllers) {
			// Ce n'est plus un spawn aleatoire
			Character character = controller.createNewCharacter(this, map.GetRandomRespawnch());
			character.attachToParent(map, character.genName());
		}

		oneLeft = false;

		// Camera
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		Camera cam = new Camera(new Vec3f(0, 0, 6));
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
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_BACK)
				| Input.actionJustPressed(Device.CONTROLLER01, Action.UI_PAUSE)) {
			if (menu == null)
				menu = new MenuPause();
			else
				menu = null;
		}
		if (menu != null) {
			menu.update(d);
			menu.draw();
			return;
		} else {

			if (oneLeft && !chooseWinner.inProcess) {
				chooseWinner.setProcessing(true);
			}
			chooseWinner.step(d);
			if (chooseWinner.isOver() && !endGame.inProcess) {
				endGame.setProcessing(true);
			}
			endGame.step(d);
			if (endGame.isOver()) {
				if (nbRounds < nbRounds) {
					currentRound++;
					init();
				} else {
					GameMaster.gm.requestNextState(new Score(), "data/mapXML/empty.xml");
				}
			}

			if (Window.getCamera() != null) {
				Window.getCamera().center(players, null, d);
				// Window.getCamera().center(players, map.cameraBounds, d); //TODO: Fix camera,
				// c'est du bousin
				// bounds and uncomment this
				Audio.setListener(Window.getCamera().pos(), Window.getCamera().rotation);
			} else
				Audio.setListener(new Vec3f(), Quat.fromAngle(0));

			// Update controllers
			for (Controller controller : GameMaster.gm.controllers)
				controller.step(d);

			super.update(d);

			// map.step(d);
		}
	}
}