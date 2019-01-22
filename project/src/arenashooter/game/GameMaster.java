package arenashooter.game;

import java.util.Collection;
import java.util.HashMap;

import arenashooter.entities.Controller;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;
import arenashooter.entities.spatials.CharacterInfo;
import arenashooter.game.gameStates.CharacterChooser;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.Loading;
import arenashooter.game.gameStates.MapChooser;
import arenashooter.game.gameStates.Start;

public class GameMaster {
	public static final GameMaster gm = new GameMaster();
	
	public HashMap<Controller, CharacterInfo> controllers = new HashMap<>(1);
	
	private static GameState current = Loading.loading;
	
	private GameMaster() {
		// Constructor untouchable
	}
	
	public void requestNextState() {
		if(current instanceof Start) {
			Loading.loading.setNextState(new CharacterChooser(), "data/mapXML/mapXML2.xml");// TODO : create the map
			current = Loading.loading;
			current.init();
		} else if (current instanceof CharacterChooser) {
			CharacterChooser c = (CharacterChooser) current;
			for (Controller controller : c.getControllers()) {
				controllers.put(controller, new CharacterInfo());
			}
			Loading.loading.setNextState(new Game(),"data/mapXML/mapXML.xml");// TODO : create the map
			current = Loading.loading;
			current.init();
		} else if (current instanceof MapChooser) {
			MapChooser mapChooser = (MapChooser) current;
			current = Loading.loading;
			current.init();
			Loading.loading.setNextState(new Game(), mapChooser.getMapChoosen());// TODO : create the map
		} else if (current == Loading.loading) {
			current = Loading.loading.getNextState();
			current.init();
		}
	}
	
	/**
	 * @return An ArrayList of all entities in the current map of the game
	 */
	public Collection<Entity> getEntities(){
		return current.getMap().children.values();
	}
	
	public Map getMap() {
		return current.getMap(); 
	}
	
	public void draw() {
		if(current.getMap() != null)
		current.draw();
	}
	
	public void update(double delta) {
		current.update(delta);
	}
}
