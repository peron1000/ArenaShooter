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

	public Button(Vec2f pos, double rot, Vec2f scale, String text) {
		super(pos, rot, scale);
		rect = new Rectangle(scale, rot, scale, new Vec4f(0, 0, 0, .8));
		label = new Label(scale, rot, scale, text);
	}

	@Override
	protected void update() { // TODO: berk
		rect.visible = this.visible;
		rect.pos.set(pos);
		rect.rotation = rotation;
		rect.scale.set(scale);

		label.visible = this.visible;
		label.pos.set(pos);
		label.rotation = rotation;
//		label.scale.set(scale);
//		label.scale.multiply(.8f);
		float scaleText = Math.min(scale.x, scale.y)*4;
		label.scale.x = scaleText;
		label.scale.y = scaleText;
		label.pos.y -= 2.2f;
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
