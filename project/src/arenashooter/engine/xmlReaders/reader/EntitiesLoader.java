package arenashooter.engine.xmlReaders.reader;

import org.w3c.dom.Element;

import arenashooter.entities.Entity;

public interface EntitiesLoader<E extends Entity> {
	E loadEntity(Element element , Entity parent);
}
