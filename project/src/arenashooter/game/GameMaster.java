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
	
	private static GameState current = new Start();
	
	private GameMaster() {
		// Constructor untouchable
		//Loading.loading.setNextState(new Start() , "data/mapXML/mapXML2.xml");// TODO : create the map
	}
	
	public void requestNextState() {
		if(current instanceof Start) {
			Start start = (Start) current;
			Loading.loading.setNextState(new Game(), "data/mapXML/mapXML.xml");// TODO : create the map
			for (Controller controller : start.getControllers()) {
				controllers.put(controller, new CharacterInfo());
			}	
			current = Loading.loading;
			current.init();
		} else if (current instanceof CharacterChooser) {
			Loading.loading.setNextState(new MapChooser(),"data/mapXML/mapXML.xml");// TODO : create the map
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
//		if(current != Loading.loading) {
			current.update(delta);
//		} else {
//			current = Loading.loading.getNextState();
//			current.init();
//		}
	}
}
