package arenashooter.engine.ui.popup;

import arenashooter.engine.ui.TextInput;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.simpleElement.Label;

public class TextPopup extends UiPopup {

	private TextInput textInput = new TextInput();
	private Label label = new Label("");

	public TextPopup(double xPos, double yPos, double xScale, double yScale, String title) {
		super(xPos, yPos, xScale, yScale);
		label.setText(title);
		label.setPosition(xPos, yPos-yScale*0.25);
		textInput.setPosition(xPos, yPos+yScale*0.1);
	}

	public TextInput getTextInput() {
		return textInput;
	}
	
	public Label getLabel() {
		return label;
	}

	public void setOnCancel(Trigger t) {
		textInput.setOnCancel(t);
	}

	public void setOnValidation(Trigger t) {
		textInput.setOnFinish(t);
	}
	
	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x , yDif = y - getPosition().y;
		textInput.addToPosition(xDif, yDif);
		label.addToPosition(xDif, yDif);
		super.setPosition(x, y);
	}
	
	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x , yDif = y - getPosition().y;
		textInput.addToPositionLerp(xDif, yDif , lerp);
		label.addToPositionLerp(xDif, yDif , lerp);
		super.setPositionLerp(x, y, lerp);
	}

	@Override
	public void setScale(double x, double y) {
		double xDif = x - getScale().x , yDif = y - getScale().y;
		textInput.addToScale(xDif, yDif);
		label.addToScale(xDif, yDif);
		super.setScale(x, y);
	}
	
	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		double xDif = x - getScale().x , yDif = y - getScale().y;
		textInput.addToScaleLerp(xDif, yDif , lerp);
		label.addToScaleLerp(xDif, yDif , lerp);
		super.setScaleLerp(x, y, lerp);
	}

	@Override
	public boolean upAction() {
		return textInput.upAction();
	}

	@Override
	public boolean downAction() {
		return textInput.downAction();
	}

	@Override
	public boolean rightAction() {
		return textInput.rightAction();
	}

	@Override
	public boolean leftAction() {
		return textInput.leftAction();
	}

	@Override
	public boolean continueAction() {
		return textInput.continueAction();
	}

	@Override
	public boolean changeAction() {
		return textInput.changeAction();
	}

	@Override
	public boolean cancelAction() {
		return textInput.cancelAction();
	}

	@Override
	public boolean backAction() {
		return textInput.backAction();
	}

	@Override
	public void update(double delta) {
		textInput.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		if (isVisible()) {
			textInput.draw();
			label.draw();
		}
	}

}
