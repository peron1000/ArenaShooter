package arenashooter.engine.ui;

import java.awt.Robot;
import java.util.LinkedList;

import org.apache.logging.log4j.core.util.Log4jThread;
import arenashooter.engine.input.Action;
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

	private UiImage selec;
	private Label op1, op2, op3, op4;
	private boolean activated = false;

	public MenuPause(float x , float y) {
		super(5 , x , y ,new Vec2f(30 ,10), "data/sprites/interface/Selector.png");
		if (maxLayout < 3) {
			Exception e = new Exception("Max layout trop petit");
			e.printStackTrace();

			// Pour eviter le crash
			for (int i = maxLayout; i < 3; i++) {
				elems.put(Integer.valueOf(i), new LinkedList<>());
			}
		}
		final float scale = 27f;

		Rectangle rec = new Rectangle(0, new Vec2f(45, 60), new Vec4f(0, 0, 0, .25));
		setBackground(rec);
		Label pause = new Label(0, new Vec2f(50, 50), "PAUSE");
		addUiElement(pause, 0);

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
					Robot robot = new Robot();
					robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
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
		setEcartement(10);
		this.setPositionRef(new Vec2f(0, -15));
		pause.setPos(new Vec2f(0, -27));
		selec.setPos(op1.getPos());
		selec.setScale(new Vec2f(scale, 9.5f));

	}

	@Override
	public void update(double delta) {

		// Detect controls
		for (Controller controller : GameMaster.gm.controllers) {
			if (Input.actionJustPressed(controller.getDevice(), Action.UI_UP)) {
				if (!activated) {
					this.up();
				}
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_DOWN)) {
				if (!activated) {
					this.down();
				}
			} else if (Input.actionJustPressed(Device.KEYBOARD, Action.JUMP)) {
				activated = !activated;
			} else if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {
				this.getTarget().lunchAction("ok");

			} else if (Input.actionJustPressed(controller.getDevice(), Action.JUMP)) {
				this.getTarget().lunchAction("ok");
			}
		}

		super.update(delta);

	}

}
