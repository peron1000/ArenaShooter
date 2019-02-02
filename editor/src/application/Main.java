package application;
	
import gamedata.GameMap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class Main extends Application {
	public static GameMap map = new GameMap();
	public static final String icon = "file:editor_data/icon.png";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = new Scene(Affichage.root,1280,720);
			Affichage.selectEntity(null);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Super Blep Editor");
			primaryStage.getIcons().add(new Image(icon));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Open a popup window with a button to close it
	 * @param title popup window title
	 * @param message popup content
	 */
	public static void popup(String title, String message) {
		Stage popup = new Stage();
		popup.setTitle(title);
		popup.getIcons().add(new Image(icon));
		
		Button b = new Button("Ok");
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				popup.close();
			}
		});
		
		Text text = new Text(message);

		VBox root = new VBox(text, b);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		
		Scene scene = new Scene(root);
		popup.setScene(scene);

		popup.setResizable(false);
		popup.show();
	}
	
	/**
	 * Create a new map
	 */
	public static void clear() {
		Affichage.selectEntity(null);
		
		Affichage.sceneTree.getRoot().getChildren().clear();
		
		ListEntite.view.getChildren().clear();
		
		ListEntite.visuals.clear();
		
		map = new GameMap();
		
		Affichage.selectEntity(null);
	}
}
