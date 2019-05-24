package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.util.CircleList;

public class ScrollerH extends UiActionable {
	private Vec4f colorOnUnSelect = new Vec4f(1, 1, 1, 1);
	private Vec4f colorOnSelect = new Vec4f(1, 1, 0, 1);
	private CircleList<String> list = new CircleList<>();
	private Label label;
	private Trigger arm = new Trigger() {

		@Override
		public void make() {
			// Nothing
		}
	};

	public ScrollerH(double rot, Vec2f scale, String... strings) {
		super(rot, scale);
		label = new Label(0, scale, "");
		for (String string : strings) {
			list.add(string);
		}
		label.setText(list.get());
	}

	public void add(String str) {
		list.add(str);
	}

	public String get(int i) {
		return list.get(i);
	}

	public String get() {
		return list.get();
	}
	
	public void setOnArm(Trigger t) {
		arm = t;
	}
	
	public void arm() {
		arm.make();
	}

	@Override
	public void setPos(Vec2f pos) {
		label.setPos(pos);
		super.setPos(pos);
	}

	@Override
	public void draw() {
		if (visible) {
			label.draw();
		}
	}

	@Override
	public void upAction() {
		// Ntohing
	}

	@Override
	public void downAction() {
		// Nothing
	}

	@Override
	public void rightAction() {
		list.next();
		label.setText(list.get());
	}

	@Override
	public void leftAction() {
		list.previous();
		label.setText(list.get());
	}

	@Override
	public void selectAction() {
		if(isSelected()) {
			label.setColor(colorOnUnSelect);
		} else {
			label.setColor(colorOnSelect);
		}
		super.selectAction();
	}
}
