package arenashooter.game.gameStates;

import arenashooter.engine.MapXMLTranslator;

public class Loading extends GameState {
	public static final Loading loading = new Loading();
	
	private GameState next;
	
	private Loading() {
		// constructor untouchable
	}
	
	public void setNextState(GameState next , String mapName) {
		this.next = next;
		next.map = MapXMLTranslator.getMap(mapName);
	}
	
	public GameState getNextState() {
		return next;
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
