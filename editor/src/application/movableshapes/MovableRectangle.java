package application.movableshapes;

import application.Affichage;
import application.ListEntite;
import gamedata.entities.Spatial;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import math.Vec2;

public class MovableRectangle extends Rectangle {
	
	protected static double initX;
	protected static double initY;
	protected static Point2D dragAnchor;
	
	public Spatial entity;

	public MovableRectangle(Spatial e, Vec2 size, Paint color) {
		super(size.x, size.y, color);
		entity = e;
		setX( entity.position.x-size.x/2 );
		setY( entity.position.y-size.y/2 );
		
		setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				if(!me.isPrimaryButtonDown()) return; //Not left click
				
				// Pour garder le point de depart en memoire
				initX = getX();
				initY = getY();
				dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
				
				//Select clicked entity
				Affichage.selectEntity(entity);
			}
		});
		setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				if(!me.isPrimaryButtonDown()) return; //Not left click
				
				//Calcule position apres le drag
				double dragX = me.getSceneX() - dragAnchor.getX();
				double dragY = me.getSceneY() - dragAnchor.getY();
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
				entity.position = new Vec2(getX()+getWidth()/2, getY()+getHeight()/2);
			}
		});
	}

	/*
	 * Resize this rectangle and move it to entity position
	 */
	public void resize(Vec2 newSize) {
		setWidth(newSize.x);
		setHeight(newSize.y);

		setX( entity.position.x-newSize.x/2 );
		setY( entity.position.y-newSize.y/2 );
	}

}
