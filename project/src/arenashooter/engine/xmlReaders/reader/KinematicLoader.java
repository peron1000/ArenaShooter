package arenashooter.engine.xmlReaders.reader;

import java.util.List;

import org.w3c.dom.Element;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.KinematicBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.xmlReaders.XmlReader;
import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.KinematicBodyContainer;

public class KinematicLoader implements EntitiesLoader<KinematicBodyContainer> {

	public static final KinematicLoader kinematicLoader = new KinematicLoader();

	private KinematicLoader() {
		// Only one instance
	}

	@Override
	public KinematicBodyContainer loadEntity(Element element, Entity parent) {
		// TODO: Add support for circular kinematic bodies
		// Read position and extent values
		List<Element> vectors = MapXmlReader.getListElementByName("vector", element);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			 XmlReader.log.error("KinematicBody element needs " + nbVec + " vectors");
		} else {
			for (Element vector : vectors) {
				XmlVector vec =  MapXmlReader.loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				case "extent":
					extent = new Vec2f(vec.x, vec.y);
					break;
				default:
					 XmlReader.log.error("Invalid vector in KinematicBody element");
					break;
				}
			}
		}

		// Read optional rotation
		double rotation = 0.0;
		if (element.hasAttribute("rotation"))
			rotation = Double.parseDouble(element.getAttribute("rotation"));

		KinematicBody body = new KinematicBody(new ShapeBox(extent), position, rotation, CollisionFlags.ARENA_KINEMATIC,
				1);

		// Create entity
		KinematicBodyContainer container = new KinematicBodyContainer(body);

		// Attach new entity
		if (element.hasAttribute("name"))
			container.attachToParent(parent, element.getAttribute("name"));
		else
			container.attachToParent(parent, container.genName());

		// Load animation
		if (element.hasAttribute("animation")) {
			container.savedAnimPath = element.getAttribute("animation");
			AnimationData animData = AnimationData.loadAnim(container.savedAnimPath);
			container.ignoreKillBounds = true;
			container.setAnim(new Animation(animData));
			container.playAnim();
		}
		return container;
	}

}
