package arenashooter.engine.animation;

import java.util.HashMap;

import arenashooter.engine.animation.tracks.AnimTrackTexture;
import arenashooter.engine.animation.tracks.AnimTrackVec2f;
import arenashooter.engine.animation.tracks.AnimTrackVec3f;
import arenashooter.engine.audio.SoundSourceSingle;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Camera;
import arenashooter.entities.Map;
import arenashooter.entities.Music;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Main;

public class Test extends Map{
	double time = 0;

	Sprite cat = new Sprite(new Vec2f(-360, 300));
	Sprite fox = new Sprite(new Vec2f(360, 300));
	TextSpatial pressStart;
	
	Sprite bg = new Sprite(new Vec2f(0, -950), "data/sprites/intro/bg.png");
	
	Sprite crowd_01, crowd_02, crowd_03, crowd_04;
	AnimTrackTexture crowdA;
	
	AnimTrackVec2f catA, foxA, foxAS;
	AnimTrackTexture catAT, foxAT;
	AnimTrackVec3f camA;
	
	SoundSourceSingle sndPunch;
	boolean punched = false;
	
	public Test() {
		Sky sky = new Sky(new Vec3f(.9, .9, 1), new Vec3f(.8, .8, 1));
		sky.attachToParent(this, "sky");
		
		//Music
		Music bgm = new Music("data/sprites/intro/sf2_title_cps-1.ogg", false);
		bgm.attachToParent(this, "bgm");
		bgm.setVolume(.2f);
		bgm.play();
		
		//Sound
		sndPunch = new SoundSourceSingle("data/sound/slap.ogg", 1, 1, false, false);
		sndPunch.setVolume(.5f);
		
		//PRESS START text
		Text txt = new Text(Main.font, TextAlignH.CENTER, "PRESS ENTER.");
		pressStart = new TextSpatial(new Vec3f(0, 325, 10), new Vec3f(500), txt);
		pressStart.attachToParent(this, "text");
		pressStart.color = new Vec4f(.925, .235, .110, 1);
		
		//Camera
		Window.camera = new Camera(new Vec3f(0, 0, 550));
		Window.camera.attachToParent(this, "camera");
		Window.camera.interpolate = false;
		
		HashMap<Float, Vec3f> vec3Map = new HashMap<>();
		vec3Map.put(5.2f, new Vec3f(0, 0, 550));
		vec3Map.put(9f, new Vec3f(0, -1900, 550));
		camA = new AnimTrackVec3f(vec3Map);
		
		//BG
		bg.size = new Vec2f(3000);
		bg.tex.setFilter(false);
		bg.attachToParent(this, "bg");
		
		//Crowd
		crowd_01 = new Sprite(new Vec2f(-650, 425));
		crowd_02 = new Sprite(new Vec2f(-310, 425));
		crowd_03 = new Sprite(new Vec2f( 310, 425));
		crowd_04 = new Sprite(new Vec2f( 650, 425));
		crowd_03.flipX = true;
		crowd_04.flipX = true;
		crowd_01.size = new Vec2f(800, 400);
		crowd_02.size = crowd_01.size;
		crowd_03.size = crowd_01.size;
		crowd_04.size = crowd_01.size;
		crowd_01.attachToParent(this, "crowd_01");
		crowd_02.attachToParent(this, "crowd_02");
		crowd_03.attachToParent(this, "crowd_03");
		crowd_04.attachToParent(this, "crowd_04");
		crowd_01.useTransparency = true;
		crowd_02.useTransparency = true;
		crowd_03.useTransparency = true;
		crowd_04.useTransparency = true;
		cat.useTransparency = true;
		fox.useTransparency = true;
		
		HashMap<Float, Texture> texMap = new HashMap<>();
		Texture t = Texture.loadTexture("data/sprites/intro/crowd_01_01.png");
		t.setFilter(false);
		Texture t2 = Texture.loadTexture("data/sprites/intro/crowd_01_02.png");
		t2.setFilter(false);
		boolean b=false;
		float time=0;
		while(time<8) {
			if(b)
				texMap.put(time, t);
			else
				texMap.put(time, t2);
			b = !b;
			time+=.50f;
		}
		crowdA = new AnimTrackTexture(texMap);
		
		//Cat
		HashMap<Float, Vec2f> vecMap = new HashMap<>();
		vecMap.put(4f, new Vec2f(0));
		vecMap.put(6f, new Vec2f(-400, 400));
		catA = new AnimTrackVec2f(vecMap);
		
		texMap = new HashMap<>();
		t = Texture.loadTexture("data/sprites/intro/cat_01.png");
		t.setFilter(false);
		t2 = Texture.loadTexture("data/sprites/intro/cat_02.png");
		t2.setFilter(false);
		b=false;
		time=0;
		while(time<4) {
			if(b)
				texMap.put(time, t);
			else
				texMap.put(time, t2);
			b = !b;
			time+=.50f;
		}
		texMap.put(0f, t);
		t = Texture.loadTexture("data/sprites/intro/cat_03.png");
		t.setFilter(false);
		texMap.put(4f, t);
		catAT = new AnimTrackTexture(texMap);
		
		cat.size = new Vec2f(600);
		cat.attachToParent(this, "cat");
		
		//Fox
		texMap = new HashMap<>();
		t = Texture.loadTexture("data/sprites/intro/fox_01.png");
		t.setFilter(false);
		t2 = Texture.loadTexture("data/sprites/intro/fox_02.png");
		t2.setFilter(false);
		b=false;
		time=0;
		while(time<3.8f) {
			if(b)
				texMap.put(time, t);
			else
				texMap.put(time, t2);
			b = !b;
			time+=.25f;
		}
		t = Texture.loadTexture("data/sprites/intro/fox_03.png");
		t.setFilter(false);
		texMap.put(3.8f, t);
		foxAT = new AnimTrackTexture(texMap);
		
		fox.size = new Vec2f(600);
		fox.attachToParent(this, "fox");
		
		vecMap = new HashMap<>();
		vecMap.put(3.8f, new Vec2f(0));
		vecMap.put(4f, new Vec2f(-75, -25));
		foxA = new AnimTrackVec2f(vecMap);
		
		vecMap = new HashMap<>();
		vecMap.put(3.79999f, new Vec2f(600));
		vecMap.put(3.8f, new Vec2f(1200, 600));
		foxAS = new AnimTrackVec2f(vecMap);
	}
	
	@Override
	public void step(double d) {
		time += d;
		cat.tex = catAT.valueAt((float)time);
		cat.localPosition.set(catA.valueAt((float)time));
		fox.tex = foxAT.valueAt((float)time);
		fox.size = foxAS.valueAt((float)time);
		fox.localPosition.set(foxA.valueAt((float)time));
		
		crowd_01.tex = crowdA.valueAt((float)time);
		crowd_02.tex = crowdA.valueAt((float)time);
		crowd_03.tex = crowdA.valueAt((float)time);
		crowd_04.tex = crowdA.valueAt((float)time);
		
		Window.camera.position = camA.valueAt((float)time);
		pressStart.position.y = 325+Window.camera.position.y;
		
		if(!punched && time >= 4f) {
			sndPunch.play();
			punched = true;
		}
		
		super.step(d);
	}
}
