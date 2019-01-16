package arenashooter.game.gameStates;

import java.util.HashMap;

import arenashooter.entities.Controller;
import arenashooter.entities.spatials.CharacterInfo;
import arenashooter.game.GameMaster;

public class CharacterChooser extends GameState {
	
	public static final CharacterChooser characterChooser = new CharacterChooser();
	
	private HashMap<Controller, CharacterInfo> choice = new HashMap<>(GameMaster.gm.controllers.size());
	
	private CharacterChooser() {
		for (Controller controller : GameMaster.gm.controllers) {
			choice.put(controller, null);
		}
		
		for (Controller controller : choice.keySet()) {
			choice.put(controller, new CharacterInfo());
		}
	}
	
	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		GameMaster.gm.requestNextState();
	}

}
