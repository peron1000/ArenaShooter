package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Vec2Input extends VBox {
	Text title;
	HBox hBox;
	TextField xField, yField;

	public Vec2Input(String label, double x, double y) {
		super();
		title = new Text(label);
	    title.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	    getChildren().add(title);
	    
	    hBox = new HBox();
	    xField = createDoubleEntry(x);
	    yField = createDoubleEntry(y);
	    hBox.getChildren().add(xField);
	    hBox.getChildren().add(yField);
	    getChildren().add(hBox);
	}
	
	public double getX() {
		return Double.valueOf(xField.getText());
	}
	
	public double getY() {
		return Double.valueOf(yField.getText());
	}
	
	private TextField createDoubleEntry(double value) {
		TextField textField = new TextField();
		textField.setText( String.valueOf(value) ); 
		
		textField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("([0-9]*)\\.([0-9]*)")) {
		        	textField.setText(oldValue);
		        }
		    }
		});
		
		return textField;
	}
}
