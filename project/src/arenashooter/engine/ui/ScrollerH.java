package arenashooter.engine.ui;

import java.util.HashMap;

import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.engine.util.CircleList;

public class ScrollerH<E> extends UiActionable {
	private Vec4f colorOnUnSelect = new Vec4f(1);
	private Vec4f colorOnSelect = new Vec4f(1, 1, 0, 1);
	private CircleList<E> list = new CircleList<>();
	private Label label;
	private UiImage backgroundSelect = new UiImage(0, 0, 0, 1) , backgroundUnselect = new UiImage(0, 0, 0, 1);
	private String title = "";
	private boolean alwaysScrollable = false, selected = false, bgVisible = false , bgChange = false;
	private Trigger valid = new Trigger() {

		@Override
		public void make() {
			// Nothing by default
		}
	};
	private HashMap<E, String> viewOfValue = new HashMap<>();

	public ScrollerH(E[] enumValues) {
		for (E e : enumValues) {
			list.add(e);
		}
		label = new Label(get().toString());
		setScale(8);
	}

	public ScrollerH() {
		label = new Label("no value");
		setScale(8);
	}

	public boolean isSelected() {
		return selected;
	}
	
	public boolean isBackgroundVisible() {
		return bgVisible;
	}
	
	public void setBackgroundVisible(boolean bgVisible) {
		this.bgVisible = bgVisible;
	}
	
	public void setBackgroundChange(boolean bgChange) {
		this.bgChange = bgChange;
	}
	
	public boolean isBackgroundChange() {
		return bgChange;
	}
	
	/**
	 * Used only if <i>backgroundChange</i> is true
	 * @param background
	 */
	public void setBackgroundSelect(UiImage background) {
		this.backgroundSelect = background;
	}
	
	/**
	 * Default background
	 * @param background
	 */
	public void setBackgroundUnselect(UiImage background) {
		this.backgroundUnselect = background;
	}
	
	public void setBackgroundScale(double x , double y) {
		backgroundSelect.setScale(x, y);
		backgroundUnselect.setScale(x, y);
	}

	public void changeValueView(E value, String view) {
		viewOfValue.put(value, view);
		if(get() == value) {
			label.setText(title + view);
		}
	}

	public void setOnValidation(Trigger t) {
		valid = t;
	}

	public void add(E e) {
		if (list.size() == 0) {
			label.setText(title + e.toString());
		}
		list.add(e);
	}

	public void remove(E e) {
		if (!list.remove(e)) {
			Exception ex = new Exception(e.toString() + " is not in the list of values");
			ex.printStackTrace();
		}
	}

	public E get(int i) {
		return list.get(i);
	}

	public E get() {
		return list.get();
	}
	
	/**
	 * change the value of the ScrollerH to <i>value</i>
	 * @param value
	 * @return if it's a success
	 */
	public boolean setValue(E value) {
		for (int i = 0; i < list.size(); i++) {
			if(value.equals(get())) {
				if (viewOfValue.containsKey(get())) {
					label.setText(title + viewOfValue.get(get()));
				} else {
					label.setText(title + get().toString());
				}
				return true;
			} else {
				list.next();
			}
		}
		return false;
	}
	
	public boolean containValue(E value) {
		return list.contains(value);
	}

	public void setTitle(String title) {
		this.title = title + " : ";
		if (list.size() > 0) {
			if(viewOfValue.containsKey(get())) {
				label.setText(this.title + viewOfValue.get(get()));
			} else {
				label.setText(this.title + get().toString());
			}
		} else {
			label.setText(this.title + "no values");
		}
	}

	public void removeTitle() {
		title = "";
	}

	public boolean isAlwaysScrollable() {
		return alwaysScrollable;
	}

	public void setSelectColor(double red, double green, double blue, double transparency) {
		colorOnSelect.set(red, green, blue, transparency);
	}

	public void setUnselectColor(double red, double green, double blue, double transparency) {
		colorOnUnSelect.set(red, green, blue, transparency);
	}

	/**
	 * if <i>true</i> the user can change the value directly (no need to select the
	 * element) and the selectAction will launch the Trigger 'arm'.</br>
	 * Otherwise if <i>false</i> the user needs to select the element to change its
	 * value and eventually can validate or not his choice with the selectAction or
	 * the cancelAction, the selectAction will in this case launch the Trigger
	 * 'validation'
	 * 
	 * @param alwaysScrollable
	 */
	public void setAlwaysScrollable(boolean alwaysScrollable) {
		this.alwaysScrollable = alwaysScrollable;
	}

	@Override
	public void setScale(double x, double y) {
		label.setScale(x, y);
		backgroundSelect.setScale(x, y);
		backgroundUnselect.setScale(x, y);
		super.setScale(x, y);
	}

	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		label.setScaleLerp(x, y, lerp);
		backgroundSelect.setScaleLerp(x, y, lerp);
		backgroundUnselect.setScaleLerp(x, y, lerp);
		super.setScaleLerp(x, y, lerp);
	}

	@Override
	public void setPosition(double x, double y) {
		label.setPosition(x, y);
		backgroundSelect.setPosition(x, y);
		backgroundUnselect.setPosition(x, y);
		super.setPosition(x, y);
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		label.setPositionLerp(x, y, lerp);
		backgroundSelect.setPositionLerp(x, y, lerp);
		backgroundUnselect.setPositionLerp(x, y, lerp);
		super.setPositionLerp(x, y, lerp);
	}

	@Override
	public void setVisible(boolean visible) {
		label.setVisible(visible);
		backgroundSelect.setVisible(visible);
		super.setVisible(visible);
	}

	@Override
	public void update(double delta) {
		label.update(delta);
		backgroundSelect.update(delta);
		super.update(delta);
	}
	
	@Override
	public void draw() {
		if (isVisible()) {
			if (bgVisible) {
				if(bgChange) {
					if(selected) {
						backgroundSelect.draw();
					} else {
						backgroundUnselect.draw();
					}
				} else {
					// By default
					backgroundUnselect.draw();
				}
			}
			label.draw();
		}
	}

	@Override
	public boolean downAction() {
		if(selected) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean upAction() {
		if(selected) {
			return true;
		}
		return false;
	}

	@Override
	public boolean rightAction() {
		if (alwaysScrollable || selected) {
			list.next();
			if (viewOfValue.containsKey(get())) {
				label.setText(title + viewOfValue.get(get()));
			} else {
				label.setText(title + get().toString());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean leftAction() {
		if (alwaysScrollable || selected) {
			list.previous();
			if (viewOfValue.containsKey(get())) {
				label.setText(title + viewOfValue.get(get()));
			} else {
				label.setText(title + get().toString());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean selectAction() {
		if (alwaysScrollable) {
			arm();
		} else {
			if (selected) {
				label.setColor(colorOnUnSelect);
				selected = false;
				valid.make();
			} else {
				label.setColor(colorOnSelect);
				selected = true;
			}
		}
		return true;
	}

	@Override
	public boolean backAction() {
		if (alwaysScrollable || !selected) {
			return false;
		} else {
			selected = false;
			label.setColor(colorOnUnSelect);
			return true;
		}
	}
}
