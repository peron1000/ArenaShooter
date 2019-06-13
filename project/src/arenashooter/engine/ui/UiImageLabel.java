package arenashooter.engine.ui;

import java.util.HashSet;
import java.util.function.Consumer;

import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;

public class UiImageLabel extends UiElement {
	
	private UiImage image;
	private HashSet<UiElement> labels = new HashSet<>();
	
	public UiImageLabel(UiImage image) {
		this.image = image;
	}
	
	public UiImageLabel(UiImage image , double xScale , double yScale) {
		this(image);
		image.setScale(xScale, yScale);
	}
	
	public UiImageLabel(UiImage image , double scale) {
		this(image);
		image.setScale(scale);
	}
	
	public void setImage(UiImage image) {
		image.setPosition(this.image.getPosition().x, this.image.getPosition().y);
		image.setScale(this.image.getScale().x, this.image.getScale().y);
		image.setRotation(this.image.getRotation());
		this.image = image;
	}
	
	public void addLabel(Label newLabel , double localX , double localY) {
		labels.add(newLabel);
		newLabel.setPosition(getPosition().x, getPosition().y);
		newLabel.addToPosition(localX, localY);
	}
	
	public void addLabel(Label label , double yPourcent) {
		double d = getScale().y*yPourcent;
		addLabel(label, 0 , d-getScale().y/2);
	}
	
	public void addLabels(Label...labels) {
		for (Label label : labels) {
			addLabel(label, 0, 0);
		}
	}
	
	public void setLocalePosition(Label label , double x , double y) {
		if(labels.contains(label)) {
			label.setPosition(getPosition().x+x, getPosition().y+y);
		}
	}
	
	public void addImagePosition(double x , double y) {
		image.addToPosition(x, y);
	}
	
	public void addimageScale(double x , double y) {
		image.addToScale(x, y);
	}
	
	private void actionOnAll(Consumer<UiElement> c) {
		c.accept(image);
		for (UiElement uiElement : labels) {
			c.accept(uiElement);
		}
	}
	
	@Override
	public void setPosition(double x, double y) {
		double xDif = x-getPosition().x, yDif = y - getPosition().y;
		super.setPosition(x, y);
		actionOnAll(e -> e.addToPosition(xDif, yDif));
	}
	
	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x-getPosition().x, yDif = y - getPosition().y;
		super.setPositionLerp(x, y , lerp);
		actionOnAll(e -> e.addToPositionLerp(xDif, yDif , lerp));
	}
	
	@Override
	public void setScale(double x, double y) {
		double xDif = x-getScale().x, yDif = y - getScale().y;
		super.setScale(x, y);
		actionOnAll(e -> e.addToScale(xDif, yDif));
	}
	
	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		double xDif = x-getScale().x, yDif = y - getScale().y;
		super.setScaleLerp(x, y , lerp);
		actionOnAll(e -> e.addToScaleLerp(xDif, yDif , lerp));
	}
	
	@Override
	public void update(double delta) {
		actionOnAll(e -> e.update(delta));
		super.update(delta);
	}

	@Override
	public void draw() {
		actionOnAll(e -> e.draw());
	}

}
