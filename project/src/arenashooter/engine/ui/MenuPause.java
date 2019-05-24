package arenashooter.engine.ui;

import java.awt.Robot;
import java.util.LinkedList;

import org.apache.logging.log4j.core.util.Log4jThread;

import arenashooter.engine.events.BooleanProperty;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.ActionTest;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.Controller;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.Intro;

public class MenuPause extends MenuSelectionV<Label> {

	private Label op1, op2, op3, op4;
	private InputListener inputs = new InputListener();

	public MenuPause(float x, float y) {
		super(5, x, y, new Vec2f(30, 10), "data/sprites/interface/Selector.png");
		if (maxLayout < 3) {
			Exception e = new Exception("Max layout trop petit");
			e.printStackTrace();

			// Pour eviter le crash
			for (int i = maxLayout; i < 3; i++) {
				elems.put(Integer.valueOf(i), new LinkedList<>());
			}
		}
		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void action(InputActionEvent event) {
				// TODO Auto-generated method stub
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_OK:
						getTarget().lunchAction("ok");
						break;

					case UI_PAUSE:
						active =  new BooleanProperty(false);
						break;
					case UI_BACK:
						active =  new BooleanProperty(false);
						break;
					case UI_UP:
						up();
						break;
					case UI_DOWN:
						down();
						break;
					default:
						break;
					}

				}
			}
		});
		final float scale = 24;
		setPositionRef(new Vec2f(getPosition().x, getPosition().y - 12.5));
		setEcartement(10);
		Rectangle rec = new Rectangle(0, new Vec2f(45, 60), new Vec4f(0, 0, 0, .25));
		setBackground(rec);
		Label pause = new Label(0, new Vec2f(50, 50), "PAUSE");
		addUiElement(pause, 0);
		pause.setPos(new Vec2f(x, y - 27));

		op1 = new Label(0, new Vec2f(scale), "Resume");
		addElementInListOfChoices(op1, 1);

		op2 = new Label(0, new Vec2f(scale), "Score");
		addElementInListOfChoices(op2, 1);

		op3 = new Label(0, new Vec2f(scale), "Option");
		addElementInListOfChoices(op3, 1);

		op4 = new Label(0, new Vec2f(scale), "Quit : Alt+f4");
		addElementInListOfChoices(op4, 1);
		
		op1.addAction("ok", new Trigger() {
			@Override
			public void make() {
				try {
//					Robot robot = new Robot();
//					robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
					active = new BooleanProperty(false);
					System.out.println("op1 : Resume");
				} catch (Exception ex) {
					System.out.println("fail pause");
					Log4jThread.getDefaultUncaughtExceptionHandler();
				}
			}
		});
		op2.addAction("ok", new Trigger() {
			@Override
			public void make() {
				System.out.println("op2 : Score");
			}
		});
		op3.addAction("ok", new Trigger() {
			@Override
			public void make() {
				System.out.println("op3 : Option");
			}
		});
		op4.addAction("ok", new Trigger() {
			@Override
			public void make() {
				System.out.println("op4 : quit");
				GameMaster.gm.requestNextState(new Intro(), GameMaster.mapEmpty);
			}
		});
		op3.visible = false;
	}

	@Override
	public void update(double delta) {
		// Detect controls
		inputs.step(delta);
		
		super.update(delta);
	}
}
