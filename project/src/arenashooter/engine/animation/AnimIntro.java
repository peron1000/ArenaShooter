package arenashooter.engine.animation;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Arena;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Main;

public class AnimIntro extends Arena{
	private Mesh cat = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(), new Material("data/shaders/sprite_simple"));
	private Mesh fox = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(), new Material("data/shaders/sprite_simple"));
	
	private TextSpatial versionText;
	
	private Mesh logo;
	
	private Mesh crowd_01, crowd_02, crowd_03, crowd_04;
	
	public AnimIntro() {
		//Fade out
//		double flashIntensity = 0.5;
//		HashMap<Double, Double> opacityMap = new HashMap<>();
//		opacityMap.put(0d, 1d);
//		opacityMap.put(1.2d, 0d);
//		sceneOpacityA = new AnimTrackDouble(opacityMap, AnimInterpolation.LINEAR);
		
		//Logo
		logo = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(), new Material("data/shaders/sprite_simple"));
		logo.useTransparency = true;
		logo.attachToParent(this, "logo");
		
		//Version text
		Text txt = new Text(Main.font, TextAlignH.LEFT, "v"+Main.version);
		versionText = new TextSpatial(new Vec3f(.65, .61, 0), new Vec3f(1f), txt);
		versionText.attachToParent(logo, "textVersion");
		versionText.setColor(new Vec4f(.35, .35, .35, 1));
		
		//Crowd
		crowd_01 = Mesh.quad(new Vec3f(-5.3, -1, 15), new Quat(), new Vec3f(5, 2.5, 1), new Material("data/shaders/sprite_simple"));
		crowd_02 = Mesh.quad(new Vec3f(-2, -1, 12), new Quat(), new Vec3f(5, 2.5, 1), new Material("data/shaders/sprite_simple"));
		crowd_03 = Mesh.quad(new Vec3f(2, -1, 11), new Quat(), new Vec3f(-5, 2.5, 1), new Material("data/shaders/sprite_simple"));
		crowd_04 = Mesh.quad(new Vec3f(5.5, -1, 13), new Quat(), new Vec3f(-5, 2.5, 1), new Material("data/shaders/sprite_simple"));
		crowd_01.attachToParent(this, "crowd_01");
		crowd_02.attachToParent(this, "crowd_02");
		crowd_03.attachToParent(this, "crowd_03");
		crowd_04.attachToParent(this, "crowd_04");
		
		//Cat
		cat.scale = new Vec3f(1.5f);
		cat.attachToParent(this, "cat");
		
		//Fox
		fox.scale = new Vec3f(1.5f);
		fox.attachToParent(this, "fox");
	}
}
