package arenashooter.game.gameStates;

import java.util.ArrayList;
import java.util.Iterator;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Controller;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;

public class Game extends GameState {
	private int nbPlayers = GameMaster.gm.controllers.size();
	private ArrayList<Character> players = new ArrayList<>(nbPlayers);
	private Iterator<Controller> iterator = GameMaster.gm.controllers.iterator();
	private boolean pause = false;

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
		if (Window.getCamera() != null) {
			Window.getCamera().center(players, null, d);
//			Window.getCamera().center(players, map.cameraBounds, d); //TODO: Fix camera bounds and uncomment this
			Audio.setListener(Window.getCamera().pos(), Window.getCamera().rotation);
		} else
			Audio.setListener(new Vec3f(), Quat.fromAngle(0));

		// Update controllers
		for (Controller controller : GameMaster.gm.controllers)
			controller.step(d);

		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_BACK)) {
			pause = !pause;
			if(!pause)
				Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
			else
				Window.postProcess = new PostProcess("data/shaders/post_process/pp_pause");
		}
		if (!pause) {
			if (map.children.get("Text_Pause") != null) {
				map.children.get("Text_Pause").destroy();
			}
			map.step(d);
		} else {
			Text text = new Text(Main.font, Text.TextAlignH.CENTER, "PAUSE");
			
			TextSpatial textEnt = new TextSpatial(Window.getCamera().pos(), new Vec3f(450), text);
			textEnt.position.z = 0;
			textEnt.localPosition.z = 0;
			textEnt.zIndex = 9999;
			textEnt.attachToParent(map, "Text_Pause");
			textEnt.color=new Vec4f(1,0,0.75,1);
		}

	}
}