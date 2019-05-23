package arenashooter.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import arenashooter.engine.graphics.Window;

public class Entity {
	private Map map = null;
	private Entity parent = null;
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
		
		//Reset map
		map = null;

		// Detach this from current parent
		if (parent != null)
			parent.children.remove(this.name);

		// Remove previously attached entity with that name
		Entity previousChild = newParent.children.get(name);
		if (previousChild != null)
			previousChild.detach();

		// Attach to new parent
		this.name = name;
		newParent.children.put(name, this);
		this.parent = newParent;

		return previousChild;
	}

	/**
	 * Detach from current parent
	 */
	public void detach() {
		if (parent != null)
			parent.children.remove(name);
		parent = null;
		map = null;
		name = "";
	}

	public Entity getParent() {
		return parent;
	}

	public HashMap<String, Entity> getChildren() {
		return children;
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
	public Map getMap() { //TODO: test
		if(map != null) return map;
		
		if (this instanceof Map)
			map = (Map)this;

		Entity current = parent;
		while (current != null && !(current instanceof Map))
			current = current.parent;

		if (current instanceof Map)
			map = (Map)current;
		else
			map = null;
		
		return map;
	}

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
	public void draw() { }

	public String genName() {
		return String.valueOf(toString()+System.nanoTime());
	}
}
