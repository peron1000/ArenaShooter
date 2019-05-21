package arenashooter.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import arenashooter.engine.graphics.Window;

public class Entity {

	protected Entity parent;
	/** Key to find this entity in its parent's children */
	private String name = "";
	private HashMap<String, Entity> children = new HashMap<String, Entity>();

	/** Drawing priority relative to parent used in getZIndex() */
	public int zIndex = 0;

	// Entity comparator based on zIndex
	protected static Comparator<Entity> comparator = new Comparator<Entity>() {
		@Override
		public int compare(Entity o1, Entity o2) {
			return o1.getZIndex() - o2.getZIndex();
		}
	};

	/**
	 * Attach this a child of another Entity
	 * 
	 * @param newParent new parent Entity
	 * @param name      used as a key in parent's children
	 * @return previous child of the new parent using that name
	 */
	public Entity attachToParent(Entity newParent, String name) {
		if (this == newParent) {
			System.err.println("Trying to attach an entity to itself!"); //TODO: Move this to log
			return null;
		}
		
		//Trying to attach the entity to its current parent, nothing to do
		if(newParent == parent) return null;

		// Detach this from current parent
		if (parent != null)
			parent.children.remove(this.name);

		// Remove previously attached entity with that name
		Entity e = newParent.children.get(name);
		if (e != null)
			e.detach();

		// Attach to new parent
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

	public HashMap<String, Entity> getChildren() {
		return children;
	}

	/**
	 * 
	 * @return LinkedList with parent's childrens, including self
	 */
	public HashMap<String, Entity> siblings() {
		if (parent != null)
			return parent.children;
		return new HashMap<String, Entity>();
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	/**
	 * Update children.
	 */
	public void step(double d) {
		if (!children.isEmpty()) {
			LinkedList<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(children.values());
			for (Entity e : toUpdate)
				e.step(d);
		}
	}

	/**
	 * @return Map containing this entity or self if this is the Map
	 */
	public Map getMap() { // TODO: Replace this with something cleaner
		if (this instanceof Map)
			return (Map) this;

		Entity current = parent;
		while (current != null && !(current instanceof Map))
			current = current.parent;

		if (current instanceof Map)
			return (Map) current;
		else
			return null;
	}

	// /**
	// * Attempts to enable or disable transparency for this entity
	// * @param value
	// */
	// public void setTransparency(boolean value) {};

	/**
	 * @return should this entity be drawn during transparency pass
	 */
	public boolean drawAsTransparent() {
		return false;
	}

	/**
	 * @return final zIndex
	 */
	public int getZIndex() {
		return zIndex;
	}

//	/**
//	 * Draw this entity if opaque/masked or add it to the transparency
//	 * collection<br/>
//	 * This will call this function of every children
//	 * 
//	 * @param transparent
//	 *            collection of transparent entities
//	 */
//	public void drawOpaque(Collection<Entity> transparent) {
//		if (drawAsTransparent())
//			transparent.add(this);
//		{Window.beginTransparency();
//		draw();
//		Window.endTransparency();}
//		else
//			draw();
//
//		ArrayList<Entity> toDraw = new ArrayList<>(children.values());
//		toDraw.sort(comparator);
//		
//		for (Entity e : toDraw)
//			e.drawOpaque(transparent);
//	}

	/**
	 * Draw this entity collection<br/>
	 * This will call this function of every children
	 * 
	 */
	public void drawSelfAndChildren() {
		if (drawAsTransparent())
			Window.beginTransparency();
		else
			Window.endTransparency();
		draw();

		ArrayList<Entity> toDraw = new ArrayList<>(children.values());
		toDraw.sort(comparator);

		for (Entity e : toDraw)
			e.drawSelfAndChildren();
	}

	/**
	 * Draw this entity<br/>
	 * This will be called during the opaque pass or the transparency pass if
	 * drawAsTransparent()
	 */
	public void draw() {
	}

	public String genName() {
		return String.valueOf(System.nanoTime());
	}
}
