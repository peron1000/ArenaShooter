package arenashooter.game.gameStates;

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
import arenashooter.game.GameMaster;

public class Game extends GameState {
	private int nbPlayers = GameMaster.gm.controllers.size();
	private ArrayList<Character> players = new ArrayList<>(nbPlayers);
	private Iterator<Controller> iterator = GameMaster.gm.controllers.iterator();

	public Game() {
	}

	@Override
	public void init() {

		while (iterator.hasNext()) {
			Controller controller = iterator.next();
			Character character = controller.createNewCharacter(map.GetRandomRespawn());
			players.add(character);
			character.attachToParent(map, character.genName());
		}

		//Camera
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		Camera cam = new Camera(new Vec3f(0, 0, 450));
		cam.attachToParent(map, "camera");
		Window.setCamera(cam);
	}

	@Override
	public void update(double d) {
		super.update(d);
		
		if(menu != null) return;
		
		if (Window.getCamera() != null) {
			Window.getCamera().center(players, null, d);
//			Window.getCamera().center(players, map.cameraBounds, d); //TODO: Fix camera bounds and uncomment this
			Audio.setListener(Window.getCamera().pos(), Window.getCamera().rotation);
		} else
			Audio.setListener(new Vec3f(), Quat.fromAngle(0));

		// Update controllers
		for (Controller controller : GameMaster.gm.controllers)
			controller.step(d);

		map.step(d);

	}
}
