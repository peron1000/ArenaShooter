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

	public Button(Menu owner, Vec2f pos, double rot, Vec2f scale, String text, int layout) {
		super(owner, pos, rot, scale, layout);
		rect = new Rectangle(owner, scale, rot, scale, new Vec4f(0, 0, 0, .8), layout);
		label = new Label(owner, scale, rot, scale, text, layout + 1);
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

	public void arm() {
		arm.make();
	}
	
	public void setOnArm(Trigger arm) {
		this.arm = arm;
	}

}
