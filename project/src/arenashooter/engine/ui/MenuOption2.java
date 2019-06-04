package arenashooter.engine.ui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

	private Button button1 = new Button(0, new Vec2f(50, 5.5), "Resolution : " + resw + "x" + resh);
//	private Button button2 = new Button(0, new Vec2f(50, 5.5), "Camera");
//	private Button button3 = new Button(0, new Vec2f(50, 5.5), "Credit");
	private Button button4 = new Button(0, new Vec2f(50, 5.5), "Back");

	private InputListener inputs = new InputListener();
	private Camera cam;
	private List<int[]> windsize = Window.getAvailableResolutions();
	private int res = 0;

	public MenuOption2() {
		super(10, 0, 25, new Vec2f(47, 7), "data/sprites/interface/Selector.png");
//		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
//		texture2.setFilter(false);

		/*selector*/
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
		
		/*background*/
		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png");
		texture1.setFilter(false);
		UiImage bg = new UiImage(0, new Vec2f(100, 25), texture1, new Vec4f(0, 0, 1, 1));
		this.setBackground(bg);
		bg.setPosition(new Vec2f(0, -10));

		this.setPositionRef(new Vec2f(forVisible.x, forVisible.y - 45));
		this.setEcartement(9f);
		button1.setScaleText(new Vec2f(27f));
	//	button2.setScaleText(scale);
		// button3.setScaleText(scale);
		button4.setScaleText(scale);



		this.addElementInListOfChoices(button1, 2);
		//this.addElementInListOfChoices(button2, 5);
		// this.addElementInListOfChoices(button3, 7);
		this.addElementInListOfChoices(button4, 1);

		button1.addAction("right", new Trigger() {

			@Override
			public void make() {
			//	setImageSelec(sr, 9);
				if (res < windsize.size()-1) {
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
				
				//setImageSelec(sl, 9);
				//sl.setVisible(true);
				if (res > 0) {
					res--;
					resw = windsize.get(res)[0];
					resh = windsize.get(res)[1];
				} else {
					res = windsize.size()-1;
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
//		button2.setOnArm(new Trigger() {
//
//			@Override
//			public void make() {
//			Window.setCamera(new Camera(new Vec3f (1,1,1)));
//			}
//		});
		button4.setOnArm(new Trigger() {

			@Override
			public void make() {
				selectorVisible = false;
				//GameMaster.gm.requestNextState(new MenuStart(), "data/mapXML/menu_empty.xml");
			}
		});

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						if(getTarget().equals(button1)) {
							getTarget().lunchAction("left");
						}else {
							leftAction();
						}
						break;
					case UI_RIGHT:
						
						if(getTarget().equals(button1)) {
							getTarget().lunchAction("right");
						}else {
							rightAction();
						}
						break;
					case UI_UP:
//							setImageSelec(selec, 9);
							upAction();
						break;
					case UI_DOWN:
//						setImageSelec(selec, 9);	 
						 downAction();
						break;
					case UI_OK:
					case UI_CONTINUE:
						getTarget().selectAction();
						break;
					case UI_BACK:
						//GameMaster.gm.requestNextState(new MenuStart(), "data/mapXML/menu_empty.xml");
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