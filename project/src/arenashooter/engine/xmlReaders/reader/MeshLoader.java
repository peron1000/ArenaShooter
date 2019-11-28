package arenashooter.engine.xmlReaders.reader;

import java.util.List;

import org.w3c.dom.Element;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.xmlReaders.XmlReader;
import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Mesh;

public class MeshLoader implements EntitiesLoader<Mesh> {

	public static final MeshLoader meshLoader = new MeshLoader();

	private MeshLoader() {
		// Only one instance
	}

	@Override
	public Mesh loadEntity(Element element, Entity parent) {
		// vectors
		List<Element> vectors = MapXmlReader.getListElementByName("vector", element);
		Vec3f position = new Vec3f(), scale = new Vec3f();
		Quat rotation = new Quat();
		int nbVec = 3;
		if (vectors.size() != nbVec) {
			XmlReader.log.error("Mesh element needs " + nbVec + " vectors [src=" + element.getAttribute("src") + "]");
			System.out.println(vectors.size());
		} else {
			for (Element vector : vectors) {
				XmlVector vec = MapXmlReader.loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "scale":
					scale = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "rotation":
					rotation = MapXmlReader.readRotation(vector);
					break;
				default:
					XmlReader.log.error("Invalid vector in Mesh element");
					break;
				}
			}
		}

		// Mesh
		String modelPath = element.getAttribute("src");
		Mesh m = new Mesh(position, rotation, scale, modelPath);
		if (element.hasAttribute("name")) {
			m.attachToParent(parent, element.getAttribute("name"));
		} else {
			m.attachToParent(parent, m.genName());
		}
		
		// Load animation
		if (element.hasAttribute("animation")) {
			m.savedAnimPath = element.getAttribute("animation");
			AnimationData animData = AnimationData.loadAnim(m.savedAnimPath);
			m.setAnim(new Animation(animData));
			m.playAnim();
		}
		return m;
	}

}
