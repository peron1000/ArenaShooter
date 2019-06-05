package arenashooter.engine.ui;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.MenuStart;

public class MenuOption2 extends MenuSelectionV<UiActionable> {
	/* MenuStart Menu */
	private Vec2f forVisible = new Vec2f(0, 25);
	private final Vec2f scale = new Vec2f(27f);
	private int resw = Window.getWidth();
	private int resh = Window.getHeight();
	private float mv = Audio.getMainVolume();
	private float mc = Audio.getChannelVolume(AudioChannel.MUSIC);
	private float msfx = Audio.getChannelVolume(AudioChannel.SFX);
	private float mui = Audio.getChannelVolume(AudioChannel.UI);
	private float rescale = Window.getResScale();
	private String s="1,0";
	private DecimalFormat df = new DecimalFormat("0.0");
	private Button button1 = new Button(0, new Vec2f(50, 5.5), "Resolution : " + resw + "x" + resh);
	private Button button11 = new Button(0, new Vec2f(50, 5.5), "Resolution Scale : " + df.format(rescale));
	private Button button2 = new Button(0, new Vec2f(50, 5.5), "Main Volume : " + df.format(mv));
	private Button button3 = new Button(0, new Vec2f(50, 5.5), "MUSIC Volume : " + df.format(mc));
	private Button button33 = new Button(0, new Vec2f(50, 5.5), "SFX Volume : " + df.format(msfx));
	private Button button34 = new Button(0, new Vec2f(50, 5.5), "UI Volume : " + df.format(mui));
	// private Button button35 = new Button(0, new Vec2f(50, 5.5), "Credit");
	private Button button4 = new Button(0, new Vec2f(50, 5.5), "Back");

	private InputListener inputs = new InputListener();
	private Camera cam;
	private List<int[]> windsize = Window.getAvailableResolutions();
	private int res = 0;

