package arenashooter.entities;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class Game {
	public static Game game;
	
	private Map map;
	private Character[] players = new Character[2];
	public Camera camera;
	
	private Game() {
		map = new Map(2);
		
		players[0] = map.players.get(0);
		players[1] = map.players.get(1);
		
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
	
	public Map getMap() {
		return map;
	}
	
	public static void newGame() {
		game = new Game();
	}
	
	public void update(double d) {
		if( camera != null ) {
			camera.center(players, d);
			Audio.setListener(camera.position, camera.rotation);
		} else
			Audio.setListener( new Vec3f(), Quat.fromAngle(0) );
		
		//TODO: remove temp particle system and sound movement
		((Spatial)map.children.get("particles")).position.x = (float) (300*Math.sin(.003*System.currentTimeMillis()));
		((Spatial)map.children.get("testSound")).position = ((Spatial)map.children.get("particles")).position;
//		((SoundEffect)map.children.get("testSound")).play();
		
		map.step(d);
	}
	
	public void draw() {
		map.draw();
	}
}
