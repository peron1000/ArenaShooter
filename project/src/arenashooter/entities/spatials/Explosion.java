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
import java.util.ArrayList;

public class Explosion extends Spatial {
	private double time = 0;
	private ArrayList<Mesh> meshesBits = new ArrayList<>();
	private ArrayList<Float> bitsScales = new ArrayList<>();
	
	public Explosion(Vec2f position) {
		super(position);
		
		Material shockwaveMat = new Material("data/shaders/sprite_simple");
		Texture tex = Texture.loadTexture("data/sprites/shockwave_tr.png");
		tex.setFilter(false);
		shockwaveMat.setParamTex("baseColor", tex);
		
		shockwaveMat.setParamVec4f("baseColorMod", new Vec4f(1, .857, .145, .9));
		
		Vec4f temp = randomRot();
		temp = Vec4f.lerp(temp, new Vec4f(0, 0, 0, 1), .75);
		Vec4f.normalize(temp);
		
		Mesh shockwave = Mesh.quad(new Vec3f(), new Quat(temp.x, temp.y, temp.z, temp.w), new Vec3f(1), shockwaveMat);
		shockwave.rotationFromParent = false;
		shockwave.useTransparency = true;
		shockwave.attachToParent(this, "shockwave_mesh");
		
		int bitsCount = (int)((Math.random()*5)+5);
		for(int i=0; i<bitsCount; i++) {
			temp = randomRot();
			Vec4f.normalize(temp);
			
			float scale = Utils.lerpF(.5f, 1.1f, Math.random());
			bitsScales.add(scale);
			
			Mesh newBit = new Mesh(new Vec3f(), new Quat(temp.x, temp.y, temp.z, temp.w), new Vec3f(.1f), "data/meshes/explosion/explosion_bit.obj");
			meshesBits.add(newBit);
			newBit.rotationFromParent = false;
			newBit.getMaterial(0).setParamVec4f("baseColor", new Vec4f(2, 1.714, .290, 1));
			newBit.attachToParent(this, "bit_"+i);
		}
	}
	
	private Vec4f randomRot() {
		return new Vec4f( (Math.random()-.5)*2, (Math.random()-.5)*2, (Math.random()-.5)*2, (Math.random()-.5)*2 );
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

}
