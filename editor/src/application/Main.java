package application;
	
import gamedata.GameMap;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	public static GameMap map = new GameMap();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = new Scene(Affichage.root,1280,720);
			Affichage.selectEntity(null);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Super Blep Editor");
			primaryStage.getIcons().add(new Image("file:editor_data/icon.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
