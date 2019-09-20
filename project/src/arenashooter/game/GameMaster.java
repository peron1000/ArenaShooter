package arenashooter.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import arenashooter.engine.Profiler;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.ActionV2;
import arenashooter.game.gameStates.Config;
import arenashooter.game.gameStates.Game;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.Intro;
import arenashooter.game.gameStates.Loading;
import arenashooter.game.gameStates.Score;
import arenashooter.game.gameStates.Start;
import arenashooter.game.gameStates.Test;
import arenashooter.game.gameStates.engineParam.GameParam;

public class GameMaster {
	public static final String mapEmpty = "data/arena/menu_empty.arena";

	public List<Controller> controllers = new ArrayList<>();

	private Stack<GameState> stateStack = new Stack<>();
	private GameState current = new Start();

	private GameParam gameParam = null;

	private GamesList gamesList;

	private InputListener inputs = new InputListener();

	GameMaster() {
		inputs.actions.add(new EventListener<InputActionEvent>() {
			@Override
			public void launch(InputActionEvent event) {
				if (event.getAction() == ActionV2.DEBUG_SHOW_COLLISION
						&& event.getActionState() == ActionState.JUST_PRESSED)
					Main.drawCollisions = !Main.drawCollisions;
				if (event.getAction() == ActionV2.DEBUG_SKIP_TRANSPARENCY
						&& event.getActionState() == ActionState.JUST_PRESSED)
					Main.skipTransparency = !Main.skipTransparency;
				if (event.getAction() == ActionV2.DEBUG_TOGGLE_PROFILER
						&& event.getActionState() == ActionState.JUST_PRESSED)
					Profiler.toggle();
			}
		});
	}

	/**
	 * @return current GameState
	 */
	public GameState getCurrent() {
		return current;
	}

	public void launchNextGame() {
		if (gameParam == null) {
			gameParam = new GameParam();
			Main.log.error("GameParam is not well created");
		}
		if (gamesList.isOver()) {
			requestNextState(new Score());
		} else if (gamesList.isNextReady()) {
			Game nextGame = gamesList.getNextGame();
			current = nextGame;
			nextGame.init();
		} else {
			System.err.println("next game not ready yet");// FIXME
		}
	}

	public void requestNextState(GameState nextState) {
		stateStack.push(current);

		if (nextState instanceof Config) {
			Config configState = (Config) nextState;
			gameParam = configState.getGameParam();
			if(gameParam.getRound() < 0) {
				gamesList = new GamesListInfinite(gameParam.maps);
			} else {
				// TODO creer un gamesList avec n rounds
			}
		}

		current.destroy();

		current = new Loading(nextState) {

			@Override
			public void endLoading() {
				GameMaster.this.current = nextState;
				nextState.init();
			}
		};

	}

	public void requestNextTest() {
		current = new Test();
	}

	public void requestPreviousState() {
		current.destroy();
		current = stateStack.pop();
		if (current instanceof Intro) {
			current = new Start();
		}
	}

	public void draw() {
		if (current.getMap() != null)
			current.draw();
	}

	public void update(double delta) {
		inputs.step(delta);
		current.update(delta);
	}

	public List<ControllerPlayer> getPlayerControllers() {
		List<ControllerPlayer> res = new ArrayList<>();
		for (Controller c : controllers) {
			if (c instanceof ControllerPlayer)
				res.add((ControllerPlayer) c);
		}
		return res;
	}
}
