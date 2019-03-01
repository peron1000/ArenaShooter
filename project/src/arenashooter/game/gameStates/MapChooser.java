package arenashooter.game.gameStates;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Entity;
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


	@Override
	public void init() {
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");

		Text text = new Text(Main.font, Text.TextAlignH.CENTER, "Choose your map");
		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -500, -10), new Vec3f(450), text);
		textEnt.attachToParent(map, "Text_Select");

		selectMap(cho[init]);

		
	}

	@Override
	public void update(double delta) {
		// Temp sprite changing
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_UP)) {
			if (init < max - 1) {
				init++;
			}
			else{
				init=0;
			}

			selectMap(cho[init]);
			//sp.draw();//
		} else if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_DOWN)) {
			if (init > 0) {
				init--;
			}
			else {
				init=max-1;
			}
			selectMap(cho[init]);
		}

		if (Input.actionPressed(Device.KEYBOARD, Action.UI_OK)) {
			GameMaster.gm.requestNextState();
		}
		
		Entity thumbnail = getMap().children.get("Map_Thumbnail");
		if(thumbnail instanceof Sprite)
			((Sprite)thumbnail).size.set( Vec2f.lerp(((Sprite)thumbnail).size, new Vec2f(700, 700), 8d*delta) );

		map.step(delta);
	}
	
	private void selectMap(String newMap) {
		mapChosen = newMap;
		getMapChoosen();
		//String n = ;
		Sprite sp = new Sprite(new Vec2f(0, 0), "data/MAP_VIS/"+mapChosen+".png");
		sp.size = new Vec2f(500, 500);
		
		Entity oldSprite = getMap().children.get("Map_Thumbnail");
		if(oldSprite != null) oldSprite.destroy();
		
		sp.attachToParent(getMap(), "Map_Thumbnail");
		
		//Regenerate map name
		Text text2 = new Text(Main.font, Text.TextAlignH.CENTER, mapChosen);
		TextSpatial textEnt2 = new TextSpatial(new Vec3f(0, 450, -10), new Vec3f(450), text2);
		
		Entity oldText = getMap().children.get("Text_Mapname");
		if(oldText != null) oldText.destroy();
		
		textEnt2.attachToParent(getMap(), "Text_Mapname");
	}
}