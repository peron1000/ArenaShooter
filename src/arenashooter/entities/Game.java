package arenashooter.entities;

import java.util.ArrayList;

import arenashooter.engine.Device;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class Game {
	public static Game game;

	private int nbPlayers = 2;
	public ArrayList<Controller> controlers = new ArrayList<>(1);
	private Map map;
	private Character[] players = new Character[nbPlayers];
	public Camera camera;
	
	private Game() {
		map = new Map(nbPlayers);
		
		initControllers(nbPlayers);
		
		for (int i = 0; i < players.length; i++) {
			players[i] = controlers.get(i).getCharacter();
		}
		
		camera = new Camera( new Vec3f(0, 0, 450) );
		camera.attachToParent(map, "camera");
		
		Particles p = new Particles(new Vec2f(0, -1000));
		p.attachToParent(map, "particles");
		
		SoundEffect testSound = new SoundEffect( new Vec2f(0, 0), "data/sound/jump.ogg" );
		testSound.setVolume(.45f);
		testSound.attachToParent(map, "testSound");
		
		Music music = new Music("data/music/Juhani Junkala [Retro Game Music Pack] Level 1.ogg", true);
		music.attachToParent(map, "music");
//		music.play();
	}
	
	private void initControllers(int nbPlayers) {
		
		// init KeyBoard as Controller1
		Controller c = new Controller(Device.KEYBOARD);
		controlers.add(0, c);
		Character character = new Character(map.spawn.get(0));
		c.setCharacter(character);
		character.attachToParent(map, "Player 1");
		
		// init other Controllers
		for (int i = 1; i < nbPlayers; i++) {
			c = new Controller(Device.values()[i-1]);
			controlers.add(i, c);
			character = new Character(map.spawn.get(i));
			c.setCharacter(character);
			character.attachToParent(map, "Player "+ (i+1));
		}
	}

	public Map getMap() {
		return map;
	}
	
	public static void newGame() {
		game = new Game();
	}
	
	public void update(double d) {
		if( camera != null ) {
			camera.center(players, null, d);
//			camera.center(players, map.cameraBounds, d); //TODO: Fix camera bounds and uncomment this
			Audio.setListener(camera.position, camera.rotation);
		} else
			Audio.setListener( new Vec3f(), Quat.fromAngle(0) );
		
		//TODO: remove temp particle system and sound movement
		((Spatial)map.children.get("particles")).position.x = (float) (300*Math.sin(.003*System.currentTimeMillis()));
		((Spatial)map.children.get("testSound")).position = ((Spatial)map.children.get("particles")).position;
//		((SoundEffect)map.children.get("testSound")).play();
		
		for (Controller controller : controlers) {
			controller.step(d);
		}
		map.step(d);
	}
	
	public void draw() {
		map.draw();
	}
}
