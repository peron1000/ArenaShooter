package arenashooter.engine.ui;

import java.text.DecimalFormat;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.UiImage;



public class MenuSettingsAudio extends MenuSelectionV<UiActionable> {


		private Vec2f forVisible = new Vec2f(0, 25);
		private final Vec2f scale = new Vec2f(27f);
		private DecimalFormat df = new DecimalFormat("0.0");
		private Button button4 = new Button(0, new Vec2f(50, 5.5), "Back");
		private InputListener inputs = new InputListener();
		private float volumeMain = Audio.getMainVolume();
		private float volumeMusic = Audio.getChannelVolume(AudioChannel.MUSIC);
		private float volumeSfx = Audio.getChannelVolume(AudioChannel.SFX);
		private float volumeUi = Audio.getChannelVolume(AudioChannel.UI);
		private Button button2 = new Button(0, new Vec2f(50, 5.5), "Main Volume : " + df.format(volumeMain));
		private Button button3 = new Button(0, new Vec2f(50, 5.5), "Music Volume : " + df.format(volumeMusic));
		private Button button33 = new Button(0, new Vec2f(50, 5.5), "SFX Volume : " + df.format(volumeSfx));
		private Button button34 = new Button(0, new Vec2f(50, 5.5), "UI Volume : " + df.format(volumeUi));

		public MenuSettingsAudio() {
		super(10, 0, 25, new Vec2f(47, 7), "data/sprites/interface/Selector.png");
		
		/* background */
		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png");
		texture1.setFilter(false);
		UiImage bg = new UiImage(0, new Vec2f(100, 75), texture1, new Vec4f(0, 0, 1, 1));
		this.setBackground(bg);
		bg.setPosition(new Vec2f(0, 5));

		this.setPositionRef(new Vec2f(forVisible.x, forVisible.y - 45));
		this.setEcartement(9f);
	
	
		button2.setScaleText(scale);
		button3.setScaleText(scale);
		button33.setScaleText(scale);
		button34.setScaleText(scale);
		button4.setScaleText(scale);
		this.addElementInListOfChoices(button2, 5);
		this.addElementInListOfChoices(button3, 5);
		this.addElementInListOfChoices(button33, 5);
		this.addElementInListOfChoices(button34, 5);
		this.addElementInListOfChoices(button4, 2);
		/* Main Volume */
		button2.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (volumeMain < 1.0f) {
					volumeMain = volumeMain + 0.1f;
				} else {
					volumeMain = 0.0f;
				}
				button2.setText("Main Volume : " + df.format(volumeMain));
			}
		});
		button2.addAction("left", new Trigger() {

			@Override
			public void make() {

				if (volumeMain > 0.05f) {
					volumeMain = volumeMain - 0.1f;
				} else {
					volumeMain = 1.0f;
				}
				button2.setText("Main Volume : " + df.format(volumeMain));
			}
		});
		button2.setOnArm(new Trigger() {

			@Override
			public void make() {
				Audio.setMainVolume((float) volumeMain);
			}
		});

		// UI, SFX, MUSIC;
		/* Music */
		button3.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (volumeMusic < 1.0f) {
					volumeMusic = volumeMusic + 0.1f;
				} else {
					volumeMusic = 0.0f;
				}
				button3.setText("Music Volume : " + df.format(volumeMusic));
			}
		});
		button3.addAction("left", new Trigger() {

			@Override
			public void make() {
				if (volumeMusic > 0.05f) {
					volumeMusic = volumeMusic - 0.1f;
				} else {
					volumeMusic = 1.0f;
				}
				button3.setText("Music Volume : " + df.format(volumeMusic));
			}
		});
		button3.setOnArm(new Trigger() {

			@Override
			public void make() {
				Audio.setChannelVolume(AudioChannel.MUSIC, ((float) volumeMusic));
			}
		});
		/* SFX */
		button33.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (volumeSfx < 1.0f) {
					volumeSfx = volumeSfx + 0.1f;
				} else {
					volumeSfx = 0.0f;
				}
				button33.setText("SFX Volume : " + df.format(volumeSfx));
			}
		});
		button33.addAction("left", new Trigger() {

			@Override
			public void make() {
				if (volumeSfx > 0.05f) {
					volumeSfx = volumeSfx - 0.1f;
				} else {
					volumeSfx = 1.0f;
				}
				button33.setText("SFX Volume : " + df.format(volumeSfx));
			}
		});
		button33.setOnArm(new Trigger() {

			@Override
			public void make() {
				Audio.setChannelVolume(AudioChannel.SFX, ((float) volumeSfx));
			}
		});

		/* UI */
		button34.addAction("right", new Trigger() {

			@Override
			public void make() {
				if (volumeUi < 1.0) {
					volumeUi = volumeUi + 0.1f;
				} else {
					volumeUi = 0.0f;
				}
				button34.setText("UI Volume : " + df.format(volumeUi));
			}
		});
		button34.addAction("left", new Trigger() {

			@Override
			public void make() {
				if (volumeUi > 0.05f) {
					volumeUi = volumeUi - 0.1f;
				} else {
					volumeUi = 1.0f;
				}
				button34.setText("UI Volume : " + df.format(volumeUi));
			}
		});
		button34.setOnArm(new Trigger() {

			@Override
			public void make() {
				Audio.setChannelVolume(AudioChannel.UI, ((float) volumeUi));
			}
		});


			
		/* Quit */
		button4.setOnArm(new Trigger() {

			@Override
			public void make() {
				selectorVisible = false;
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
