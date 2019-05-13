package arenashooter.engine.xmlReaders;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.Plateform;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.entities.spatials.Tuple;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.game.gameStates.Loading;

public class MapXmlReader extends XmlReader {

	private Element root;
	private boolean done;

	public MapXmlReader(String path) {
		parse(path);
		root = document.getDocumentElement();

	}

	public boolean isDone() {
		return done;
	}

	/**
	 * @param name   balise recherchée
	 * @param parent balise du parent
	 * @return Le premier Element correspondant au name parmi les enfants de parent
	 */
	private Element getFirstElementByName(String name, Element parent) {

		List<Element> list = getListElementByName(name, parent);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	private List<Element> getListElementByName(String name, Element parent) {
		ArrayList<Element> array = new ArrayList<>();
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase(name)) {
				array.add((Element) list.item(i));
			}
		}
		return array;
	}

	public void load(Map map) {
		Loading.loading.update(0);
		loadInformation(getFirstElementByName("information", root), map);
		Loading.loading.update(0);
		loadEntities(getFirstElementByName("entities", root), map);
		Loading.loading.update(0);
		done = true;
	}

	private void loadInformation(Element information, Map map) {
		map.spawn = new ArrayList<>();
		loadGravity(getFirstElementByName("gravity", information), map);
		loadSky(getFirstElementByName("sky", information), map);
		loadCameraBound(getFirstElementByName("cameraBound", information), map);

		// Load spawns
		List<Element> listSpawn = getListElementByName("spawn", information);
		for (Element element : listSpawn) {
			loadSpawns(element, map);
		}
	}

	private void loadSpawns(Element spawn, Map map) {
		List<Tuple<String, Integer>> table = new ArrayList<Tuple<String, Integer>>();

		XmlVecteur vec = loadVecteur(getFirstElementByName("vecteur", spawn));
		double cooldown = 0;

		if (spawn.hasAttribute("cooldown")) {
			cooldown = Double.parseDouble(spawn.getAttribute("cooldown"));
		}
		List<Element> usable = getListElementByName("usable", spawn);
		
		// Chargement des guns dans la table pour d�terminer quelle arme � faire spawn
		for (Element e : usable) {
			if(e.hasAttribute("gun"));
				List<Element> guns = getListElementByName("gun", e);
				for (Element gun : guns) {
					loadGunsIntoSpawn(table, gun);
				}
			}
			
		Spawner spawner = new Spawner(new Vec2f(vec.x, vec.y), cooldown, table);
		// entities
		List<Element> entitiess = getListElementByName("entities", spawn);
		for (Element entities : entitiess) {
			loadEntities(entities, spawner);
		}
		spawner.attachToParent(map, spawner.genName());
	}

	private void loadGunsIntoSpawn(List<Tuple<String, Integer>> table, Element gun) {
		if (gun.hasAttribute("name")) {
			Tuple<String, Integer> g = new Tuple<String, Integer>(gun.getAttribute("name"),
					Integer.decode(gun.getAttribute("proba")));
			table.add(g);
		}
	}

