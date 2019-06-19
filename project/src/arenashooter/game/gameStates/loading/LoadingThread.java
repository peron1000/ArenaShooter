package arenashooter.game.gameStates.loading;

import arenashooter.engine.xmlReaders.reader.MapXmlReader;
import arenashooter.entities.Arena;

public class LoadingThread extends Thread {
	
	private Arena arena = null;
	private String namePath = null;
	
	public LoadingThread(Arena arena , String namePath) {
		this.arena = arena;
		this.namePath = namePath;
	}
	
	public LoadingThread() {
		this(null , null);
	}
	
	public synchronized void setArena(Arena arena) {
		this.arena = arena;
	}
	
	public synchronized void setNamePath(String namePath) {
		this.namePath = namePath;
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
