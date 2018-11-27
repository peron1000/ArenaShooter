package arenashooter.entities;

import java.util.HashMap;

public class Entity {

	public HashMap<String, Entity> children;
	
	public Entity() {
		children = new HashMap<>();
	}

	public void attachToParent(Entity newParent, String name) {
		Entity e = newParent.children.get(name);
		if (e != null)
			e.detach();

		newParent.children.put(name, this);
		this.name = name;
	}

	public void detach() {
		if (parent != null)
			parent.children.remove(name);
		parent = null;
		name = null;
	}

	private String name;
	private Entity parent;

	public Entity getParent() {
		return parent;
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	public void step(double d) {
		for (Entity e : children.values())
			e.step(d);
	}

	public void draw() {
		for (Entity e : children.values())
			e.draw();
	}

	public String genName() {
		return String.valueOf(System.nanoTime());
	}
}
