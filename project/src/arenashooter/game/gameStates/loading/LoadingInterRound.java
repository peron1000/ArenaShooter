package arenashooter.game.gameStates.loading;

import arenashooter.entities.Arena;
import arenashooter.game.Main;
import arenashooter.game.gameStates.Game;
import arenashooter.game.gameStates.Loading;

public class LoadingInterRound extends Loading {
	
	private Game game;
	private LoadingGame loading;

	public LoadingInterRound(Game game, LoadingGame loadingGame) {
		super(game);
		this.game = game;
		this.loading = loadingGame;
	}
	
	public boolean isReady() {
		return !Main.preLoadMainSound.isAlive() && loading.hasNum(game.getCurrentRound());
	}
	
	public Arena getArenaForNewRound() {
		if(isReady()) {
			return loading.getArena(game.getCurrentRound()-1);
		} else {
			Main.log.error("Try to play next Round whereas it has not been loaded yet");
			return loading.getArena(loading.arenaLoaded()-1);
		}
	}

	@Override
	public void endLoading() {
		// TODO Auto-generated method stub
		
	}
	
}
