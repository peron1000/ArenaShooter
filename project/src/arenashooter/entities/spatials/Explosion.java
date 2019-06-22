package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class Explosion extends Spatial {
	private double time = 0;
	private List<Mesh> meshesBits = new ArrayList<>();
	private List<Float> bitsScales = new ArrayList<>();
	
	private Set<Spatial> damaged = new HashSet<>();
	private DamageInfo dmgInfo;
	
	private float radius;
	
	public Explosion(Vec2f position, DamageInfo dmgInfo, float radius) {
		super(position);
		
		this.dmgInfo = dmgInfo.clone();

		this.radius = radius;
		
		Material shockwaveMat = Material.loadMaterial("data/materials/sprite_simple.xml");
		shockwaveMat.setParamTex("baseColor", Texture.loadTexture("data/sprites/shockwave_tr.png").setFilter(false));
		
		shockwaveMat.setParamVec4f("baseColorMod", new Vec4f(1, .857, .145, .9));
		
		Vec4f temp = randomRot();
		temp = Vec4f.lerp(temp, new Vec4f(0, 0, 0, 1), .75);
		Vec4f.normalize(temp);
		
		Mesh shockwave = Mesh.quad(new Vec3f(), new Quat(temp.x, temp.y, temp.z, temp.w), new Vec3f(1), shockwaveMat);
		shockwave.attachRot = false;
		shockwave.getMaterial(0).transparency = true;
		shockwave.attachToParent(this, "shockwave_mesh");
		
		int bitsCount = (int)((Math.random()*5)+5);
		for(int i=0; i<bitsCount; i++) {
			temp = randomRot();
			Vec4f.normalize(temp);
			
			float scale = Utils.lerpF(.5f, 1.1f, Math.random());
			bitsScales.add(scale);
			
			Mesh newBit = new Mesh(new Vec3f(), new Quat(temp.x, temp.y, temp.z, temp.w), new Vec3f(.1f), "data/meshes/explosion/explosion_bit.obj");
			meshesBits.add(newBit);
			newBit.attachRot = false;
			newBit.getMaterial(0).setParamVec4f("baseColor", new Vec4f(2, 1.714, .290, 1));
			newBit.attachToParent(this, "bit_"+i);
		}
	}
	
	private Vec4f randomRot() {
		return new Vec4f( (Math.random()-.5)*2, (Math.random()-.5)*2, (Math.random()-.5)*2, (Math.random()-.5)*2 );
	}
	
	private void affect(Spatial target) {
		double distance = Vec2f.distanceSquared(getWorldPos(), target.getWorldPos());
		Vec2f direction = Vec2f.fromAngle(Vec2f.direction(getWorldPos(), target.getWorldPos()));
		
		float damage = dmgInfo.damage;
		if(distance > 3)
			damage = (float)(damage / (distance - 2));
		
		//TODO: reduce impulse with distance
		DamageInfo dmg = new DamageInfo(damage, dmgInfo.dmgType, direction, dmgInfo.impulse, dmgInfo.instigator);
		target.takeDamage(dmg);
	}
	
	@Override
	public void step(double d) {
		super.step(d);

		float rayLength = (float)(radius*time*8);
		if(rayLength <= radius) {
			touched.clear();
			for( float i = 0; i<Math.PI*2; i+=.1f ) {
				raycast(i, rayLength);
			}
			for(Spatial spatial : touched) {
				if(!damaged.contains(spatial)) {
					damaged.add(spatial);
					affect(spatial);
				}
			}
		}

		if(time <= 0) {
			Particles particles = new Particles(getWorldPos(), "data/particles/explosion.xml");
			particles.attachToParent(getArena(), "particles"+genName());

			Audio.playSound2D("data/sound/explosion_01.ogg", AudioChannel.SFX, 1, Utils.lerpF(.8f, 1.2f, Math.random()), getWorldPos());
			
			Window.getCamera().setCameraShake(4);
		}
		
		if(time >= 1) detach();
		time += d*.5;
		
		double oneMinusTime = 1-time;
		
		if( getChild("shockwave_mesh") instanceof Mesh ) {
			double size = ((Mesh)getChild("shockwave_mesh")).scale.x;
			
			size = Utils.lerpD(size, 35, 1-(oneMinusTime*oneMinusTime));
			((Mesh)getChild("shockwave_mesh")).scale.set(size, size, size);
			
			Vec4f color = ((Mesh)getChild("shockwave_mesh")).getMaterial(0).getParamVec4f("baseColorMod");
			((Mesh)getChild("shockwave_mesh")).getMaterial(0).setParamVec4f("baseColorMod", Vec4f.lerp(color, new Vec4f(.976, .367, .161, 0), time*time));
		}
		
		double oneMinusScaleTime = 1-Utils.clampD(time*3, 0, 1);
		for(int i=0; i<meshesBits.size(); i++) {
			float pos = Utils.lerpF(-25, 0, oneMinusScaleTime);
			meshesBits.get(i).localPosition.set( meshesBits.get(i).getWorldRot().forward().multiply(pos) );
//			float scale = Utils.lerpF(bitsScales.get(i), .1f, oneMinusScaleTime);
			float scale = bitsScales.get(i);
			meshesBits.get(i).scale.set(bitsScales.get(i)*oneMinusScaleTime, bitsScales.get(i)*oneMinusScaleTime, scale);
		}
		
	}
	
	private Vec2f raycastEnd = new Vec2f();
	private void raycast(double angle, float length) {
		Vec2f.fromAngle(angle, raycastEnd);
		raycastEnd.multiply(length);
		raycastEnd.add(getWorldPos());
		
		getArena().physic.getB2World().raycast(PunchRaycastCallback, getWorldPos().toB2Vec(), raycastEnd.toB2Vec());
	}

	private Set<Spatial> touched = new HashSet<>();
	/** Farthest blocking entity hit by the punch */
	RayCastCallback PunchRaycastCallback = new RayCastCallback() {
		@Override
		public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
			//Ignore sensors
			if(fixture.isSensor())
				return -1;
			
			if((fixture.getFilterData().categoryBits & CollisionFlags.EXPLOSION.maskBits) == 0)
				return -1;

			//We hit something that isn't self
			if(fixture.getUserData() instanceof Spatial && fixture.getUserData() != this)
				touched.add((Spatial) fixture.getUserData());

			return fraction;
		}
	};
}
