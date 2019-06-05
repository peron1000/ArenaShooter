package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;

import java.util.HashMap;
import java.util.HashSet;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class Punch extends Spatial {
	private double time = 0;

	private HashSet<Spatial> damaged = new HashSet<>();
	private DamageInfo dmgInfo;

	private boolean superPoing;
	private float radius;
	private float hitWidth;

	public Punch(Vec2f localPosition, DamageInfo dmgInfo, float hitWidth, float radius, boolean superPoing) {
		super(localPosition);

		this.dmgInfo = dmgInfo.clone();

		this.radius = radius;

		this.hitWidth = hitWidth;

		this.superPoing = superPoing;

		Texture tex = Texture.loadTexture("data/sprites/shockwave_tr.png");
		tex.setFilter(false);
	}

	private void affect(Spatial target) {
		Vec2f direction = Vec2f.fromAngle(Vec2f.direction(getWorldPos(), target.getWorldPos()));

		float damage = (float) dmgInfo.damage;

		DamageInfo dmg = new DamageInfo(damage, dmgInfo.dmgType, direction, dmgInfo.instigator);
		target.takeDamage(dmg);
	}

	@Override
	public void step(double d) {
		super.step(d);

		float rayLength = (float) (radius * time);
		float direction = (float) dmgInfo.direction.angle();

		punchHit.clear();
		for (float i = direction - (hitWidth / 2); i < direction + (hitWidth / 2); i += .1f) {
			raycast(i, rayLength);
		}
		for (Spatial spatial : punchHit.keySet()) {
			if (!damaged.contains(spatial)) {
				damaged.add(spatial);
				affect(spatial);
			}
		}

		if (time <= 0) {
			if (!punchHit.isEmpty()) {
				float randomPitch = (float) (1 + (Math.random() - 0.5) * 0.2);
				if (superPoing) {
					Audio.playSound2D("data/sound/SuperPunch.ogg", AudioChannel.SFX, 2f, 1 * randomPitch,
							getWorldPos());
					Window.getCamera().setCameraShake(0.25f);
				} else {
					Audio.playSound2D("data/sound/snd_Punch_Hit2.ogg", AudioChannel.SFX, .7f, 1 * randomPitch,
							getWorldPos());
					Window.getCamera().setCameraShake(0.15f);
				}
			} else
				Window.getCamera().setCameraShake(0.05f);
		}

		if (time >= 1)
			detach();
		time += d * 5;
	}

	private Vec2f raycastEnd = new Vec2f();

	private void raycast(double angle, float length) {
		Vec2f.fromAngle(angle, raycastEnd);
		raycastEnd.multiply(length);
		raycastEnd.add(getWorldPos());

		if(getArena() == null) return;
		getArena().physic.getB2World().raycast(PunchRaycastCallback, getWorldPos().toB2Vec(), raycastEnd.toB2Vec());
	}

	private HashMap<Spatial, Float> punchHit = new HashMap<>();
	/** Farthest blocking entity hit by the punch */
	RayCastCallback PunchRaycastCallback = new RayCastCallback() {
		@Override
		public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
			// Ignore sensors
			if (fixture.isSensor())
				return -1;

			if ((fixture.getFilterData().categoryBits & CollisionFlags.EXPLOSION.maskBits) == 0)
				return -1;

			// We hit something that isn't self or Parent Character
			if (fixture.getUserData() instanceof Spatial && fixture.getUserData() != this
					&& fixture.getUserData() instanceof Spatial && fixture.getUserData() != (Spatial) getParent()) {
				if (punchHit.containsKey(fixture.getUserData())) {
					if (punchHit.get(fixture.getUserData()) > fraction)
						punchHit.put((Spatial) fixture.getUserData(), fraction);
				} else {
					punchHit.put((Spatial) fixture.getUserData(), fraction);
				}
			}
			return fraction;
		}
	};
}
