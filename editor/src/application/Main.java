package application;
	
import gamedata.GameMap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


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
		Button b = new Button("ok");
		VBox root = new VBox(10);

		popup.setWidth(250);
		popup.setHeight(150);
		
		Text text = new Text(message);
		text.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().addAll(text, b);
		root.setAlignment(Pos.CENTER);
		
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				popup.close();
			}
		});
		

		Scene scene = new Scene(root);
		popup.setScene(scene);
		popup.show();
	}
}
