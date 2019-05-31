package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.TextInput;

public class TestUi extends GameState {

	InputListener inputs = new InputListener();
	Menu menu = new Menu(2);
	TextInput textInput = new TextInput();

	public TestUi() {
		super(1);
		menu.addUiElement(textInput, 0);
		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_OK:
						textInput.selectAction();
						break;
					case UI_DOWN:
						textInput.downAction();
						break;
					case UI_UP:
						textInput.upAction();
						break;
					case UI_LEFT:
						textInput.leftAction();
						break;
					case UI_RIGHT:
						textInput.rightAction();
						break;
					case UI_CONTINUE:
						System.out.println(textInput.getText());
						break;
					case UI_CANCEL:
						textInput.cancelChar();
						break;
					case UI_RIGHT2:
						textInput.setScaleLerp(Vec2f.add(textInput.getScale(), new Vec2f(2)));
						break;
					case UI_CHANGE:
						textInput.changeType();
						break;
					default:
						break;
					}
				}
			}
		});
	}

	@Override
	public void draw() {
		super.draw();
		Window.beginUi();
		menu.draw();
		Window.endUi();
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		inputs.step(delta);
		menu.update(delta);
	}

}
