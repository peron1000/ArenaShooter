package arenashooter.game.gameStates;

import java.io.File;
import java.util.ArrayList;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Shader;
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
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;

public class MapChooser extends GameState {

	private String mapChosen = "mapXML";
//	private String[] cho = { "AAHH", "720TREE", "bat", "BUG", "empty", "mapclosecombat", "mapCloseSn","mapPOP","mapPop2","mapt1","MARZI","TENDEM","mapXML" };
	private ArrayList<String> maps = new ArrayList<>();
	private int init = 0;
//	private int max = cho.length;
	
	private double ringAngle = 0;
	private final double ringRadius = 2500;

	public String getMapChoosen() {
//		System.out.println(mapChosen);
		return "data/mapXML/" + mapChosen + ".xml";
	}


	@Override
	public void init() {
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		
		File mapFolder = new File("data/mapXML");
		File[] folderContent = mapFolder.listFiles();
		for(int i=0; i<folderContent.length; i++) {
			String name = folderContent[i].getPath();
			int index = name.lastIndexOf('/');
			if(index < 0)
				index = name.lastIndexOf('\\'); //Special case for Windows
			name = name.substring(index+1);
			if( !name.endsWith(".dtd") ) {
				name = name.substring(0, name.lastIndexOf('.'));
				maps.add(name);
			}
		}

		Text text = new Text(Main.font, Text.TextAlignH.CENTER, "Choose your map");
		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -500, -10), new Vec3f(450), text);
		textEnt.attachToParent(map, "Text_Select");

		selectMap(0);
		
		Shader shader = Shader.loadShader("data/shaders/sprite_simple");
		for(int i=0; i<maps.size(); i++) {
			Mesh m = Mesh.quad(new Vec3f(0), Quat.fromAngle(0), new Vec3f(1), shader, Texture.loadTexture("data/MAP_VIS/"+maps.get(i)+".png"));
			m.attachToParent(getMap(), "Map_Thumbnail_"+maps.get(i));
		}
		
	}

	@Override
	public void update(double delta) {
		// Temp sprite changing
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_RIGHT)) {
			if(init < maps.size()-1) {
				init++;
			}
			else {
				init=0;
			}

			selectMap(init);
		} else if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_LEFT)) {
			if (init > 0) {
				init--;
			}
			else {
				init = maps.size()-1;
			}
			selectMap(init);
		}

		if (Input.actionPressed(Device.KEYBOARD, Action.UI_OK)) {
			GameMaster.gm.requestNextState();
		}
		
		ringAngle = Utils.lerpAngle(ringAngle, Math.PI/2+(init*Utils.PI2/maps.size()), Math.min(1, 8d*delta));
		
		for(int i=0; i<maps.size(); i++) {
			Entity thumbnail = getMap().children.get("Map_Thumbnail_"+maps.get(i));
			if(thumbnail instanceof Mesh) {
				double angle = ringAngle-(i*Utils.PI2/maps.size());
				((Mesh)thumbnail).position = new Vec3f(ringRadius*Math.cos(angle), 0, ringRadius*Math.sin(angle)-ringRadius);
				
				if(i == init)
					((Mesh)thumbnail).scale.set( Vec3f.lerp(((Mesh)thumbnail).scale, new Vec3f(700), Math.min(1, 8d*delta)) );
				else
					((Mesh)thumbnail).scale.set( Vec3f.lerp(((Mesh)thumbnail).scale, new Vec3f(300), Math.min(1, 12d*delta)) );
			}
		}

		map.step(delta);
	}
	
	private void selectMap(int newMap) {
		mapChosen = maps.get(newMap);
		
		//Regenerate map name
		Text text2 = new Text(Main.font, Text.TextAlignH.CENTER, mapChosen);
		TextSpatial textEnt2 = new TextSpatial(new Vec3f(0, 450, -10), new Vec3f(450), text2);
		
		Entity oldText = getMap().children.get("Text_Mapname");
		if(oldText != null) oldText.destroy();
		
		textEnt2.attachToParent(getMap(), "Text_Mapname");
	}
}