package arenashooter.game;

import java.util.ArrayList;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Camera;
import arenashooter.entities.Controller;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Particles;
import arenashooter.entities.spatials.Spatial;
import arenashooter.game.gameStates.GameState;

public class Game extends GameState {
	private int nbPlayers = GameMaster.gm.controllers.size();
	private ArrayList<Character> players = new ArrayList<>(nbPlayers);

	public Game() { }
	
	@Override
	public void init() {
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		
		for(Controller controller : GameMaster.gm.controllers) {
			Character character = controller.createNewCharacter(map.GetRandomRespawn());
			players.add(character);
			character.attachToParent(map, character.genName());
		}
		
		Window.camera = new Camera(new Vec3f(0, 0, 450));
		Window.camera.attachToParent(map, "camera");

		Particles p = new Particles(new Vec2f(0, -1000), "data/particles/test.xml");
		p.attachToParent(map, "particles");
	}

	@Override
	public void update(double d) {
		if (Window.camera != null) {
			Window.camera.center(players, null, d);
//			camera.center(players, map.cameraBounds, d); //TODO: Fix camera bounds and uncomment this
			Audio.setListener(Window.camera.position, Window.camera.rotation);
		} else
			Audio.setListener(new Vec3f(), Quat.fromAngle(0));

		//TODO: remove temp particle system
		((Spatial) map.children.get("particles")).position.x = (float) (300 * Math.sin(.003 * System.currentTimeMillis()));

		//Update controllers
		for(Controller controller : GameMaster.gm.controllers)
			controller.step(d);
			
		map.step(d);
	}
}
