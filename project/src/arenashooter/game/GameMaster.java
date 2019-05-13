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
import arenashooter.game.gameStates.Start;

public class GameMaster {

	public ArrayList<Controller> controllers = new ArrayList<>();

	Stack<GameState> stateStack = new Stack<>();
	public static GameState current = new Start();

	public static final GameMaster gm = new GameMaster();

	private GameMaster() {
		// Constructor untouchable
	}

	public void requestNextState() {

		if (current == Loading.loading) { // Loading
			current = Loading.loading.getNextState();
		} else {
			stateStack.push(current);
			if (current instanceof Start) { // Start
				current = Loading.loading;
				current.init();
				Loading.loading.setNextState(new Intro(), "data/mapXML/empty.xml");// TODO : create the map
			} else if (current instanceof CharacterChooser) { // Character chooser
				CharacterChooser c = (CharacterChooser) current;
				controllers.clear();
				for (Controller controller : c.getControllers())
					controllers.add(controller);

				current = Loading.loading;
				current.init();
				Loading.loading.setNextState(new MapChooser(), "data/mapXML/empty.xml");
			} else if (current instanceof MapChooser) { // Map chooser
				MapChooser mapChooser = (MapChooser) current;
				current = Loading.loading;
				current.init();
				Loading.loading.setNextState(new Game(), mapChooser.getMapChoosen());// TODO : create the map
			} else if (current instanceof Intro) { // Intro movie
				current = Loading.loading;
				current.init();
				Loading.loading.setNextState(new CharacterChooser(), "data/mapXML/empty.xml");// TODO : create the map
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
