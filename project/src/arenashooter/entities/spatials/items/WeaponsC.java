package arenashooter.entities.spatials.items;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.game.Game;

public class WeaponsC extends Item {

	private Timer fire = new Timer(0.25);
	Collider coll;

	public WeaponsC(Vec2f position, ItemSprite itemSprite) {
		super(position, itemSprite);
		fire.attachToParent(this, "attack timer");
		tag = "Arme";
		coll = new Collider(position, new Vec2f(40, 40));
	}

	public void fire(boolean lookRight) { // Visée uniquement droite et gauche pour l'instant. TODO :
		if (fire.isOver()) {
			float pX;
			float vX;
			if (lookRight) {
				pX = position.x + 70;
				vX = 500;
			} else {
				pX = position.x - 70;
				vX = -500;
			}
			fire.restart();
			Bullet bul = new Bullet(new Vec2f(pX, position.y), new Vec2f(vX, 0));
			bul.attachToParent(Game.game.map, ("bullet" + bul.genName()));
		}
	}

	public void step(double d) {
		Profiler.startTimer(Profiler.PHYSIC);
		Profiler.endTimer(Profiler.PHYSIC);
		super.step(d);
	}
}
