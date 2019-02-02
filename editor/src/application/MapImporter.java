package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public final class MapImporter {

	private MapImporter() { }
	
	public static void load() {
		Stage stage = new Stage();
		FileChooser f = new FileChooser();
		f.setInitialDirectory(new File("export"));
		f.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"),
				new FileChooser.ExtensionFilter("All", "*.*"));
		File file = f.showOpenDialog(stage);
		
		if(file != null) {
			readFile(file);
		} else { //Import cancelled
			return;
		}
	}

	private static void readFile(File f) {
		//TODO: Clean current map and load new one
		popupErreur("Unimplemented feature, sorry");
	}
	
	/**
	 * Ouvre une fenêtre signalant une erreur 
	 * @param message Explication de l'erreur
	 */
	public static void popupErreur(String message) {
		Stage erreur = new Stage();
		erreur.setWidth(200);
		erreur.setHeight(150);
		Button b = new Button("ok");
		VBox root = new VBox(10);
		Scene scene = new Scene(root);
		erreur.setScene(scene);
		Text text = new Text(message);
		text.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().addAll(text, b);
		root.setAlignment(Pos.CENTER);
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				erreur.close();
			}
		});
		erreur.show();
	}
}
