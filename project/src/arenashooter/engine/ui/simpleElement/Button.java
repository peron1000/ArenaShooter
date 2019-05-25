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
		label = new Label(rot, new Vec2f(scale.x*0.35, scale.y*5), text);
	}
	
	@Override
	public void setPos(Vec2f pos) {
		label.setPos(pos);
		rect.setPos(pos);
		super.setPos(pos);
	}

	@Override
	public void draw() {
		if (visible) {
			rect.draw();
			label.draw();
		}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void downAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rightAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leftAction() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void selectAction() {
		arm();
	}

}
