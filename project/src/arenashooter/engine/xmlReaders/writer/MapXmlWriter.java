package arenashooter.engine.xmlReaders.writer;

import java.io.File;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.Sky;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.KinematicBodyContainer;
import arenashooter.entities.spatials.LightContainer;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.StaticBodyContainer;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Melee;
import arenashooter.entities.spatials.items.Shotgun;
import arenashooter.game.Main;

public class MapXmlWriter {
	public static final MapXmlWriter writer = new MapXmlWriter();
	public static Document doc;

	private MapXmlWriter() { }

	public static void exportArena(Arena arena, String name) {
		try {
			// Creation / instantiation file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// creation Arena dans Xml
			// Document
			doc = docBuilder.newDocument();

			Element map = doc.createElement("map");
			doc.appendChild(map);

			Element info = doc.createElement("information");
			map.appendChild(info);

			// Items
			for (Entry<String, Item> entry : arena.spawnList.entrySet()) {
				Element item = doc.createElement("item");
				if (entry instanceof Shotgun) {
					new ShootgunXml(doc, item, (Shotgun) entry.getValue());
				} else if (entry instanceof Gun) {
					new GunXml(doc, item, (Gun) entry.getValue());
				} else if (entry instanceof Melee) {
					new MeleeXml(doc, item, (Melee) entry.getValue());
				} else {
					Main.log.warn("Unsupported item "+entry.getValue().getClass());
					continue;
				}
				info.appendChild(item);
			}

			//Gravity
			Element gravity = doc.createElement("gravity");
			gravity.setAttribute("x", String.valueOf(arena.gravity.x));
			gravity.setAttribute("y", String.valueOf(arena.gravity.y));
			info.appendChild(gravity);

			//Ambient light
			Element ambientLight = doc.createElement("ambientLight");
			ambientLight.setAttribute("r", String.valueOf(arena.ambientLight.x));
			ambientLight.setAttribute("g", String.valueOf(arena.ambientLight.y));
			ambientLight.setAttribute("b", String.valueOf(arena.ambientLight.z));
			info.appendChild(ambientLight);

			//Camera pos
			Element camera = doc.createElement("cameraPos");
			camera.setAttribute("x", String.valueOf(arena.cameraBasePos.x));
			camera.setAttribute("y", String.valueOf(arena.cameraBasePos.y));
			camera.setAttribute("z", String.valueOf(arena.cameraBasePos.z));
			info.appendChild(camera);

			//Kill bounds
			Element killbounds = doc.createElement("killBounds");
			killbounds.setAttribute("minX", "" + arena.killBound.x);
			killbounds.setAttribute("minY", "" + arena.killBound.y);
			killbounds.setAttribute("maxX", "" + arena.killBound.z);
			killbounds.setAttribute("maxY", "" + arena.killBound.w);
			info.appendChild(killbounds);

			// Sky
			Element sky = doc.createElement("sky");
			boolean foundSky = false;
			for (Entry<String, Entity> entry : arena.getChildren().entrySet()) {
				if (entry.getValue() instanceof Sky) {
					if(foundSky) {
						Main.log.warn("Multiple skies found in Arena, only the first has been written");
					} else {
						Sky s = (Sky) entry.getValue();

						sky.appendChild( createVec3(doc, "top", s.material.getParamVec3f("colorTop")) );
						sky.appendChild( createVec3(doc, "bottom", s.material.getParamVec3f("colorBot")) );

						foundSky = true;
					}
				}
			}
			info.appendChild(sky);

			//Write entities
			Element entities = doc.createElement("entities");
			map.appendChild(entities);
			addChildren(entities, arena, arena);
			
			// Create arenas folder if necessary
			File file1 = new File("data/mapXML");
			if (!file1.exists()) {
				file1.mkdirs();
			}

			File file = new File("data/mapXML/" + name + ".xml");

			//Set file encoding
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			//Enable indentation
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			//Add DOCTYPE
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType docType = domImpl.createDocumentType("doctype", "", "mapDTD.dtd");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());

			// Write file
			StreamResult resultat = new StreamResult(file);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, resultat);

