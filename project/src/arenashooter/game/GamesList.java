package arenashooter.game;

import arenashooter.game.gameStates.Game;

public interface GamesList {
	public boolean isOver();
	public boolean isNextReady();
	public Game getNextGame();
}
