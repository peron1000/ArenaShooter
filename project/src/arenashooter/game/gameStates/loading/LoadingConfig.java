package arenashooter.game.gameStates.loading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arenashooter.engine.ContentManager;
import arenashooter.engine.FileUtils;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.ui.Imageinput;
import arenashooter.game.Main;

public class LoadingConfig extends Thread {
	
	private Map<String, Texture> load = new HashMap<>();
	
	public synchronized Set<String> getFile() {
		return load.keySet();
	}
	
	public synchronized Texture getTexture(String file) {
		return load.getOrDefault(file, Main.getRenderer().getDefaultTexture());
	}
	
	@Override
	public void run() {
		Main.log.info("Discovering arenas...");
		
		List<String> folderContent = ContentManager.listRes("data/arena");
		
		for (String file : folderContent) {
			
			if (FileUtils.getName(file).endsWith(".arena") && !FileUtils.getName(file).startsWith("menu_")) {
				String texture = file.substring(0, file.lastIndexOf('.')) + ".png";
				if(!ContentManager.resExists(texture))
					texture = "data/arena_thumbnail_default.png";
				load.put(file, Main.getRenderer().loadTexture(texture));
			}
		}
		
		Main.log.info(load.size()+" arenas found.");
		
		Main.getRenderer().loadTexture("data/sprites/interface/Fond Menu.png");
		Main.getRenderer().loadTexture("data/sprites/interface/Selector_1.png");
		
		for (Imageinput image : Imageinput.values()) {
			image.getImage();
		}
	}
}
