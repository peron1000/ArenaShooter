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

	private static final String mapEmpty = "data/mapXML/empty.xml";

	private GameMaster() {
		// Constructor untouchable
	}

	public void requestNextState(GameState nextState) {

		if (current == Loading.loading) { // Loading
			current = Loading.loading.getNextState();
		} else {
			stateStack.push(current);
			if (current instanceof Start) { // Start
				current = Loading.loading;
				current.init();
				Loading.loading.setNextState(nextState, mapEmpty);// TODO : remettre Intro
			} else if (current instanceof CharacterChooser) { // Character chooser
				controllers.clear();
				for (Controller controller : ((CharacterChooser) current).getControllers())
					controllers.add(controller);

				current = Loading.loading;
				current.init();
				Loading.loading.setNextState(nextState, mapEmpty);
			} else if (current instanceof MapChooser) { // Map chooser
				MapChooser mapChooser = (MapChooser) current;
				current = Loading.loading;
				current.init();
				Loading.loading.setNextState(nextState, mapChooser.getMapChoosen());
			} else if (current instanceof Intro) { // Intro movie
				current = Loading.loading;
				current.init();
				Loading.loading.setNextState(nextState, mapEmpty);// TODO : create the map
			} else if(current instanceof Param) {
				current  = Loading.loading;
				current.init();
				Loading.loading.setNextState(nextState, mapEmpty);
			} else if(current instanceof Game) {
				current  = Loading.loading;
				current.init();
				Loading.loading.setNextState(nextState, mapEmpty);
			}
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
