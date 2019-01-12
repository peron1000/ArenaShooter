package application;

import gamedata.entities.Entity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Entity name field 
 * Changing the text will update linked entity's name
 */
public class NameField extends HBox {
	Text title;
	TextField field;
	final Entity entity;

	public NameField(String label, Entity entity) {
		super();
		this.entity = entity;
		
		setAlignment(Pos.CENTER_LEFT);
		
		title = new Text(label);
	    title.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	    getChildren().add(title);
	    
	    field = createNameEntry(entity);
	    getChildren().add(field);
	}
	
	public void setText(String newText) {
		field.setText(newText);
	}
	
	public String getText() {
		return field.getText();
	}
	
	private TextField createNameEntry(Entity e) {
		TextField textField = new TextField();
		textField.setText( e.name ); 
		
		textField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(!newValue.matches("^[A-Za-z\\d_-]+$")) {
		        	textField.setText(oldValue);
		        } else
		        	textField.setText(e.rename(newValue));
		    }
		});
		
		return textField;
	}

}
