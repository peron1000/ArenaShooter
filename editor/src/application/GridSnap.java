package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GridSnap extends HBox {
	Text title;
	TextField textField;
	
	public GridSnap(int value) {
		super();
		
		setAlignment(Pos.CENTER_LEFT);
		
		title = new Text("Grid snap: ");
	    title.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	    
	    textField = createIntEntry(0);
	    setValue(value);
	    
	    getChildren().add(title);
	    getChildren().add(textField);
	}
	
	/**
	 * Set grid size
	 * @param value new grid size
	 */
	public void setValue(int value) { textField.setText(String.valueOf(Math.max(0, value))); }
	
	/**
	 * Get grid size
	 * @return grid size
	 */
	public int getValue() { return Integer.valueOf(textField.getText()); }
	
	private TextField createIntEntry(int value) {
		TextField textField = new TextField();
		textField.setText( String.valueOf(value) ); 
		
		textField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("[0-9]+")) {
		        	textField.setText(oldValue);
		        }
		    }
		});
		
		return textField;
	}
}
