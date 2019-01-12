package application.propertiestabs;

import application.Vec2Input;
import gamedata.GameMap;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MapProperties extends PropertiesTab {

	private GameMap map;
	
	public ColorPicker skyBot, skyTop;
	
	public MapProperties(GameMap map) {
		super();
		this.map = map;
		
		Vec2Input mapSize = new Vec2Input("World size", 1280, 720);
		
		skyTop = new ColorPicker();
		HBox skyTopContainer = new HBox();
		skyTopContainer.getChildren().add(new Label("Sky top color: "));
		skyTopContainer.getChildren().add(skyTop);
		
		skyBot = new ColorPicker();
		HBox skyBotContainer = new HBox();
		skyBotContainer.getChildren().add(new Label("Sky bottom color: "));
		skyBotContainer.getChildren().add(skyBot);
		
		getChildren().add(mapSize);
		getChildren().add(skyTopContainer);
		getChildren().add(skyBotContainer);
	}

	@Override
	public void update() {
		//TODO
	}

}
