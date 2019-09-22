package arenashooter.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import arenashooter.game.gameStates.Game;

public class GamesListN implements GamesList {
	private int remaining;

	private final int bufferedGame = 4;

	private LinkedList<String> toShuffle;
	private Game[] games = new Game[bufferedGame];
	private int indexNext = 0;

	public GamesListN(Set<String> arenas, int rounds) {
		remaining = rounds;
		
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
		return remaining <= 0;
	}

	@Override
	public boolean isNextReady() {
		return games[indexNext].isReady();
	}

	@Override
	public Game getNextGame() {
		Game next = games[indexNext];
		games[indexNext] = new Game(getNextArenaPath());
		indexNext = (indexNext+1) % bufferedGame;
		return next;
	}

}
