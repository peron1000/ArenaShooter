package arenashooter.entities;

import java.util.ArrayList;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class Map extends Entity {
	
	ArrayList<Vec2f> spawn;
	
	/**
	 * world bounds (min x, min y, max x, max y)
	 */
	public Vec4f cameraBounds;
	
	public Map(int nbPlayer) {
		spawn = new ArrayList<>(nbPlayer);
		creationPlateforme();
		creationSpawn(nbPlayer);
		
		cameraBounds = new Vec4f(-3000, -1000, 3000, 1000);
	}

	private void creationSpawn(int nbPlayer) {
		for (int i = 0; i < nbPlayer; i++) {
			spawn.add(i, new Vec2f(i*200-300, 0));
		}
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
