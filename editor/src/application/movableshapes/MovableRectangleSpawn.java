package application.movableshapes;

import application.Affichage;
import application.ListEntite;
import application.Main;
import application.Vec2Input;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import math.Vec2;

public class MovableRectangleSpawn extends Rectangle {
	
	protected static double initX;
	protected static double initY;
	protected static Point2D dragAnchor;
	
	public Vec2 position;

	public MovableRectangleSpawn(Vec2 position, Vec2Input input) {
		super(50, 120, Color.GREENYELLOW);
		setX(position.x);
		setY(position.y);
		setOnMousePressed(new EventHandler<MouseEvent>() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(MouseEvent me) {
				if(!me.isPrimaryButtonDown()) return; //Not left click
				
				// Pour garder le point de depart en memoire
				initX = getX();
				initY = getY();
				dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
				
				//Select clicked entity
				Affichage.selectEntity(null);
				
				Main.map.propertiesTab.spawns.setExpanded(true);
				((ListView<Vec2Input>)Main.map.propertiesTab.spawns.getContent()).getSelectionModel().select(input);
			}
		});
		setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				if(!me.isPrimaryButtonDown()) return; //Not left click
				
				//Calcule position apres le drag
				double dragX = (me.getSceneX() - dragAnchor.getX())/Affichage.getZoom();
				double dragY = (me.getSceneY() - dragAnchor.getY())/Affichage.getZoom();
				double newXPosition = initX + dragX;
				double newYPosition = initY + dragY;
				
				//Restriction de fenetre
				setX(  Math.min(Math.max(0, newXPosition), ListEntite.view.getWidth()-2) );
				setY(  Math.min(Math.max(0, newYPosition), ListEntite.view.getHeight()-2) );
				
				//Snap position to grid
				int gridSnap = Affichage.gridSnap.getValue();
				if(gridSnap > 0) {
					setX( Math.floor(getX()/gridSnap)*gridSnap );
					setY( Math.floor(getY()/gridSnap)*gridSnap );
				}
				
				//On change la position de l'entité liée
				position.x = getX();
				position.y = getY();
				input.setValX(getX());
				input.setValY(getY());
			}
		});
	}

}
