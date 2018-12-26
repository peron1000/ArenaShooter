package arenashooter.entities;

import java.util.ArrayList;

import arenashooter.engine.math.Vec2f;

public class Map extends Entity {
	
	ArrayList<Character> players;
	
	public Map(int nbPlayer) {
		players = new ArrayList<>(nbPlayer);
		
		players.add(0, new Character(new Vec2f(300, 0)));
		players.add(1, new Character(new Vec2f(-300, 0)));
		
		players.get(0).attachToParent(this, "Player 1");
		players.get(1).attachToParent(this, "Player 2");
		
		creationPlateforme();
	}

	private void creationPlateforme() {
		Plateform plat = new Plateform(new Vec2f(0, 510), new Vec2f(5000, 20));
		plat.attachToParent(this, "Platform 1");
		
		Plateform plat2 = new Plateform(new Vec2f(-800, 220), new Vec2f(300, 300));
		plat2.attachToParent(this, "Platform 2");
		
		Plateform plat3 = new Plateform(new Vec2f(800, 220), new Vec2f(300, 300));
		plat3.attachToParent(this, "Platform 3");
		
		Plateform plat4 = new Plateform(new Vec2f(0, -450), new Vec2f(500, 20));
		plat4.attachToParent(this, "Platform 4");
	}
}
