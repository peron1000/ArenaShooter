package arenashooter.game;

import java.util.ArrayList;
import java.util.Iterator;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Controller;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Character;
import arenashooter.game.gameStates.GameState;

public class Game extends GameState {
	private int nbPlayers = GameMaster.gm.controllers.size();
	private ArrayList<Character> players = new ArrayList<>(nbPlayers);
	private Iterator<Controller> iterator = GameMaster.gm.controllers.iterator();

	public Game() { }
	
	@Override
	public void init() {
		
		long time = System.currentTimeMillis();
		
		while(iterator.hasNext()) {
			Controller controller = iterator.next();
			Character character = controller.createNewCharacter(map.GetRandomRespawn());
			players.add(character);
			character.attachToParent(map, character.genName());
		}
		
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		Window.camera = new Camera(new Vec3f(0, 0, 450));
		Window.camera.attachToParent(map, "camera");
	}

	@Override
	public void update(double d) {
		if (Window.camera != null) {
			Window.camera.center(players, null, d);
//			camera.center(players, map.cameraBounds, d); //TODO: Fix camera bounds and uncomment this
			Audio.setListener(Window.camera.position, Window.camera.rotation);
		} else
			Audio.setListener(new Vec3f(), Quat.fromAngle(0));

		//Update controllers
		for(Controller controller : GameMaster.gm.controllers)
			controller.step(d);
			
		map.step(d);
	}
}
