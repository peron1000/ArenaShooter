package application;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Affichage {

	private BorderPane root;

	public Affichage(BorderPane borderPane) {
		root = borderPane;
	}

	public void make() {
		// MenuBar
		Menu fill = new Menu("Fichier");
		MenuBar mb1 = new MenuBar(fill);
		root.setTop(mb1);

		// Center
		StackPane center = ListEntite.pane;
		center.setBackground(new Background(new BackgroundFill(Color.DARKGREY, new CornerRadii(0), new Insets(0))));
		center.setAlignment(Pos.CENTER);
		root.setCenter(center);
		

		// Right
		Label label1 = nouveauLabel("Plateforme");
		label1.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				ListEntite.newPlateforme();
			}
		});
		
		Label label2 = nouveauLabel("Zoom out");
		label2.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO : faire bouger les entites
				Enregistreur.erreur();
			}
		});
		
		Label label3 = nouveauLabel("Enregistrer");
		label3.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Enregistreur.enregistrer();
			}
		});
		
		VBox vBox = new VBox(10, label1 , label2 , label3);
		vBox.setBorder(new Border(new BorderStroke(Color.AZURE, BorderStrokeStyle.SOLID, new CornerRadii(1),
				new BorderWidths(1), new Insets(3))));
		root.setRight(vBox);

	}
	
	/**
	 * Crée un Label un minumum stylisé
	 * @param name (ce qui sera écrit sur le Label)
	 * @return Un nouveau Label
	 */
	private Label nouveauLabel(String name) {
		Label label = new Label(name);
		label.setPadding(new Insets(2, 5, 2, 5));
		label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3),
				new BorderWidths(1), new Insets(10))));
		label.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				label.setCursor(Cursor.HAND);
			}
		});
		label.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				label.setOpacity(0.5);
			}
		});
		label.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				label.setOpacity(1);
			}
		});
		return label;
	}

}
