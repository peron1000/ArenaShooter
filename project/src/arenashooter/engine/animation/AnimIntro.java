package arenashooter.engine.animation;

import java.util.HashMap;

import arenashooter.engine.animation.tracks.AnimTrackDouble;
import arenashooter.engine.animation.tracks.AnimTrackTexture;
import arenashooter.engine.animation.tracks.AnimTrackVec2f;
import arenashooter.engine.animation.tracks.AnimTrackVec3f;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Arena;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Main;

public class AnimIntro extends Arena{
	double time = 0;
	
	private Sky sky;
	private Vec3f skyTop, skyBot;

	private Sprite cat = new Sprite(new Vec2f());
	private Sprite fox = new Sprite(new Vec2f());
	
	private TextSpatial pressStart;
	private Vec4f textColorA = new Vec4f(.925, .635, .110, 1), textColorB = new Vec4f(.859, .125, .714, 1);
	
	private TextSpatial versionText;
	private AnimTrackDouble versionThickness;
	
	private AnimTrackDouble sceneOpacityA;
	
	private Sprite bg = new Sprite(new Vec2f(0, -950), "data/sprites/intro/bg.png");
	private Sprite logo;
	private AnimTrackTexture logoTex;
	private AnimTrackDouble logoRotA;
	private AnimTrackVec2f logoPosA, logoSizeA;
	
	private Sprite crowd_01, crowd_02, crowd_03, crowd_04;
	private AnimTrackTexture crowdA;
	
	private AnimTrackVec2f catA, foxA, foxAS;
	private AnimTrackTexture catAT, foxAT;
	private AnimTrackVec3f camA;
	
	private boolean punched = false;
	
	public AnimIntro() {
		skyBot = new Vec3f(.9, .9, 1);
		skyTop = new Vec3f(.8, .8, 1);
		sky = new Sky(skyBot, skyTop);
		sky.attachToParent(this, "sky");
		
		//Music
		
		//Camera
		Camera cam = new Camera(new Vec3f(0, 0, 850));
		cam.setFOV(90);
		cam.interpolate = false;
		cam.attachToParent(this, "camera");
		Window.setCamera(cam);
		
		HashMap<Double, Vec3f> vec3Map = new HashMap<>();
		vec3Map.put(5.2, new Vec3f(0, 0, 550));
		vec3Map.put(9.0, new Vec3f(0, -1900, 550));
		camA = new AnimTrackVec3f(vec3Map, AnimInterpolation.LINEAR);

		//PRESS START text
		Text txt = new Text(Main.font, TextAlignH.CENTER, "PRESS ENTER.");
		pressStart = new TextSpatial(new Vec3f(0, 325, 100), new Vec3f(500), txt);
		pressStart.attachToParent(this, "textPressStart");
		
		//Version text
		txt = new Text(Main.font, TextAlignH.LEFT, "v"+Main.version);
		versionText = new TextSpatial(new Vec3f(24, 7.5, -30), new Vec3f(20), txt);
		versionText.attachToParent(cam, "textVersion");
		versionText.setColor(new Vec4f(.35, .35, .35, 1));
		HashMap<Double, Double> textThickness = new HashMap<>();
		textThickness.put(0d, 0d);
		textThickness.put(11d, 0d);
		textThickness.put(12d, .25);
		versionThickness = new AnimTrackDouble(textThickness);
		
		//BG
		bg.size = new Vec2f(3000);
		bg.getTexture().setFilter(false);
		bg.attachToParent(this, "bg");
		
		//Fade out
		double flashIntensity = 0.5;
		HashMap<Double, Double> opacityMap = new HashMap<>();
		opacityMap.put(9d, 1d);
		opacityMap.put(10d, 0d);
		opacityMap.put(10.95, 0d);
		opacityMap.put(11.00, flashIntensity);
		opacityMap.put(11.05, 0d);
		opacityMap.put(11.10, flashIntensity);
		opacityMap.put(11.15, 0d);
		opacityMap.put(11.20, flashIntensity);
		opacityMap.put(11.25, 0d);
		opacityMap.put(11.30, flashIntensity);
		opacityMap.put(11.40, 0d);
		sceneOpacityA = new AnimTrackDouble(opacityMap, AnimInterpolation.LINEAR);
		
		//Logo
		logo = new Sprite(new Vec2f());
		logo.getTexture().setFilter(false);
		logo.attachToParent(this, "logo");
		logo.zIndex = 1;
		
		HashMap<Double, Texture> texMap = new HashMap<>();
		Texture t = Texture.loadTexture("data/sprites/intro/logo_no_blood.png");
		t.setFilter(false);
		Texture t2 = Texture.loadTexture("data/sprites/intro/logo.png");
		t2.setFilter(false);
		texMap.put(0d, t);
		texMap.put(11d, t2);
		logoTex = new AnimTrackTexture(texMap);
		
		HashMap<Double, Double> rotMap = new HashMap<>();
		rotMap.put(10d, 0d);
		rotMap.put(11d, -8*Math.PI);
		logoRotA = new AnimTrackDouble(rotMap, AnimInterpolation.LINEAR);
		
		HashMap<Double, Vec2f> vecMap = new HashMap<>();
		vecMap.put(10d, new Vec2f(655));
		vecMap.put(11d, new Vec2f(1200));
		logoSizeA = new AnimTrackVec2f(vecMap, AnimInterpolation.LINEAR);
		
		vecMap = new HashMap<>();
		vecMap.put(10d, new Vec2f(-50, -2018));
		vecMap.put(11d, new Vec2f(0, -2000));
		logoPosA = new AnimTrackVec2f(vecMap, AnimInterpolation.LINEAR);
		
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
		crowd_01.zIndex = 1;
		crowd_02.zIndex = 1;
		crowd_03.zIndex = 1;
		crowd_04.zIndex = 1;
		cat.zIndex = 2;
		fox.zIndex = 2;
		
		texMap = new HashMap<>();
		t = Texture.loadTexture("data/sprites/intro/crowd_01_01.png");
		t.setFilter(false);
		t2 = Texture.loadTexture("data/sprites/intro/crowd_01_02.png");
		t2.setFilter(false);
		boolean b=false;
		double time=0;
		while(time<8) {
			if(b)
				texMap.put(time, t);
			else
				texMap.put(time, t2);
			b = !b;
			time+=.50;
		}
		crowdA = new AnimTrackTexture(texMap);
		
		//Cat
		vecMap = new HashMap<>();
		vecMap.put(4d, new Vec2f(-360, 300));
		vecMap.put(6d, new Vec2f(-760, 700));
		catA = new AnimTrackVec2f(vecMap, AnimInterpolation.LINEAR);
		
		texMap = new HashMap<>();
		t = Texture.loadTexture("data/sprites/intro/cat_01.png");
		t.setFilter(false);
		t2 = Texture.loadTexture("data/sprites/intro/cat_02.png");
		t2.setFilter(false);
		b = false;
		time = 0;
		while(time<4) {
			if(b)
				texMap.put(time, t);
			else
				texMap.put(time, t2);
			b = !b;
			time+=.50;
		}
		texMap.put(0d, t);
		t = Texture.loadTexture("data/sprites/intro/cat_03.png");
		t.setFilter(false);
		texMap.put(4d, t);
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
		texMap.put(3.8, t);
		foxAT = new AnimTrackTexture(texMap);
		
		fox.size = new Vec2f(600);
		fox.attachToParent(this, "fox");
		
		vecMap = new HashMap<>();
		vecMap.put(3.8, new Vec2f(360, 300));
		vecMap.put(4.0, new Vec2f(260, 275));
		foxA = new AnimTrackVec2f(vecMap, AnimInterpolation.LINEAR);
		
		vecMap = new HashMap<>();
		vecMap.put(3.79999, new Vec2f(600));
		vecMap.put(3.8, new Vec2f(1200, 600));
		foxAS = new AnimTrackVec2f(vecMap, AnimInterpolation.LINEAR);
	}
	
