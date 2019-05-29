package arenashooter.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import arenashooter.engine.graphics.Window;
import arenashooter.game.Main;

public class Entity {
	private Arena arena = null;
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
			Main.log.warn("Trying to attach an entity to itself!");
			return null;
		}
		
		//Trying to attach the entity to its current parent, nothing to do
		if(newParent == parent) return null;

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
		arena = null;
		name = "";
	}

	/**
	 * @return this Entity's parent (null if not attached)
	 */
	public Entity getParent() {
		return parent;
	}

	/**
	 * Get the entire children map. 
	 * If you intend to use this for get(), you should use getChild() instead.
	 * @return this Entity's children map (name->child)
	 */
	public HashMap<String, Entity> getChildren() { return children; }
	
	/**
	 * Get a child Entity from its name.
	 * <br/> This is a shortcut for getChildren().get(<i>name</i>).
	 * @param name
	 * @return child Entity attached as <i>name</i> or null if none
	 */
	public Entity getChild(String name) {
		return children.get(name);
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
	 * @return Arena containing this entity or self if this is the Arena
	 */
	public Arena getArena() { //TODO: test
//		if(arena != null) return arena;
//		
//		if (this instanceof Arena)
//			arena = (Arena)this;

		Entity current = parent;
		while (current != null && !(current instanceof Arena))
			current = current.parent;

		if (current instanceof Arena)
			arena = (Arena)current;
		else
			arena = null;
		
		return arena;
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