	public MenuOption2() {
		super(10, 0, 25, new Vec2f(47, 7), "data/sprites/interface/Selector.png");
//		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
//		texture2.setFilter(false);

		/* selector */
//		Texture texturesl = Texture.loadTexture("data/sprites/interface/SelectorLeft.png");
//		texturesl.setFilter(false);
//		UiImage sl = new UiImage(0, new Vec2f(47, 7), texturesl, new Vec4f(1, 1, 1, 1));
//		sl.setVisible(false);
//		
//		Texture texturesr = Texture.loadTexture("data/sprites/interface/SelectorRight.png");
//		texturesr.setFilter(false);
//		UiImage sr = new UiImage(0, new Vec2f(47, 7), texturesr, new Vec4f(1, 1, 1, 1));
//		sr.setVisible(false);
//		
//		Texture textureselec = Texture.loadTexture("data/sprites/interface/Selector.png");
//		textureselec.setFilter(false);
//		UiImage selec = new UiImage(0, new Vec2f(47, 7), textureselec, new Vec4f(1, 1, 1, 1));
//		selec.setVisible(false);
//		

		/* background */
		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png");
		texture1.setFilter(false);
		UiImage bg = new UiImage(0, new Vec2f(100, 75), texture1, new Vec4f(0, 0, 1, 1));
		this.setBackground(bg);
		bg.setPosition(new Vec2f(0, 5));

		this.setPositionRef(new Vec2f(forVisible.x, forVisible.y - 45));
		this.setEcartement(9f);
		button1.setScaleText(new Vec2f(27f));
		button2.setScaleText(scale);
		button3.setScaleText(scale);
		button33.setScaleText(scale);
		button34.setScaleText(scale);
		button4.setScaleText(scale);
		button11.setScaleText(scale);

		this.addElementInListOfChoices(button1, 2);
		this.addElementInListOfChoices(button11, 2);
		this.addElementInListOfChoices(button2, 5);
		this.addElementInListOfChoices(button3, 5);
		this.addElementInListOfChoices(button33, 5);
		this.addElementInListOfChoices(button34, 5);
		this.addElementInListOfChoices(button4, 1);

		button1.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (res < windsize.size() - 1) {
					res++;
					resw = windsize.get(res)[0];
					resh = windsize.get(res)[1];
				} else {
					res = 0;
					resw = windsize.get(res)[0];
					resh = windsize.get(res)[1];
				}
				button1.setText("Resolution : " + resw + "x" + resh);
			}
		});
		button1.addAction("left", new Trigger() {

			@Override
			public void make() {
				if (res > 0) {
					res--;
					resw = windsize.get(res)[0];
					resh = windsize.get(res)[1];
				} else {
					res = windsize.size() - 1;
					resw = windsize.get(res)[0];
					resh = windsize.get(res)[1];
				}
				button1.setText("Resolution : " + resw + "x" + resh);

			}
		});
		button1.setOnArm(new Trigger() {

			@Override
			public void make() {
				Window.resize(resw, resh);
			}
		});

		/*Rescale*/
		button11.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (rescale < 2.0f) {
					rescale = rescale + 0.1f;
			} else {
					rescale = 0.5f;
				}
				s = df.format(rescale);
				button11.setText("Rescale : " + s);
			}
		});
		button11.addAction("left", new Trigger() {

			@Override
			public void make() {
				if (rescale > 0.505f) {
					rescale = rescale - 0.1f;
					
				} else {
					rescale = 2.0f;
				}
				s = df.format(rescale);
				button11.setText("Rescale : " + s);

			}
		});
		button11.setOnArm(new Trigger() {
			@Override
			public void make() {
				Window.setResScale((float) rescale);
			}
		});

		
		/* Main Volume */
		button2.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (mv < 1.0f) {
					mv = mv + 0.1f;
				} else {
					mv = 0.0f;
				}
				s = df.format(mv);
				button2.setText("Main Volume : " + s);
			}
		});
		button2.addAction("left", new Trigger() {

			@Override
			public void make() {

				if (mv > 0.05f) {
					mv = mv - 0.1f;
				} else {
					mv = 1.0f;
				}
				s =df.format(mv);
				button2.setText("Main Volume : " + s);
			}
		});
		button2.setOnArm(new Trigger() {

			@Override
			public void make() {
				Audio.setMainVolume((float) mv);
			}
		});

		// UI, SFX, MUSIC;
		/* Music */
		button3.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (mc < 1.0f) {
					mc = mc + 0.1f;
				} else {
					mc = 0.0f;
				}
				s = df.format(mc);
				button3.setText("Music Volume : " + s);
			}
		});
		button3.addAction("left", new Trigger() {

			@Override
			public void make() {
				if (mc > 0.05f) {
					mc = mc - 0.1f;
				} else {
					mc = 1.0f;
				}
				s = df.format(mc);
				button3.setText("Music Volume : " + s);
			}
		});
		button3.setOnArm(new Trigger() {

			@Override
			public void make() {
				Audio.setChannelVolume(AudioChannel.MUSIC, ((float) mc));
			}
		});
		/* SFX */
		button33.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (msfx < 1.0f) {
					msfx = msfx + 0.1f;
				} else {
					msfx = 0.0f;
				}
				s = df.format(msfx);
				button33.setText("SFX Volume : " + s);
			}
		});
		button33.addAction("left", new Trigger() {

			@Override
			public void make() {
				if (msfx > 0.05f) {
					msfx = msfx - 0.1f;
				} else {
					msfx = 1.0f;
				}
				s = df.format(msfx);
				button33.setText("SFX Volume : " + s);
			}
		});
		button33.setOnArm(new Trigger() {

			@Override
			public void make() {
				Audio.setChannelVolume(AudioChannel.SFX, ((float) msfx));
			}
		});

		/* UI */
		button34.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (mui < 1.0) {
					mui = mui + 0.1f;
				} else {
					mui = 0.0f;
				}
				s = df.format(mui);
				button34.setText("UI Volume : " + s);
			}
		});
		button34.addAction("left", new Trigger() {

			@Override
			public void make() {
				if (mui > 0.05f) {
					mui = mui - 0.1f;
				} else {
					mui = 1.0f;
				}
				s = df.format(mui);
				button34.setText("UI Volume : " + s);
			}
		});
		button34.setOnArm(new Trigger() {

			@Override
			public void make() {
				Audio.setChannelVolume(AudioChannel.UI, ((float) mui));
			}
		});


		/* Quit */
		button4.setOnArm(new Trigger() {

			@Override
			public void make() {
				selectorVisible = false;
				// GameMaster.gm.requestNextState(new MenuStart(),
				// "data/mapXML/menu_empty.xml");
			}
		});

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						if (!getTarget().equals(button4)) {
							getTarget().lunchAction("left");
						} else {
							leftAction();
						}
						break;
					case UI_RIGHT:

						if (!getTarget().equals(button4)) {
							getTarget().lunchAction("right");
						} else {
							rightAction();
						}
						break;
					case UI_UP:
						upAction();
						break;
					case UI_DOWN:
						downAction();
						break;
					case UI_OK:
					case UI_CONTINUE:
						getTarget().selectAction();
						break;
					case UI_BACK:
						selectorVisible = false;
						break;

					default:
						break;
					}
				}
			}

		});
	}
//
//	@Override
//	public void init() {
//		super.init();
//		cam = (Camera) current.getChild("camera");
//	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
	}
}