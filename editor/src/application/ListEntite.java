package application;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ListEntite {

	private static HashMap<Rectangle, Entite> entites = new HashMap<>();
	protected static double initX;
	protected static double initY;
	private static double scale = 5;
	protected static Point2D dragAnchor;
	private static Rectangle character = new Rectangle(50, 120, Color.RED);
	static Pane pane = new Pane(character);

	private ListEntite() {
	}

	/**
<<<<<<< HEAD
	 * Rectangle représentant la taille du collider d'un Character de SuperBlep
=======
	 * Rectangle repr�sentant la taille du collider d'un Character de SuperBlep
>>>>>>> branch 'master' of https://github.com/peron1000/ArenaShooter.git
	 * 
	 * @return Un rectangle
	 */
	public static Rectangle getRecChar() {
		return character;
	}
	
	public static HashMap<Rectangle, Entite> getHashMapEntites(){
		return entites;
	}

	/**
<<<<<<< HEAD
	 * Crée un rectangle bougeable avec la souris et une entite qui lui est associé
=======
	 * Cr�e un rectangle bougeable avec la souris et une entite qui lui est associ�
>>>>>>> branch 'master' of https://github.com/peron1000/ArenaShooter.git
	 * dans la map d'entite
	 */
	public static void newPlateforme() {
		Rectangle nw = newRectangleSuitSouris(300, 20, Color.YELLOW);
		pane.getChildren().add(nw);
		Entite e = new Entite(Entite.Type.Plateforme, 0, 0, nw.getWidth() / 2, nw.getHeight() / 2);
		entites.put(nw, e);
	}

	/**
<<<<<<< HEAD
	 * Crée un rectangle qui suit la souris lors d'un click and drag
=======
	 * Cr�e un rectangle qui suit la souris lors d'un click and drag
>>>>>>> branch 'master' of https://github.com/peron1000/ArenaShooter.git
	 * @param x longueur
	 * @param y hauteur
	 * @param c couleur
	 * @return Un rectangle
	 */
	private static Rectangle newRectangleSuitSouris(double x, double y, Paint c) {
		Rectangle rec = new Rectangle(x, y, c);
		rec.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				// Pour garder le point de depart en memoire
				initX = rec.getX();
				initY = rec.getY();
				dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
			}
		});
		rec.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				//Calcule position apres le drag
				double dragX = me.getSceneX() - dragAnchor.getX();
				double dragY = me.getSceneY() - dragAnchor.getY();
				double newXPosition = initX + dragX;
				double newYPosition = initY + dragY;
				
				//Restriction de fenetre
				rec.setX(  Math.min(Math.max(0, newXPosition), pane.getWidth()-2) );
				rec.setY(  Math.min(Math.max(0, newYPosition), pane.getHeight()-2) );
				
				//Snap position to grid
				int gridSnap = Affichage.gridSnap.getValue();
				if(gridSnap > 0) {
					rec.setX( Math.floor(rec.getX()/gridSnap)*gridSnap );
					rec.setY( Math.floor(rec.getY()/gridSnap)*gridSnap );
				}
				
				//On change la position de l'entité liée
				Entite e = entites.get(rec);
				e.xPosition += dragX / scale;
				e.yPosition += dragY / scale;
			}
		});
		return rec;
	}
}
