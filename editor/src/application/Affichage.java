package application;

import application.propertiestabs.PropertiesTab;
import gamedata.entities.Entity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Separator;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import math.Vec2;

public final class Affichage {
	//Right menu
	public static GridSnap gridSnap;
	public static SceneTree sceneTree;
	public static ScrollPane propertiesContainer;
	private static Label selectedName;

	public static BorderPane root = new BorderPane();
	
	public static Entity selected = null;
	
	//Used for context-menu entity spawning
	private static Vec2 contextMenuLoc = new Vec2();
	
	//Center view
	private static ScrollPane scrollContainer;
	private static Group zoomGroup;
	private static double scrollInitialH, scrollInitialV;
	private static Point2D dragAnchor;
	private static double zoom = 1;

	private Affichage() { }
	
	static {
		//MenuBar
		createMenuBar();		
		
		//Bottom
		createBottomBar();
		
		// Right
		createRightMenu();

		// Center
		createCenterView();
	}
	
	public static double getZoom() { return zoom; }
	
	public static void setZoom(double newZoom) {
		zoom = Math.max(.2, Math.min(newZoom, 3));
		
		ListEntite.view.setScaleX(zoom);
		ListEntite.view.setScaleY(zoom);
	}
	
	public static void setZoom(double newZoom, Point2D pivot) { //TODO: Zoom towards pivot
		zoom = Math.max(.2, Math.min(newZoom, 3));
		
		ListEntite.view.setScaleX(zoom);
		ListEntite.view.setScaleY(zoom);
	}
	

	/**
	 * @return center of the view
	 */
	public static Vec2 getViewCenter() { //TODO: fix this
		double x = scrollContainer.getHvalue()*ListEntite.view.getWidth();
		double y = scrollContainer.getVvalue()*ListEntite.view.getHeight();
//		x -= scrollContainer.getWidth()/2;
//		y -= scrollContainer.getHeight()/2;
		return new Vec2(x, y);
	}
	
	/**
	 * Change global selected entity
	 * @param e
	 */
	public static void selectEntity(Entity e) {
		//Remove glow from previously selected object
		Node previousVisual = ListEntite.visuals.get(selected);
		if(previousVisual != null) 
			previousVisual.setStyle("-fx-effect: none;");
		
		selected = e;
		if(e == null) {
			if(Main.map != null)
				propertiesContainer.setContent(Main.map.propertiesTab);
			else
				propertiesContainer.setContent(null);
		} else {
			sceneTree.setSelected(e);
			propertiesContainer.setContent(e.properties);
		}
		
		if((PropertiesTab)propertiesContainer.getContent() != null)
			((PropertiesTab)propertiesContainer.getContent()).update();
		
		//Upadte displayed name
		refreshSelectionTitle();
		
		//Add glow around newly selected object and push it to front
		Node visual = ListEntite.visuals.get(selected);
		if(visual != null)  {
			visual.setStyle("-fx-effect: dropshadow(two-pass-box, rgba(255,200,0,1), 4, 3, 0, 0);");
			visual.toFront();
		}
	}
	
	public static void refreshSelectionTitle() {
		if(selected == null)
			selectedName.setText("World properties");
		else
			selectedName.setText(selected.name);
	}

