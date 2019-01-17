package arenashooter.game.gameStates;

import arenashooter.game.GameMaster;

public class CharacterChooser extends GameState {
	@Override
	public void update(double delta) {
		// TODO : update GameMaster->controllers->CharacterInfo
		
		// TODO : remove next instructions
		GameMaster.gm.requestNextState();
	}

}
