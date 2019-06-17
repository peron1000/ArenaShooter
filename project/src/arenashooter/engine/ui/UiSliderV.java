package arenashooter.engine.ui;

import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;

public class UiSliderV extends UiActionable {
	private UiImage cursor, bar;
	
	private double oldValue;
	public double value;
	public double increments = 0.1;
	
	private String title = "";
	private Label label;
	
	private boolean selected = false;

	public UiSliderV() {
		bar = new UiImage(new Vec4f(0.8, 0.8, 0.8, 1.0));
		cursor = new UiImage(new Vec4f(1.0, 1.0, 1.0, 1.0));
		label = new Label("");
	}
	
	@Override
	public boolean selectAction() {
		if(selected) return continueAction();
		setActive(true);
		return true;
	}
	
	@Override
	public boolean backAction() {
		if(!selected) return false;
		setActive(false);
		value = oldValue; //Reset value
		return true;
	}
	
	@Override
	public boolean continueAction() {
		if(!selected) return false;
		setActive(false);
		arm();
		return true;
	}
	
	@Override
	public boolean upAction() {
		return selected;
	}
	
	@Override
	public boolean downAction() {
		return selected;
	}
	
	@Override
	public boolean leftAction() {
		if(!selected) return false;
		value = Math.max(0, value-increments);
		return true;
	}
	
	@Override
	public boolean rightAction() {
		if(!selected) return false;
		value = Math.min(1, value+increments);
		return true;
	}
	
	private void setActive(boolean active) {
		if(active) {
			oldValue = value;
			cursor.getMaterial().setParamVec4f("color", new Vec4f(1.0, 1.0, 0.5, 1.0));
			selected = true;
		} else {
			cursor.getMaterial().setParamVec4f("color", new Vec4f(1.0, 1.0, 1.0, 1.0));
			selected = false;
		}
	}
	
	@Override
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		bar.setPosition(x, y);
		setScale(getScale());
	}
	
	@Override
	public void setScale(double x, double y) {
		super.setScale(x, y);
		if(title.isEmpty()) {
			bar.setPosition(getPosition());
			bar.setScale(x, .8);
		} else {
			label.setScale(y, y);
			float labelW = label.getTextWidth();
			label.setPosition(getPosition().x-labelW/2, getPosition().y);//TODO: Improve this
			bar.setPosition(getPosition().x+x/4, getPosition().y);
			bar.setScale(x/2, .8);
		}
		cursor.setScale(1.2, y);
	}

	@Override
	public void draw() {
		value = Utils.clampD(value, 0, 1);
		cursor.setPosition(Utils.lerpF(bar.getLeft(), bar.getRight(), value), bar.getPosition().y);
		
		label.draw();
		bar.draw();
		cursor.draw();
	}
	
	public void setTitle(String title) {
		this.title = title + " : ";
		label.setText(this.title);
		label.setAlignH(TextAlignH.RIGHT);
		label.setVisible(true);
		setScale(getScale());
	}

	public void removeTitle() {
		title = "";
		label.setVisible(false);
		setScale(getScale());
	}

}
