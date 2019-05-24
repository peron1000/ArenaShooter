package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.ScrollerHTitle;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.game.gameStates.editorEnum.NameFile;
import arenashooter.game.gameStates.editorEnum.Prime;

public class Editor extends GameState {

	private final Vec2f scale = new Vec2f(15);
	private MenuSelectionV<UiActionable> menu = new MenuSelectionV<>(10, -36.5f, -20f, new Vec2f(20, 6),
			"data/sprites/interface/Selector.png");
	private ScrollerH<Prime> prime = new ScrollerH<>(0, scale, Prime.values());
	private ScrollerHTitle<NameFile> name = new ScrollerHTitle<>(0, scale,"Name file ", NameFile.values());
	private Button button = new Button(0, new Vec2f(50, 5.5), "Object");
	private InputListener inputs = new InputListener();

	public Editor() {
		super(1);
		menu.active.setValue(true);
		menu.addElementInListOfChoices(prime, 1);
		button.setColorFond(new Vec4f(0, 0, 0, 0));

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void action(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						menu.left();
						break;
					case UI_RIGHT:
						menu.right();
						break;
					case UI_UP:
						menu.up();
						break;
					case UI_DOWN:
						menu.down();
						break;
					case UI_OK:
						menu.getTarget().selectAction();
						break;
					default:
						break;
					}
					majPrime();
				}
			}
		});
		majPrime();
	}

	private void majPrime() {
		switch (prime.get()) {
		case Save:
			menu.removeElementInListOfChoices(button);
			menu.addElementInListOfChoices(name, 1);
			break;
		case Adding:
			menu.removeElementInListOfChoices(name);
			menu.addElementInListOfChoices(button, 1);
			break;
		default:
			menu.removeElementInListOfChoices(button);
			menu.removeElementInListOfChoices(name);
			break;
		}
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		menu.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		menu.draw();
	}

}
