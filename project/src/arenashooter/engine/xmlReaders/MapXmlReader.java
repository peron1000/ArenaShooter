package arenashooter.engine.xmlReaders;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.Physic;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Plateform;

public class MapXmlReader extends XmlReader {
	private static ArrayList<Entity> entities = new ArrayList<>();
	private static ArrayList<Vec2f> spawn = new ArrayList<>();
	private static Vec2f gravity = new Vec2f();
	private static Vec4f cameraBounds = new Vec4f();

	private MapXmlReader() {
		// TODO Auto-generated constructor stub
	}

	public static Map read(String path) {
		parse(path);
		resetCollections();

		readInformations(document.getElementsByTagName("information"));
		
		readEntities(document.getElementsByTagName("entities"));
		
		Map map = new Map(entities);
		map.cameraBounds = cameraBounds;
		map.spawn = spawn;
		map.gravity = gravity;
//		Physic.globalForce = map.gravity; //TODO: Do this in a cleaner way

		return map;
	}

	private static void readEntities(NodeList entity) {
		entities.add(new Sky(new Vec3f(.996, .9098, .003922), new Vec3f(.34901960784, .13725490196, .48235294118))); //TODO: Temp sky
		
		NodeList entitiesNodes = ((Element) entity.item(0)).getChildNodes();
		for (int i = 0; i < entitiesNodes.getLength(); i++) {
			if (entitiesNodes.item(i).getNodeName() == "plateform") {

				// Navigation into Platforms part

				Vec2f position = new Vec2f();
				Vec2f extent = new Vec2f();
				NodeList vectors = entitiesNodes.item(i).getChildNodes();
				getVectorsEntity(vectors, position, extent);
				entities.add(new Plateform(position, extent));
			} else if(entitiesNodes.item(i).getNodeName() == "weapon") {
				Vec2f position = new Vec2f();
				Vec2f extent = new Vec2f();
				NodeList vectors = entitiesNodes.item(i).getChildNodes();
				getVectorsEntity(vectors, position, extent);
				// TODO : add the weapon in entities
			}
		}
	}
	
	/**
	 * Change position and extent to match with the vectors given in the NodeList
	 * @param vectors
	 * @param position
	 * @param extent
	 */
	private static void getVectorsEntity(NodeList vectors , Vec2f position , Vec2f extent) {
		for (int i = 0; i < vectors.getLength(); i++) {
			if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element vector = (Element) vectors.item(i);
				if (vector.hasAttribute("use") && vector.getAttribute("use").equals("position")) {
					position.x = Float.parseFloat(vector.getAttribute("x"));
					position.y = Float.parseFloat(vector.getAttribute("y"));
				}
				if (vector.hasAttribute("use") && vector.getAttribute("use").equals("extent")) {
					extent.x = Float.parseFloat(vector.getAttribute("x"));
					extent.y = Float.parseFloat(vector.getAttribute("y"));
				}
			}
		}
	}

	private static void readInformations(NodeList informations) {
		NodeList information = ((Element) informations.item(0)).getChildNodes();
		for (int j = 0; j < information.getLength(); j++) {
			if (information.item(j).getNodeName() == "spawn") {

				// Navigation into Spawn part

				NodeList spawns = information.item(j).getChildNodes();
				for (int k = 0; k < spawns.getLength(); k++) {
					if (spawns.item(k).getNodeName() == "vecteur") {
						Element position = (Element) spawns.item(k);
						float x = Float.parseFloat(position.getAttribute("x"));
						float y = Float.parseFloat(position.getAttribute("y"));
						spawn.add(new Vec2f(x, y));
					}
				}
			} else if (information.item(j).getNodeName() == "gravity") {
				// Navigation into Vector part
				NodeList vector = information.item(j).getChildNodes();
				for (int k = 0; k < vector.getLength(); k++) {
					if (vector.item(k).getNodeName() == "vecteur") {
						Element position = (Element) vector.item(k);
						gravity.x = Float.parseFloat(position.getAttribute("x"));
						gravity.y = Float.parseFloat(position.getAttribute("y"));
					}
				}
			} else if (information.item(j).getNodeName() == "cameraBound") {
				// Navigation into CameraBound part
				Element cameraBound = (Element) information.item(j);
				cameraBounds.x = Float.parseFloat(cameraBound.getAttribute("x"));
				cameraBounds.y = Float.parseFloat(cameraBound.getAttribute("y"));
				cameraBounds.z = Float.parseFloat(cameraBound.getAttribute("z"));
				cameraBounds.w = Float.parseFloat(cameraBound.getAttribute("w"));
			}
		}
	}

	private static void resetCollections() {
		entities.clear();
		spawn.clear();
	}
}
