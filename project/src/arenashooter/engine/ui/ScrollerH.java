package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.util.CircleList;

public class ScrollerH<E extends Enum<E>> extends UiActionable implements Valuable<E> {
	private Vec4f colorOnUnSelect = new Vec4f(1, 1, 1, 1);
	private Vec4f colorOnSelect = new Vec4f(1, 1, 0, 1);
	private CircleList<E> list = new CircleList<>();
	private Label label;
	private String title = "";
	private boolean alwaysScrollable = false;
	private Trigger arm = new Trigger() {

		@Override
		public void make() {
			// Nothing
		}
	};

	public ScrollerH(double rot, Vec2f scale, E[] enumValues) {
		super(rot, scale);
		label = new Label(0, scale, "");
		for (E e : enumValues) {
			list.add(e);
		}
		label.setText(get().name());
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
	public void setPosLerp(Vec2f pos, double lerp) {
		label.setPosLerp(pos, lerp);
		super.setPosLerp(pos, lerp);
	}

	@Override
	public void draw() {
		if (isVisible()) {
			label.draw();
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		label.setVisible(visible);
		super.setVisible(visible);
	}

	@Override
	public void update(double delta) {
		label.update(delta);
		super.update(delta);
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
	
	public void setTitle(String title) {
		this.title = title+" : ";
		label.setText(this.title+get().name());
	}
	
	public String getTitle() {
		return title;
	}
	
	public void removeTitle() {
		title = "";
	}
	
	@Override
	public boolean isSelected() {
		if(alwaysScrollable) {
			return true;
		} else {
			return super.isSelected();
		}
	}

	public boolean isAlwaysScrollable() {
		return alwaysScrollable;
	}

	public void setAlwaysScrollable(boolean alwaysScrollable) {
		this.alwaysScrollable = alwaysScrollable;
	}
}
