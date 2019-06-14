package arenashooter.engine.xmlReaders.reader;

import java.util.List;

import org.w3c.dom.Element;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.StaticBodyContainer;

public class StaticLoader implements EntitiesLoader<StaticBodyContainer> {

	public static final StaticLoader staticLoader = new StaticLoader();

	private StaticLoader() {
		// Only one instance
	}

	@Override
	public StaticBodyContainer loadEntity(Element element, Entity parent) {
		// TODO: Add support for circular static bodies
		// Read position and extent values
		List<Element> vectors = MapXmlReader.getListElementByName("vector", element);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			MapXmlReader.log.error("StaticBody element needs " + nbVec + " vectors");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = MapXmlReader.loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				case "extent":
					extent = new Vec2f(vec.x, vec.y);
					break;
				default:
					MapXmlReader.log.error("Invalid vector in StaticBody element");
					break;
				}
			}
		}

		// Read optional rotation
		double rotation = 0.0;
		if (element.hasAttribute("rotation"))
			rotation = Double.parseDouble(element.getAttribute("rotation"));

		// Create entity
		StaticBodyContainer container = new StaticBodyContainer(position, extent, rotation);

		// Attach new entity
		if (element.hasAttribute("name"))
			container.attachToParent(parent, element.getAttribute("name"));
		else
			container.attachToParent(parent, container.genName());
		return container;

	}

}