	@Override
	public void step(double d) {
		time += d;
		cat.setTexture(catAT.valueAt(time));
		cat.localPosition.set(catA.valueAt(time));
		fox.setTexture(foxAT.valueAt(time));
		fox.size = foxAS.valueAt(time);
		fox.localPosition.set(foxA.valueAt(time));
		
		crowd_01.setTexture(crowdA.valueAt(time));
		crowd_02.setTexture(crowdA.valueAt(time));
		crowd_03.setTexture(crowdA.valueAt(time));
		crowd_04.setTexture(crowdA.valueAt(time));
		
		logo.setTexture(logoTex.valueAt(time));
		logo.localPosition.set(logoPosA.valueAt(time));
		logo.localRotation = logoRotA.valueAt(time);
		logo.size.set(logoSizeA.valueAt(time));
		
		Window.getCamera().localPosition = camA.valueAt(time);
		pressStart.localPosition.y = 325+Window.getCamera().getWorldPos().y;
		
		//Fade
		float opacity = sceneOpacityA.valueAt(time).floatValue();
		bg.material.setParamVec4f("baseColorMod", new Vec4f(opacity, opacity, opacity, 1.0));
		sky.setColors(Vec3f.lerp(new Vec3f(), skyBot, opacity), Vec3f.lerp(new Vec3f(), skyTop, opacity));
		
		//Text
		pressStart.setColor( Vec4f.lerp(textColorA, textColorB, (1+Math.sin(time*10))/2d) );
		pressStart.setThickness( Utils.lerpF(.2f, .42f, (1+Math.sin(time*8))/2d) );
		
		versionText.setThickness( versionThickness.valueAt(time).floatValue() );
		
		if(!punched && time >= 4f) {
			Audio.playSound("data/sound/slap.ogg", AudioChannel.UI, .48f, 1);
			Window.getCamera().setCameraShake(3f);
			punched = true;
		}
		
		super.step(d);
	}
}
