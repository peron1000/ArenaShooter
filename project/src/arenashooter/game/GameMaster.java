package arenashooter.game;

import java.util.ArrayList;

import arenashooter.entities.Controller;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.Loading;
import arenashooter.game.gameStates.Start;

public class GameMaster {
	public static final GameMaster gs = new GameMaster();
	
	public ArrayList<Controller> controllers = new ArrayList<>();
	
	private GameState current = Loading.loading;
	
	private GameMaster() {
		// Constructor untouchable
		Loading.loading.setNextState(Start.start , "mapName");
	}
	
	public void setState(GameState gameState) {
		// TODO : to be completed
		if(current == Start.start) {
			controllers = Start.start.getActivatedControllers();
		}
		current = gameState;
	}
	
	public void draw() {
		current.draw();
	}
	
	public void update(double delta) {
		current.update(delta);
	}
}
