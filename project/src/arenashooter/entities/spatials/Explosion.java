package arenashooter.entities.spatials;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class Explosion extends Spatial {
	private double time = 0;
	
	public Explosion(Vec2f position) {
		super(position);
		
		Material shockwaveMat = new Material("data/shaders/sprite_simple");
		Texture tex = Texture.loadTexture("data/sprites/shockwave_tr.png");
		tex.setFilter(false);
		shockwaveMat.setParamTex("baseColor", tex);
		
		shockwaveMat.setParamVec4f("baseColorMod", new Vec4f(1, .857, .145, .9));
		
		Vec4f temp = new Vec4f(Math.random(), Math.random(), Math.random(), Math.random()+.01);
		temp = Vec4f.lerp(temp, new Vec4f(0, 0, 0, 1), .75);
		Vec4f.normalize(temp);
		
		Mesh shockwave = Mesh.quad(new Vec3f(), new Quat(temp.x, temp.y, temp.z, temp.w), new Vec3f(1), shockwaveMat);
		shockwave.rotationFromParent = false;
		shockwave.useTransparency = true;
		shockwave.attachToParent(this, "shockwave_mesh");
	}
	
	@Override
	public void step(double d) {
		super.step(d);
		
		if(time <= 0) {
			Particles particles = new Particles(getWorldPos(), "data/particles/explosion.xml");
			particles.attachToParent(getMap(), "particles");

			Audio.playSound2D("data/sound/explosion_01.ogg", 1, Utils.lerpF(.8f, 1.2f, Math.random()), getWorldPos());
			
			Window.getCamera().setCameraShake(3);
		}
		
		if(time >= 1) detach();
		time += d*.5;
		
		if( getChild("shockwave_mesh") instanceof Mesh ) {
			double size = ((Mesh)getChild("shockwave_mesh")).scale.x;
			
			double oneMinusTime = 1-time;
			
			size = Utils.lerpD(size, 35, 1-(oneMinusTime*oneMinusTime));
			((Mesh)getChild("shockwave_mesh")).scale.set(size, size, size);
			
			Vec4f color = ((Mesh)getChild("shockwave_mesh")).getMaterial(0).getParamVec4f("baseColorMod");
			((Mesh)getChild("shockwave_mesh")).getMaterial(0).setParamVec4f("baseColorMod", Vec4f.lerp(color, new Vec4f(.976, .367, .161, 0), time*time));
		}
		
	}

}
