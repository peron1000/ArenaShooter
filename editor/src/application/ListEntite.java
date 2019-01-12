package application;

import java.util.HashMap;

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
	static Pane pane = new Pane(character);
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
	
	/**
	 * Create a platform and its movable rectangle in the scene view
	 */
	public static void newPlateforme() {
		Platform entity = new Platform(new Vec2(), 0, new Vec2(150, 10));
		entity.name = "Platform_"+String.valueOf(System.currentTimeMillis());
		entity.createProperties();
		Main.map.children.put(entity.name, entity);
		Affichage.sceneTree.addEntity(entity);
		Affichage.selectEntity(entity);
		
		//Create movable rectangle attached to the platform
		Node visual = new MovableRectangle(entity, new Vec2(entity.extent.x*2, entity.extent.y*2), Color.YELLOW);
		visuals.put(entity, visual);
		pane.getChildren().add(visual);
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
