package gamedata;

import java.util.ArrayList;
import java.util.HashMap;

import application.ListEntite;
import application.Main;
import application.Vec2Input;
import application.customevents.CustomEvent;
import application.customevents.CustomEventHandler;
import application.movableshapes.MovableRectangleSpawn;
import application.propertiestabs.MapProperties;
import gamedata.entities.Entity;
import math.Vec2;

public class GameMap {
	
	public Vec2 gravity = new Vec2(0, 9.807);
	
	public HashMap<String, Entity> children = new HashMap<String, Entity>();

	public static final int MIN_SPAWNS = 10;
	public ArrayList<Vec2> spawns = new ArrayList<>(MIN_SPAWNS);
	
	public MapProperties propertiesTab;
	
	public GameMap() {
		propertiesTab = new MapProperties(this);
	}
	
	public void addSpawn(Vec2 spawn) {
		spawns.add(spawn);
		
		Vec2Input input = new Vec2Input(String.valueOf(spawns.size()), spawn.x, spawn.y);
		
		MovableRectangleSpawn visual = new MovableRectangleSpawn(spawn, input);
		ListEntite.view.getChildren().add(visual);
		
		input.addEventHandler(CustomEvent.CUSTOM_EVENT_TYPE, new CustomEventHandler() {
			@Override
			public void onEventVec2Change(double newX, double newY) {
				spawn.x = newX;
				spawn.y = newY;
				visual.setX(newX-visual.getWidth()/2);
				visual.setY(newY-visual.getHeight()/2);
			}
		});
		
		Main.map.propertiesTab.spawnsList.getItems().add(input);
	}

}
