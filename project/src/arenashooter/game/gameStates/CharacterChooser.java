package arenashooter.game.gameStates;

import java.util.HashMap;

import arenashooter.entities.Controller;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.game.GameMaster;

public class CharacterChooser extends GameState {
	
	public static CharacterChooser characterChooser = new CharacterChooser();
	
	private HashMap<Controller, CharacterSprite> choice = new HashMap<>(GameMaster.gs.controllers.size());
	
	private CharacterChooser() {
		for (Controller controller : GameMaster.gs.controllers) {
			choice.put(controller, null);
		}
	}

	private void setCharSprite(Controller controller , CharacterSprite charSprite) {
		choice.put(controller, charSprite);
	}
	
	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

}
