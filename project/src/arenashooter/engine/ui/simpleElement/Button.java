package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;

public class Button extends UiActionable {
	private Rectangle rect;
	private Label label;
	private Trigger arm = new Trigger() {
		
		@Override
		public void make() {
			// Nothing
		}
	};

	public Button(double rot, Vec2f scale, String text) {
		super(rot, scale);
		rect = new Rectangle(rot, scale, new Vec4f(0, 0, 0, .8));
		label = new Label(rot, new Vec2f(scale.x*3, scale.y*5), text);
	}
	
	@Override
	public void setPos(Vec2f pos) {
		label.setPos(pos);
		rect.setPos(pos);
		super.setPos(pos);
	}
	
	@Override
	public void setPosLerp(Vec2f pos, double lerp) {
		label.setPosLerp(pos, lerp);
		rect.setPosLerp(pos, lerp);
		super.setPosLerp(pos, lerp);
	}
	
	@Override
	public void setVisible(boolean visible) {
		rect.setVisible(visible);
		label.setVisible(visible);
		super.setVisible(visible);
	}

	@Override
	public void draw() {
		if (isVisible()) {
			rect.draw();
			label.draw();
		}
	}
	
	@Override
	public void update(double delta) {
		label.update(delta);
		rect.update(delta);
		super.update(delta);
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
	
	public void setScaleText(Vec2f scale) {
		label.setScale(scale);
	}
	
	public void setScaleRect(Vec2f scale) {
		rect.setScale(scale);
	}
	
	public void setColorFond(Vec4f color) {
		rect.setColor(color);
	}

	public void arm() {
		arm.make();
	}
	
	public void setOnArm(Trigger arm) {
		this.arm = arm;
	}

	@Override
	public void upAction() {
		// Nothing
	}

	@Override
	public void downAction() {
		// Nothing
	}

	@Override
	public void rightAction() {
		// Nothing
	}

	@Override
	public void leftAction() {
		// Nothing
	}
	
	@Override
	public void selectAction() {
		arm();
	}

}
