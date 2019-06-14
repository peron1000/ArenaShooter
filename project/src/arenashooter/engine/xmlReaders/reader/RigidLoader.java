package arenashooter.engine.xmlReaders.reader;

import java.util.List;

import org.w3c.dom.Element;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.RigidBodyContainer;

public class RigidLoader implements EntitiesLoader<RigidBodyContainer> {

	public static final RigidLoader rigidLoader = new RigidLoader();

	private RigidLoader() {
		// Only one instance
	}

	@Override
	public RigidBodyContainer loadEntity(Element element, Entity parent) {
		// Vectors
		List<Element> vectors = MapXmlReader.getListElementByName("vector", element);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			MapXmlReader.log.error("RigidBody element needs " + nbVec + " vectors");
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
					MapXmlReader.log.error("Invalid vector in RigidBody element");
					break;
				}
			}
		}

		CollisionFlags flags = CollisionFlags.RIGIDBODY;

		double rotation = 0.0;
		if (element.hasAttribute("rotation"))
			rotation = Double.parseDouble(element.getAttribute("rotation"));

		float density = 1;
		if (element.hasAttribute("density"))
			density = Float.parseFloat(element.getAttribute("density"));

		float friction = 0.8f;
		if (element.hasAttribute("friction"))
			friction = Float.parseFloat(element.getAttribute("friction"));

		float radius = -1;
		if (element.hasAttribute("radius"))
			radius = Float.parseFloat(element.getAttribute("radius"));

		RigidBody body;
		if (element.hasAttribute("radius")) {
			body = new RigidBody(new ShapeDisk(radius), position, rotation, flags, density, friction);
		} else
			body = new RigidBody(new ShapeBox(extent), position, rotation, flags, density, friction);

		// Rigid
		RigidBodyContainer container = new RigidBodyContainer(body);
		if (element.hasAttribute("name"))
			container.attachToParent(parent, element.getAttribute("name"));
		else
			container.attachToParent(parent, container.genName());
		return container;

	}

}
