package arenashooter.game.gameStates;

import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.editor.Editor;

/**
 * Startup State
 */
public class Start extends GameState {
	@Override
	public void update(double delta) {
		Main.getGameMaster().requestNextState(new Editor(), GameMaster.mapEmpty);
	}

}
