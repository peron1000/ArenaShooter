package arenashooter.game.gameStates.engineParam;

import java.util.LinkedList;

public class GameParam {
	private LinkedList<Couple> params = new LinkedList<>();
	
	private class Couple {
		ParamElement param;
		boolean affiche = false;
		public Couple(ParamElement param) {
			this.param = param;
		}
	}
	
	
	
}
