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
		camera.position.z = 450;
		camera.attachToParent(map, "camera");
		player = new Character();
		player.attachToParent(map, "Player 1");
		Plateform plat = new Plateform(new Vec2f(500, 20));
		plat.position = new Vec2f(0, 510);
		plat.attachToParent(map, "Platform 1");
		Plateform plat2 = new Plateform(new Vec2f(300, 300));
		plat2.position = new Vec2f(-800, 200);
		plat2.attachToParent(map, "Platform 2");
		Plateform plat3 = new Plateform(new Vec2f(300, 300));
		plat3.position = new Vec2f(800, 200);
		plat3.attachToParent(map, "Platform 3");
		Plateform plat4 = new Plateform(new Vec2f(500, 20));
		plat4.position = new Vec2f(0, -410);
		plat4.attachToParent(map, "Platform 4");
	}
	
	public Map getMap() {
		return map;
	}
	
	public void newGame() {
		game = new Game();
	}
	
	public void update(double d) {
		camera.position.x = Utils.lerpF(camera.position.x, player.position.x, (float)(d*8));
		float targetY = Utils.clampF(camera.position.y, player.position.y-50, player.position.y+50);
		camera.position.y = Utils.lerpF(camera.position.y, targetY, (float)(d*7));
		map.step(d);
	}
	
	public void draw() {
		map.draw();
	}
}
