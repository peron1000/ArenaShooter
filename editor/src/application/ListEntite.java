package application;

import java.util.HashMap;

import application.movableshapes.MovableRectangle;
import gamedata.entities.Entity;
import gamedata.entities.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import math.Vec2;

public class ListEntite {

	protected static double initX;
	protected static double initY;
	private static Rectangle character = new Rectangle(50, 120, Color.RED);
	public static Pane view = new Pane(character);
	static HashMap<Entity, Node> visuals = new HashMap<Entity, Node>();

	private ListEntite() {
	}

	/**
	 * Rectangle représentant la taille du collider d'un Character de SuperBlep
	 * 
	 * @return Un rectangle
	 */
	public static Rectangle getRecChar() {
		return character;
	}
	
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
	
	public static void newPlatform() {
		newPlatform(new Vec2());
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
}
