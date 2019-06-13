package arenashooter.engine.xmlReaders;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.KinematicBody;
import arenashooter.engine.physic.bodies.PhysicBody;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.joints.JointPin;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Entity;
import arenashooter.entities.Arena;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.KinematicBodyContainer;
import arenashooter.entities.spatials.LightContainer;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.StaticBodyContainer;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.entities.spatials.items.Melee;
import arenashooter.entities.spatials.items.Shotgun;
import arenashooter.entities.spatials.items.Usable;
import arenashooter.entities.spatials.items.UsableTimer;
import arenashooter.game.gameStates.Loading;

public class MapXmlReader extends XmlReader {

	private Element root;
	private boolean done;

	/** All spawners */
	public ArrayList<Vec2f> spawner;
	/** Character spawners */
	public ArrayList<Vec2f> spawnerChars;

	public MapXmlReader(String path) {
		parse(path);
		root = document.getDocumentElement();
		spawner = new ArrayList<Vec2f>();
		spawnerChars = new ArrayList<Vec2f>();
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

	public void load(Arena map) {
		Loading.loading.update(.01667);
		loadInformation(getFirstElementByName("information", root), map);
		Loading.loading.update(.01667);
		loadEntities(getFirstElementByName("entities", root), map);
		Loading.loading.update(.01667);
		done = true;
	}

	private void loadInformation(Element information, Arena map) {
		loadGravity(getFirstElementByName("gravity", information), map);
		loadAmbientLight(getFirstElementByName("ambientLight", information), map);
		loadSky(getFirstElementByName("sky", information), map);
		loadCameraBasePos(getFirstElementByName("cameraPos", information), map);
		loadKillBounds(getFirstElementByName("killBounds", information), map);
		loadItems(information, map);
	}
	
	private void loadItems(Element parent, Arena arena) {
		List<Element> itemNodes = getListElementByName("item", parent);
		for(Element elem : itemNodes) {
			List<Element> elems = getListElementByName("usable", elem);
			for(Element item : elems)
				loadUsable(item, arena);
			
			elems = getListElementByName("gun", elem);
			for(Element item : elems)
				loadGun(item, arena);
			
			elems = getListElementByName("shotgun", elem);
			for(Element item : elems)
				loadShotgun(item, arena);
			
			elems = getListElementByName("melee", elem);
			for(Element item : elems)
				loadMelee(item, arena);
			
			elems = getListElementByName("usableTimee", elem);
			for(Element item : elems)
				loadUsableTimer(item, arena);
		}
	}

	private void loadUsableTimer(Element usableTimer, Arena arena) {
		// Attributs
		String name = usableTimer.getAttribute("name");
		int weight = Integer.parseInt(usableTimer.getAttribute("weight"));
		String pathSprite = usableTimer.getAttribute("pathSprite");
		double cooldown = Double.parseDouble(usableTimer.getAttribute("cooldown"));
		int duration = Integer.parseInt(usableTimer.getAttribute("duration"));
		String animPath = usableTimer.getAttribute("animPath");
		double warmup = Double.parseDouble(usableTimer.getAttribute("warmupDuration"));
		String soundWarmup = usableTimer.getAttribute("soundWarmup");
		if(!soundWarmup.isEmpty()) soundWarmup = preloadSound("data/sound/"+soundWarmup+".ogg");
		String soundPickup = preloadSound("data/sound/"+usableTimer.getAttribute("soundPickup")+".ogg");
		String soundAttack = preloadSound("data/sound/"+usableTimer.getAttribute("bangSound")+".ogg");

		// Vectors
		List<Element> vectors = getListElementByName("vector", usableTimer);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				log.error("Invalid use attribute");
				break;
			}
		}
		UsableTimer item = new UsableTimer(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown,
				duration, animPath, warmup, soundWarmup, soundAttack);
		arena.spawnList.put(name, item);
	}

	private void loadMelee(Element melee, Arena arena) {
		// Attributs
		String name = melee.getAttribute("name");
		int weight = Integer.parseInt(melee.getAttribute("weight"));
		String pathSprite = melee.getAttribute("pathSprite");
		double cooldown = Double.parseDouble(melee.getAttribute("cooldown"));
		int uses = Integer.parseInt(melee.getAttribute("uses"));
		String animPath = melee.getAttribute("animPath");
		double warmup = Double.parseDouble(melee.getAttribute("warmupDuration"));
		String soundPickup = preloadSound("data/sound/"+melee.getAttribute("soundPickup")+".ogg");
		String soundWarmup = melee.getAttribute("soundWarmup");
		if(!soundWarmup.isEmpty()) soundWarmup = preloadSound("data/sound/"+soundWarmup+".ogg");
		String attackSound = preloadSound("data/sound/"+melee.getAttribute("bangSound")+".ogg");
		float damage = Float.parseFloat(melee.getAttribute("damage"));
		double size = Double.parseDouble(melee.getAttribute("size"));

		// Vectors
		List<Element> vectors = getListElementByName("vector", melee);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				log.error("Invalid use attribute");
				break;
			}
		}
		
		Melee item = new Melee(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses,
				animPath, warmup, soundWarmup, attackSound, damage, size);
		arena.spawnList.put(name, item);
	}

	private void loadGun(Element gun, Arena arena) {
		// Attributs
		String name = gun.getAttribute("name");
		int weight = Integer.parseInt(gun.getAttribute("weight"));
		String pathSprite = gun.getAttribute("pathSprite");
		double cooldown = Double.parseDouble(gun.getAttribute("cooldown"));
		int uses = Integer.parseInt(gun.getAttribute("uses"));
		String animPath = gun.getAttribute("animPath");
		double warmup = Double.parseDouble(gun.getAttribute("warmupDuration"));
		String soundPickup = preloadSound("data/sound/"+gun.getAttribute("soundPickup")+".ogg");
		String soundWarmup = gun.getAttribute("soundWarmup");
		if(!soundWarmup.isEmpty()) soundWarmup = preloadSound("data/sound/"+soundWarmup+".ogg");
		String soundAttack = preloadSound("data/sound/"+gun.getAttribute("bangSound")+".ogg");
		String soundNoAmmo = preloadSound("data/sound/"+gun.getAttribute("noAmmoSound")+".ogg");
		float damage = Float.parseFloat(gun.getAttribute("damage"));
		double size = Double.parseDouble(gun.getAttribute("size"));
		int bulletType = Integer.parseInt(gun.getAttribute("bulletType"));
		float bulletSpeed = Float.parseFloat(gun.getAttribute("bulletSpeed"));
		double cannonLength = Double.parseDouble(gun.getAttribute("cannonLength"));
		double recoil = Double.parseDouble(gun.getAttribute("recoil"));
		double thrust = Double.parseDouble(gun.getAttribute("thrust"));

		// Vectors
		List<Element> vectors = getListElementByName("vector", gun);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				log.error("Invalid use attribute");
				break;
			}
		}
		
		Gun item = new Gun(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses, animPath,
				warmup, soundWarmup, soundAttack, soundNoAmmo, bulletType, bulletSpeed, damage, cannonLength, recoil,
				thrust, size);
		arena.spawnList.put(name, item);
	}

	private void loadShotgun(Element shotgun, Arena arena) {
		// Attributs
		String name = shotgun.getAttribute("name");
		int weight = Integer.parseInt(shotgun.getAttribute("weight"));
		String pathSprite = shotgun.getAttribute("pathSprite");
		double cooldown = Double.parseDouble(shotgun.getAttribute("cooldown"));
		int uses = Integer.parseInt(shotgun.getAttribute("uses"));
		String animPath = shotgun.getAttribute("animPath");
		double warmup = Double.parseDouble(shotgun.getAttribute("warmupDuration"));
		String soundPickup = preloadSound("data/sound/"+shotgun.getAttribute("soundPickup")+".ogg");
		String soundWarmup = shotgun.getAttribute("soundWarmup");
		if(!soundWarmup.isEmpty()) soundWarmup = preloadSound("data/sound/"+soundWarmup+".ogg");
		String soundAttack = preloadSound("data/sound/"+shotgun.getAttribute("bangSound")+".ogg");
		String soundNoAmmo = preloadSound("data/sound/"+shotgun.getAttribute("noAmmoSound")+".ogg");
		float damage = Float.parseFloat(shotgun.getAttribute("damage"));
		double size = Double.parseDouble(shotgun.getAttribute("size"));
		int bulletType = Integer.parseInt(shotgun.getAttribute("bulletType"));
		float bulletSpeed = Float.parseFloat(shotgun.getAttribute("bulletSpeed"));
		double cannonLength = Double.parseDouble(shotgun.getAttribute("cannonLength"));
		double recoil = Double.parseDouble(shotgun.getAttribute("recoil"));
		double thrust = Double.parseDouble(shotgun.getAttribute("thrust"));
		int multiShot = Integer.parseInt(shotgun.getAttribute("multiShot"));
		double dispersion = Double.parseDouble(shotgun.getAttribute("dispersion"));

		// Vectors
		List<Element> vectors = getListElementByName("vector", shotgun);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				log.error("Invalid use attribute");
				break;
			}
		}
		Shotgun item = new Shotgun(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses,
				animPath, warmup, soundWarmup, soundAttack, soundNoAmmo, multiShot, dispersion, bulletType, bulletSpeed,
				damage, cannonLength, recoil, thrust, size);
		arena.spawnList.put(name, item);
	}

	private void loadUsable(Element usable, Arena arena) {
		// Attributs
		String name = usable.getAttribute("name");
		int weight = Integer.parseInt(usable.getAttribute("weight"));
		String pathSprite = usable.getAttribute("pathSprite");
		double cooldown = Double.parseDouble(usable.getAttribute("cooldown"));
		int uses = Integer.parseInt(usable.getAttribute("uses"));
		String animPath = usable.getAttribute("animPath");
		double warmup = Double.parseDouble(usable.getAttribute("warmupDuration"));
		String soundPickup = preloadSound("data/sound/"+usable.getAttribute("soundPickup")+".ogg");
		String soundWarmup = usable.getAttribute("soundWarmup");
		if(!soundWarmup.isEmpty()) soundWarmup = preloadSound("data/sound/"+soundWarmup+".ogg");
		String soundAttack = preloadSound("data/sound/"+usable.getAttribute("bangSound")+".ogg");

		// Vectors
		List<Element> vectors = getListElementByName("vector", usable);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				log.error("Invalid use attribute");
				break;
			}
		}
		Usable item = new Usable(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses,
				animPath, warmup, soundWarmup, soundAttack);
		arena.spawnList.put(name, item);
	}

	private XmlVector loadVector(Element vector) {
		return new XmlVector(vector);
	}

	private void loadKillBounds(Element killBounds, Arena map) {
		double minX = 0, minY = 0, maxX = 0, maxY = 0;
		if (killBounds.hasAttribute("minX")) {
			minX = Double.parseDouble(killBounds.getAttribute("minX"));
		} else {
			log.error("Missing min X value in kill bounds");
		}
		if (killBounds.hasAttribute("minY")) {
			minY = Double.parseDouble(killBounds.getAttribute("minY"));
		} else {
			log.error("Missing min Y value in kill bounds");
		}
		if (killBounds.hasAttribute("maxX")) {
			maxX = Double.parseDouble(killBounds.getAttribute("maxX"));
		} else {
			log.error("Missing max X value in kill bounds");
		}
		if (killBounds.hasAttribute("maxY")) {
			maxY = Double.parseDouble(killBounds.getAttribute("maxY"));
		} else {
			log.error("Missing max Y value in kill bounds");
		}
		map.killBound.set(minX, minY, maxX, maxY);
	}
	
	private void loadCameraBasePos(Element cameraPos, Arena map) {
		double x = 0, y = 0, z = 0;
		if (cameraPos.hasAttribute("x")) {
			x = Double.parseDouble(cameraPos.getAttribute("x"));
		} else {
			log.error("Missing x coordinate in camera base position");
		}
		if (cameraPos.hasAttribute("y")) {
			y = Double.parseDouble(cameraPos.getAttribute("y"));
		} else {
			log.error("Missing y coordinate in camera base position");
		}
		if (cameraPos.hasAttribute("z")) {
			z = Double.parseDouble(cameraPos.getAttribute("z"));
		} else {
			log.error("Missing z coordinate in camera base position");
		}
		map.cameraBasePos.set(x, y, z);
	}

	private void loadSky(Element sky, Arena map) {
		List<Element> vectors = getListElementByName("vector", sky);
		Vec3f top = new Vec3f(), bottom = new Vec3f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			log.error("Balise Sky dans XML ne possÃ¨de pas " + nbVec + " vectors");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "top":
					top = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "bottom":
					bottom = new Vec3f(vec.x, vec.y, vec.z);
					break;

				default:
					log.error("Sky mal dÃ©fini");
					break;
				}
			}
		}
		Sky s = new Sky(bottom, top);
		s.attachToParent(map, s.genName());

	}

	private void loadGravity(Element gravity, Arena map) {
		float x = 0;
		if(gravity.hasAttribute("x"))
			x = Float.parseFloat(gravity.getAttribute("x"));
		else
			log.error("Missing x coordinate in gravity vector");

		float y = 9.807f;
		if(gravity.hasAttribute("y"))
			y = Float.parseFloat(gravity.getAttribute("y"));
		else
			log.error("Missing y coordinate in gravity vector");
		
		map.gravity = new Vec2f(x, y);
	}
	
	private void loadAmbientLight(Element ambient, Arena arena) {
		float x = arena.ambientLight.x;
		if(ambient.hasAttribute("r"))
			x = Float.parseFloat(ambient.getAttribute("r"));
		else
			log.error("Missing red channel in ambient light");

		float y = arena.ambientLight.y;
		if(ambient.hasAttribute("g"))
			y = Float.parseFloat(ambient.getAttribute("g"));
		else
			log.error("Missing green channel in ambient light");

		float z = arena.ambientLight.z;
		if(ambient.hasAttribute("b"))
			z = Float.parseFloat(ambient.getAttribute("g"));
		else
			log.error("Missing blue channel in ambient light");
		
		arena.ambientLight.set(x, y, z);
	}

	private void loadEntities(Element entities, Entity parent) {
		// Entity
		List<Element> elems = getListElementByName("entity", entities);
		for (Element entity : elems)
			loadEntity(entity, parent);
		
		//Spawner
		elems = getListElementByName("spawn", entities);
		for(Element entity : elems)
			loadSpawn(entity, parent);

		// Rigid Bodies
		elems = getListElementByName("rigid", entities);
		for (Element rigid : elems)
			loadRigid(rigid, parent);

		// Kinematic bodies
		elems = getListElementByName("kinematic", entities);
		for (Element body : elems)
			loadKinematicBody(body, parent);

		// Static bodies
		elems = getListElementByName("static", entities);
		for (Element body : elems)
			loadStaticBody(body, parent);

		// Mesh
		elems = getListElementByName("mesh", entities);
		for (Element mesh : elems)
			loadMesh(mesh, parent);
		
		// Point light
		elems = getListElementByName("pointLight", entities);
		for (Element mesh : elems)
			loadLightPoint(mesh, parent);
		
		// Directional light
		elems = getListElementByName("directionalLight", entities);
		for (Element mesh : elems)
			loadLightDirectional(mesh, parent);
		
		// Text
		elems = getListElementByName("text", entities);
		for (Element text : elems)
			loadText(text, parent);
		
		//Physic joints loaded at last
		// JointPin
		elems = getListElementByName("jointPin", entities);
		for (Element pin : elems)
			loadJointPin(pin, parent);
	}

	private void loadText(Element text, Entity parent) {
		// vectors
		List<Element> vectors = getListElementByName("vector", text);
		Vec3f position = new Vec3f();
		Vec3f scale = new Vec3f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			log.error("Text element needs " + nbVec + " vectors");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "scale":
					scale = new Vec3f(vec.x, vec.y, vec.z);
					break;

				default:
					log.error("Invalid vector in Text element");
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

		// Load children
		List<Element> entitiess = getListElementByName("entities", text);
		for (Element entities : entitiess) {
			loadEntities(entities, spatial);
		}
	}
	
	private void loadSpawn(Element spawn, Entity parent) {
		//Position
		List<Element> vectors = getListElementByName("vector", spawn);
		Vec2f position = new Vec2f();
		if (vectors.size() != 1) {
			log.error("Spawn element needs a position vector");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				default:
					log.error("Invalid vector in Spawn element");
					break;
				}
			}
		}
		
		//Cooldown
		double cooldown = 5;
		if (spawn.hasAttribute("cooldown")) {
			cooldown = Double.parseDouble(spawn.getAttribute("cooldown"));
		} else {
			log.error("Pas d'attribut cooldown");
		}
		
		//Spawner
		Spawner spawner = new Spawner(position, cooldown);

		if(spawn.hasAttribute("name"))
			spawner.attachToParent(parent, spawn.getAttribute("name"));
		else
			spawner.attachToParent(parent, spawner.genName());

		//Add this spawn to the arena's list if needed
		boolean playerSpawn = true;
		if(spawn.hasAttribute("playerSpawn"))
			playerSpawn = Boolean.parseBoolean(spawn.getAttribute("playerSpawn"));
		if(playerSpawn) {
			if(parent.getArena() != null)
				parent.getArena().playerSpawns.add(spawner.getWorldPos());
			else
				log.error("Cannot create a player spawn: spawn's parent cannot access its containing Arena");
		}
		
		//Load items
		List<Element> items = getListElementByName("itemRef", spawn);
		for(Element item : items)
			spawner.addItem(item.getAttribute("item"), Integer.parseInt(item.getAttribute("proba")));
		
		//Load children
		List<Element> children = getListElementByName("entities", spawn);
		for (Element entities : children)
			loadEntities(entities, spawner);
	}

	private void loadMesh(Element mesh, Entity parent) {
		// vectors
		List<Element> vectors = getListElementByName("vector", mesh);
		Vec3f position = new Vec3f(), scale = new Vec3f();
		Quat rotation = new Quat();
		int nbVec = 3;
		if (vectors.size() != nbVec) {
			log.error("Mesh element needs " + nbVec + " vectors [src="
					+ mesh.getAttribute("src") + "]");
			System.out.println(vectors.size());
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "scale":
					scale = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "rotation":
					rotation = readRotation(vector);
					break;
				default:
					log.error("Invalid vector in Mesh element");
					break;
				}
			}
		}

		// Mesh
		String modelPath = mesh.getAttribute("src");
		Mesh m = new Mesh(position, rotation, scale, modelPath);
		if (mesh.hasAttribute("name")) {
			m.attachToParent(parent, mesh.getAttribute("name"));
		} else {
			m.attachToParent(parent, m.genName());
		}

		// Load children
		List<Element> entitiess = getListElementByName("entities", mesh);
		for (Element entities : entitiess) {
			loadEntities(entities, m);
		}
	}
	
	private void loadLightPoint(Element light, Entity parent) {
		// vectors
		List<Element> vectors = getListElementByName("vector", light);
		Vec3f position = new Vec3f();
		Vec3f color = new Vec3f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			log.error("Point light element needs " + nbVec + " vectors");
			System.out.println(vectors.size());
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "color":
					color = new Vec3f(vec.x, vec.y, vec.z);
					break;
				default:
					log.error("Invalid vector in Point light element");
					break;
				}
			}
		}

		// Light
		Light lightObject = new Light();
		lightObject.radius = Float.parseFloat(light.getAttribute("radius"));
		if(lightObject.radius < 0) {
			log.error("Invalid radius for Point light: "+lightObject.radius);
			lightObject.radius = 0;
		}
		lightObject.color.set(color);
		LightContainer container = new LightContainer(position, lightObject);
		if (light.hasAttribute("name")) {
			container.attachToParent(parent, light.getAttribute("name"));
		} else {
			container.attachToParent(parent, container.genName());
		}

		// Load children
		List<Element> entitiess = getListElementByName("entities", light);
		for (Element entities : entitiess) {
			loadEntities(entities, container);
		}
	}
	
	private void loadLightDirectional(Element light, Entity parent) {
		// vectors
		List<Element> vectors = getListElementByName("vector", light);
		Vec3f position = new Vec3f();
		Vec3f color = new Vec3f();
		Quat rotation = new Quat();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			log.error("Directional light element needs " + nbVec + " vectors");
			System.out.println(vectors.size());
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "color":
					color = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "rotation":
					rotation = readRotation(vector);
					break;
				default:
					log.error("Invalid vector in Directional light element");
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
		if (light.hasAttribute("name")) {
			container.attachToParent(parent, light.getAttribute("name"));
		} else {
			container.attachToParent(parent, container.genName());
		}

		// Load children
		List<Element> entitiess = getListElementByName("entities", light);
		for (Element entities : entitiess) {
			loadEntities(entities, container);
		}
	}

	private void loadRigid(Element rigid, Entity parent) {
		//Vectors
		List<Element> vectors = getListElementByName("vector", rigid);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			log.error("RigidBody element needs " + nbVec + " vectors");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				case "extent":
					extent = new Vec2f(vec.x, vec.y);
					break;
				default:
					log.error("Invalid vector in RigidBody element");
					break;
				}
			}
		}

		CollisionFlags flags = CollisionFlags.RIGIDBODY;

		double rotation = 0.0;
		if (rigid.hasAttribute("rotation"))
			rotation = Double.parseDouble(rigid.getAttribute("rotation"));

		float density = 1;
		if (rigid.hasAttribute("density"))
			density = Float.parseFloat(rigid.getAttribute("density"));

		float friction = 0.8f;
		if (rigid.hasAttribute("friction"))
			friction = Float.parseFloat(rigid.getAttribute("friction"));

		float radius = -1;
		if (rigid.hasAttribute("radius"))
			radius = Float.parseFloat(rigid.getAttribute("radius"));

		RigidBody body;
		if (rigid.hasAttribute("radius")) {
			body = new RigidBody(new ShapeDisk(radius), position, rotation, flags, density, friction);
		} else
			body = new RigidBody(new ShapeBox(extent), position, rotation, flags, density, friction);

		// Rigid
		RigidBodyContainer container = new RigidBodyContainer(body);
		if (rigid.hasAttribute("name"))
			container.attachToParent(parent, rigid.getAttribute("name"));
		else
			container.attachToParent(parent, container.genName());

		// Load children
		List<Element> entitiess = getListElementByName("entities", rigid);
		for (Element entities : entitiess) {
			loadEntities(entities, container);
		}
	}

	private void loadStaticBody(Element entity, Entity parent) { // TODO: Add support for circular static bodies
		// Read position and extent values
		List<Element> vectors = getListElementByName("vector", entity);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			log.error("StaticBody element needs " + nbVec + " vectors");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				case "extent":
					extent = new Vec2f(vec.x, vec.y);
					break;
				default:
					log.error("Invalid vector in StaticBody element");
					break;
				}
			}
		}
		
		// Read optional rotation
		double rotation = 0.0;
		if (entity.hasAttribute("rotation"))
			rotation = Double.parseDouble(entity.getAttribute("rotation"));

		// Create entity
		StaticBodyContainer container = new StaticBodyContainer(position, extent, rotation);

		// Attach new entity
		if (entity.hasAttribute("name"))
			container.attachToParent(parent, entity.getAttribute("name"));
		else
			container.attachToParent(parent, container.genName());

		// Load children
		List<Element> entitiess = getListElementByName("entities", entity);
		for (Element entities : entitiess)
			loadEntities(entities, container);
	}
	
	private void loadKinematicBody(Element entity, Entity parent) { // TODO: Add support for circular kinematic bodies
		// Read position and extent values
		List<Element> vectors = getListElementByName("vector", entity);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			log.error("KinematicBody element needs " + nbVec + " vectors");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				case "extent":
					extent = new Vec2f(vec.x, vec.y);
					break;
				default:
					log.error("Invalid vector in KinematicBody element");
					break;
				}
			}
		}

		// Read optional rotation
		double rotation = 0.0;
		if (entity.hasAttribute("rotation"))
			rotation = Double.parseDouble(entity.getAttribute("rotation"));

		KinematicBody body = new KinematicBody(new ShapeBox(extent), position, rotation, CollisionFlags.ARENA_KINEMATIC, 1);
		
		// Create entity
		KinematicBodyContainer container = new KinematicBodyContainer(body);

		// Attach new entity
		if (entity.hasAttribute("name"))
			container.attachToParent(parent, entity.getAttribute("name"));
		else
			container.attachToParent(parent, container.genName());
		
		//Load animation
		if(entity.hasAttribute("animation")) {
			AnimationData animData = AnimationData.loadAnim(entity.getAttribute("animation"));
			container.ignoreKillBounds = true;
			container.setAnim(new Animation(animData));
			container.playAnim();
		}

		// Load children
		List<Element> entitiess = getListElementByName("entities", entity);
		for (Element entities : entitiess)
			loadEntities(entities, container);
	}

	private void loadEntity(Element entity, Entity parent) {
		Entity e = new Entity();
		if (entity.hasAttribute("name")) {
			e.attachToParent(parent, entity.getAttribute("name"));
		} else {
			e.attachToParent(parent, e.genName());
		}
		
		// Load children
		List<Element> entitiess = getListElementByName("entities", entity);
		for (Element entities : entitiess) {
			loadEntities(entities, e);
		}
	}
	
	/**
	 * Load a pin joint
	 * @param joint
	 * @param parent
	 */
	private void loadJointPin(Element joint, Entity parent) {
		Entity entityA = parent.getChild(joint.getAttribute("bodyA"));
		Entity entityB = parent.getChild(joint.getAttribute("bodyB"));
		
		if(!(entityA instanceof StaticBodyContainer) 
				&& !(entityA instanceof RigidBodyContainer)
				&& !(entityA instanceof KinematicBodyContainer)) {
			log.error("Invalid bodyA attribute in joint definition: \""+joint.getAttribute("bodyA")+"\"");
			return;
		}
		if(!(entityB instanceof StaticBodyContainer)
				&& !(entityB instanceof RigidBodyContainer)
				&& !(entityB instanceof KinematicBodyContainer)) {
			log.error("Invalid bodyB attribute in joint definition: \""+joint.getAttribute("bodyB")+"\"");
			return;
		}

		PhysicBody bodyA = null, bodyB = null;
		if(entityA instanceof RigidBodyContainer)
			bodyA = ((RigidBodyContainer)entityA).getBody();
		else if(entityA instanceof StaticBodyContainer)
			bodyA = ((StaticBodyContainer)entityA).getBody();
		else
			bodyA = ((KinematicBodyContainer)entityA).getBody();

		if(entityB instanceof RigidBodyContainer)
			bodyB = ((RigidBodyContainer)entityB).getBody();
		else if(entityB instanceof StaticBodyContainer)
			bodyB = ((StaticBodyContainer)entityB).getBody();
		else
			bodyB = ((KinematicBodyContainer)entityB).getBody();
		
		List<Element> vectors = getListElementByName("vector", joint);
		Vec2f anchorA = new Vec2f(), anchorB = new Vec2f(), angleLimit = null;
		if (vectors.size() < 2 || vectors.size() > 3) {
			log.error("JointPin element needs 2 or 3 vectors");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = loadVector(vector);
				switch (vec.use) {
				case "anchorA":
					anchorA.set(vec.x, vec.y);
					break;
				case "anchorB":
					anchorB.set(vec.x, vec.y);
					break;
				case "angleLimit":
					angleLimit = new Vec2f(vec.x, vec.y);
					break;
				default:
					log.error("Invalid vector in JointPin element");
					return;
				}
			}
		}
		
		JointPin pin = new JointPin(bodyA, bodyB, anchorA, anchorB);
		if(angleLimit != null) pin.enableLimit(angleLimit.x, angleLimit.y);
		pin.addToWorld(entityA.getArena().physic);
	}
}
