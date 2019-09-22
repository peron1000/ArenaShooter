package arenashooter.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import arenashooter.game.gameStates.Game;

public class GamesListInfinite implements GamesList {

	private final int bufferedGame = 4;

	private LinkedList<String> toShuffle;
	private Game[] games = new Game[bufferedGame];
	private int indexNext = 0;

	public GamesListInfinite(Set<String> arenas) {
		toShuffle = new LinkedList<String>(arenas);
		Collections.shuffle(toShuffle);
		for (int i = 0; i < games.length; i++) {
			games[i] = new Game(getNextArenaPath());
		}
	}
	
	private String getNextArenaPath() {
		String ret = toShuffle.pollFirst();
		toShuffle.addLast(ret);
		return ret;
	}

	@Override
	public boolean isOver() {
		return false;
	}

	@Override
	public Game getNextGame() {
		Game next = games[indexNext];
		games[indexNext] = new Game(getNextArenaPath());
		indexNext = (indexNext+1) % bufferedGame;
		return next;
	}

	@Override
	public boolean isNextReady() {
		return games[indexNext].isReady();
	}
}
