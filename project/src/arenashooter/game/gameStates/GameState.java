package arenashooter.game.gameStates;

public abstract class GameState {
	protected GameState() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract void update(double delta);
	public abstract void draw();
}
