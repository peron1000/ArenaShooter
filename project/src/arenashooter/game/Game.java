package arenashooter.game;

import java.util.ArrayList;

import arenashooter.engine.audio.Audio;
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
	public static Camera camera = new Camera(new Vec3f(0, 0, 450));

	public Game() {


//		Music music = new Music("data/music/Juhani Junkala [Retro Game Music Pack] Level 1.ogg", true);
//		music.attachToParent(map, "music");
//		music.play();
	}
	
	public void init() {
		map.addWeapons();
		
		for (Controller controller : GameMaster.gm.controllers.keySet()) {
			Character character = controller.createNewCharacter(map.GetRandomRespawn());
			players.add(character);
			character.attachToParent(map, character.genName());
		}
		
		camera.attachToParent(map, "camera");

		Particles p = new Particles(new Vec2f(0, -1000), "data/particles/test.xml");
		p.attachToParent(map, "particles");
	}

	public void update(double d) {
		if (camera != null) {
			camera.center(players, null, d);
			// camera.center(players, map.cameraBounds, d); //TODO: Fix camera bounds and
			// uncomment this
			Audio.setListener(camera.position, camera.rotation);
		} else
			Audio.setListener(new Vec3f(), Quat.fromAngle(0));

		// TODO: remove temp particle system
		((Spatial) map.children.get("particles")).position.x = (float) (300
				* Math.sin(.003 * System.currentTimeMillis()));

		for (Controller controller : GameMaster.gm.controllers.keySet()) {
			controller.step(d);
		}
		map.step(d);

	}

	public void draw() {
		map.draw();
	}
}
