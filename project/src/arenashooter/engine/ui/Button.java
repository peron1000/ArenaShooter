package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class Button extends UiElement {
	private Rectangle rect;
	private Label label;

	public Button(Menu owner, Vec2f pos, double rot, Vec2f scale, String text) {
		super(owner, pos, rot, scale);
		rect = new Rectangle(owner, scale, rot, scale, new Vec4f(0, 0, 0, .8));
		label = new Label(owner, scale, rot, scale, text);
	}

	@Override
	protected void update() { //TODO: berk
		rect.pos.set(pos);
		rect.rotation = rotation;
		rect.scale.set(scale);
		
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
		rect.draw();
		label.draw();
	}

}