	private static void createCenterView() {
		ListEntite.view.setBackground(new Background(new BackgroundFill(Color.rgb(56, 56, 56), new CornerRadii(0), new Insets(0))));
		scrollContainer = new ScrollPane();
		scrollContainer.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollContainer.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		ListEntite.view.setPadding(new Insets(300));
		
		zoomGroup = new Group(ListEntite.view);
		Group contentGroup = new Group(zoomGroup);
		scrollContainer.setContent(contentGroup);
		
		root.setCenter(scrollContainer);

		//Middle-mouse drag
		ListEntite.view.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(me.isMiddleButtonDown()) { //Middle click
					//Pour garder le point de depart en memoire
					scrollInitialH = scrollContainer.getHvalue();
					scrollInitialV = scrollContainer.getVvalue();
					dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
				}
			}
		});
		ListEntite.view.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(!me.isMiddleButtonDown()) return; //Not middle click
				
				//Calcule position apres le drag
				double dragX = me.getSceneX() - dragAnchor.getX();
				double dragY = me.getSceneY() - dragAnchor.getY();
				scrollContainer.setHvalue( scrollInitialH - (2*dragX/ListEntite.view.getWidth())/zoom );
				scrollContainer.setVvalue( scrollInitialV - (2*dragY/ListEntite.view.getHeight())/zoom );
			}
		});
		
		ListEntite.view.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if(event.isControlDown()) { //Only zoom if control is pressed
					event.consume(); //Consume event to prevent scrolling
					setZoom(zoom + event.getDeltaY()/1000, new Point2D(event.getX(), event.getY()));
				}
			}
		});
		
		//Context Menu
		ContextMenu contextMenu = new ContextMenu();
		
		//Add spatial
		MenuItem contextAddSpatial = new MenuItem("Add spatial");
		contextAddSpatial.setGraphic( makeIcon("file:editor_data/icons/spatial.png") );
		contextAddSpatial.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ListEntite.newSpatial( gridSnap.snap(contextMenuLoc) );
			}
		});
		//Add platform
		MenuItem contextAddPlatform = new MenuItem("Add platform");
		contextAddPlatform.setGraphic( makeIcon("file:editor_data/icons/platform.png") );
		contextAddPlatform.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ListEntite.newPlatform( gridSnap.snap(contextMenuLoc) );
			}
		});
		//Add Spawn
		MenuItem contextAddSpawn = new MenuItem("Add spawn");
		contextAddSpawn.setGraphic( makeIcon("file:editor_data/icons/spawn.png") );
		contextAddSpawn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.map.addSpawn( gridSnap.snap(contextMenuLoc.clone()) );
			}
		});

		contextMenu.getItems().addAll(contextAddSpatial, contextAddPlatform, contextAddSpawn);

		ListEntite.view.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				contextMenuLoc.x = event.getX();
				contextMenuLoc.y = event.getY();
				contextMenu.show(ListEntite.view, event.getScreenX(), event.getScreenY());
			}
		});
	}
	
	private static void createMenuBar() {
		//File
		Menu menuFile = new Menu("_File");
		MenuItem menuFileSave = new MenuItem("_Save");
		menuFileSave.setGraphic( makeIcon("file:editor_data/icons/button_save.png") );
		menuFileSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				MapExporter.export();
			}
		});
		menuFile.getItems().addAll(menuFileSave);
		
		MenuItem menuFileLoad = new MenuItem("_Open");
		menuFileLoad.setGraphic( makeIcon("file:editor_data/icons/button_load.png") );
		menuFileLoad.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				MapImporter.load();
			}
		});
		menuFile.getItems().addAll(menuFileLoad);

		//Add entities and spawns
		Menu menuAdd = new Menu("_Add...");
		MenuItem menuAddEntity = new MenuItem("_Entity");
		menuAddEntity.setGraphic( makeIcon("file:editor_data/icons/entity.png") );
		menuAddEntity.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ListEntite.newEntity();
			}
		});
		MenuItem menuAddSpatial = new MenuItem("_Spatial");
		menuAddSpatial.setGraphic( makeIcon("file:editor_data/icons/spatial.png") );
		menuAddSpatial.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ListEntite.newSpatial( gridSnap.snap(getViewCenter()) );
			}
		});
		MenuItem menuAddPlatform = new MenuItem("_Platform");
		menuAddPlatform.setGraphic( makeIcon("file:editor_data/icons/platform.png") );
		menuAddPlatform.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ListEntite.newPlatform( gridSnap.snap(getViewCenter()) );
			}
		});
		MenuItem menuAddSpawn = new MenuItem("Spa_wn");
		menuAddSpawn.setGraphic( makeIcon("file:editor_data/icons/spawn.png") );
		menuAddSpawn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Main.map.addSpawn( gridSnap.snap(getViewCenter()) );
			}
		});
		
		menuAdd.getItems().addAll(menuAddEntity, menuAddSpatial, menuAddPlatform , menuAddSpawn);

		MenuBar mb1 = new MenuBar(menuFile, menuAdd);
		root.setTop(mb1);
	}
	
	private static void createBottomBar() {
		gridSnap = new GridSnap(10);
		Button zoomOut = new Button("Reset zoom");
		zoomOut.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setZoom(1);
			}
		});
		HBox bottomBar = new HBox(10, gridSnap, zoomOut);
		root.setBottom(bottomBar);
	}
	
	private static void createRightMenu() {
		sceneTree = new SceneTree();
		
		Separator separator = new Separator();
		
		selectedName = new Label("");
		selectedName.setFont((Font.font("Arial", FontWeight.BOLD, 14)));

		propertiesContainer = new ScrollPane();
		propertiesContainer.setFitToWidth(true);
		propertiesContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
		propertiesContainer.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		propertiesContainer.setPadding(new Insets(5));

		VBox vBox = new VBox(10, sceneTree, separator, selectedName, propertiesContainer);
		vBox.setBorder(new Border(new BorderStroke(Color.AZURE, BorderStrokeStyle.SOLID, new CornerRadii(1),
				new BorderWidths(1), new Insets(3))));
		
		//Set right menu's width
		vBox.setFillWidth(true);
		vBox.setPrefWidth(450);
		
		root.setRight(vBox);
	}
	
	/**
	 * Create an icon with a shadow from a string
	 * @param path
	 * @return
	 */
	public static ImageView makeIcon(String path) {
		ImageView icon = new ImageView( new Image(path) );
		icon.setStyle("-fx-effect: dropshadow(two-pass-box, black, 5, 0, 0, 0);");
		return icon;
	}
}
