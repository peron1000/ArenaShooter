package arenashooter.game.gameStates;

import java.util.Collection;
import java.util.HashMap;
import org.lwjgl.system.linux.X11;
import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Controller;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;

public class MapChooser extends GameState {

	private String mapChosen = "mapXML";
	private String[] cho = { "AAHH", "720TREE", "bat", "BUG", "empty", "mapclosecombat", "mapCloseSn","mapPOP","mapPop2","mapt1","MARZI","TENDEM","mapXML" };
	private int init = 0;
	private int max = cho.length;

	public String getMapChoosen() {
		System.out.println(mapChosen);
		return "data/mapXML/" + mapChosen + ".xml";
	}

	private HashMap<Device, Controller> controllers = new HashMap<>(1);
	private HashMap<Controller, CharacterSprite> sprites = new HashMap<>(1);

	private int i = -300;

	public Collection<Controller> getControllers() {
		return controllers.values();
	}

	@Override
	public void init() {
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");

		Text text = new Text(Main.font, Text.TextAlignH.CENTER, "Choose your map");
		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -500, -10), new Vec3f(450), text);
		textEnt.attachToParent(map, "Text_Select");

		Controller controllerKeyboard = new Controller(Device.KEYBOARD);
		controllers.put(Device.KEYBOARD, controllerKeyboard);
		i += 150;
	}

	@Override
	public void update(double delta) {
		for (Device device : Device.values()) {
			if (Input.actionPressed(device, Action.UI_OK) && !controllers.keySet().contains(device)) {

			}
			// TODO : remove controller when UI_BACK is pressed
		}

		// Update controllers
		for (Controller controller : controllers.values()) {
			controller.step(delta);

			// Temp sprite changing
			if (Input.actionJustPressed(controller.getDevice(), Action.UI_UP)) {
				if (init < max - 1) {
					init++;
				}
				else{
					init=0;
				}
				mapChosen = cho[init];
				getMapChoosen();
				//String n = ;
				Sprite sp = new Sprite(new Vec2f(0, 0), "data/MAP_VIS/TREE.png");
				sp.attachToParent(getMap(), mapChosen);
				//sp.draw();//
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_DOWN)) {
				if (init > 0) {
					init--;
				}
				else {
					init=max-1;
				}
				mapChosen = cho[init];
				getMapChoosen();
			}
		}

		if (Input.actionPressed(Device.KEYBOARD, Action.UI_OK)) {
			GameMaster.gm.requestNextState();
		}

		map.step(delta);
	}
}