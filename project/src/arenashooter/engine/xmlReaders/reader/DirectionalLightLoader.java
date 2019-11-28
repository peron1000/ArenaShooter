package arenashooter.engine.xmlReaders.reader;

import java.util.List;

import org.w3c.dom.Element;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.xmlReaders.XmlReader;
import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.LightContainer;

public class DirectionalLightLoader implements EntitiesLoader<LightContainer> {

	public static final DirectionalLightLoader directionalLightLoader = new DirectionalLightLoader();

	private DirectionalLightLoader() {
		// Only one instance
	}

	@Override
	public LightContainer loadEntity(Element element, Entity parent) {
		// vectors
		List<Element> vectors = MapXmlReader.getListElementByName("vector", element);
		Vec3f position = new Vec3f();
		Vec3f color = new Vec3f();
		Quat rotation = new Quat();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			XmlReader.log.error("Directional light element needs " + nbVec + " vectors");
			System.out.println(vectors.size());
		} else {
			for (Element vector : vectors) {
				XmlVector vec = MapXmlReader.loadVector(vector);
				switch (vec.use) {
				case "color":
					color = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "rotation":
					rotation = XmlReader.readRotation(vector);
					break;
				default:
					XmlReader.log.error("Invalid vector in Directional light element");
					break;
				}
			}
		}

		// Light
		Light lightObject = new Light();
		lightObject.radius = -1;
		lightObject.color.set(color);
		LightContainer container = new LightContainer(position, lightObject);
		container.localRotation.set(rotation);
		if (element.hasAttribute("name")) {
			container.attachToParent(parent, element.getAttribute("name"));
		} else {
			container.attachToParent(parent, container.genName());
		}

		// Load animation
		if (element.hasAttribute("animation")) {
			container.savedAnimPath = element.getAttribute("animation");
			AnimationData animData = AnimationData.loadAnim(element.getAttribute("animation"));
			container.setAnim(new Animation(animData));
			container.playAnim();
		}
		return container;
	}

}
