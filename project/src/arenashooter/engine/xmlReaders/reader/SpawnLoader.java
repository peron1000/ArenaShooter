package arenashooter.engine.xmlReaders.reader;

import java.util.List;

import org.w3c.dom.Element;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.xmlReaders.XmlReader;
import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Spawner;

public class SpawnLoader implements EntitiesLoader<Spawner> {

	public static final SpawnLoader spawnerLoader = new SpawnLoader();

	private SpawnLoader() {
		// Only one instance
	}

	@Override
	public Spawner loadEntity(Element element, Entity parent) {
		// Position
		List<Element> vectors = MapXmlReader.getListElementByName("vector", element);
		Vec2f position = new Vec2f();
		if (vectors.size() != 1) {
			XmlReader.log.error("Spawn element needs a position vector");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = MapXmlReader.loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				default:
					XmlReader.log.error("Invalid vector in Spawn element");
					break;
				}
			}
		}

		// Cooldown
		double cooldown = 5;
		if (element.hasAttribute("cooldown")) {
			cooldown = Double.parseDouble(element.getAttribute("cooldown"));
		} else {
			XmlReader.log.error("Pas d'attribut cooldown");
		}

		// Spawner
		Spawner spawner = new Spawner(position, cooldown);

		if (element.hasAttribute("name"))
			spawner.attachToParent(parent, element.getAttribute("name"));
		else
			spawner.attachToParent(parent, spawner.genName());

		// Add this spawn to the arena's list if needed
		boolean playerSpawn = true;
		if (element.hasAttribute("playerSpawn"))
			playerSpawn = Boolean.parseBoolean(element.getAttribute("playerSpawn"));
		if (playerSpawn) {
			if (parent.getArena() != null)
				parent.getArena().playerSpawns.add(spawner);
			else
				XmlReader.log.error("Cannot create a player spawn: spawn's parent cannot access its containing Arena");
		}

		return spawner;
	}

}
