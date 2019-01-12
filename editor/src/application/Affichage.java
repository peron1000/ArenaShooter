package application;

import application.propertiestabs.PropertiesTab;
import gamedata.entities.Entity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Affichage {
	
	public static SceneTree sceneTree;
	public static ScrollPane propertiesContainer;

	private BorderPane root;
	
	public static GridSnap gridSnap;
	
	public static Entity selected = null;

	public Affichage(BorderPane borderPane) {
		root = borderPane;
	}
	
	/**
	 * Change global selected entity
	 * @param e
	 */
	public static void selectEntity(Entity e) {
		selected = e;
		if(e == null) {
			System.out.println("Selected scene root");
			if(Main.map != null)
				propertiesContainer.setContent(Main.map.propertiesTab);
			else
				propertiesContainer.setContent(null);
		} else {
			sceneTree.setSelected(e);
			propertiesContainer.setContent(e.properties);
			System.out.println("Selected "+e.name);
		}
		
		if((PropertiesTab)propertiesContainer.getContent() != null)
			((PropertiesTab)propertiesContainer.getContent()).update();
	}

	public void make() {
		//MenuBar
		
		//File
		Menu menuFile = new Menu("_File");
		MenuItem menuFileSave = new MenuItem("_Save");
		menuFileSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Enregistreur.enregistrer();
			}
		});
		menuFile.getItems().addAll(menuFileSave);
		
		//Add entity
		Menu menuAdd = new Menu("_Add entity");
		MenuItem menuAddPlatform = new MenuItem("_Platform");
		menuAddPlatform.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ListEntite.newPlateforme();
			}
		});
		menuAdd.getItems().addAll(menuAddPlatform);
		
		MenuBar mb1 = new MenuBar(menuFile, menuAdd);
		root.setTop(mb1);

		// Center
		ListEntite.pane.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));
		ScrollPane scrollContainer = new ScrollPane();
		scrollContainer.setContent(ListEntite.pane);
		scrollContainer.setFitToHeight(true);
		scrollContainer.setFitToWidth(true);
		root.setCenter(scrollContainer);
		
		// Right
		sceneTree = new SceneTree();
		
		propertiesContainer = new ScrollPane();
		propertiesContainer.setFitToWidth(true);
		propertiesContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
		propertiesContainer.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		Label label2 = nouveauLabel("Zoom out");
		label2.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO : faire bouger les entites
				Enregistreur.erreur();
			}
		});
		
		gridSnap = new GridSnap(10);
		
		VBox vBox = new VBox(10, label2, gridSnap, sceneTree, propertiesContainer);
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
