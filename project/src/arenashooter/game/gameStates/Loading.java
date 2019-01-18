package arenashooter.game.gameStates;

import arenashooter.engine.xmlReaders.MapXmlReader;

public class Loading extends GameState {
	public static final Loading loading = new Loading();
	
	private GameState next;
	
	private Loading() {
		// constructor untouchable
	}
	
	public void setNextState(GameState next , String mapName) {
		long test = System.currentTimeMillis();
		this.next = next;
		next.map = MapXmlReader.read(mapName);
		System.out.println("Time to load map : "+(System.currentTimeMillis() - test));
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
