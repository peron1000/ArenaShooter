package arenashooter.engine.animation;

import java.util.HashMap;

import arenashooter.engine.animation.tracks.AnimTrackDouble;
import arenashooter.engine.animation.tracks.AnimTrackTexture;
import arenashooter.engine.animation.tracks.AnimTrackVec3f;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Arena;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Main;

public class AnimIntro extends Arena{
	double time = 0;
	
	private Sky sky;
	private Vec3f skyTop, skyBot;
	
	private Mesh bigMesh;

	private Mesh cat = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(), new Material("data/shaders/sprite_simple"));
	private Mesh fox = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(), new Material("data/shaders/sprite_simple"));
	
	private TextSpatial pressStart;
	private Vec4f textColorA = new Vec4f(.925, .635, .110, 1), textColorB = new Vec4f(.859, .125, .714, 1);
	
	private TextSpatial versionText;
	private AnimTrackDouble versionThickness;
	
	private AnimTrackDouble sceneOpacityA, logoOpacity;
	
	private Mesh logo;
	private AnimTrackTexture logoTex;
	private AnimTrackDouble logoRotA;
	private AnimTrackVec3f logoPosA, logoSizeA;
	
	private Mesh crowd_01, crowd_02, crowd_03, crowd_04;
	private AnimTrackTexture crowdA;
	
	private AnimTrackVec3f catA, foxA, foxAS;
	private AnimTrackTexture catAT, foxAT;
	private AnimTrackVec3f camA;
	
	private boolean punched = false;
	
	public AnimIntro() {
		skyBot = new Vec3f(.85, .85, 1);
		skyTop = new Vec3f(.65, .65, 1);
		sky = new Sky(skyBot, skyTop);
		sky.attachToParent(this, "sky");
		
		//Music
		
		//Camera
		Camera cam = new Camera(new Vec3f(0, 0, 50));
		cam.setFOV(55);
		cam.interpolate = false;
		cam.attachToParent(this, "camera");
		Window.setCamera(cam);
		Window.postProcess.fadeToBlack = 1;
		
		HashMap<Double, Vec3f> vec3Map = new HashMap<>();
		vec3Map.put(0.1, new Vec3f(0, -4, 50));
		vec3Map.put(4.0, new Vec3f(0, -1.75, 21));
		vec3Map.put(5.2, new Vec3f(0, -1.75, 21));
		vec3Map.put(9.0, new Vec3f(0, -7.3, 18));
		vec3Map.put(10.0, new Vec3f(0, -7.3, 18));
		vec3Map.put(11.0, new Vec3f(0, -7.4, 9.9));
		camA = new AnimTrackVec3f(vec3Map, AnimInterpolation.LINEAR);

		//PRESS START text
		Text txt = new Text(Main.font, TextAlignH.CENTER, "PRESS ENTER.");
		pressStart = new TextSpatial(new Vec3f(0, .75, -2), new Vec3f(1.5f), txt);
		pressStart.attachToParent(cam, "textPressStart");
		
		//BG
		bigMesh = new Mesh(new Vec3f(), "data/meshes/intro/intro.obj");
		bigMesh.attachToParent(this, "bigMesh");
		
		//Fade out
		double flashIntensity = 0.5;
		HashMap<Double, Double> opacityMap = new HashMap<>();
		opacityMap.put(0d, 1d);
		opacityMap.put(1.2d, 0d);
//		opacityMap.put(9d, 1d);
//		opacityMap.put(10d, 0d);
//		opacityMap.put(10.95, 0d);
//		opacityMap.put(11.00, flashIntensity);
//		opacityMap.put(11.05, 0d);
//		opacityMap.put(11.10, flashIntensity);
//		opacityMap.put(11.15, 0d);
//		opacityMap.put(11.20, flashIntensity);
//		opacityMap.put(11.25, 0d);
//		opacityMap.put(11.30, flashIntensity);
//		opacityMap.put(11.40, 0d);
		sceneOpacityA = new AnimTrackDouble(opacityMap, AnimInterpolation.LINEAR);
		
		//Logo
		logo = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(), new Material("data/shaders/sprite_simple"));
		logo.useTransparency = true;
		logo.attachToParent(this, "logo");
		
		HashMap<Double, Texture> texMap = new HashMap<>();
		Texture t = Texture.loadTexture("data/sprites/intro/logo_no_blood.png");
		t.setFilter(false);
		Texture t2 = Texture.loadTexture("data/logo.png");
		t2.setFilter(false);
		texMap.put(0d, t);
		texMap.put(11d, t2);
		logoTex = new AnimTrackTexture(texMap);
		
		opacityMap = new HashMap<>();
		opacityMap.put(9d, 0d);
		opacityMap.put(10d, 1d);
		logoOpacity = new AnimTrackDouble(opacityMap, AnimInterpolation.LINEAR);
		
		HashMap<Double, Double> rotMap = new HashMap<>();
		rotMap.put(10d, 0d);
		rotMap.put(11d, -8*Math.PI);
		logoRotA = new AnimTrackDouble(rotMap, AnimInterpolation.LINEAR);

		vec3Map = new HashMap<>();
		vec3Map.put(10d, new Vec3f(2f));
		vec3Map.put(11d, new Vec3f(2.3f));
		logoSizeA = new AnimTrackVec3f(vec3Map, AnimInterpolation.LINEAR);

		vec3Map = new HashMap<>();
		vec3Map.put(10d, new Vec3f(-.05, -7.45, 9));
		vec3Map.put(11d, new Vec3f(-.07, -7.5, 8));
		logoPosA = new AnimTrackVec3f(vec3Map, AnimInterpolation.LINEAR);
		
		//Version text
		txt = new Text(Main.font, TextAlignH.LEFT, "v"+Main.version);
		versionText = new TextSpatial(new Vec3f(.65, .61, 0), new Vec3f(1f), txt);
		versionText.attachToParent(logo, "textVersion");
		versionText.setColor(new Vec4f(.35, .35, .35, 1));
		HashMap<Double, Double> textThickness = new HashMap<>();
		textThickness.put(0d, 0d);
		textThickness.put(11d, 0d);
		textThickness.put(12d, .25);
		versionThickness = new AnimTrackDouble(textThickness);
		
		//Crowd
		crowd_01 = Mesh.quad(new Vec3f(-5.3, -1, 15), new Quat(), new Vec3f(5, 2.5, 1), new Material("data/shaders/sprite_simple"));
		crowd_02 = Mesh.quad(new Vec3f(-2, -1, 12), new Quat(), new Vec3f(5, 2.5, 1), new Material("data/shaders/sprite_simple"));
		crowd_03 = Mesh.quad(new Vec3f(2, -1, 11), new Quat(), new Vec3f(-5, 2.5, 1), new Material("data/shaders/sprite_simple"));
		crowd_04 = Mesh.quad(new Vec3f(5.5, -1, 13), new Quat(), new Vec3f(-5, 2.5, 1), new Material("data/shaders/sprite_simple"));
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
		vec3Map = new HashMap<>();
		vec3Map.put(4d, new Vec3f(-1, -1, 16.01));
		vec3Map.put(6d, new Vec3f(-1.2, -.5, 16.01));
		catA = new AnimTrackVec3f(vec3Map, AnimInterpolation.LINEAR);
		
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
		
		cat.scale = new Vec3f(1.5f);
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
		
		fox.scale = new Vec3f(1.5f);
		fox.attachToParent(this, "fox");
		
		vec3Map = new HashMap<>();
		vec3Map.put(3.8, new Vec3f(1, -1, 15.99));
		vec3Map.put(4.0, new Vec3f(.4, -1.2, 15.99));
		foxA = new AnimTrackVec3f(vec3Map, AnimInterpolation.LINEAR);
		
		vec3Map = new HashMap<>();
		vec3Map.put(3.79999, new Vec3f(1.5f));
		vec3Map.put(3.8, new Vec3f(3, 1.5f, 1));
		foxAS = new AnimTrackVec3f(vec3Map, AnimInterpolation.LINEAR);
	}
	
	@Override
	public void step(double d) {
		time += d;
		cat.getMaterial(0).setParamTex("baseColor", catAT.valueAt(time));
		cat.localPosition.set(catA.valueAt(time));
		fox.getMaterial(0).setParamTex("baseColor", foxAT.valueAt(time));
		fox.scale.set(foxAS.valueAt(time));
		fox.localPosition.set(foxA.valueAt(time));
		
		crowd_01.getMaterial(0).setParamTex("baseColor", crowdA.valueAt(time));
		crowd_02.getMaterial(0).setParamTex("baseColor", crowdA.valueAt(time));
		crowd_03.getMaterial(0).setParamTex("baseColor", crowdA.valueAt(time));
		crowd_04.getMaterial(0).setParamTex("baseColor", crowdA.valueAt(time));
		
		logo.getMaterial(0).setParamTex("baseColor", logoTex.valueAt(time));
		logo.getMaterial(0).setParamVec4f("baseColorMod", new Vec4f(1, 1, 1, logoOpacity.valueAt(time)));
		logo.localPosition.set(logoPosA.valueAt(time));
		logo.localRotation = Quat.fromAngle(logoRotA.valueAt(time));
		logo.scale.set(logoSizeA.valueAt(time));
		
		Window.getCamera().localPosition = camA.valueAt(time);
		
//		pressStart.localPosition.y = 325+Window.getCamera().getWorldPos().y;
		
		//Fade
		float opacity = sceneOpacityA.valueAt(time).floatValue();
		Window.postProcess.fadeToBlack = opacity;
//		bg.material.setParamVec4f("baseColorMod", new Vec4f(opacity, opacity, opacity, 1.0));
//		sky.setColors(Vec3f.lerp(new Vec3f(), skyBot, opacity), Vec3f.lerp(new Vec3f(), skyTop, opacity));
		
		//Text
		pressStart.setColor( Vec4f.lerp(textColorA, textColorB, (1+Math.sin(time*10))/2d) );
		pressStart.setThickness( Utils.lerpF(.2f, .42f, (1+Math.sin(time*8))/2d) );
		
		versionText.setThickness( versionThickness.valueAt(time).floatValue() );
		
		if(!punched && time >= 4f) {
			Audio.playSound("data/sound/slap.ogg", AudioChannel.UI, .48f, 1);
			Window.getCamera().setCameraShake(1f);
			punched = true;
		}
		
		super.step(d);
	}
}
