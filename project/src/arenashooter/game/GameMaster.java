package arenashooter.game;

import java.util.ArrayList;

import arenashooter.entities.Controller;
import arenashooter.game.gameStates.CharacterChooser;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.Loading;
import arenashooter.game.gameStates.MapChooser;
import arenashooter.game.gameStates.Start;

public class GameMaster {
	public static final GameMaster gm = new GameMaster();
	
	public ArrayList<Controller> controllers = new ArrayList<>();
	
	private static GameState current = Loading.loading;
	
	private GameMaster() {
		// Constructor untouchable
		Loading.loading.setNextState(Start.start , "mapName");// TODO : create the map
	}
	
	public void requestNextState() {
		if(current == Start.start) {
			// TODO : call Loading
			current = CharacterChooser.characterChooser;
		} else if (current == CharacterChooser.characterChooser) {
			// TODO : call Loading
			current = MapChooser.mapChooser;
		} else if (current == MapChooser.mapChooser) {
			current = Loading.loading;
			Loading.loading.setNextState(MapChooser.mapChooser, MapChooser.mapChooser.getMapChoosen());
		}
	}
	
	public void draw() {
		current.draw();
	}
	
	public void update(double delta) {
		if(current != Loading.loading) {
			current.update(delta);
		} else {
			current = Loading.loading.getNextState();
		}
	}
}
