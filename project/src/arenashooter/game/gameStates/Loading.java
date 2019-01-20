package arenashooter.game.gameStates;

import java.util.ArrayList;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.xmlReaders.MapXmlReader;
import arenashooter.entities.Camera;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.CharacterInfo;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.LoadingFloor;
import arenashooter.game.GameMaster;

public class Loading extends GameState {
	public static final Loading loading = new Loading();
	
	private GameState next;
//	private Camera camera = new Camera(new Vec3f(0, 0, 450));
	
	private Loading() { }
	
	public void init() {
		Window.camera = new Camera(new Vec3f(0, 0, 450));
		
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_loading");
		
		ArrayList<Entity> entities = new ArrayList<>();
		
		float startX = -(GameMaster.gm.controllers.size()*64f)/2f;
		int i=0;
		for(CharacterInfo c : GameMaster.gm.controllers.values()) {
			float x = startX+(64f*i);
			
			entities.add( new CharacterSprite(new Vec2f(x, 0), c) );
			entities.add( new LoadingFloor(new Vec2f(x, 0)) );
			i++;
		}
		
//		entities.add(camera);
//		entities.add(new Sky(new Vec3f(0), new Vec3f(0)));
		
		map = new Map(entities);
	}
	
	
	/**
	 * Set loading target
	 * @param next
	 * @param mapName
	 */
	public void setNextState(GameState next , String mapName) {
		long test = System.currentTimeMillis();
		this.next = next;
		next.map = MapXmlReader.read(mapName);
		System.out.println("Time to load map : "+(System.currentTimeMillis() - test));
	}
	
	public GameState getNextState() {
		return next;
	}

	double time = 0; //TODO: Remove fake load time
	@Override
	public void update(double delta) {
		map.step(delta);
		
		//Load next
		time += delta;
		if(time > 1) GameMaster.gm.requestNextState(); //Loading finished, go to next state
	}

	@Override
	public void draw() {
		map.draw();
	}
}
