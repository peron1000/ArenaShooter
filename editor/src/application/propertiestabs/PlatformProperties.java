package application.propertiestabs;

import application.Vec2Input;
import gamedata.entities.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import math.Vec2;

public class PlatformProperties extends PropertiesTab {

	Platform p;
	
	private Vec2Input size;
	private TextField nameInput;
	
	public PlatformProperties(Platform p) {
		this.p = p;
		
		nameInput = new TextField(p.name);
		nameInput.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	nameInput.setText(p.rename(newValue));
		    }
		});
		
		size = new Vec2Input("Size", p.extent.x*2, p.extent.y*2);
		
		getChildren().add(nameInput);
		getChildren().add(size);
	}

	/**
	 * Get sizeInput value
	 * @return
	 */
	public Vec2 getSize() {
		return new Vec2(size.getValX(), size.getValY());
	}
	
	/**
	 * Set sizeInput value and update platform extent
	 * @param size
	 */
	public void setSize(Vec2 size) {
		p.extent.x = size.x/2;
		p.extent.y = size.y/2;
		this.size.setValX(size.x);
		this.size.setValY(size.y);
	}

	@Override
	public void update() {
		nameInput.setText(p.name);
		
		size.setValX(p.extent.x*2);
		size.setValY(p.extent.y*2);
	}
}
