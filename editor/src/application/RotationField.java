package application;

import application.movableshapes.MovableRectangle;
import gamedata.entities.Entity;
import gamedata.entities.Spatial;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Entity name field 
 * Changing the text will update linked entity's name
 */
public class RotationField extends HBox {
	Text title;
	TextField field;
	final Spatial entity;

	public RotationField(String label, Spatial entity) {
		super();
		this.entity = entity;
		
		setAlignment(Pos.CENTER_LEFT);
		
		title = new Text(label);
	    title.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	    getChildren().add(title);
	    
	    field = createNameEntry(entity);
	    getChildren().add(field);
	}
	
	/**
	 * Set entity rotation
	 * @param newVal degrees
	 */
	public void setValueD(double newVal) {
		field.setText(String.valueOf(newVal));
	}
	
	/**
	 * Set entity rotation
	 * @param newVal radians
	 */
	public void setValueR(double newVal) {
		setValueD(Math.toDegrees(newVal));
	}
	
	/**
	 * Get field value
	 * @return degrees
	 */
	public double getValue() {
		return Double.valueOf(field.getText());
	}
	
	private TextField createNameEntry(Entity e) {
		TextField textField = new TextField();
		textField.setText( e.name ); 
		
		textField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(!newValue.matches("^\\-?[0-9]*\\.?[0-9]*$") || newValue.length()==0) {
		        	textField.setText(oldValue);
		        } else {
		        	entity.rotation = Math.toRadians(Double.valueOf(newValue));
		        	Node visual = ListEntite.getVisual(e);
		        	if( (MovableRectangle)visual != null ) ((MovableRectangle)visual).rotateProperty().set( -Math.toDegrees(entity.rotation) );
		        }
		    }
		});
		
		return textField;
	}

}