package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.util.CircleList;

public class ScrollerHTitle<E extends Enum<E>> extends UiActionable {
	private Vec4f colorOnUnSelect = new Vec4f(1, 1, 1, 1);
	private Vec4f colorOnSelect = new Vec4f(1, 1, 0, 1);
	private CircleList<E> list = new CircleList<>();
	private Label label;
	private String title;
	private Trigger arm = new Trigger() {

		@Override
		public void make() {
			// Nothing
		}
	};

	public ScrollerHTitle(double rot, Vec2f scale,String title , E[] enumValues) {
		super(rot, scale);
		this.title = title+" : ";
		label = new Label(0, scale, "");
		for (E e : enumValues) {
			list.add(e);
		}
		label.setText(this.title+get().name());
	}

	public void add(E e) {
		list.add(e);
	}

	public E get(int i) {
		return list.get(i);
	}

	public E get() {
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
		label.setText(title+get().name());
	}

	@Override
	public void leftAction() {
		list.previous();
		label.setText(title+get().name());
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
	
	@Override
	public void unSelec() {
		label.setColor(colorOnUnSelect);
		super.unSelec();
	}
}
