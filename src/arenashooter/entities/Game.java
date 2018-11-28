package arenashooter.entities;

public class Game {
	public static Game game = new Game();
	
	private Map map;
	private Character player;
	
	private Game() {
		map = new Map();
		player = new Character();
		player.attachToParent(map, "Player 1");
	}
	
	public Map getMap() {
		return map;
	}
	
	public void newGame() {
		game = new Game();
	}
	
	public void update(double d) {
		map.step(d);
	}
	
	public void draw() {
		map.draw();
	}
}
