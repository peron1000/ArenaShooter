package arenashooter.engine.xmlReaders;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Entity;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Plateform;

public class MapXmlReader extends XmlReader {
	
	// Entities variables
	private static int iteratorEntite = 0;
	private static ArrayList<Entity> entities = new ArrayList<>();
	private static NodeList entitiesNodeList;
	
	// Informations variables
	private static int iteratorInfo = 0;
	private static ArrayList<Vec2f> spawn = new ArrayList<>();
	private static Vec2f gravity = new Vec2f();
	private static Vec4f cameraBounds = new Vec4f();
	private static NodeList infoNodeList;

	private MapXmlReader() {
		// Untouchable constructor
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

	private static void resetCollections() {
		entities.clear();
		spawn.clear();
	}
	
	public static void setMapToRead(String path) {
		parse(path);
		resetCollections();
		entities.add(new Sky(new Vec3f(.996, .9098, .003922), new Vec3f(.34901960784, .13725490196, .48235294118))); //TODO: Temp sky
		
		NodeList infoTag = document.getElementsByTagName("information");
		infoNodeList = infoTag.item(0).getChildNodes();
		iteratorInfo = 0;
		
		NodeList entitieTag = document.getElementsByTagName("entities");
		entitiesNodeList = entitieTag.item(0).getChildNodes();
		iteratorEntite = 0;
	}
	
	/**
	 * Load a new entity
	 * @return <i>true</i> if all entities are already loaded
	 */
	public static boolean loadNextEntity() {
		if(iteratorEntite < entitiesNodeList.getLength() && entitiesNodeList.item(iteratorEntite).getNodeType() == Node.ELEMENT_NODE) {
			Element entity = (Element) entitiesNodeList.item(iteratorEntite);
			if (entity.getNodeName() == "plateform") {
				Vec2f position = new Vec2f();
				Vec2f extent = new Vec2f();
				NodeList vectors = entity.getChildNodes();
				getVectorsEntity(vectors, position, extent);
				entities.add(new Plateform(position, extent));
			} else if(entity.getNodeName() == "weapon") {
				Vec2f position = new Vec2f();
				Vec2f extent = new Vec2f();
				NodeList vectors = entity.getChildNodes();
				getVectorsEntity(vectors, position, extent);
				// TODO : add the weapon in entities
			}
		}
		iteratorEntite++;
		return !(iteratorEntite < entitiesNodeList.getLength());
	}
	
	/**
	 * Load a new information
	 * @return <i>true</i> if all informations are already loaded
	 */
	public static boolean loadNextInformation() {
		if(iteratorInfo < infoNodeList.getLength() && infoNodeList.item(iteratorInfo).getNodeType() == Node.ELEMENT_NODE) {
			Element info = (Element) infoNodeList.item(iteratorInfo);
			
			if (info.getNodeName() == "spawn") {

				NodeList spawns = info.getChildNodes();
				for (int i = 0; i < spawns.getLength(); i++) {
					if (spawns.item(i).getNodeName() == "vecteur") {
						Element position = (Element) spawns.item(i);
						float x = Float.parseFloat(position.getAttribute("x"));
						float y = Float.parseFloat(position.getAttribute("y"));
						spawn.add(new Vec2f(x, y));
					}
				}
			} else if (info.getNodeName() == "gravity") {
				// Navigation into Vector part
				NodeList vector = info.getChildNodes();
				for (int i = 0; i < vector.getLength(); i++) {
					if (vector.item(i).getNodeName() == "vecteur") {
						Element position = (Element) vector.item(i);
						gravity.x = Float.parseFloat(position.getAttribute("x"));
						gravity.y = Float.parseFloat(position.getAttribute("y"));
					}
				}
			} else if (info.getNodeName() == "cameraBound") {
				// Navigation into CameraBound part
				cameraBounds.x = Float.parseFloat(info.getAttribute("x"));
				cameraBounds.y = Float.parseFloat(info.getAttribute("y"));
				cameraBounds.z = Float.parseFloat(info.getAttribute("z"));
				cameraBounds.w = Float.parseFloat(info.getAttribute("w"));
			}
			
		}
		iteratorInfo++;
		return !(iteratorInfo < infoNodeList.getLength());
	}

	public static ArrayList<Entity> getEntities() {
		return entities;
	}

	public static ArrayList<Vec2f> getSpawn() {
		return spawn;
	}

	public static Vec2f getGravity() {
		return gravity;
	}

	public static Vec4f getCameraBounds() {
		return cameraBounds;
	}
}
