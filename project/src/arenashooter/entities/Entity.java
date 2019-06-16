package arenashooter.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;
import arenashooter.game.Main;

public class Entity implements Editable {
	/** Arena containing this */
	private Arena arena = null;
	/** Arena value needs to be refreshed */
	private boolean arenaDirty = true;
	private Entity parent = null;
	/** Key to find this entity in its parent's children */
	private String name = "";
	private Map<String, Entity> children = new HashMap<String, Entity>();

	/** Drawing priority relative to parent used in getZIndex() */
	public int zIndex = 0;
	
	private boolean isEditorTarget = false;
	
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
		Entity previousChild = newParent.getChild(name);
		if (previousChild != null)
			previousChild.detach();

		// Attach to new parent
		this.name = name;
		newParent.children.put(name, this);
		this.parent = newParent;

		recursiveAttach(this.parent);
		
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
		recursiveDetach(arena);
	}
	
	protected void recursiveAttach(Entity newParent) {
		arenaDirty = true;
		for(Entity e : children.values())
			e.recursiveAttach(newParent);
	}
	
	protected void recursiveDetach(Arena oldArena) {
		arenaDirty = true;
		for(Entity e : children.values())
			e.recursiveDetach(oldArena);
	}
	
	/**
	 * Update position/rotation according to parent	
	 */
	public void updateAttachment() {} //Entities aren't spatials, they don't need that

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
	public Map<String, Entity> getChildren() { return children; }
	
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
			List<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(children.values());
			for (Entity e : toUpdate) {
				e.updateAttachment();
				e.step(d);
			}
		}
	}

	/**
	 * @return Arena containing this entity
	 */
	public Arena getArena() { //TODO: test
		if(!arenaDirty && arena != null) return arena;
		
		Entity current = parent;
		while (current != null && !(current instanceof Arena))
			current = current.parent;

		if (current instanceof Arena)
			arena = (Arena)current;
		else
			arena = null;
		
		arenaDirty = false;
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
//		List<Entity> toDraw = new ArrayList<>(children.values());
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

		List<Entity> toDraw = new ArrayList<>(children.values());
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

	@Override
	public boolean isEditorTarget() {
		return isEditorTarget;
	}

	@Override
	public void setEditorTarget(boolean editorTarget) {
		isEditorTarget = editorTarget;
	}

	@Override
	public void editorAddPosition(Vec2f position) {
		// Nothing
	}

	@Override
	public void editorAddScale(Vec2f scale) {
		// Nothing
	}

	@Override
	public void editorAddRotationZ(double angle) {
		// Nothing
	}

	@Override
	public void editorDraw() {
		draw();
	}

	@Override
	public void editorAddDeep(float deep) {
		// Nothing		
	}

	@Override
	public void editorAddRotationX(double angle) {
		// Nothing
	}

	@Override
	public void editorAddRotationY(double angle) {
		// Nothing
	}
}
