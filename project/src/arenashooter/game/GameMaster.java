package arenashooter.game;

import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.Loading;
import arenashooter.game.gameStates.Start;

public class GameMaster {
	public static final GameMaster gs = new GameMaster();
	
	/**
	 * 0 -> loading <br/>
	 * 1 -> Start <br/>
	 */
	public static final GameState[] allGameState = new GameState[2];
	
	private GameState current = allGameState[0];
	
	private GameMaster() {
		// Constructor untouchable
		
		allGameState[0] = Loading.loading;
		allGameState[1] = Start.start;
	}
	
	public void setState(GameState gameState) {
		current = gameState;
		// TODO : to be completed
	}
	
	public void draw() {
		current.draw();
	}
}
