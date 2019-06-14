package arenashooter.engine.ui;

import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Button;

public class UiValuableButton<T> extends UiActionable {
	
	private String title;
	private Button button;
	private T value;

	public UiValuableButton(String title , T defaultValue) {
		this.title = title;
		button = new Button(title+" : "+defaultValue.toString());
		button.setRectangleVisible(false);
		button.setRatio(2);
		setScale(button.getScale().x, button.getScale().y);
		value = defaultValue;
	}
	
	public void setRectangleVisible(boolean visible) {
		button.setRectangleVisible(visible);
	}
	
	public void setScaleText(double square) {
		button.setScaleText(square);
	}
	
	public void setColorText(Vec4f color) {
		button.setColorText(color);
	}
	
	public void setScaleRectangle(double x , double y) {
		button.setScaleRect(x, y);
	}
	
	public void setColorRectangle(Vec4f color) {
		button.setColorRect(color);
	}
	
	public void setTitle(String title) {
		this.title = title;
		button.setText(title+" : "+value.toString());
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
		button.setText(title+" : "+value.toString());
	}
	
	@Override
	public void setPosition(double x, double y) {
		button.setPosition(x, y);
		super.setPosition(x, y);
	}
	
	@Override
	public void setScale(double x, double y) {
		button.setScale(x , y);
		super.setScale(x , y);
	}
	
	@Override
	public boolean selectAction() {
		arm();
		return true;
	}
	
	@Override
	public void update(double delta) {
		button.update(delta);
		super.update(delta);
	}
	
	@Override
	public void draw() {
		if(isVisible()) {
			button.draw();
		}
	}

}
