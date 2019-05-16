package arenashooter.engine.ui;

import java.util.HashMap;

import arenashooter.engine.math.Vec2f;

public abstract class UiElement {
	Menu owner;
	
	public UiElement left = null, right = null, up = null, down = null;
	
	private HashMap<String, Trigger> actions = new HashMap<>();
	
	public Vec2f pos , scale;
	public double rotation;
	public boolean visible = true;
	
	public UiElement( Menu owner, Vec2f pos, double rot, Vec2f scale ) {
		this.owner = owner;
		owner.elems.add(this);
		this.pos = pos.clone();
		this.rotation = rot;
		this.scale = scale.clone();
	}
	
	protected abstract void update();
	
	protected abstract void draw();
	
	public void addAction(String name , Trigger trigger) {
		actions.put(name, trigger);
	}
	
	public void lunchAction(String name) {
		try {
			actions.get(name).make();
		} catch (Exception e) {
			System.err.println("Action "+name+" doesn't exist");
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
