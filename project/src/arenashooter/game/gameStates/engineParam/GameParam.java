package arenashooter.game.gameStates.engineParam;

import java.util.LinkedList;

import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Map;
import arenashooter.game.Main;
import arenashooter.game.gameStates.Param;

public class GameParam {
	private LinkedList<ParamElement> UIparams = new LinkedList<>();
	private ParamElement mode , round;
	public GameMode gameMode = GameMode.Death_Match;
	public int nbRound = 1;
	
	private Map map;

	public GameParam(Map map) {
		this.map = map;
		
		// mode
		String[] gameModes = new String[GameMode.values().length];
		for (int i = 0; i < gameModes.length; i++) {
			gameModes[i] = GameMode.values()[i].name();
		}
		mode = new ParamElement("Game Mode", gameModes);
		UIparams.add(mode);
		
		// round
		String[] r = new String[10];
		for (int i = 0; i < r.length; i++) {
			r[i] = ""+(i+1);
		}
		round = new ParamElement("Round(s)", r);
		UIparams.add(round);
		
		final float x = -950 , scale = 230;
		for (int i = 0; i < UIparams.size(); i++) {
			UIparams.get(i).afficherH(map, new Vec3f(x, i*Param.selectorArray[0].size.y, 0), new Vec3f(scale), Main.font);
		}
	}
	
	public void next(int index) {
		ParamElement element = UIparams.get(index);
		if(element == mode) {
			mode.next();
			mode.text.detach();
			mode.afficherH(map, new Vec3f(-1000, -500, 0), new Vec3f(250), Main.font);
			gameMode = gameMode.getNext();
		}
	}
	
	public Vec3f getPosition(int index) {
		return UIparams.get(index).position;
	}
	
}
