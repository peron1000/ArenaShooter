package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Collider;
import arenashooter.entities.spatials.SoundEffect;

public class Melee extends Usable {
	private Timer fireRate = new Timer(0.15);
	Collider collider;
	protected float damage = 10f;
	private boolean val = false;

	public Melee(Vec2f position, String itemSprite, float damage, double fireRate, String soundpickup) {
		super(position, itemSprite, damage);
		this.fireRate = new Timer(fireRate);
		SoundEffect soundPickup = new SoundEffect(position, "data/sound/" + soundpickup + ".ogg", 2);
		soundPickup.attachToParent(this, "snd_Pickup");
	}

	@Override
	public void step(double d) {
		getSprite().rotation = rotation;
		super.step(d);
	}

	@Override
	protected void setLocalPositionOfSprite() {
		localPosition = Vec2f.rotate(new Vec2f(20, 0), rotation);
	}
}
