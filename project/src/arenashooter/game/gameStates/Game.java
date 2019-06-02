package arenashooter.game.gameStates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.ui.MenuPause;
import arenashooter.entities.Arena;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;
import arenashooter.game.Controller;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.engineParam.GameMode;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Game extends GameState {
	private int nbPlayers = GameMaster.gm.controllers.size();
	private ArrayList<Character> players = new ArrayList<>(nbPlayers);
	private ArrayList<Arena> mapsToShuffle = new ArrayList<Arena>();

	MenuPause menu;
	int currentRound = 1;
	private final GameMode gameMode = GameParam.getGameMode();
	private final int nbRounds = GameParam.getRound();
	private final boolean teams = GameParam.getTeam();
	private InputListener inputs = new InputListener();
	
	private static SoundSource bgm;

	// Les teams sont pour l'instant au nombre de 2. On pourra changer
	// l'implementation plus tard en faisant en sorte d'avoir autant de team que
	// voulues.
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
	public Timer endRound = new Timer(2);

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

	public Game(int nbMap) {
		super(nbMap);
		menu = new MenuPause(0, 0);
		menu.selectorVisible = false;
		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_PAUSE:
						menu.selectorVisible = !menu.selectorVisible;
						break;

					default:
						break;
					}
				}
			}
		});
		
		if(bgm != null) {
			bgm.destroy();
			bgm = null;
		}
		bgm = Audio.createSource("data/music/Super_blep_serious_fight.ogg", AudioChannel.MUSIC, .5f, 1);
		bgm.setLooping(true);
	}

	@Override
	public void init() {
		for (Arena map : maps) {
			mapsToShuffle.add(map);
		}
		Collections.shuffle(mapsToShuffle);
		current = mapsToShuffle.get(0);
		newRound();
//		bgm.play();
	}

	public void characterDeath(Controller controller, Character character) {
		players.remove(character);
		evalOneLeft();
	}

	private void newRound() {
		current = mapsToShuffle.get(currentRound % mapsToShuffle.size());
		endRound.reset();
		chooseWinner.reset();
		for (Controller controller : GameMaster.gm.controllers) {
			// Ce n'est plus un spawn aleatoire
			Character character = controller.createNewCharacter(current.GetRandomRespawnch2());
			players.add(character);
			character.attachToParent(current, character.genName());
		}
		oneLeft = false;
		super.init();
	}

	@Override
	public void update(double d) {
		if (menu.selectorVisible) {
			menu.update(d);
			return;
		} else {

			if (oneLeft && !chooseWinner.inProcess) {
				chooseWinner.setProcessing(true);
			}
			chooseWinner.step(d);
			if (chooseWinner.isOver() && !endRound.inProcess) {
				endRound.setProcessing(true);
			}
			endRound.step(d);
			if (endRound.isOver()) {
				if (currentRound < nbRounds || nbRounds == -1) {
					currentRound++;
					newRound();
				} else {
					bgm.stop();
					GameMaster.gm.requestNextState(new Score(), "data/mapXML/menu_empty.xml");
				}
			}

			if (getCamera() != null) {
				getCamera().center(players, current.cameraBasePos, d);
				Audio.setListener(getCamera().getWorldPos(), getCamera().getWorldRot());
			} else
				Audio.setListener(new Vec3f(), Quat.fromAngle(0));

			// Update controllers
			for (Controller controller1 : GameMaster.gm.controllers)
				controller1.step(d);

			super.update(d);

		}
		inputs.step(d);
	}

	@Override
	public void draw() {
		super.draw();
		if (menu.selectorVisible) {
			menu.draw();
		}
	}
}