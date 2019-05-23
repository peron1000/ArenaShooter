package arenashooter.game.gameStates;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Controller;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;

public class MapChooser extends GameState {

	private String mapChosen = "mapXML";
	private ArrayList<String> maps = new ArrayList<>();
	private int init = 0;

	private double ringAngle = 0;
	private final double ringRadius = 50;

	public MapChooser() {
		super(1);
	}

	public String getMapChosen() {
		return "data/mapXML/" + mapChosen + ".xml";
	}

	@Override
	public void init() {
		super.init();

		File mapFolder = new File("data/mapXML");
		File[] folderContent = mapFolder.listFiles();
		for (int i = 0; i < folderContent.length; i++) {
			String name = folderContent[i].getPath();
			int index = name.lastIndexOf('/');
			if (index < 0)
				index = name.lastIndexOf('\\'); // Special case for Windows
			name = name.substring(index + 1);
			if (!name.endsWith(".dtd")) {
				name = name.substring(0, name.lastIndexOf('.'));
				maps.add(name);
			}
		}

		// Sort maps alphabetically
		Collections.sort(maps);

		// Set FOV
		Camera cam = new Camera(new Vec3f(0, 0, 8));
		cam.setFOV(90);
		current.attachToParent(cam, "camera");
		Window.setCamera(cam);

		Text text = new Text(Main.font, Text.TextAlignH.CENTER, "Choose your map");
		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -7, 0), new Vec3f(7.3f), text);
		textEnt.attachToParent(current, "Text_Select");

		Text text2 = new Text(Main.font, Text.TextAlignH.CENTER, "Q or D to change the map");
		TextSpatial textEnt2 = new TextSpatial(new Vec3f(0, -5.6, 0), new Vec3f(4.25f), text2);
		textEnt2.attachToParent(current, "Text_touch");

		Text text3 = new Text(Main.font, Text.TextAlignH.CENTER, "Press ENTER to continue");
		TextSpatial textEnt3 = new TextSpatial(new Vec3f(0, 5.65, 0), new Vec3f(7.15f), text3);
		textEnt3.attachToParent(current, "Text_Choice");

		selectMap(0);

		for (int i = 0; i < maps.size(); i++) {
			Material mat = new Material("data/shaders/sprite_simple");
			mat.setParamTex("baseColor", Texture.loadTexture("data/MAP_VIS/" + maps.get(i) + ".png"));
			Mesh m = Mesh.quad(new Vec3f(0), Quat.fromAngle(0), new Vec3f(1), mat);
			m.attachToParent(getMap(), "Map_Thumbnail_" + maps.get(i));
		}

		// params
		// createTextGameMode(gameModeString+gameParam.getMode().name());
		// gameMode.attachToParent(map, gameMode.genName());
	}

	// private void createTextGameMode(String str) {
	// gameMode = new TextSpatial(new Vec3f(750, -570, 0), new Vec3f(250), new
	// Text(Main.font, TextAlignH.CENTER, str));
	// gameMode.attachToParent(map, gameMode.genName());
	// }

	@Override
	public void update(double delta) {
		// // Refresh gameMode
		// if(Input.actionJustPressed(Device.KEYBOARD, Action.UI_NathanTest)) {
		// gameParam.setMode(gameParam.getMode().getNext());
		// }
		// if(gameParam.isChangedMode()) {
		// gameMode.detach();
		// createTextGameMode(gameModeString+gameParam.getMode());
		// }

		// Temp sprite changing
		for (Controller controller : GameMaster.gm.controllers) {
			if (Input.actionJustPressed(controller.getDevice(), Action.UI_RIGHT)) {
				if (init < maps.size() - 1) {
					init++;
				} else {
					init = 0;
				}

				selectMap(init);
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_LEFT)) {
				if (init > 0) {
					init--;
				} else {
					init = maps.size() - 1;
				}
				selectMap(init);
			}

			if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {

				GameMaster.gm.requestNextState(new Game(1), getMapChosen());
			} else if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_BACK)) {
				GameMaster.gm.requestPreviousState();
			}
		}

		ringAngle = Utils.lerpAngle(ringAngle, Math.PI / 2 + (init * Utils.PI2 / maps.size()), Math.min(1, 8d * delta));

		for (int i = 0; i < maps.size(); i++) {
			Entity thumbnail = getMap().getChild("Map_Thumbnail_" + maps.get(i));
			if (thumbnail instanceof Mesh) {
				double angle = ringAngle - (i * Utils.PI2 / maps.size());
				((Mesh) thumbnail).parentPosition = new Vec3f(ringRadius * Math.cos(angle), 0,
						ringRadius * Math.sin(angle) - ringRadius);

				if (i == init)
					((Mesh) thumbnail).scale
							.set(Vec3f.lerp(((Mesh) thumbnail).scale, new Vec3f(9f), Math.min(1, 8d * delta)));
				else
					((Mesh) thumbnail).scale
							.set(Vec3f.lerp(((Mesh) thumbnail).scale, new Vec3f(3), Math.min(1, 12d * delta)));
			}
		}

		super.update(delta);
	}

	private void selectMap(int newMap) {
		mapChosen = maps.get(newMap);

		// Regenerate map name
		Text text4 = new Text(Main.font, Text.TextAlignH.CENTER, mapChosen);
		TextSpatial textEnt4 = new TextSpatial(new Vec3f(0, 4.50, -.10), new Vec3f(7.15f), text4);

		Entity oldText = getMap().getChild("Text_Mapname");
		if (oldText != null)
			oldText.detach();

		textEnt4.attachToParent(getMap(), "Text_Mapname");
	}
}