package arenashooter.game.gameStates;

import java.util.LinkedList;

import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Param extends GameState {

	private TextSpatial textGameMode_Titre , textGameMode_Variable , textRound;
	private static final String strGameMode = "Game Mode", strNbRound = "Round(s) : ";
	private GameParam gameParam = new GameParam();
	private LinkedList<GameParam> p = new LinkedList<>();

	@Override
	public void init() {

		super.init();
	}

	@Override
	public void update(double delta) {
		super.update(delta);
	}
	
}
