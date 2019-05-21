package arenashooter.engine.ui;

import java.util.HashMap;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public abstract class UiElement {
	public UiElement left = null, right = null, up = null, down = null;

	private HashMap<String, Trigger> actions = new HashMap<>();

	private Vec2f pos, scale, rePos, reScale;
	public double rotation;
	public boolean visible = true;
	Menu owner = null;
	int layout = -1;
	private double lerp = 2.5;

	public UiElement(double rot, Vec2f scale) {
		this.rotation = rot;
		this.scale = scale.clone();
		this.reScale = scale.clone();
	}

	protected void update(double delta) {
		scale.set(Vec2f.lerp(scale, reScale, Utils.clampD(delta * 5, 0, 1)));
		pos.set(Vec2f.lerp(pos, rePos, Utils.clampD(delta * lerp, 0, 1)));
	}

	protected abstract void draw();

	public Vec2f getPos() {
		return pos;
	}

	public void setPos(Vec2f pos) {
		if (this.pos == null || rePos == null) {
			this.pos = pos.clone();
			this.rePos = pos.clone();
		} else {
			this.pos.set(pos);
			this.rePos.set(pos);
		}
	}

	public void setPosLerp(Vec2f pos, double lerp) {
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

	public void ui_Pointation(UiElement up, UiElement down, UiElement right, UiElement left) {
		this.up = up;
		this.down = down;
		this.right = right;
		this.left = left;
	}

}
