package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.UiActionable;

public class Button extends UiActionable {
	private double  ratio = 3;
	public final double defaultXScale = 10 , defaultYScale = defaultXScale/ratio;
	private UiImage rect;
	private Label label;
	
	public Button(String text , Vec4f color) {
		rect = new UiImage(color.clone());
		label = new Label(text);
		setScale(defaultXScale, defaultYScale);
	}

	public Button(String text) {
		this(text , new Vec4f(0 , 0 , 0 , 0.8));
	}
	
	public double getRatio() {
		return ratio;
	}
	
	public void setRatio(double ratio) {
		this.ratio = ratio;
		setScale(getScale().x, getScale().x/ratio);
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
	
	public void setAlignH(TextAlignH align) {
		label.setAlignementH(align);
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
		label.setScale(y*0.6, y*0.6);
		super.setScale(x, y);
	}
	
	/** 
	 * Keep the ratio for the rectangle
	 * @see arenashooter.engine.ui.UiElement#addToScale(double)
	 */
	@Override
	public void addToScale(double square) {
		super.addToScale(square , square/ratio);
	}
	
	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		rect.setScaleLerp(x, y , lerp);
		label.setScaleLerp(y, y , lerp);
		super.setScaleLerp(x, y, lerp);
	}
	
	/** Keep the ratio for the rectangle
	 * @see arenashooter.engine.ui.UiElement#addToScaleLerp(double, double)
	 */
	@Override
	public void addToScaleLerp(double add, double lerp) {
		super.addToScaleLerp(add, add/ratio, lerp);
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
			rect.draw();
			Window.stackScissor(getPosition().x-getScale().x/2, getPosition().y+getScale().y/2, getScale().x, getScale().y);
			label.draw();
			Window.popScissor();
		}
	}

	@Override
	public void update(double delta) {
		label.update(delta);
		rect.update(delta);
		super.update(delta);
	}

}
