package arenashooter.engine.xmlReaders.reader;

import java.util.List;

import org.w3c.dom.Element;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.LightContainer;

public class PointLightLoader implements EntitiesLoader<LightContainer> {

	public static final PointLightLoader pointLightLoader = new PointLightLoader();

	private PointLightLoader() {
		// Only one instance
	}

	@Override
	public LightContainer loadEntity(Element element, Entity parent) {
		// vectors
		List<Element> vectors = MapXmlReader.getListElementByName("vector", element);
		Vec3f position = new Vec3f();
		Vec3f color = new Vec3f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			MapXmlReader.log.error("Point light element needs " + nbVec + " vectors");
			System.out.println(vectors.size());
		} else {
			for (Element vector : vectors) {
				XmlVector vec = MapXmlReader.loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "color":
					color = new Vec3f(vec.x, vec.y, vec.z);
					break;
				default:
					MapXmlReader.log.error("Invalid vector in Point light element");
					break;
				}
			}
		}

		// Light
		Light lightObject = new Light();
		lightObject.radius = Float.parseFloat(element.getAttribute("radius"));
		if (lightObject.radius < 0) {
			MapXmlReader.log.error("Invalid radius for Point light: " + lightObject.radius);
			lightObject.radius = 0;
		}
		lightObject.color.set(color);
		LightContainer container = new LightContainer(position, lightObject);
		if (element.hasAttribute("name")) {
			container.attachToParent(parent, element.getAttribute("name"));
		} else {
			container.attachToParent(parent, container.genName());
		}

		// Load animation
		if (element.hasAttribute("animation")) {
			container.savedAnimPath = element.getAttribute("animation");
			AnimationData animData = AnimationData.loadAnim(container.savedAnimPath);
			container.setAnim(new Animation(animData));
			container.playAnim();
		}
		return container;
	}

}
