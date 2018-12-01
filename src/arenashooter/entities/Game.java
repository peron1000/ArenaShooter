package arenashooter.entities;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class Game {
	public static Game game = new Game();
	
	private Map map;
	private Character player;
	public Camera camera;
	
	private Game() {
		map = new Map();
		camera = new Camera();
		camera.position.z = 250;
		camera.attachToParent(map, "camera");
		player = new Character();
		player.attachToParent(map, "Player 1");
		Plateform plat = new Plateform(new Vec2f(500, 20));
		plat.position = new Vec2f(0, 510);
		plat.attachToParent(map, "Platform 1");
	}
	
	public Map getMap() {
		return map;
	}
	
	public void newGame() {
		game = new Game();
	}
	
	public void update(double d) {
		camera.position.x = Utils.lerpF(camera.position.x, player.position.x, (float)(d*.8));
		camera.position.y = Utils.lerpF(camera.position.y, player.position.y, (float)(d*.8));
		map.step(d);
	}
	
	public void draw() {
		map.draw();
	}
}
