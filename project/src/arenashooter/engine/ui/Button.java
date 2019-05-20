package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class Button extends UiElement {
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
		label.setPos(new Vec2f(pos.x, pos.y-(getScale().y*0.37)));
		rect.setPos(pos);
		super.setPos(pos);
	}

	@Override
	protected void update() { // TODO: berk
//		rect.visible = this.visible;
//		rect.setPos(getPos());
//		rect.rotation = rotation;
//		rect.setPos(getScale());
//
//		label.visible = this.visible;
//		label.setPos(getPos());
//		label.rotation = rotation;
////		label.scale.set(scale);
////		label.scale.multiply(.8f);
//		float scaleText = Math.min(getScale().x, getScale().y)*4;
//		label.setPos(getPos());
//		label.setScale(new Vec2f(scaleText));
//		label.getPos().y -= 2.2f;
	}

	@Override
	protected void draw() {
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

}
