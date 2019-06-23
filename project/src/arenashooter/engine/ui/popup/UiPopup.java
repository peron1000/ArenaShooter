package arenashooter.engine.ui.popup;

import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.simpleElement.UiImage;

public abstract class UiPopup extends UiElement {
	
	private UiImage backgroud = new UiImage(0, 0, 0, 1);
	private UiImage border = new UiImage(1, 1, 1, 1);
	private double borderSize = 0.5;
	private Trigger valid = new Trigger() {
		
		@Override
		public void make() {
			// Nothing by default
		}
	};
	private Trigger cancel = new Trigger() {
		
		@Override
		public void make() {
			// Nothing by default
		}
	};
	
	public UiPopup(double xPos , double yPos , double xScale , double yScale) {
		setScale(xScale, yScale);
		setPosition(xPos, yPos);
	}
	
	/**
	 * @return the borderSize
	 */
	public double getBorderSize() {
		return borderSize;
	}

	/**
	 * @param borderSize the borderSize to set
	 */
	public void setBorderSize(double borderSize) {
		this.borderSize = borderSize;
		setScale(getScale());
	}

	public UiImage getBackgroud() {
		return backgroud;
	}
	
	public void setOnCancel(Trigger cancel) {
		this.cancel = cancel;
	}
	
	public void setOnValidation(Trigger valid) {
		this.valid = valid;
	}
	
	public void setBorderColor(Vec4f color) {
		border.setColor(color);
	}
	
	public void setBackgroundColor(Vec4f color) {
		backgroud.setColor(color);
	}
	
	@Override
	public boolean backAction() {
		cancel.make();
		return true;
	}
	
	@Override
	public boolean continueAction() {
		valid.make();
		return true;
	}
	
	@Override
	public boolean selectAction() {
		valid.make();
		return true;
	}
	
	@Override
	public void setPosition(double x, double y) {
		backgroud.setPosition(x ,y);
		border.setPosition(x, y);
		super.setPosition(x, y);
	}
	
	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		backgroud.setPositionLerp(x, y, lerp);
		border.setPositionLerp(x, y, lerp);
		super.setPositionLerp(x, y, lerp);
	}
	
	@Override
	public void setScale(double x, double y) {
		backgroud.setScale(x, y);
		border.setScale(x+getBorderSize(), y+getBorderSize());
		super.setScale(x, y);
	}
	
	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		backgroud.setScaleLerp(x, y, lerp);
		border.setScaleLerp(x+borderSize, y+borderSize, lerp);
		super.setScaleLerp(x, y, lerp);
	}
	
	@Override
	public void update(double delta) {
		backgroud.update(delta);
		border.update(delta);
		super.update(delta);
	}
	
	@Override
	public void draw() {
		backgroud.draw();
	}
}
