package arenashooter.game.gameStates.loading;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.ui.Imageinput;

public class LoadingConfig extends Thread {
	
	private Map<File, Texture> load = new HashMap<>();
	
	public synchronized Set<File> getFile() {
		return load.keySet();
	}
	
	public synchronized Texture getTexture(File file) {
		return load.getOrDefault(file, Texture.default_tex);
	}
	
	@Override
	public void run() {
		File mapFolder = new File("data/mapXML");
		File[] folderContent = mapFolder.listFiles();
		for (File file : folderContent) {
			if (file.getName().endsWith(".xml") && !file.getName().startsWith("menu_")) {
				String texture = file.getPath().substring(0, file.getPath().lastIndexOf('.')) + ".png";
				if(!new File(texture).exists())
					texture = "data/arena_thumbnail_default.png";
				load.put(file, Texture.loadTexture(texture));
			}
		}
		
		Texture.loadTexture("data/sprites/interface/Fond Menu.png");
		Texture.loadTexture("data/sprites/interface/Selector_1.png");
		
		for (Imageinput image : Imageinput.values()) {
			image.getImage();
		}
	}
}
