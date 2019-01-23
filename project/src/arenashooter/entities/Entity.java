package arenashooter.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Entity {

	protected Entity parent;
	/** Key to find this entity in its parent's children */
	private String name = "";
	public HashMap<String, Entity> children = new HashMap<String, Entity>();

	/**
	 * Attach this a child of another Entity
	 * 
	 * @param newParent
	 *            new parent Entity
	 * @param name
	 *            used as a key in parent's children
	 * @return previous child of the new parent using that name
	 */
	public Entity attachToParent(Entity newParent, String name) {
		if (this == newParent) {
			System.err.println("Trying to attach an entity to itself!");
			return null;
		}

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

	public HashMap<String, Entity> siblings() { //Retourne une HashMap vide si l'entit� n'a pas de parent
		if (parent != null)
			return parent.children;
		return new HashMap<String, Entity>();
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	public void destroy() {
//		GameMaster.gm.getMap().toDestroy.add(this);
		detach();
	}

	/**
	 * Update children.
	 */
	public void step(double d) {
		if(!children.isEmpty()) {
			LinkedList<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(children.values());
			for (Entity e : toUpdate)
				e.step(d);
		}
	}
	
//	/**
//	 * Attempts to enable or disable transparency for this entity
//	 * @param value
//	 */
//	public void setTransparency(boolean value) {};
	
	/**
	 * @return should this entity be drawn during transparency pass
	 */
	public boolean drawAsTransparent() { return false; }
	
	/**
	 * Draw this entity of opaque/masked or add it to the transparency collection<br/>
	 * This will call this function of every children
	 * @param transparent collection of transparent entities
	 */
	public void drawOpaque(Collection<Entity> transparent) {
		if(drawAsTransparent())
			transparent.add(this);
		else
			draw();
		
		for (Entity e : children.values())
			e.drawOpaque(transparent);
	}

	/**
	 * Draw this entity<br/>
	 * This will be called during the opaque pass or the transparency pass if drawAsTransparent()
	 */
	public void draw() { }

	public String genName() {
		return String.valueOf(System.nanoTime());
	}
}
