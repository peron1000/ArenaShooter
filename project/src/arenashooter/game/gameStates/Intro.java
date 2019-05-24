package arenashooter.game.gameStates;

import arenashooter.engine.animation.AnimIntro;
import arenashooter.engine.events.BooleanProperty;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.entities.Entity;
import arenashooter.entities.Music;
import arenashooter.game.GameMaster;

public class Intro extends GameState {
	private InputListener inputs = new InputListener();

	public Intro() {
		super(1);
		inputs.actions.add(new EventListener<InputActionEvent>() {
			@Override
			public void action(InputActionEvent event) {
				// TODO Auto-generated method stub
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_CONTINUE:
						Entity bgm = current.getChild("bgm");
						if (bgm instanceof Music)
							((Music) bgm).stop();
						GameMaster.gm.requestNextState(new Config(), "data/mapXML/menu_empty.xml");
						break;
					default:
						break;
					}

				}
			}
		});
	}

	@Override
	public void init() {
		super.init();
		current = new AnimIntro();
	}

	@Override
	public void update(double delta) {
		// Detect controls
		inputs.step(delta);
		current.step(delta);
	}

}
