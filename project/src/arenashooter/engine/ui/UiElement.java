package arenashooter.engine.ui;

import java.util.HashMap;

import arenashooter.engine.math.Vec2f;

public abstract class UiElement {
	public UiElement left = null, right = null, up = null, down = null;

	private HashMap<String, Trigger> actions = new HashMap<>();

	private Vec2f pos, scale;
	public double rotation;
	public boolean visible = true;
	Menu owner = null;
	int layout = -1;

	public UiElement(double rot, Vec2f scale) {
		this.rotation = rot;
		this.scale = scale.clone();
	}
	
	protected abstract void update();

	protected abstract void draw();

	public Vec2f getPos() {
		return pos;
	}

	public void setPos(Vec2f pos) {
		this.pos = pos;
	}

	public Vec2f getScale() {
		return scale;
	}

	public void setScale(Vec2f scale) {
		this.scale = scale;
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
