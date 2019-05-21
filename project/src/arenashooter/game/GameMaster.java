package arenashooter.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Stack;

import arenashooter.entities.Controller;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;
import arenashooter.game.gameStates.CharacterChooser;
import arenashooter.game.gameStates.Game;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.Intro;
import arenashooter.game.gameStates.Loading;
import arenashooter.game.gameStates.MapChooser;
import arenashooter.game.gameStates.Param;
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

	public void requestNextState(GameState nextState, String... nextStateMap) {

		if (current == Loading.loading) { // Loading
			current = Loading.loading.getNextState();
		} else {
			stateStack.push(current);
			if (current instanceof Start) { // Start
			} else if (current instanceof CharacterChooser) { // Character chooser
			} else if (current instanceof MapChooser) { // Map chooser
			} else if (current instanceof Intro) { // Intro movie
			} else if(current instanceof Param) {
			} else if(current instanceof Game) {
			} else if(current instanceof Score) {
			}
			current = Loading.loading;
			current.init();
			Loading.loading.setNextState(nextState, nextStateMap);
		}
	}

	public void requestPreviousState() {
		if(current instanceof CharacterChooser) {
			controllers.clear();
		}
		 current = stateStack.pop();
		 if(current instanceof Intro) {
			 current = new Start();
		 }
	}

	/**
	 * @return An ArrayList of all entities in the current map of the game
	 */
	public Collection<Entity> getEntities() {
		Collection<Entity> collection = new HashSet<>();
		collection.addAll(current.getMap().getChildren().values());
		return collection;
	}
	
	public Map getMap() {
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
