package arenashooter.entities.spatials.items;

import java.util.Vector;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;
import arenashooter.game.GameMaster;

public class Melee extends Weapon {
	private Timer fireRate = new Timer(0.15);
	Collider collider;
	private float damage = 10f;
	private boolean val = false;

	public Melee(Vec2f position, String itemSprite, double size, double fireRate, String soundpickup) {
		super(position, itemSprite, size);
		this.fireRate = new Timer(fireRate);
		SoundEffect soundPickup = new SoundEffect(position, "data/sound/" + soundpickup + ".ogg", 16);
		soundPickup.attachToParent(this, "snd_Pickup");
	}

	@Override
	public void attackStart() {

	}

	@Override
	public void attackStop() {

	}

	@Override
	public void step(double d) {
		if (fireRate.isOver()) {
			fireRate.restart();
			position = Vec2f.rotate90(position);
		}
		super.step(d);
	}
}