/*	private void loadMeleeIntoSpawn(List<Tuple<String, Integer>> table, Element melee) {
		String name = "";
		if (melee.hasAttribute(name)) {
			Tuple<String, Integer> g = new Tuple<String, Integer>(melee.getAttribute("name"),
					Integer.decode(melee.getAttribute("proba")));
			table.add(g);
		}
	}*/
	
	private XmlVecteur loadVecteur(Element vecteur) {
		return new XmlVecteur(vecteur);
	}

	private void loadCameraBound(Element cameraBound, Map map) {
		double w = 0, x = 0, y = 0, z = 0;
		if (cameraBound.hasAttribute("x")) {
			x = Double.parseDouble(cameraBound.getAttribute("x"));
		} else {
			System.err.println("X dans camera Bound non renseigné");
		}
		if (cameraBound.hasAttribute("y")) {
			y = Double.parseDouble(cameraBound.getAttribute("y"));
		} else {
			System.err.println("Y dans camera Bound non renseigné");
		}
		if (cameraBound.hasAttribute("z")) {
			z = Double.parseDouble(cameraBound.getAttribute("z"));
		} else {
			System.err.println("Z dans camera Bound non renseigné");
		}
		if (cameraBound.hasAttribute("w")) {
			w = Double.parseDouble(cameraBound.getAttribute("w"));
		} else {
			System.err.println("W dans camera Bound non renseigné");
		}
		map.cameraBounds = new Vec4f(x, y, z, w);
	}

	private void loadSky(Element sky, Map map) {
		List<Element> vecteurs = getListElementByName("vecteur", sky);
		Vec3f top = new Vec3f(), bottom = new Vec3f();
		int nbVec = 2;
		if (vecteurs.size() != nbVec) {
			System.err.println("Balise Sky dans XML ne possède pas " + nbVec + " vecteurs");
		} else {
			for (Element vecteur : vecteurs) {
				XmlVecteur vec = loadVecteur(vecteur);
				switch (vec.use) {
				case "top":
					top = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "bottom":
					bottom = new Vec3f(vec.x, vec.y, vec.z);
					break;

				default:
					System.err.println("Sky mal défini");
					break;
				}
			}
		}
		Sky s = new Sky(bottom, top);
		s.attachToParent(map, s.genName());

	}

	private void loadGravity(Element gravity, Map map) {
		XmlVecteur vec = new XmlVecteur(getFirstElementByName("vecteur", gravity));
		map.gravity = new Vec2f(vec.x, vec.y);
	}

	private void loadEntities(Element entities, Entity parent) {
		// entity
		List<Element> entitys = getListElementByName("entity", entities);
		for (Element entity : entitys) {
			loadEntity(entity, parent);
		}

		// plateform
		List<Element> plateforms = getListElementByName("plateform", entities);
		for (Element plateform : plateforms) {
			loadPlateform(plateform, parent);
		}

		// mesh
		List<Element> meshs = getListElementByName("mesh", entities);
		for (Element mesh : meshs) {
			loadMesh(mesh, parent);
		}

		// text
		List<Element> texts = getListElementByName("text", entities);
		for (Element text : texts) {
			loadText(text, parent);
		}
	}

	private void loadText(Element text, Entity parent) {
		// vecteurs
		List<Element> vecteurs = getListElementByName("vecteur", text);
		Vec3f position = new Vec3f();
		Vec3f scale = new Vec3f();
		int nbVec = 2;
		if (vecteurs.size() != nbVec) {
			System.err.println("Balise Text dans XML ne possède pas " + nbVec + " vecteurs");
		} else {
			for (Element vecteur : vecteurs) {
				XmlVecteur vec = loadVecteur(vecteur);
				switch (vec.use) {
				case "position":
					position = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "scale":
					scale = new Vec3f(vec.x, vec.y, vec.z);
					break;

				default:
					System.err.println("Vecteur dans Text mal défini");
					break;
				}
			}
		}

		// Text
		String content, font;
		font = text.getAttribute("font");
		Font f = Font.loadFont(font);
		content = text.getAttribute("content");
		Text t = new Text(f, Text.TextAlignH.CENTER, content);
		TextSpatial spatial = new TextSpatial(position, scale, t);
		if (text.hasAttribute("name")) {
			spatial.attachToParent(parent, text.getAttribute("name"));
		} else {
			spatial.attachToParent(parent, spatial.genName());
		}

		// entities
		List<Element> entitiess = getListElementByName("entities", text);
		for (Element entities : entitiess) {
			loadEntities(entities, parent);
		}
	}

	private void loadMesh(Element mesh, Entity parent) {
		Loading.loading.getMap().step(0);
		// vecteurs
		List<Element> vecteurs = getListElementByName("vecteur", mesh);
		Vec3f position = new Vec3f(), scale = new Vec3f();
		Vec4f rotation = new Vec4f();
		int nbVec = 3;
		if (vecteurs.size() != nbVec) {
			System.err.println("Balise Mesh dans XML ne possède pas " + nbVec + " vecteurs [src="
					+ mesh.getAttribute("src") + "]");
			System.out.println(vecteurs.size());
		} else {
			for (Element vecteur : vecteurs) {
				XmlVecteur vec = loadVecteur(vecteur);
				switch (vec.use) {
				case "position":
					position = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "scale":
					scale = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "rotation":
					rotation = new Vec4f(vec.x, vec.y, vec.z, vec.w);
					break;
				default:
					System.err.println("Vecteur dans Mesh mal défini");
					break;
				}
			}
		}

		// Mesh
		String modelPath = mesh.getAttribute("src");
		Mesh m = new Mesh(position, new Quat(rotation.x, rotation.y, rotation.z, rotation.w), scale, modelPath);
		if (mesh.hasAttribute("name")) {
			m.attachToParent(parent, mesh.getAttribute("name"));
		} else {
			m.attachToParent(parent, m.genName());
		}

		// entities
		List<Element> entitiess = getListElementByName("entities", mesh);
		for (Element entities : entitiess) {
			loadEntities(entities, parent);
		}
	}

	private void loadPlateform(Element plateform, Entity parent) {
		// vecteurs
		List<Element> vecteurs = getListElementByName("vecteur", plateform);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vecteurs.size() != nbVec) {
			System.err.println("Balise Plateform dans XML ne possède pas " + nbVec + " vecteurs");
		} else {
			for (Element vecteur : vecteurs) {
				XmlVecteur vec = loadVecteur(vecteur);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				case "extent":
					extent = new Vec2f(vec.x, vec.y);
					break;
				default:
					System.err.println("Vecteur dans Plateform mal défini");
					break;
				}
			}
		}

		// Plateform
		Plateform p = new Plateform(position, extent);
		if (plateform.hasAttribute("name")) {
			p.attachToParent(parent, plateform.getAttribute("name"));
		} else {
			p.attachToParent(parent, p.genName());
		}

		// entities
		List<Element> entitiess = getListElementByName("entities", plateform);
		for (Element entities : entitiess) {
			loadEntities(entities, parent);
		}
	}

	private void loadEntity(Element entity, Entity parent) {
		Entity e = new Entity();
		if (entity.hasAttribute("name")) {
			e.attachToParent(parent, entity.getAttribute("name"));
		} else {
			e.attachToParent(parent, e.genName());
		}
		// entities
		List<Element> entitiess = getListElementByName("entities", entity);
		for (Element entities : entitiess) {
			loadEntities(entities, e);
		}
	}
}
