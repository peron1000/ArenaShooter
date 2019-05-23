package arenashooter.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Stack;

import arenashooter.entities.Entity;
import arenashooter.entities.Arena;
import arenashooter.game.gameStates.CharacterChooser;
import arenashooter.game.gameStates.Game;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.Intro;
import arenashooter.game.gameStates.Loading;
import arenashooter.game.gameStates.MapChooser;
import arenashooter.game.gameStates.Config;
import arenashooter.game.gameStates.Score;
import arenashooter.game.gameStates.Start;

public class GameMaster {

	public ArrayList<Controller> controllers = new ArrayList<>();

	Stack<GameState> stateStack = new Stack<>();
	public static GameState current = new Start();

	public static final GameMaster gm = new GameMaster();

	public static final String mapEmpty = "data/mapXML/empty.xml";

	private GameMaster() {
		// Constructor untouchable
	}

	@SuppressWarnings("unchecked")
	public void requestNextState(GameState nextState, String... nextStateMap) {

		if (current == Loading.loading) { // Loading
			current = Loading.loading.getNextState();
		} else {
			if (current instanceof Start) { // Start
			} else if (current instanceof CharacterChooser) { // Character chooser
			} else if (current instanceof MapChooser) { // Map chooser
			} else if (current instanceof Intro) { // Intro movie
			} else if(current instanceof Config) {
			} else if(current instanceof Game) {
			} else if(current instanceof Score) {
				goBackTo((Class<GameState>) nextState.getClass());
			}
			stateStack.push(current);
			current = Loading.loading;
			current.init();
			Loading.loading.setNextState(nextState, nextStateMap);
		}
	}

	public void requestPreviousState() {
		 current = stateStack.pop();
		 if(current instanceof Intro) {
			 current = new Start();
		 }
	}

	private void goBackTo(Class<GameState> stateClass) {
		boolean boo = false;
		for (GameState gameState : stateStack) {
			if (gameState.getClass().equals(stateClass)) {
				boo = true;
			}
		}
		if(boo)
			while(!(current.getClass().equals(stateClass))){
				System.out.println(current.getClass().getName());
				requestPreviousState();
			}
		requestPreviousState();
	}
	
	public Arena getMap() {
		return current.getMap();
	}

	public void draw() {
		if (current.getMap() != null)
			current.draw();
	}

	public void update(double delta) {
		current.update(delta);
	}
}
