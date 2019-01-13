package arenashooter.game;

import java.util.ArrayList;

import arenashooter.engine.Device;
import arenashooter.engine.MapXMLTranslator;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Camera;
import arenashooter.entities.Controller;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;
import arenashooter.entities.Music;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Particles;
import arenashooter.entities.spatials.Spatial;

public class Game {
	public static Game game;

	private int nbPlayers = 2;
	public ArrayList<Controller> controllers = new ArrayList<>(1);
	public Map map;
	private Character[] players = new Character[nbPlayers];
	public Camera camera;

	public ArrayList<Entity> toDestroy = new ArrayList<Entity>();
	

	private Game() {
		map = MapXMLTranslator.getMap("data/mapXML/mapXML.xml");
		map.addWeapons();

		initControllers(nbPlayers);

		for (int i = 0; i < players.length; i++) {
			players[i] = controllers.get(i).getCharacter();
		}

		camera = new Camera(new Vec3f(0, 0, 450));
		camera.attachToParent(map, "camera");

		Particles p = new Particles(new Vec2f(0, -1000));
		p.attachToParent(map, "particles");

//		Music music = new Music("data/music/Juhani Junkala [Retro Game Music Pack] Level 1.ogg", true);
//		music.attachToParent(map, "music");
//		music.play();
	}

	private void initControllers(int nbPlayers) {

		// init KeyBoard as Controller1
		Controller c = new Controller(Device.KEYBOARD);
		controllers.add(0, c);
		Character character = new Character(map.spawn.get(0));
		c.setCharacter(character);
		character.attachToParent(map, "Player 1");

		// init other Controllers
		for (int i = 1; i < nbPlayers; i++) {
			c = new Controller(Device.values()[i - 1]);
			controllers.add(i, c);
			character = new Character(map.spawn.get(i));
			c.setCharacter(character);
			character.attachToParent(map, "Player " + (i + 1));
		}
	}

	public Map getMap() {
		return map;
	}

	public static void newGame() {
		game = new Game();
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
		((Spatial) map.children.get("particles")).position.x = (float) (300 * Math.sin(.003 * System.currentTimeMillis()));

		for (Controller controller : controllers) {
			controller.step(d);
		}
		map.step(d);

		//Destroy entities
		for(Entity en : toDestroy)
			en.detach();
		toDestroy.clear();
	}

	public void draw() {
		map.draw();
	}
}
