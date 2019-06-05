package arenashooter.engine.ui;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.UiImage;

public class MenuSettings extends MenuSelectionV<UiActionable> {
	private MenuSettingsVideo resolution;
	private MenuSettingsAudio music;
	/* MenuStart Menu */
	private Vec2f forVisible = new Vec2f(0, 25);
	private final Vec2f scale = new Vec2f(27f);
	public MenuSettingsVideo getResolution() {
		return resolution;
	}

	public MenuSettingsAudio getMusic() {
		return music;
	}
	//private MultiMenu<optionoption> multi = new MultiMenu<>(10, optionoption.values(), "data/sprites/interface/Selector.png", new Vec2f (30 ,10));
	private Button button15 = new Button(0, new Vec2f(50, 5.5), "Video");
	private Button button16 = new Button(0, new Vec2f(50, 5.5), "Audio");
	private Button button4 = new Button(0, new Vec2f(50, 5.5), "Back");

	private InputListener inputs = new InputListener();

	public MenuSettings() {
		super(10, 0, 25, new Vec2f(47, 7), "data/sprites/interface/Selector.png");

		/*menu resolution*/
		resolution = new MenuSettingsVideo();
		resolution.selectorVisible = false;
		/*menu resolution*/
		music = new MenuSettingsAudio();
		music.selectorVisible = false;	

		/* background */
		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png");
		texture1.setFilter(false);
		UiImage bg = new UiImage(0, new Vec2f(100, 75), texture1, new Vec4f(0, 0, 1, 1));
		this.setBackground(bg);
		bg.setPosition(new Vec2f(0, 5));

		this.setPositionRef(new Vec2f(forVisible.x, forVisible.y - 45));
		this.setEcartement(9f);
		button4.setScaleText(scale);
		button15.setScaleText(scale);
		button16.setScaleText(scale);

		this.addElementInListOfChoices(button15, 2);
		this.addElementInListOfChoices(button16, 2);

		this.addElementInListOfChoices(button4, 1);

		button15.setOnArm(new Trigger() {

			@Override
			public void make() {
				resolution.selectorVisible = !resolution.selectorVisible;
			}
		});
		button16.setOnArm(new Trigger() {

			@Override
			public void make() {
				music.selectorVisible = !music.selectorVisible;
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
		super.update(delta);
		if (!resolution.selectorVisible && !music.selectorVisible) {
			inputs.step(delta);
		}
		else if(music.selectorVisible){
			music.update(delta);
		}
		else {
			resolution.update(delta);
		}
	}

	@Override
	public void draw() {
		super.draw();
		if (resolution.selectorVisible) {
			resolution.draw();
		}
		else if(music.selectorVisible){
			music.draw();
		}
	}
}