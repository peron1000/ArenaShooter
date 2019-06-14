package arenashooter.engine.xmlReaders.reader;

import org.w3c.dom.Element;

import arenashooter.entities.Entity;

public class EntityLoader implements EntitiesLoader<Entity> {
	
	public static final EntityLoader entityLoader = new EntityLoader();
	
	private EntityLoader() {
		// Only one instance
	}

	@Override
	public Entity loadEntity(Element element, Entity parent) {
		Entity e = new Entity();
		if (element.hasAttribute("name")) {
			e.attachToParent(parent, element.getAttribute("name"));
		} else {
			e.attachToParent(parent, e.genName());
		}
		return e;
	}

}
