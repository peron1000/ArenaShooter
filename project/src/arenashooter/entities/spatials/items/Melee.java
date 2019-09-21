package arenashooter.entities.spatials.items;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Spatial;

public class Melee extends Usable {
	protected Timer fireRate = null;
	protected float damage = 10f;

	protected AnimMelee animMelee = null;

	protected Timer timerWarmup = null;
	
	//Damage dealing
	private Spatial bladeBot,bladeTop;
	private Map<Spatial, Float> hitEntities = new HashMap<>();
	private Set<Spatial> damagedEntities = new HashSet<>();
	private Vec2f lastBladeBot, lastBladeTop;
	private boolean dealingDamage = false;
	private float bladeRayFraction = 1;
	private double size = 0;
	
	private Melee() {
		super(Texture.default_tex.getPath());
	}
	
	public Melee(Vec2f localPosition, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,Vec2f extent,
			String soundPickup, double cooldown, int uses, String animPath, double warmupDuration, String soundWarmup,
			String attackSound, float damage) {
		super(localPosition, name, weight, pathSprite, handPosL, handPosR, extent, soundPickup, cooldown, uses, animPath,
				warmupDuration, soundWarmup, attackSound);
		this.animMelee = new AnimMelee(new Vec2f(), this);
		this.extent = extent;
		getSprite().attachRot = true;
		
		//TODO: Read these values per-weapon
		bladeBot = new Spatial();
		bladeBot.attachToParent(getSprite(), "blade_bot");
		bladeTop = new Spatial();
		bladeTop.attachToParent(getSprite(), "blade_top");
		bladeTop.localPosition.set(.5, 0);
	}
	

	/**
	 * @return the damage
	 */
	public float getDamage() {
		return damage;
	}

	/**
	 * @return the animMelee
	 */
	public AnimMelee getAnimMelee() {
		return animMelee;
	}

	/**
	 * @return the size
	 */
	public double getSize() {
		return size;
	}

	@Override
	public void detach() {
		animMelee.stopAnim();
		super.detach();
	}
	
	@Override
	public void attackStart(boolean demo) {
		animMelee.attachToParent(this, "anim_attack_01");
		animMelee.stopAnim();
		animMelee.playAnim();
	}

	@Override
	public void attackStop() {
		if( getChild("anim_attack_01") instanceof AnimMelee ) {
			((AnimMelee)getChild("anim_attack_01")).stopAnim();
			getChild("anim_attack_01").detach();
		}
	} 

	@Override
	protected void setLocalPositionOfSprite() {
		if (getParent() instanceof Character) {
			if (((Character) getParent()).lookRight) {
				localPosition = new Vec2f(0.3, 0.1);
			} else {
				localPosition = new Vec2f(-0.3, 0.1);
			}
		}
	}
	
	public void startDamage() {
		dealingDamage = true;
		lastBladeBot = null;
		lastBladeTop = null;
		damagedEntities.clear();
	}
	
	public void stopDamage() {
		dealingDamage = false;
	}
	
	@Override
	public void step(double d) {
		super.step(d);
		
		if(!animMelee.isPlaying())
			stopDamage();
		
		if(dealingDamage) {
			if(lastBladeBot == null || lastBladeTop == null) {
				lastBladeBot = bladeBot.getWorldPos().clone();
				lastBladeTop = bladeTop.getWorldPos().clone();
			} else {
				hitEntities.clear();
				bladeRayFraction = 1;
				
				//Top raycast
				getArena().physic.raycast(lastBladeBot, bladeBot.getWorldPos(), DamageRaycastCallback);
				
				//Bottom raycast
				getArena().physic.raycast(lastBladeTop, bladeTop.getWorldPos(), DamageRaycastCallback);
				
				DamageInfo dmgInfo = new DamageInfo(damage, DamageType.MELEE, Vec2f.fromAngle(Vec2f.direction(lastBladeTop, bladeTop.getWorldPos())), 10, getCharacter());
				
				//Damage all newly detected entities
				for(Entry<Spatial, Float> entry : hitEntities.entrySet()) {
					if(entry.getValue() <= bladeRayFraction && !damagedEntities.contains(entry.getKey())) {
						entry.getKey().takeDamage(dmgInfo);
						damagedEntities.add(entry.getKey());
					}
				}
				
				//Update blade points
				lastBladeBot.set( bladeBot.getWorldPos() );
				lastBladeTop.set( bladeTop.getWorldPos() );
			}
		}
	}

	@Override
	public Melee clone() {
		Melee clone = new Melee(localPosition, this.genName(), weight, pathSprite, handPosL, handPosL, extent, soundPickup, warmup,
				uses, animPath, warmup, animPath, animPath, damage) {
		};
		return clone;
	}
	
	RayCastCallback DamageRaycastCallback = new RayCastCallback() {
		@Override
		public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
			//Ignore sensors
			if(fixture.isSensor()) return -1;
			
			//We hit something that isn't owning character
			if(fixture.getUserData() instanceof Spatial && fixture.getUserData() != getCharacter()) {
				if(hitEntities.containsKey(fixture.getUserData())) {
					if(hitEntities.get(fixture.getUserData()) > fraction)
						hitEntities.put((Spatial) fixture.getUserData(), fraction);
				} else {
					hitEntities.put((Spatial) fixture.getUserData(), fraction);
				}
			}
			
			//Ignore anything the character doesn't collide with
			if((fixture.getFilterData().categoryBits & CollisionFlags.CHARACTER.maskBits) == 0) return -1;

			bladeRayFraction = Math.max(bladeRayFraction, fraction);
			return fraction;
		}
	};
	
	
	/*
	 * JSON
	 */
	
	public static Melee fromJson(JsonObject json) throws Exception {
		Melee e = new Melee();
		useKeys(e, json);
		return e;
	}
}
