package application.propertiestabs;

import javafx.scene.layout.VBox;

public abstract class PropertiesTab extends VBox {

	public PropertiesTab() {
		super(5);
	}
	
	public abstract void update();

}
