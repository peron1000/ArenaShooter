package arenashooter.game.gameStates.loading;

import java.util.Map.Entry;

import arenashooter.engine.json.JsonTransformer;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.xmlReaders.reader.MapXmlReader;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.game.gameStates.GameState;

public class LoadingArena extends Thread {
	
	private GameState next = null;
	private String namePath = null;
	private Trigger onFinish;
	
	public LoadingArena(GameState next , String namePath) {
		this.next = next;
		this.namePath = namePath;
	}
	
	public LoadingArena() {
		this(null , null);
	}
	
	@Override
	public void run() {
		if(next != null && namePath != null) {
			try {
				Arena arena = JsonTransformer.importArena(namePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			MapXmlReader reader = new MapXmlReader(namePath);
//			reader.load(arena);
//			
//			while(!reader.loadNextEntity()) {}
		}
		onFinish.make();
		System.out.println("arena LoadingArena:"+next);
	}
	
	public synchronized void setOnFinish(Trigger t) {
		onFinish = t;
	}
}
