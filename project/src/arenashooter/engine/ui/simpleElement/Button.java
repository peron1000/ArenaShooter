package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.UiActionable;

public class Button extends UiActionable {
	private double ratio = 3;
	public final double defaultXScale = 10, defaultYScale = defaultXScale / ratio;
	private UiImage rect;
	private Label label;
	private float labelOffset = 0, labelRatio = 0.6f;
	private TextAlignH oldAlignH;

	public Button(String text, Vec4f color) {
		rect = new UiImage(color.clone());
		label = new Label(text);
		oldAlignH = label.getAlignH();
		setScale(defaultXScale, defaultYScale);
	}

	public Button(String text) {
		this(text, new Vec4f(0, 0, 0, 0.8));
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
		setScale(getScale().x, getScale().x / ratio);
	}

	/**
	 * @return the labelRatio
	 */
	public float getLabelRatio() {
		return labelRatio;
	}

	/**
	 * @param labelRatio the labelRatio to set
	 */
	public void setLabelRatio(float labelRatio) {
		this.labelRatio = labelRatio;
	}

	public void setRectangleVisible(boolean visible) {
		rect.setVisible(visible);
	}

	public void setText(String newString) {
		label.setText(newString);
	}

	public void setColorText(Vec4f color) {
		label.setColor(color);
	}

	public void setColorShadowText(Vec4f color) {
		label.setShadowColor(color);
	}

	public void setShadowThicknessText(float color) {
		label.setShadowThickness(color);
	}

	public void setThicknessText(float thickness) {
		label.setThickness(thickness);
	}

	public void setColorRect(Vec4f color) {
		rect.getMaterial().setParamVec4f("color", color.clone());
	}

	public TextAlignH getAlignH() {
		return oldAlignH;
	}

	public void setAlignH(TextAlignH align) {
		oldAlignH = align;
	}

	@Override
	public void setPosition(double x, double y) {
		rect.setPosition(x, y);
		label.setPosition(x, y);
		super.setPosition(x, y);
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		rect.setPositionLerp(x, y, lerp);
		label.setPositionLerp(x, y, lerp);
		super.setPositionLerp(x, y, lerp);
	}

	@Override
	public void setScale(double x, double y) {
		rect.setScale(x, y);
		label.setScale(y * labelRatio, y * labelRatio);
		super.setScale(x, y);
	}

	/**
	 * Keep the ratio for the rectangle
	 * 
	 * @see arenashooter.engine.ui.UiElement#addToScale(double)
	 */
	@Override
	public void addToScale(double square) {
		super.addToScale(square, square / ratio);
	}

	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		rect.setScaleLerp(x, y, lerp);
		label.setScaleLerp(y * labelRatio, y * labelRatio, lerp);
		super.setScaleLerp(x, y, lerp);
	}

	/**
	 * Keep the ratio for the rectangle
	 * 
	 * @see arenashooter.engine.ui.UiElement#addToScaleLerp(double, double)
	 */
	@Override
	public void addToScaleLerp(double add, double lerp) {
		super.addToScaleLerp(add, add / ratio, lerp);
	}

	@Override
	public void setVisible(boolean visible) {
		rect.setVisible(visible);
		label.setVisible(visible);
		super.setVisible(visible);
	}

	@Override
	public boolean selectAction() {
		arm();
		return true;
	}

	@Override
	public void draw() {
		if (isVisible()) {
			if (isScissorOk()) {
				rect.draw();
				Window.stackScissor(getLeft(), getBottom(), getScale().x, getScale().y);
				label.draw();
				Window.popScissor();
			} else {
				rect.draw();
				label.draw();
			}
		}
	}

	@Override
	public void update(double delta) {
		label.update(delta);
		rect.update(delta);
		super.update(delta);

		if (getScale().x < label.getTextWidth()) { // Text is too long to fit in button
			if (label.getAlignH() != TextAlignH.LEFT) { // Set horizontal alignment to left
				oldAlignH = label.getAlignH();
				label.setAlignH(TextAlignH.LEFT);
			}

			float minOffset = (getScale().x / 2) - label.getTextWidth();
			float maxOffset = -getScale().x / 2;

			if (labelOffset < minOffset - 12)
				labelOffset = maxOffset + 6;
			labelOffset -= delta * 6;

			labelOffset = Math.min(labelOffset, maxOffset + 6);

			float posX = Utils.clampF(labelOffset, minOffset, maxOffset),
					xDif = getPosition().x - label.getPosition().x;

			label.addToPositionSafely(xDif + posX, 0);

		} else { // Text fit in button
			if (label.getAlignH() != oldAlignH) // Restore alignment
				label.setAlignH(oldAlignH);
			label.setPosition(getPosition());
		}

	}

}