			Main.log.info("Successfully exported Arena to "+file.getPath());

		} catch ( ParserConfigurationException pce) {
			pce.printStackTrace();
			System.out.println(pce);
		} catch (TransformerException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	// <!ELEMENT entities
	// (entity|spawn|mesh|directionalLight|pointLight|text|rigid|static|kinematic|jointPin)*>

	/**
	 * Write entities
	 * @param entitiesElem Element to fill
	 * @param entity parent Entity
	 * @param arena containing Arena
	 */
	private static void addChildren(Element entitiesElem, Entity parent, Arena arena) {
		if( parent.getChildren() == null || parent.getChildren().isEmpty() ) return;

		/** Current entity's Element */
		Element elem;

		for (Entry<String, Entity> entry : parent.getChildren().entrySet()) {
			String entityName = entry.getKey();

			// Camera
			if (entry.getValue() instanceof Camera) {
				continue;
			}

			// Sky
			if (entry.getValue() instanceof Sky) {
				continue;
			}

			// Timer
			if (entry.getValue() instanceof Timer) {
				continue;
			}

			// Spawner
			else if (entry.getValue() instanceof Spawner) {
				Spawner entity = ((Spawner)entry.getValue());

				elem = doc.createElement("spawn");

				//Player Spawn
				elem.setAttribute("playerSpawn", String.valueOf( arena.playerSpawns.contains(entity) ));

				//Cooldown
				elem.setAttribute("cooldown", String.valueOf( entity.getCooldown() ) );

				//Position
				elem.appendChild( createVec2(doc, "position", entity.localPosition) );

				//Item references
				for (Entry<String, Integer> itemEntry : entity.getAvailableItems().entrySet()) {
					Element itemRef = doc.createElement("itemRef");
					itemRef.setAttribute("item", itemEntry.getKey());
					itemRef.setAttribute("proba", String.valueOf(itemEntry.getValue()));
					elem.appendChild(itemRef);
				}
			}

			// Mesh
			else if (entry.getValue() instanceof Mesh) {
				Mesh entity = ((Mesh) entry.getValue());

				if (entity.getModelPath() == null) continue; //Mesh was created from code and cannot be saved in XML

				elem = doc.createElement("mesh");

				elem.setAttribute("src", entity.getModelPath());

				elem.appendChild( createVec3(doc, "position", entity.localPosition) );

				elem.appendChild( createVec3(doc, "rotation", entity.localRotation.toEuler()) );

				elem.appendChild( createVec3(doc, "scale", entity.scale) );
			}

			// Lights
			else if (entry.getValue() instanceof LightContainer) {
				LightContainer entity = ((LightContainer)entry.getValue());

				switch(entity.getLight().getType()) {
				case POINT:
					elem = doc.createElement("pointLight");

					elem.appendChild(createVec3(doc, "position", entity.localPosition) );

					elem.setAttribute("radius", String.valueOf(entity.getLight().radius));

					break;
				case DIRECTIONAL:
					elem = doc.createElement("directionalLight");

					elem.appendChild( createVec3(doc, "rotation", entity.localRotation.toEuler()) );

					break;
				default:
					Main.log.warn("Unsupported light type for \""+entityName+"\" will not be saved to XML");
					elem = null;
					break;
				}

				if(elem != null) elem.appendChild( createVec3(doc, "color", entity.getLight().color) );
			}

			// Text
			else if (entry.getValue() instanceof TextSpatial) {
				TextSpatial entity = ((TextSpatial)entry.getValue());

				elem = doc.createElement("text");

				elem.appendChild( createVec3(doc, "position", entity.localPosition) );

				elem.appendChild( createVec3(doc, "rotation", entity.localRotation.toEuler()) );

				elem.setAttribute("content", entity.getText().getText());

				elem.setAttribute("font", entity.getText().getFont().getPath());
			}

			// Rigid body
			else if (entry.getValue() instanceof RigidBodyContainer) {
				RigidBodyContainer entity = (RigidBodyContainer) entry.getValue();

				elem = doc.createElement("rigid");

				//Position
				elem.appendChild( createVec2(doc, "position", entity.getWorldPos()) );

				//Rotation
				elem.setAttribute( "rotation", String.valueOf(entity.getWorldRot()) );

				//Shape
				if(entity.getBody().getShape() instanceof ShapeBox)
					elem.appendChild( createVec2(doc, "extent", ((ShapeBox) entity.getBody().getShape()).getExtent()) );
				else if (entity.getBody().getShape() instanceof ShapeDisk)
					elem.setAttribute("radius", String.valueOf( ((ShapeDisk) entity.getBody().getShape()).getRadius() ));

				//TODO: Write density and friction
			}

			// Static body
			else if (entry.getValue() instanceof StaticBodyContainer) {
				StaticBodyContainer entity = (StaticBodyContainer) entry.getValue();

				elem = doc.createElement("static");

				//Position
				elem.appendChild( createVec2(doc, "position", entity.getWorldPos()) );

				//Rotation
				elem.setAttribute( "rotation", String.valueOf(entity.getWorldRot()) );

				//Shape
				if(entity.getBody().getShape() instanceof ShapeBox)
					elem.appendChild( createVec2(doc, "extent", ((ShapeBox) entity.getBody().getShape()).getExtent()) );
				else if (entity.getBody().getShape() instanceof ShapeDisk)
					elem.setAttribute("radius", String.valueOf( ((ShapeDisk) entity.getBody().getShape()).getRadius() ));
			}

			// Kinematic body
			else if (entry.getValue() instanceof KinematicBodyContainer) {
				KinematicBodyContainer entity = (KinematicBodyContainer) entry.getValue();

				elem = doc.createElement("static");

				//Position
				elem.appendChild( createVec2(doc, "position", entity.getWorldPos()) );

				//Rotation
				elem.setAttribute( "rotation", String.valueOf(entity.getWorldRot()) );

				//Shape
				if(entity.getBody().getShape() instanceof ShapeBox)
					elem.appendChild( createVec2(doc, "extent", ((ShapeBox) entity.getBody().getShape()).getExtent()) );
				else if (entity.getBody().getShape() instanceof ShapeDisk)
					elem.setAttribute("radius", String.valueOf( ((ShapeDisk) entity.getBody().getShape()).getRadius() ));

				//				if(entity.getAnim() != null) elem.setAttribute("animation", entity.getAnim().); //TODO: Get anim path if it has been loaded from XML
			}

			//TODO: Add other classes while being careful of condition order (everything is an instance of Entity)

			// Spatial
			else if (entry.getValue() instanceof Spatial) {
				Spatial entity = (Spatial) entry.getValue();

				elem = doc.createElement("spatial");

				elem.appendChild( createVec2(doc, "position", entity.localPosition) );

				elem.setAttribute("rotation", String.valueOf(entity.localRotation));
			}

			// Entity
			else {
				elem = doc.createElement("entity");
			}

			//Add current entity to parent
			entitiesElem.appendChild(elem);

			//Name
			elem.setAttribute("name", entityName);

			//Write children
			if( !entry.getValue().getChildren().isEmpty() ) {
				Element children = doc.createElement("entities");
				elem.appendChild(children);
				addChildren(children, entry.getValue(), arena);
			}

		}

	}

	/**
	 * Create a vector xml element from a Vec2f
	 * @param doc
	 * @param use
	 * @param vec
	 * @return new Element
	 */
	private static Element createVec2(Document doc, String use, Vec2f vec) {
		return createVec2(doc, use, vec.x, vec.y);
	}

	/**
	 * Create a vector xml element from a set of (x, y) coordinates
	 * @param doc
	 * @param use
	 * @param x
	 * @param y
	 * @return new Element
	 */
	private static Element createVec2(Document doc, String use, double x, double y) {
		Element elem = doc.createElement("vector");
		elem.setAttribute("use", use);
		elem.setAttribute("x", String.valueOf(x));
		elem.setAttribute("y", String.valueOf(y));
		return elem;
	}

	/**
	 * Create a vector xml element from a Vec3f
	 * @param doc
	 * @param use
	 * @param vec
	 * @return new Element
	 */
	private static Element createVec3(Document doc, String use, Vec3f vec) {
		return createVec3(doc, use, vec.x, vec.y, vec.z);
	}

	/**
	 * Create a vector xml element from a set of (x, y, z) coordinates
	 * @param doc
	 * @param use
	 * @param x
	 * @param y
	 * @param z
	 * @return new Element
	 */
	private static Element createVec3(Document doc, String use, double x, double y, double z) {
		Element elem = doc.createElement("vector");
		elem.setAttribute("use", use);
		elem.setAttribute("x", String.valueOf(x));
		elem.setAttribute("y", String.valueOf(y));
		elem.setAttribute("z", String.valueOf(z));
		return elem;
	}

	/**
	 * Create a vector xml element from a Vec4f
	 * @param doc
	 * @param use
	 * @param vec
	 * @return new Element
	 */
	private static Element createVec4(Document doc, String use, Vec4f vec) {
		return createVec4(doc, use, vec.x, vec.y, vec.z, vec.w);
	}

	//TODO: createVec4 from Quat

	/**
	 * Create a vector xml element from a set of (x, y, z, w) coordinates
	 * @param doc
	 * @param use
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return new Element
	 */
	private static Element createVec4(Document doc, String use, double x, double y, double z, double w) {
		Element elem = doc.createElement("vector");
		elem.setAttribute("use", use);
		elem.setAttribute("x", String.valueOf(x));
		elem.setAttribute("y", String.valueOf(y));
		elem.setAttribute("z", String.valueOf(z));
		elem.setAttribute("w", String.valueOf(w));
		return elem;
	}


	//	public static void main(String argv[]) {
	//		try {
	//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	//			Document doc = docBuilder.newDocument();
	//			System.out.print("Nom map : ");
	//			Scanner sc = new Scanner(System.in);
	//			String line = sc.nextLine();
	//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	//			Transformer transformer = transformerFactory.newTransformer();
	//			DOMSource source = new DOMSource(doc);
	//			File file = new File("data/testMap");
	//			if (!file.exists()) {
	//				file.mkdirs();
	//			}
	//			StreamResult result = new StreamResult(new File("data/testMap/" + line + ".xml"));
	//
	//			// Creation of the test arena
	//			Arena arena = new Arena();
	//			Mesh mesh = new Mesh(new Vec3f(), "data/meshes/item_pickup/weapon_pickup.obj");
	//			mesh.attachToParent(arena, "meshTest");
	//
	//			transformer.transform(source, result);
	//			System.out.println("File saved!");
	//		} catch (ParserConfigurationException pce) {
	//			pce.printStackTrace();
	//		} catch (TransformerException tfe) {
	//			tfe.printStackTrace();
	//		}
	//	}
}