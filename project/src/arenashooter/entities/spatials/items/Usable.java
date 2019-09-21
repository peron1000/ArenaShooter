package arenashooter.entities.spatials.items;

import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;

public class Usable extends Item {

	/** Time in between attacks */
	protected double fireRate = .1;
	protected Timer timerCooldown = new Timer(fireRate);
	protected int uses = 10;
	protected String animPath = "";
	protected double warmup = .1;
	protected String soundWarmup = "";
	protected String soundFire = "";
	protected boolean demo = false;
	
	private Usable() {
		super(Texture.default_tex.getPath());
	}

	public Usable(Vec2f localPosition, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			Vec2f extent, String soundPickup, double fireRate, int uses, String animPath, double warmup,
			String soundWarmup, String soundFire) {
		super(localPosition, name, weight, pathSprite, handPosL, handPosR, extent, soundPickup);

		// Cooldown
		timerCooldown = new Timer(fireRate);
		attachCooldownTimer();

		this.setFireRate(fireRate);
		this.setUses(uses);
		this.animPath = animPath;
		this.warmup = warmup;
		this.soundWarmup = soundWarmup;
		this.setSoundFire(soundFire);
	}

	/**
	 * Constructor for the Editor to avoid a new Item creation for each change state
	 * 
	 * @author Nathan
	 * @param sprite
	 */
	public Usable(String sprite) {
		super(sprite);
		attachCooldownTimer();
	}

	private void attachCooldownTimer() {
		timerCooldown.attachToParent(this, timerCooldown.genName());
		this.timerCooldown.setIncreasing(true);
		this.timerCooldown.setProcessing(true);
	}

	/**
	 * @return the fireRate
	 */
	public double getFireRate() {
		return fireRate;
	}

	/**
	 * @return the uses
	 */
	public int getUses() {
		return uses;
	}

	/**
	 * @param uses the uses to set
	 */
	public void setUses(int uses) {
		this.uses = uses;
	}

	/**
	 * @return the animPath
	 */
	public String getAnimPath() {
		return animPath;
	}

	/**
	 * @return the warmup
	 */
	public double getWarmup() {
		return warmup;
	}

	/**
	 * @return the soundWarmup
	 */
	public String getSoundWarmup() {
		return soundWarmup;
	}

	/**
	 * @return the soundFire
	 */
	public String getSoundFire() {
		return soundFire;
	}

	/**
	 * @param fireRate the fireRate to set
	 */
	public void setFireRate(double fireRate) {
		this.fireRate = fireRate;
		timerCooldown = new Timer(fireRate);
		attachCooldownTimer();
	}

	/**
	 * @param soundFire the soundFire to set
	 */
	public void setSoundFire(String soundFire) {
		this.soundFire = soundFire;
	}

	public void attackStart(boolean demo) {
		this.demo = demo;
		timerCooldown.setIncreasing(true);
		timerCooldown.setProcessing(true);
	}

	public void attackStop() {
		timerCooldown.setIncreasing(false);
	}

	public void step(double d) {
		Vec2f targetOffSet = Vec2f.rotate(new Vec2f(0, 0), getWorldRot());
		localPosition.x = (float) Utils.lerpD((double) localPosition.x, targetOffSet.x, Math.min(1, d * 55));
		localPosition.y = (float) Utils.lerpD((double) localPosition.y, targetOffSet.y, Math.min(1, d * 55));
		if (isEquipped()) {
			localRotation = Utils.lerpAngle(localRotation, ((Character) getParent()).aimInput, Math.min(1, d * 17));
//			getSprite().localRotation = getWorldRot();
		}
		super.step(d);
	}

	@Override
	public Usable clone() {
		Usable clone = new Usable(localPosition, this.genName(), weight, pathSprite, handPosL, handPosR, extent,
				soundPickup, fireRate, uses, animPath, fireRate, soundWarmup, soundFire) {
		};
		return clone;
	}
	
	
	/*
	 * JSON
	 */
	
	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();

		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return soundWarmup;
			}
			@Override
			public String getKey() {
				return "sound warmup";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				soundWarmup = json.getString(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return soundFire;
			}
			@Override
			public String getKey() {
				return "sound fire";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				soundFire = json.getString(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return fireRate;
			}
			@Override
			public String getKey() {
				return "fireRate";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				fireRate = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return warmup;
			}
			@Override
			public String getKey() {
				return "warmup";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				warmup = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return uses;
			}
			@Override
			public String getKey() {
				return "uses";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				uses = json.getInteger(this);
			}
		});
		
		return set;
	}
	
	public static Usable fromJson(JsonObject json) throws Exception {
		Usable e = new Usable();
		useKeys(e, json);
		return e;
	}
}
