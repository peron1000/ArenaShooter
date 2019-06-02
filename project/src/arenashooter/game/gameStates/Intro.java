package arenashooter.game.gameStates;

import arenashooter.engine.animation.AnimIntro;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.input.ActionState;
import arenashooter.game.GameMaster;

public class Intro extends GameState {
	private InputListener inputs = new InputListener();

	private SoundSource bgm;
	
	public Intro() {
		super(1);

		bgm = Audio.createSource("data/sprites/intro/sf2_title_cps-1.ogg", AudioChannel.MUSIC, .2f, 1);
		bgm.play();
		
		inputs.actions.add(new EventListener<InputActionEvent>() {
			@Override
			public void launch(InputActionEvent event) {
				// TODO Auto-generated method stub
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_CONTINUE:
						bgm.destroy();
						GameMaster.gm.requestNextState(new MenuStart(), "data/mapXML/menu_empty.xml");
						//GameMaster.gm.requestNextState(new Config(), "data/mapXML/menu_empty.xml");
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
