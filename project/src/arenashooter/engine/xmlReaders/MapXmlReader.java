package arenashooter.engine.xmlReaders;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Entity;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.Plateform;
import arenashooter.entities.spatials.Text;

public class MapXmlReader extends XmlReader {

	// Entities variables
	private int iteratorEntite = 0;
	private ArrayList<Entity> entities = new ArrayList<>();
	private NodeList entitiesNodeList;

	// Informations variables
	private int iteratorInfo = 0;
	private ArrayList<Vec2f> spawn = new ArrayList<>();
	private Vec2f gravity = new Vec2f();
	private Vec4f cameraBounds = new Vec4f();
	private NodeList infoNodeList;

	public MapXmlReader(String path) {
		parse(path);
		resetCollections();

		NodeList infoTag = document.getElementsByTagName("information");
		infoNodeList = infoTag.item(0).getChildNodes();
		iteratorInfo = 0;

		NodeList entitieTag = document.getElementsByTagName("entities");
		entitiesNodeList = entitieTag.item(0).getChildNodes();
		iteratorEntite = 0;
	}

	/**
	 * Change position and extent to match with the vectors given in the NodeList
	 * 
	 * @param vectors
	 * @param position
	 * @param extent
	 */
	private void getVectorsEntity(NodeList vectors, Vec2f position, Vec2f extent) {
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

	private void resetCollections() {
		iteratorEntite = 0;
		iteratorInfo = 0;
		entities.clear();
		spawn.clear();
	}

	/**
	 * Load a new entity
	 * 
	 * @return <i>true</i> if all entities are already loaded
	 */
	public boolean loadNextEntity() {
		if (iteratorEntite < entitiesNodeList.getLength()
				&& entitiesNodeList.item(iteratorEntite).getNodeType() == Node.ELEMENT_NODE) {
			Element entity = (Element) entitiesNodeList.item(iteratorEntite);
			if (entity.getNodeName() == "plateform") {
				Vec2f position = new Vec2f();
				Vec2f extent = new Vec2f();
				NodeList vectors = entity.getChildNodes();
				getVectorsEntity(vectors, position, extent);
				entities.add(new Plateform(position, extent));
			} else if (entity.getNodeName() == "weapon") {
				Vec2f position = new Vec2f();
				Vec2f extent = new Vec2f();
				NodeList vectors = entity.getChildNodes();
				getVectorsEntity(vectors, position, extent);
				// TODO : add the weapon in entities
			} else if (entity.getNodeName().equals("text")) {
				Font font = Font.loadFont(entity.getAttribute("font"));
				if( font != null ) {
					Vec3f position = new Vec3f();
					Vec3f size = new Vec3f(1);
					String content = entity.getAttribute("content");
					NodeList vectors = entity.getChildNodes();
					for (int i = 0; i < vectors.getLength(); i++) {
						if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element vector = (Element) vectors.item(i);
							if(vector.hasAttribute("use") && vector.getAttribute("use").equals("position")) {
								position.x = Float.parseFloat(vector.getAttribute("x"));
								position.y = Float.parseFloat(vector.getAttribute("y"));
								position.y = Float.parseFloat(vector.getAttribute("z"));
							} else if(vector.hasAttribute("use") && vector.getAttribute("use").equals("scale")) {
								size.x = Float.parseFloat(vector.getAttribute("x"));
								size.y = Float.parseFloat(vector.getAttribute("y"));
							}
						}
					}
					entities.add(new Text(position, size, font, content));
				}
			} else if( entity.getNodeName().equals("mesh") ) {
				Vec3f position = new Vec3f();
				Quat rot = Quat.fromAngle(0);
				Vec3f scale = new Vec3f(1);
				String src = entity.getAttribute("src");
				
				NodeList vectors = entity.getChildNodes();
				for (int i = 0; i < vectors.getLength(); i++) {
					if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element vector = (Element) vectors.item(i);
						if(vector.hasAttribute("use") && vector.getAttribute("use").equals("position")) {
							position.x = Float.parseFloat(vector.getAttribute("x"));
							position.y = Float.parseFloat(vector.getAttribute("y"));
							position.z = Float.parseFloat(vector.getAttribute("z"));
						} else if(vector.hasAttribute("use") && vector.getAttribute("use").equals("rotation")) {
							float w = Float.parseFloat(vector.getAttribute("w"));
							float x = Float.parseFloat(vector.getAttribute("x"));
							float y = Float.parseFloat(vector.getAttribute("y"));
							float z = Float.parseFloat(vector.getAttribute("z"));
							rot = new Quat(x, y, z, w);
						} else if(vector.hasAttribute("use") && vector.getAttribute("use").equals("scale")) {
							scale.x = Float.parseFloat(vector.getAttribute("x"));
							scale.y = Float.parseFloat(vector.getAttribute("y"));
							scale.z = Float.parseFloat(vector.getAttribute("z"));
						}
					}
				}
				
				entities.add(new Mesh(position, rot, scale, src));
			}
		}
		iteratorEntite++;
		return !(iteratorEntite < entitiesNodeList.getLength());
	}

	/**
	 * Load a new information
	 * 
	 * @return <i>true</i> if all informations are already loaded
	 */
	public boolean loadNextInformation() {
		if (iteratorInfo < infoNodeList.getLength()
				&& infoNodeList.item(iteratorInfo).getNodeType() == Node.ELEMENT_NODE) {
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
			} else if (info.getNodeName() == "sky") {
				Vec3f bottom = new Vec3f();
				Vec3f top = new Vec3f();
				NodeList vectors = info.getChildNodes();
				for (int i = 0; i < vectors.getLength(); i++) {
					if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element vector = (Element) vectors.item(i);
						if (vector.hasAttribute("use") && vector.getAttribute("use").equals("bottom")) {
							bottom.x = Float.parseFloat(vector.getAttribute("x"));
							bottom.y = Float.parseFloat(vector.getAttribute("y"));
							bottom.z = Float.parseFloat(vector.getAttribute("z"));
						}
						if (vector.hasAttribute("use") && vector.getAttribute("use").equals("top")) {
							top.x = Float.parseFloat(vector.getAttribute("x"));
							top.y = Float.parseFloat(vector.getAttribute("y"));
							top.z = Float.parseFloat(vector.getAttribute("z"));
						}
					}
				}
				entities.add(new Sky(bottom, top));
			}

		}
		iteratorInfo++;
		return !(iteratorInfo < infoNodeList.getLength());
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public ArrayList<Vec2f> getSpawn() {
		return spawn;
	}

	public Vec2f getGravity() {
		return gravity;
	}

	public Vec4f getCameraBounds() {
		return cameraBounds;
	}
}
