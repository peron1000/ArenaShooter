package arenashooter.game.gameParam;

public class GameParam {
	private GameMode mode = GameMode.Death_Match;
	private int nbRound = 1;
	private boolean hasChanged = false;
	
	public GameMode getMode() {
		hasChanged = false;
		return mode;
	}
	public void setMode(GameMode mode) {
		hasChanged = true;
		this.mode = mode;
	}
	public int getNbRound() {
		hasChanged = false;
		return nbRound;
	}
	public void setNbRound(int nbRound) {
		hasChanged = true;
		this.nbRound = nbRound;
	}
	public boolean isChanged() {
		return hasChanged;
	}
	
}
