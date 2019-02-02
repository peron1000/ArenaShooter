package application;
	
import java.util.Optional;

import gamedata.GameMap;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;


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
	
	public static void popupInfo(String title, String message) {
		popup(title, message, AlertType.INFORMATION);
	}
	
	public static void popupError(String title, String message) {
		popup(title, message, AlertType.ERROR);
	}
	
	private static void popup(String title, String message, AlertType type) {
		Alert alert = new Alert(type);
		// Get the Stage.
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		// Add a custom icon.
		stage.getIcons().add(new Image(icon));
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}
	
	public static boolean popupConfirmation(String title, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		// Get the Stage.
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		// Add a custom icon.
		stage.getIcons().add(new Image(icon));
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
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
