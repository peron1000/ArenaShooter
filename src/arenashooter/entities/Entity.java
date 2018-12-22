package arenashooter.entities;

import java.util.HashMap;

public class Entity {

	private Entity parent;
	/**
	 * Key to find this entity in its parent's children
	 */
	private String name = "";
	public HashMap<String, Entity> children = new HashMap<String, Entity>();
	
	/**
	 * Attach this a child of another Entity
	 * @param newParent new parent Entity
	 * @param name used as a key in parent's children
	 * @return previous child of the new parent using that name
	 */
	public Entity attachToParent(Entity newParent, String name) {
		if(this == newParent) {
			System.err.println("Trying to attach an entity to itself!");
			return null;
		}
		
		//Remove previously attached entity with that name
		Entity e = newParent.children.get(name);
		if (e != null)
			e.detach();

		//Attach to new parent
		this.name = name;
		newParent.children.put(name, this);
		this.parent = newParent;
		
		return e;
	}

	public void detach() {
		if (parent != null)
			parent.children.remove(name);
		parent = null;
		name = "";
	}

	public Entity getParent() {
		return parent;
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	/**
	 * Update children.
	 */
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
