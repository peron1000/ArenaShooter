package arenashooter.entities;

public class Game {
	public static Game game = new Game();
	
	private Game() {
		// TODO Auto-generated constructor stub
	}
	
	public void newGame() {
		game = new Game();
	}
}
