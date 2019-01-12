package application.propertiestabs;

import application.Main;
import application.Vec2Input;
import application.customevents.CustomEvent;
import application.customevents.CustomEventHandler;
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
		
		Vec2Input mapGravity = new Vec2Input("Gravity", this.map.gravity.x, this.map.gravity.y);
		mapGravity.addEventHandler(CustomEvent.CUSTOM_EVENT_TYPE, new CustomEventHandler() {

			@Override
			public void onEventVec2Change(double newX, double newY) {
				Main.map.gravity.x = newX;
				Main.map.gravity.y = newY;
			}
		});
		
		skyTop = new ColorPicker();
		HBox skyTopContainer = new HBox();
		skyTopContainer.getChildren().add(new Label("Sky top color"));
		skyTopContainer.getChildren().add(skyTop);
		
		skyBot = new ColorPicker();
		HBox skyBotContainer = new HBox();
		skyBotContainer.getChildren().add(new Label("Sky bottom color"));
		skyBotContainer.getChildren().add(skyBot);
		
		getChildren().add(mapGravity);
		getChildren().add(skyTopContainer);
		getChildren().add(skyBotContainer);
	}

	@Override
	public void update() {
		//TODO
	}

}
