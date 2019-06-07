package arenashooter.engine.ui;

import java.util.HashMap;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public abstract class UiElement implements Navigable {
	private HashMap<String, Trigger> actions = new HashMap<>();

	private Vec2f pos = new Vec2f(), scale, rePos = new Vec2f(), reScale;
	public double rotation;
	boolean visible = true;
	protected Menu owner = null;
	protected int layout = -1;
	private double lerp = 2.5;

	public UiElement(double rot, Vec2f scale) {
		this.rotation = rot;
		this.scale = scale.clone();
		this.reScale = scale.clone();
	}
	
	@Override
	public boolean continueAction() {
		return false;
	}
	
	@Override
	public boolean backAction() {
		return false;
	}
	
	@Override
	public boolean downAction() {
		return false;
	}

	@Override
	public boolean leftAction() {
		return false;
	}

	@Override
	public boolean rightAction() {
		return false;
	}
	
	@Override
	public boolean upAction() {
		return false;
	}
	
	@Override
	public boolean selectAction() {
		return false;
	}
	
	@Override
	public boolean cancelAction() {
		return false;
	}
	
	@Override
	public boolean changeAction() {
		return false;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void update(double delta) {
		scale.set(Vec2f.lerp(scale, reScale, Utils.clampD(delta * 5, 0, 1)));
		pos.set(Vec2f.lerp(pos, rePos, Utils.clampD(delta * lerp, 0, 1)));
	}

	public abstract void draw();

	public Vec2f getPosition() {
		return pos;
	}

	public void setPosition(Vec2f pos) {
		if (this.pos == null || rePos == null) {
			this.pos = pos.clone();
			this.rePos = pos.clone();
		} else {
			this.pos.set(pos);
			this.rePos.set(pos);
		}
	}
	public void setPosition(double x, double y) {
		if (this.pos == null || rePos == null) {
			pos = new Vec2f(x, y);
			rePos = new Vec2f(x, y);
		} else {
			pos.set(x, y);
			rePos.set(x, y);
		}
	}

	public void setPositionLerp(Vec2f pos , double lerp) {
		if (this.pos == null) {
			this.pos = pos.clone();
			this.rePos = pos.clone();
		}
		this.rePos.set(pos.clone());
		this.lerp = lerp;
	}

	public Vec2f getScale() {
		return scale;
	}

	public void setScale(Vec2f scale) {
		this.scale.set(scale);
		this.reScale.set(scale);
	}
	
	public void setScale(double x, double y) {
		scale.set(x, y);
		reScale.set(x, y);
	}
	
	public void setScaleLerp(Vec2f scale) {
		this.reScale.set(scale);
	}

	public void addAction(String name, Trigger trigger) {
		actions.put(name, trigger);
	}

	public Menu getOwner() {
		return owner;
	}

	public void lunchAction(String name) {
		try {
			actions.get(name).make();
		} catch (Exception e) {
			System.err.println("Action " + name + " doesn't exist");
		}
	}

	public void removeAction(String name) {
		actions.remove(name);
	}
}
