package arenashooter.engine.ui.popup;

import arenashooter.engine.ui.simpleElement.Label;

public class MessagePopup extends UiPopup {

	private Label message = new Label("");

	public MessagePopup(double xPos, double yPos, double xScale, double yScale, String message) {
		super(xPos, yPos, xScale, yScale);
		this.message.setText(message);
	}

	public Label getMessage() {
		return message;
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x , yDif = y - getPosition().y;
		message.addToPosition(xDif, yDif);
		super.setPosition(x, y);
	}
	
	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x , yDif = y - getPosition().y;
		message.addToPositionLerp(xDif, yDif ,lerp);
		super.setPositionLerp(x, y, lerp);
	}

	@Override
	public void setScale(double x, double y) {
		double xDif = x - getScale().x , yDif = y - getScale().y;
		message.addToScale(xDif, yDif);
		super.setScale(x, y);
	}
	
	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		double xDif = x - getScale().x , yDif = y - getScale().y;
		message.addToScaleLerp(xDif, yDif , lerp);
		super.setScaleLerp(x, y, lerp);
	}
	
	@Override
	public void update(double delta) {
		message.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		if (isVisible()) {
			super.draw();
			message.draw();
		}
	}

}
