package application;

import java.util.HashMap;

import application.movableshapes.MovableRectangle;
import gamedata.entities.Entity;
import gamedata.entities.Platform;
import gamedata.entities.Spatial;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import math.Vec2;

public class ListEntite {

	protected static double initX;
	protected static double initY;
	public static Pane view = new Pane();
	static HashMap<Entity, Node> visuals = new HashMap<Entity, Node>();

	private ListEntite() {}
	
	public static Node getVisual(Entity e) {
		return visuals.get(e);
	}
	
	/**
	 * Create a platform and its movable rectangle in the scene view
	 */
	public static void newPlatform(Vec2 loc) {
		Vec2 extent = new Vec2(150, 10);
		
		//Snap position to grid
		int gridSnap = Affichage.gridSnap.getValue();
		if(gridSnap > 0) {
			loc.x = extent.x+( Math.floor((loc.x-extent.x)/gridSnap)*gridSnap );
			loc.y = extent.y+( Math.floor((loc.y-extent.y)/gridSnap)*gridSnap );
		}
		
		Platform entity = new Platform(loc, 0, extent);
		entity.name = "Platform_"+String.valueOf(System.currentTimeMillis());
		entity.createProperties();
		Main.map.children.put(entity.name, entity);
		Affichage.sceneTree.addEntity(entity);
		Affichage.selectEntity(entity);
		
		//Create movable rectangle attached to the platform
		Node visual = new MovableRectangle(entity, new Vec2(extent.x*2, extent.y*2), Color.YELLOW);
		visuals.put(entity, visual);
		view.getChildren().add(visual);
	}
	
	/**
	 * Create a spatial and its movable rectangle in the scene view
	 */
	public static void newSpatial(Vec2 loc) {
		Vec2 extent = new Vec2(10, 10);
		
		//Snap position to grid
		int gridSnap = Affichage.gridSnap.getValue();
		if(gridSnap > 0) {
			loc.x = extent.x+( Math.floor((loc.x-extent.x)/gridSnap)*gridSnap );
			loc.y = extent.y+( Math.floor((loc.y-extent.y)/gridSnap)*gridSnap );
		}
		
		Spatial entity = new Spatial(loc, 0);
		entity.name = "Spatial_"+String.valueOf(System.currentTimeMillis());
		entity.createProperties();
		Main.map.children.put(entity.name, entity);
		Affichage.sceneTree.addEntity(entity);
		Affichage.selectEntity(entity);
		
		//Create movable rectangle attached to the platform
		Node visual = new MovableRectangle(entity, new Vec2(extent.x*2, extent.y*2), Color.RED);
		visuals.put(entity, visual);
		view.getChildren().add(visual);
	}
	
	/**
	 * Create an entity
	 */
	public static void newEntity() {
		Entity entity = new Entity();
		entity.name = "Entity_"+String.valueOf(System.currentTimeMillis());
		entity.createProperties();
		Main.map.children.put(entity.name, entity);
		Affichage.sceneTree.addEntity(entity);
		Affichage.selectEntity(entity);
	}
	
	/**
	 * Destroy an entity
	 * @param e
	 */
	public static void destroyEntity(Entity e) {
		if(e.treeItem != null && e.treeItem.getParent() != null)
			e.treeItem.getParent().getChildren().remove(e.treeItem);

		view.getChildren().remove(visuals.get(e));
		visuals.remove(e);

		e.parent.children.remove(e.name);
	}
}
