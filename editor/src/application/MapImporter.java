package application;

import java.io.File;

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
		Main.popup("Error", "Unimplemented feature, sorry");
	}
}
