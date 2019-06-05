package arenashooter.engine.ui;

import java.text.DecimalFormat;
import java.util.List;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.UiImage;

public class MenuResolution extends MenuSelectionV<UiActionable> {

	private Vec2f forVisible = new Vec2f(0, 25);
	private final Vec2f scale = new Vec2f(27f);
	private int resw = Window.getWidth();
	private int resh = Window.getHeight();
	private float rescale = Window.getResScale();
	private String s = "1,0";
	private DecimalFormat df = new DecimalFormat("0.0");
	private Button button1 = new Button(0, new Vec2f(50, 5.5), "Resolution : " + resw + "x" + resh);
	private Button button11 = new Button(0, new Vec2f(50, 5.5), "Resolution Scale : " + df.format(rescale));
	private Button button4 = new Button(0, new Vec2f(50, 5.5), "Back");
	private InputListener inputs = new InputListener();
	private List<int[]> windsize = Window.getAvailableResolutions();
	private int res = 0;

	public MenuResolution() {
	super(10, 0, 25, new Vec2f(47, 7), "data/sprites/interface/Selector.png");
	
	/* background */
	Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png");
	texture1.setFilter(false);
	UiImage bg = new UiImage(0, new Vec2f(100, 75), texture1, new Vec4f(0, 0, 1, 1));
	this.setBackground(bg);
	bg.setPosition(new Vec2f(0, 5));

	this.setPositionRef(new Vec2f(forVisible.x, forVisible.y - 45));
	this.setEcartement(9f);
	button1.setScaleText(scale);
	button11.setScaleText(scale);
	button4.setScaleText(scale);
	this.addElementInListOfChoices(button1, 2);
	this.addElementInListOfChoices(button11, 2);
	this.addElementInListOfChoices(button4, 2);
	
	
	
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
