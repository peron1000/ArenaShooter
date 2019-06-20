package arenashooter.game.gameStates.loading;

import arenashooter.engine.xmlReaders.reader.MapXmlReader;
import arenashooter.entities.Arena;

public class LoadingArena extends Thread {
	
	private Arena arena = null;
	private String namePath = null;
	
	public LoadingArena(Arena arena , String namePath) {
		this.arena = arena;
		this.namePath = namePath;
	}
	
	public LoadingArena() {
		this(null , null);
	}
	
	@Override
	public void run() {
		if(arena != null && namePath != null) {
			MapXmlReader reader = new MapXmlReader(namePath);
			reader.load(arena);
			
			while(!reader.loadNextEntity()) {}
		}
	}
}
