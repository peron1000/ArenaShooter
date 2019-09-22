package arenashooter.game.gameStates;

import java.util.Queue;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.animevents.AnimEventCustom;
import arenashooter.engine.animation.animevents.AnimEventSound;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Main;

/**
 * Intro movie
 */
public class Intro extends GameState {
	private InputListener inputs = new InputListener();
	
	private Animation anim;

	private SoundSource bgm, bgmLoop;
	
	private Vec4f textColorA = new Vec4f(.925, .635, .110, 1), textColorB = new Vec4f(.859, .125, .714, 1);
	
	public Intro() {
		super("data/arena/menu_intro.arena");
		anim = new Animation(AnimationData.loadAnim("data/animations/anim_intro.xml"));

		bgm = Main.getAudioManager().createSource("data/music/Juhani Junkala [Chiptune Adventures] 4. Stage Select [Edited].ogg", AudioChannel.MUSIC, .67f, 1);
		bgmLoop = Main.getAudioManager().createSource("data/music/Juhani Junkala [Retro Game Music Pack] Title Screen.ogg", AudioChannel.MUSIC, .8f, 1.05f);
		bgmLoop.setLooping(true);
		
		inputs.actions.add(new EventListener<InputActionEvent>() {
			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_CONTINUE:
						bgm.destroy();
						bgmLoop.destroy();
						Main.getGameMaster().requestNextState(new MenuStart());
						break;
					default:
						break;
					}

				}
			}
		});
	}

	@Override
	public void init() {
		super.init();
		Main.getRenderer().getPostProcess().fadeToBlack = 1;
		getCamera().setFOV(38.3f);
		getCamera().interpolate = false;
		
		///Fill map with intro-specific stuff TODO: De-harcode this
		//Logo
		Mesh logo = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(), Main.getRenderer().loadMaterial("data/materials/sprite_simple.material"));
		logo.getMaterial(0).setTransparency(true);
		logo.attachToParent(current, "logo");
		
		//Logo bg
		Mesh logoBg = Mesh.quad(new Vec3f(0, -7.5, 7.95), new Quat(), new Vec3f(), Main.getRenderer().loadMaterial("data/materials/color.material"));
		logoBg.scale.set(7, 5, 1);
		logoBg.attachToParent(current, "logo_bg");
		
		//Version text
		Text txt = new Text(Main.font, TextAlignH.LEFT, Text.TextAlignV.TOP, "v"+Main.version);
		TextSpatial versionText = new TextSpatial(new Vec3f(.65, .61, 0), new Vec3f(1f), txt);
		versionText.attachToParent(logo, "textVersion");
		versionText.setColor(new Vec4f(.35, .35, .35, 1));
		
		//PRESS START text
		txt = new Text(Main.font, TextAlignH.CENTER, Text.TextAlignV.TOP, "PRESS ENTER.");
		TextSpatial pressStart = new TextSpatial(new Vec3f(0, .75, -2), new Vec3f(1.5f), txt);
		pressStart.attachToParent(getCamera(), "textPressStart");
		
		//Fox
		Mesh fox= Mesh.quad(new Vec3f(), new Quat(), new Vec3f(), Main.getRenderer().loadMaterial("data/materials/sprite_simple.material"));
		fox.scale = new Vec3f(1.5f);
		fox.attachToParent(current, "fox");
		
		//Cat
		Mesh cat = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(), Main.getRenderer().loadMaterial("data/materials/sprite_simple.material"));
		cat.scale = new Vec3f(1.5f);
		cat.attachToParent(current, "cat");
		
		//Crowds
		Mesh crowd = Mesh.quad(new Vec3f(-5.3, -1, 15), new Quat(), new Vec3f(5, 2.5, 1), Main.getRenderer().loadMaterial("data/materials/sprite_simple.material"));
		crowd.attachToParent(current, "crowd_01");
		crowd = Mesh.quad(new Vec3f(-2, -1, 12), new Quat(), new Vec3f(5, 2.5, 1), Main.getRenderer().loadMaterial("data/materials/sprite_simple.material"));
		crowd.attachToParent(current, "crowd_02");
		crowd = Mesh.quad(new Vec3f(2, -1, 11), new Quat(), new Vec3f(-5, 2.5, 1), Main.getRenderer().loadMaterial("data/materials/sprite_simple.material"));
		crowd.attachToParent(current, "crowd_03");
		crowd = Mesh.quad(new Vec3f(5.5, -1, 13), new Quat(), new Vec3f(-5, 2.5, 1), Main.getRenderer().loadMaterial("data/materials/sprite_simple.material"));
		crowd.attachToParent(current, "crowd_04");
		
		bgm.play();
		anim.play();
	}

	double time = 0;
	
	@Override
	public void update(double delta) {
		anim.step(delta);
		
		time += delta;
		
		Queue<AnimEvent> events = anim.getEvents();
		AnimEvent currentEvent = events.peek();
		while( (currentEvent = events.poll()) != null ) {
			if(currentEvent instanceof AnimEventSound)
				((AnimEventSound) currentEvent).play(null);
			else if(currentEvent instanceof AnimEventCustom) {
				if( ((AnimEventCustom) currentEvent).data.equals("changeMusic") )
					bgmLoop.play();
			}
		}
		
		//Fade to black
		Main.getRenderer().getPostProcess().fadeToBlack = (float) anim.getTrackD("fadeToBlack");
		
		//Camera position
		getCamera().localPosition = anim.getTrackVec3f("cam_pos");
		
		//Logo
		((Mesh)current.getChild("logo")).getMaterial(0).setParamTex("baseColor", anim.getTrackTex("logo_tex").setFilter(false));
		Vec4f color = new Vec4f(((Mesh)current.getChild("logo")).getMaterial(0).getParamVec4f("baseColorMod"));
		color.w = (float) anim.getTrackD("logo_opacity");
		((Mesh)current.getChild("logo")).getMaterial(0).setParamVec4f("baseColorMod", color);
		((Mesh)current.getChild("logo")).localPosition.set(anim.getTrackVec3f("logo_pos"));
		Quat.fromAngle(anim.getTrackD("logo_rot"), ((Mesh)current.getChild("logo")).localRotation);
		((Mesh)current.getChild("logo")).scale.set(anim.getTrackVec3f("logo_scale"));
		
		((Mesh)current.getChild("logo_bg")).getMaterial(0).setParamVec4f("baseColor", new Vec4f( anim.getTrackVec3f("logo_bg_color"), 1) );

		//Cat
		((Mesh)current.getChild("cat")).getMaterial(0).setParamTex("baseColor", anim.getTrackTex("cat_tex").setFilter(false));
		((Mesh)current.getChild("cat")).localPosition.set(anim.getTrackVec3f("cat_pos"));
		
		//Fox
		((Mesh)current.getChild("fox")).getMaterial(0).setParamTex("baseColor", anim.getTrackTex("fox_tex").setFilter(false));
		((Mesh)current.getChild("fox")).localPosition.set(anim.getTrackVec3f("fox_pos"));
		((Mesh)current.getChild("fox")).scale.set(anim.getTrackVec3f("fox_scale"));
		
		//Crowds
		((Mesh)current.getChild("crowd_01")).getMaterial(0).setParamTex("baseColor", anim.getTrackTex("crowd_tex").setFilter(false));
		((Mesh)current.getChild("crowd_02")).getMaterial(0).setParamTex("baseColor", anim.getTrackTex("crowd_tex"));
		((Mesh)current.getChild("crowd_03")).getMaterial(0).setParamTex("baseColor", anim.getTrackTex("crowd_tex"));
		((Mesh)current.getChild("crowd_04")).getMaterial(0).setParamTex("baseColor", anim.getTrackTex("crowd_tex"));
		
		//Text
		((TextSpatial)getCamera().getChild("textPressStart")).setColor( Vec4f.lerp(textColorA, textColorB, (1+Math.sin(time*10))/2d, new Vec4f()) );
		((TextSpatial)getCamera().getChild("textPressStart")).setThickness( Utils.lerpF(.2f, .42f, (1+Math.sin(time*8))/2d) );
		
		((TextSpatial)current.getChild("logo").getChild("textVersion")).setThickness( (float) anim.getTrackD("text_version_thickness") );
		
		// Detect controls
		inputs.step(delta);
		current.step(delta);
	}

}
