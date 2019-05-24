package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.UiActionable;

public class Editor extends GameState {

	private MenuSelectionV<UiActionable> menu = new MenuSelectionV<>(10, -36.5f, -20f, new Vec2f(30, 10),
			"data/sprites/interface/Selector.png");
	private ScrollerH prime = new ScrollerH(0, new Vec2f(20), "Nom du fichier", "Reglage", "Ajout");
	private ScrollerH name = new ScrollerH(0, new Vec2f(20), "map1", "map2", "map3");
	private InputListener inputs = new InputListener();

	public Editor() {
		super(1);
		menu.active.setValue(true);
		menu.addElementInListOfChoices(prime, 1);

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
		case "Nom du fichier":
			menu.addElementInListOfChoices(name, 1);
			break;

		default:
			menu.removeElementInListOfChoices(name);
			System.out.println(prime.get());
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
