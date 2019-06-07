package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.simpleElement.Label;

public class UiValuableButton<T> extends UiActionable {
	
	private String title;
	private Label labelValue;
	private T value;

	public UiValuableButton(double rot, Vec2f scale , String title , T defaultValue) {
		super(rot, scale);
		this.title = title;
		labelValue = new Label(rot, scale, title+" : "+defaultValue.toString());
		value = defaultValue;
	}
	
	public UiValuableButton(String title , T defaultValue) {
		this(0 , new Vec2f(30) , title , defaultValue);
	}
	
	public UiValuableButton(T defaultValue) {
		this("title : " , defaultValue);
	}
	
	public void setTitle(String title) {
		labelValue.setText(title+" : "+value.toString());
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
		labelValue.setText(title+" : "+value.toString());
	}
	
	@Override
	public void setPosition(Vec2f pos) {
		labelValue.setPosition(pos);
		super.setPosition(pos);
	}
	
	@Override
	public void setPositionLerp(Vec2f pos, double lerp) {
		Vec2f dif = Vec2f.subtract(pos, getPosition());
		labelValue.setPositionLerp(Vec2f.add(dif, getPosition()), lerp);
		super.setPositionLerp(pos, lerp);
	}
	
	@Override
	public void setPosition(double x, double y) {
		labelValue.setPosition(x, y);
		super.setPosition(x, y);
	}
	
	@Override
	public void setScale(double x, double y) {
		labelValue.setScale(x, y);
		super.setScale(x, y);
	}
	
	@Override
	public void setScale(Vec2f scale) {
		labelValue.setScale(scale);
		super.setScale(scale);
	}
	
	@Override
	public void setScaleLerp(Vec2f scale) {
		labelValue.setScale(scale);
		super.setScaleLerp(scale);
	}
	
	@Override
	public void update(double delta) {
		labelValue.update(delta);
		super.update(delta);
	}
	
	@Override
	public void draw() {
		if(isVisible()) {
			labelValue.draw();
		}
	}
	
	@Override
	public boolean selectAction() {
		arm();
		return true;
	}

}
