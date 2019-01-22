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
import arenashooter.entities.spatials.CharacterInfo;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.LoadingFloor;
import arenashooter.game.GameMaster;

public class Loading extends GameState {
	public static final Loading loading = new Loading();
	
	private static boolean first = true;
	
	private GameState next;
	
	private Loading() { }
	
	public void init() {
		
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_loading");
		
		ArrayList<Entity> entities = new ArrayList<>();
		
		Window.camera = new Camera(new Vec3f(0, 0, 600));
		entities.add(Window.camera);
		
		float startX = -(GameMaster.gm.controllers.size()*128f)/2f;
		int i=0;
		for(CharacterInfo c : GameMaster.gm.controllers.values()) {
			float x = startX+(128f*i);
			
			entities.add( new CharacterSprite(new Vec2f(x, -30), c) );
			entities.add( new LoadingFloor(new Vec2f(x, 80)) );
			i++;
		}
		if(GameMaster.gm.controllers.size() == 0) {
			float x = startX+(128f*i);
			entities.add( new CharacterSprite(new Vec2f(x, -30), new CharacterInfo()) );
			entities.add( new LoadingFloor(new Vec2f(x, 80)) );
		}
		
//		entities.add(camera);
//		entities.add(new Sky(new Vec3f(255), new Vec3f(126,255,255)));
		
		map = new Map(entities);
		
	}
	
	
	/**
	 * Set loading target
	 * @param next
	 * @param mapName
	 */
	public void setNextState(GameState next , String mapName) {
		this.next = next;
		MapXmlReader.mapReader.setMapToRead(mapName);
	}
	
	public GameState getNextState() {
		return next;
	}
	
	private void createNewMap() {
		Map m = new Map(MapXmlReader.mapReader.getEntities());
		m.spawn = MapXmlReader.mapReader.getSpawn();
		m.cameraBounds = MapXmlReader.mapReader.getCameraBounds();
		m.gravity = MapXmlReader.mapReader.getGravity();
		m.init();
		next.map = m;
	}

	@Override
	public void update(double delta) {
		if(first) {
			init();
			next = new CharacterChooser();
			MapXmlReader.mapReader.setMapToRead("data/mapXML/mapXML2.xml");
			first = false;
		}
		
		map.step(delta);
		
		//Load next / TODO : to update
		boolean entitiesLoaded = MapXmlReader.mapReader.loadNextEntity();
		boolean informationsloaded = MapXmlReader.mapReader.loadNextInformation();
		
		if(entitiesLoaded && informationsloaded) {
			createNewMap();
			GameMaster.gm.requestNextState();
		}
		
	}

	@Override
	public void draw() {
		map.draw();
	}
}
