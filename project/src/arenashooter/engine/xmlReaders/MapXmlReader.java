package arenashooter.engine.xmlReaders;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.itemCollection.ItemCollection;
import arenashooter.engine.itemCollection.ItemConcept;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Entity;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.Plateform;
import arenashooter.entities.spatials.TextSpatial;

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

	// Items variables
	private int iteratorItems = 0;
	private ItemCollection<ItemConcept> itemCollection = new ItemCollection<ItemConcept>();
	private NodeList itemNodeList;

	public MapXmlReader(String path) {
		parse(path);
		resetCollections();

		NodeList infoTag = document.getElementsByTagName("information");
		if (infoTag.item(0) != null)
			infoNodeList = infoTag.item(0).getChildNodes();
		iteratorInfo = 0;

		NodeList entitieTag = document.getElementsByTagName("entities");
		if (entitieTag.item(0) != null)
			entitiesNodeList = entitieTag.item(0).getChildNodes();
		iteratorEntite = 0;

		NodeList itemTag = document.getElementsByTagName("items");
		if (itemTag.item(0) != null) {
			itemNodeList = itemTag.item(0).getChildNodes();
		}
		iteratorItems = 0;
	}

	/**
	 * Change position and extent to match with the vectors given in the NodeList
	 * 
	 * @param vectors
	 * @param position
	 * @param extent
	 */
	private static void getVectorsEntity(NodeList vectors, Vec2f position, Vec2f extent) {
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

	/**
	 * Initialization of all collections
	 */
	private void resetCollections() {
		iteratorEntite = 0;
		iteratorInfo = 0;
		iteratorItems = 0;
		entities.clear();
		spawn.clear();
		itemCollection = new ItemCollection<ItemConcept>();
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

			Entity newEntity = null;

			switch (entity.getNodeName()) {
			case "entity":
				newEntity = new Entity();
				break;
			case "plateform":
				newEntity = loadPlateform(entity);
				break;
			case "mesh":
				newEntity = loadMesh(entity);
				break;
			case "text":
				newEntity = loadText(entity);
				break;
			default:
				break;
			}

			if (newEntity != null)
				entities.add(newEntity);
			else
				System.out.println(entity.getNodeName());
		}
		iteratorEntite++;
		return !(iteratorEntite < entitiesNodeList.getLength());
	}

	/**
	 * Load a new item
	 * 
	 * @return <i>true</i> if all items are already loaded
	 */
	public boolean loadNextItem() {
		if (itemNodeList == null)
			return true;
		if (iteratorItems < itemNodeList.getLength()
				&& itemNodeList.item(iteratorItems).getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) itemNodeList.item(iteratorItems);

			ItemConcept ic = null;

			switch (element.getNodeName()) {
			case "weapon":
				ic = loadWeapon(element);
				break;
			case "equipement":
				break;
			case "melee":
				ic = loadMelee(element);
				break;
			default:
				break;
			}
			if (ic != null) {
				itemCollection.add(ic);
			} else {
				System.err.println("Error in MapXmlReader on element read : " + element.getNodeName());
			}
		}
		iteratorItems++;
		return !(iteratorItems < itemNodeList.getLength());
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

			Element vecteur = null;

			switch (info.getNodeName()) {
			case "spawn":
				try {
					vecteur = getSingleElement(info, "vecteur");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (vecteur != null) {
					spawn.add(loadVecteurXY(vecteur));
				}
				break;
			case "gravity":
				try {
					vecteur = getSingleElement(info, "vecteur");
				} catch (Exception e) {
					System.out.println("watch out ! gravity null !!!");
					e.printStackTrace();
				}
				if (vecteur != null) {
					gravity = loadVecteurXY(vecteur);
				} else {
					gravity = new Vec2f(0, 9.81);
				}
				break;
			case "cameraBound":
				cameraBounds = loadVecteurWXYZ(info);
				break;
			case "sky":
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
				break;
			default:
				break;
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

	public ItemCollection<ItemConcept> getItemCollection() {
		return itemCollection;
	}

	private static Plateform loadPlateform(Element entity) {
		Vec2f position = new Vec2f();
		Vec2f extent = new Vec2f();
		NodeList vectors = entity.getChildNodes();
		getVectorsEntity(vectors, position, extent);
		return new Plateform(position, extent);
	}

	private static ItemConcept loadWeapon(Element entity) {

		// ItemConcept required
		Vec2f colliderExtent = new Vec2f();
		try {
			Element vecteur = getSingleElement(entity, "vecteur");
			colliderExtent.x = Float.parseFloat(vecteur.getAttribute("x"));
			colliderExtent.y = Float.parseFloat(vecteur.getAttribute("y"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String type = entity.getAttribute("type");
		String proba = entity.getAttribute("proba");
		String size = entity.getAttribute("size");
		ItemConcept ic = new ItemConcept(type, Double.parseDouble(proba), colliderExtent, Double.parseDouble(size));

		// ItemConcept implied
		if (entity.hasAttribute("name")) {
			ic.name = entity.getAttribute("name");
		}
		if (entity.hasAttribute("damage")) {
			ic.damage = Float.parseFloat(entity.getAttribute("damage"));
		}
		if (entity.hasAttribute("spritePath")) {
			ic.spritePath = entity.getAttribute("spritePath");
		}
		if (entity.hasAttribute("bangSound")) {
			ic.bangSound = entity.getAttribute("bangSound");
		}
		if (entity.hasAttribute("pickupSound")) {
			ic.pickupSound = entity.getAttribute("pickupSound");
		}
		if (entity.hasAttribute("chargeSound")) {
			ic.chargeSound = entity.getAttribute("chargeSound");
		}
		if (entity.hasAttribute("noAmmoSound")) {
			ic.noAmmoSound = entity.getAttribute("noAmmoSound");
		}
		if (entity.hasAttribute("fireRate")) {
			ic.fireRate = Double.parseDouble("fireRate");
		}
		if (entity.hasAttribute("cannonLength")) {
			ic.cannonLength = Double.parseDouble("cannonLength");
		}
		if (entity.hasAttribute("recoil")) {
			ic.recoil = Double.parseDouble("recoil");
		}
		if (entity.hasAttribute("thrust")) {
			ic.thrust = Double.parseDouble("thrust");
		}
		if (entity.hasAttribute("tpsCharge")) {
			ic.tpsCharge = Double.parseDouble("tpsCharge");
		}
		if (entity.hasAttribute("bulletSpeed")) {
			ic.bulletSpeed = Float.parseFloat(entity.getAttribute("bulletSpeed"));
		}
		if (entity.hasAttribute("bulletType")) {
			ic.bulletType = Integer.parseInt(entity.getAttribute("bulletType"));
		}

		return ic;
	}
	
	private static ItemConcept loadMelee(Element entity) {
		// ItemConcept required
				Vec2f colliderExtent = new Vec2f();
				try {
					Element vecteur = getSingleElement(entity, "vecteur");
					colliderExtent.x = Float.parseFloat(vecteur.getAttribute("x"));
					colliderExtent.y = Float.parseFloat(vecteur.getAttribute("y"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				String type = entity.getAttribute("type");
				String proba = entity.getAttribute("proba");
				String size = entity.getAttribute("size");
				ItemConcept ic = new ItemConcept(type, Double.parseDouble(proba), colliderExtent, Double.parseDouble(size));
		
				// ItemConcept implied
				if (entity.hasAttribute("name")) {
					ic.name = entity.getAttribute("name");
				}
				if (entity.hasAttribute("damage")) {
					ic.damage = Float.parseFloat(entity.getAttribute("damage"));
				}
				if (entity.hasAttribute("bangSound")) {
					ic.bangSound = entity.getAttribute("bangSound");
				}
				if (entity.hasAttribute("spritePath")) {
					ic.spritePath = entity.getAttribute("spritePath");
				}
				if (entity.hasAttribute("fireRate")) {
					ic.fireRate = Double.parseDouble(entity.getAttribute("fireRate"));
				}
				return ic;

	}

	private static Mesh loadMesh(Element entity) {
		Vec3f position = new Vec3f();
		Quat rot = Quat.fromAngle(0);
		Vec3f scale = new Vec3f(1);
		String src = entity.getAttribute("src");

		NodeList vectors = entity.getChildNodes();
		for (int i = 0; i < vectors.getLength(); i++) {
			if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element vector = (Element) vectors.item(i);
				if (vector.hasAttribute("use") && vector.getAttribute("use").equals("position")) {
					position.x = Float.parseFloat(vector.getAttribute("x"));
					position.y = Float.parseFloat(vector.getAttribute("y"));
					position.z = Float.parseFloat(vector.getAttribute("z"));
				} else if (vector.hasAttribute("use") && vector.getAttribute("use").equals("rotation")) {
					float w = Float.parseFloat(vector.getAttribute("w"));
					float x = Float.parseFloat(vector.getAttribute("x"));
					float y = Float.parseFloat(vector.getAttribute("y"));
					float z = Float.parseFloat(vector.getAttribute("z"));
					rot = new Quat(x, y, z, w);
				} else if (vector.hasAttribute("use") && vector.getAttribute("use").equals("scale")) {
					scale.x = Float.parseFloat(vector.getAttribute("x"));
					scale.y = Float.parseFloat(vector.getAttribute("y"));
					scale.z = Float.parseFloat(vector.getAttribute("z"));
				}
			}
		}

		return new Mesh(position, rot, scale, src);
	}

	private static TextSpatial loadText(Element entity) {
		Font font = Font.loadFont(entity.getAttribute("font"));
		if (font != null) {
			Vec3f position = new Vec3f();
			Vec3f size = new Vec3f(1);
			String content = entity.getAttribute("content");
			NodeList vectors = entity.getChildNodes();
			for (int i = 0; i < vectors.getLength(); i++) {
				if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element vector = (Element) vectors.item(i);
					if (vector.hasAttribute("use") && vector.getAttribute("use").equals("position")) {
						position.x = Float.parseFloat(vector.getAttribute("x"));
						position.y = Float.parseFloat(vector.getAttribute("y"));
						position.y = Float.parseFloat(vector.getAttribute("z"));
					} else if (vector.hasAttribute("use") && vector.getAttribute("use").equals("scale")) {
						size.x = Float.parseFloat(vector.getAttribute("x"));
						size.y = Float.parseFloat(vector.getAttribute("y"));
					}
				}
			}
			Text text = new Text(font, Text.TextAlignH.CENTER, content); // TODO: Load text align from XML
			return new TextSpatial(position, size, text);
		}
		return null;
	}

	private static Element getSingleElement(Element parent, String elementName) throws Exception {
		NodeList list = parent.getElementsByTagName(elementName);
		if (list.getLength() == 1) {
			return (Element) list.item(0);
		}
		throw new Exception("Not single element (Nathan exception)");
	}

	private static Vec2f loadVecteurXY(Element vecteur) {
		float x = Float.parseFloat(vecteur.getAttribute("x"));
		float y = Float.parseFloat(vecteur.getAttribute("y"));
		return new Vec2f(x, y);
	}

	private static Vec4f loadVecteurWXYZ(Element vecteur) {
		float x = Float.parseFloat(vecteur.getAttribute("x"));
		float y = Float.parseFloat(vecteur.getAttribute("y"));
		float z = Float.parseFloat(vecteur.getAttribute("z"));
		float w = Float.parseFloat(vecteur.getAttribute("w"));
		return new Vec4f(x, y, z, w);
	}
}
