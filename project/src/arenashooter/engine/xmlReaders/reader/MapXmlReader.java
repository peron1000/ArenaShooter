package arenashooter.engine.xmlReaders.reader;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.w3c.dom.Element;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.physic.bodies.PhysicBody;
import arenashooter.engine.physic.joints.JointPin;
import arenashooter.engine.util.Tuple;
import arenashooter.engine.xmlReaders.XmlReader;
import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;
import arenashooter.entities.Arena;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.KinematicBodyContainer;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.StaticBodyContainer;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.entities.spatials.items.Melee;
import arenashooter.entities.spatials.items.Shotgun;
import arenashooter.entities.spatials.items.Usable;
import arenashooter.entities.spatials.items.UsableTimer;

public class MapXmlReader extends XmlReader {

	private Element root;
	private boolean done = false;

	private Map<EntitiesLoader<? extends Entity>, Deque<Tuple<Element, Entity>>> entitiesToLoad = new HashMap<>();
	private Deque<Tuple<Element, Entity>> joinPinToLoad = new LinkedList<>();

	public MapXmlReader(String path) {
		parse(path);
		root = document.getDocumentElement();

		initMap(e -> entitiesToLoad.put(e, new LinkedList<>()));
	}

	private void initMap(Consumer<EntitiesLoader<? extends Entity>> c) {
		c.accept(EntityLoader.entityLoader);
		c.accept(DirectionalLightLoader.directionalLightLoader);
		c.accept(KinematicLoader.kinematicLoader);
		c.accept(MeshLoader.meshLoader);
		c.accept(PointLightLoader.pointLightLoader);
		c.accept(RigidLoader.rigidLoader);
		c.accept(SpawnLoader.spawnerLoader);
		c.accept(StaticLoader.staticLoader);
		c.accept(TextLoader.textLoader);
	}

	public boolean loadNextEntity() {
		for (Entry<EntitiesLoader<? extends Entity>, Deque<Tuple<Element, Entity>>> entry : entitiesToLoad.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				Tuple<Element, Entity> tuple = entry.getValue().pollFirst();
				Entity newEntity = entry.getKey().loadEntity(tuple.x, tuple.y);
				// Load children
				Element entities = getFirstElementByName("entities", tuple.x);
				if (entities != null)
					loadEntities(entities, newEntity);
				return false;
			}
		}
		if (!joinPinToLoad.isEmpty()) {
			Tuple<Element, Entity> tuple = joinPinToLoad.pollFirst();
			loadJointPin(tuple.x, tuple.y);
			return false;
		}
		return true;
	}

	public boolean isDone() {
		return done;
	}

	static XmlVector loadVector(Element vector) {
		return new XmlVector(vector);
	}

	public void load(Arena map) {
		loadInformation(getFirstElementByName("information", root), map);
		loadEntities(getFirstElementByName("entities", root), map);
	}

	private void loadInformation(Element information, Arena arena) {
		loadGravity(getFirstElementByName("gravity", information), arena);
		loadAmbientLight(getFirstElementByName("ambientLight", information), arena);
		loadFog(getFirstElementByName("fog", information), arena);
		loadSky(getFirstElementByName("sky", information), arena);
		loadCameraBasePos(getFirstElementByName("cameraPos", information), arena);
		loadKillBounds(getFirstElementByName("killBounds", information), arena);
		loadItems(information, arena);
		loadMusic(getFirstElementByName("music", information), arena);
	}

	private void loadItems(Element parent, Arena arena) {
		List<Element> itemNodes = getListElementByName("item", parent);
		for (Element elem : itemNodes) {
			List<Element> elems = getListElementByName("usable", elem);
			for (Element item : elems)
				loadUsable(item, arena);

			elems = getListElementByName("gun", elem);
			for (Element item : elems)
				loadGun(item, arena);

			elems = getListElementByName("shotgun", elem);
			for (Element item : elems)
				loadShotgun(item, arena);

			elems = getListElementByName("melee", elem);
			for (Element item : elems)
				loadMelee(item, arena);

			elems = getListElementByName("usableTimee", elem);
			for (Element item : elems)
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
		if (!soundWarmup.isEmpty())
			soundWarmup = preloadSound("data/sound/" + soundWarmup + ".ogg");
		String soundPickup = preloadSound("data/sound/" + usableTimer.getAttribute("soundPickup") + ".ogg");
		String soundAttack = preloadSound("data/sound/" + usableTimer.getAttribute("bangSound") + ".ogg");

		// Vectors
		List<Element> vectors = getListElementByName("vector", usableTimer);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		Vec2f size = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			case "size":
				size = new Vec2f(vec.x, vec.y);
				break;
			default:
				log.error("Invalid use attribute");
				break;
			}
		}
		UsableTimer item = new UsableTimer(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, size, soundPickup,
				cooldown, duration, animPath, warmup, soundWarmup, soundAttack);
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
		String soundPickup = preloadSound("data/sound/" + melee.getAttribute("soundPickup") + ".ogg");
		String soundWarmup = melee.getAttribute("soundWarmup");
		if (!soundWarmup.isEmpty())
			soundWarmup = preloadSound("data/sound/" + soundWarmup + ".ogg");
		String attackSound = preloadSound("data/sound/" + melee.getAttribute("bangSound") + ".ogg");
		float damage = Float.parseFloat(melee.getAttribute("damage"));
		@SuppressWarnings("unused")
		double size = 0;
		// Vectors
		List<Element> vectors = getListElementByName("vector", melee);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		Vec2f extent = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			case "extent":
				extent = new Vec2f(vec.x, vec.y);
				break;
			default:
				log.error("Invalid use attribute");
				break;
			}
		}

		Melee item = new Melee(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, extent, soundPickup, cooldown,
				uses, animPath, warmup, soundWarmup, attackSound, damage);
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
		String soundPickup = preloadSound("data/sound/" + gun.getAttribute("soundPickup") + ".ogg");
		String soundWarmup = gun.getAttribute("soundWarmup");
		if (!soundWarmup.isEmpty())
			soundWarmup = preloadSound("data/sound/" + soundWarmup + ".ogg");
		String soundAttack = preloadSound("data/sound/" + gun.getAttribute("bangSound") + ".ogg");
		String soundNoAmmo = preloadSound("data/sound/" + gun.getAttribute("noAmmoSound") + ".ogg");
		float damage = Float.parseFloat(gun.getAttribute("damage"));
		int bulletType = Integer.parseInt(gun.getAttribute("bulletType"));
		float bulletSpeed = Float.parseFloat(gun.getAttribute("bulletSpeed"));
		double cannonLength = Double.parseDouble(gun.getAttribute("cannonLength"));
		double recoil = Double.parseDouble(gun.getAttribute("recoil"));
		double thrust = Double.parseDouble(gun.getAttribute("thrust"));

		// Vectors
		List<Element> vectors = getListElementByName("vector", gun);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		Vec2f extent = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			case "extent":
				extent = new Vec2f(vec.x, vec.y);
				break;
			default:
				log.error("Invalid use attribute");
				break;
			}
		}

		Gun item = new Gun(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, extent, soundPickup, cooldown,
				uses, animPath, warmup, soundWarmup, soundAttack, soundNoAmmo, bulletType, bulletSpeed, damage,
				cannonLength, recoil, thrust);
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
		String soundPickup = preloadSound("data/sound/" + shotgun.getAttribute("soundPickup") + ".ogg");
		String soundWarmup = shotgun.getAttribute("soundWarmup");
		if (!soundWarmup.isEmpty())
			soundWarmup = preloadSound("data/sound/" + soundWarmup + ".ogg");
		String soundAttack = preloadSound("data/sound/" + shotgun.getAttribute("bangSound") + ".ogg");
		String soundNoAmmo = preloadSound("data/sound/" + shotgun.getAttribute("noAmmoSound") + ".ogg");
		float damage = Float.parseFloat(shotgun.getAttribute("damage"));
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
		Vec2f extent = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			case "extent":
				extent = new Vec2f(vec.x, vec.y);
				break;
			default:
				log.error("Invalid use attribute");
				break;
			}
		}
		Shotgun item = new Shotgun(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, extent, soundPickup,
				cooldown, uses, animPath, warmup, soundWarmup, soundAttack, soundNoAmmo, multiShot, dispersion,
				bulletType, bulletSpeed, damage, cannonLength, recoil, thrust);
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
		String soundPickup = preloadSound("data/sound/" + usable.getAttribute("soundPickup") + ".ogg");
		String soundWarmup = usable.getAttribute("soundWarmup");
		if (!soundWarmup.isEmpty())
			soundWarmup = preloadSound("data/sound/" + soundWarmup + ".ogg");
		String soundAttack = preloadSound("data/sound/" + usable.getAttribute("bangSound") + ".ogg");

		// Vectors
		List<Element> vectors = getListElementByName("vector", usable);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		Vec2f size = new Vec2f();
		for (Element vector : vectors) {
			XmlVector vec = loadVector(vector);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			case "size":
				size = new Vec2f(vec.x, vec.y);
			default:
				log.error("Invalid use attribute");
				break;
			}
		}
		Usable item = new Usable(new Vec2f(), name, weight, pathSprite, handPosL, handPosR, size, soundPickup, cooldown,
				uses, animPath, warmup, soundWarmup, soundAttack);
		arena.spawnList.put(name, item);
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
		s.attachToParent(map, "sky");

	}

	private void loadGravity(Element gravity, Arena map) {
		float x = 0;
		if (gravity.hasAttribute("x"))
			x = Float.parseFloat(gravity.getAttribute("x"));
		else
			log.error("Missing x coordinate in gravity vector");

		float y = 9.807f;
		if (gravity.hasAttribute("y"))
			y = Float.parseFloat(gravity.getAttribute("y"));
		else
			log.error("Missing y coordinate in gravity vector");

		map.gravity = new Vec2f(x, y);
	}

	private void loadAmbientLight(Element ambient, Arena arena) {
		float x = arena.ambientLight.x;
		if (ambient.hasAttribute("r"))
			x = Float.parseFloat(ambient.getAttribute("r"));
		else
			log.error("Missing red channel in ambient light");

		float y = arena.ambientLight.y;
		if (ambient.hasAttribute("g"))
			y = Float.parseFloat(ambient.getAttribute("g"));
		else
			log.error("Missing green channel in ambient light");

		float z = arena.ambientLight.z;
		if (ambient.hasAttribute("b"))
			z = Float.parseFloat(ambient.getAttribute("g"));
		else
			log.error("Missing blue channel in ambient light");

		arena.ambientLight.set(x, y, z);
	}

	private void loadFog(Element elem, Arena arena) {
		if (elem == null)
			return;

		float x = arena.fogColor.x;
		if (elem.hasAttribute("r"))
			x = Float.parseFloat(elem.getAttribute("r"));
		else
			log.error("Missing red channel in fog");

		float y = arena.fogColor.y;
		if (elem.hasAttribute("g"))
			y = Float.parseFloat(elem.getAttribute("g"));
		else
			log.error("Missing green channel in fog");

		float z = arena.fogColor.z;
		if (elem.hasAttribute("b"))
			z = Float.parseFloat(elem.getAttribute("g"));
		else
			log.error("Missing blue channel in fog");

		arena.fogColor.set(x, y, z);

		float distance = arena.fogDistance;
		if (elem.hasAttribute("distance"))
			distance = Float.parseFloat(elem.getAttribute("distance"));
		else
			log.error("Missing attribute distance in fog");

		arena.fogDistance = distance;
	}

	private void loadMusic(Element elem, Arena arena) {
		if (elem == null)
			return; // No custom music
		arena.musicPath = preloadSound(elem.getAttribute("path"));
		arena.musicVolume = Float.parseFloat(elem.getAttribute("volume"));
		arena.musicPitch = 1;
		if (elem.hasAttribute("pitch"))
			arena.musicPitch = Float.parseFloat(elem.getAttribute("pitch"));
	}

	private void loadEntities(Element entities, Entity parent) {
		// Entity
		List<Element> elems = getListElementByName("entity", entities);
		for (Element entity : elems) {
			entitiesToLoad.get(EntityLoader.entityLoader).add(new Tuple<Element, Entity>(entity, parent));
		}

		// Spawner
		elems = getListElementByName("spawn", entities);
		for (Element entity : elems) {
			entitiesToLoad.get(SpawnLoader.spawnerLoader).add(new Tuple<Element, Entity>(entity, parent));
		}

		// Rigid Bodies
		elems = getListElementByName("rigid", entities);
		for (Element rigid : elems) {
			entitiesToLoad.get(RigidLoader.rigidLoader).add(new Tuple<Element, Entity>(rigid, parent));
		}

		// Kinematic bodies
		elems = getListElementByName("kinematic", entities);
		for (Element body : elems) {
			entitiesToLoad.get(KinematicLoader.kinematicLoader).add(new Tuple<Element, Entity>(body, parent));
		}

		// Static bodies
		elems = getListElementByName("static", entities);
		for (Element body : elems) {
			entitiesToLoad.get(StaticLoader.staticLoader).add(new Tuple<Element, Entity>(body, parent));
		}

		// Mesh
		elems = getListElementByName("mesh", entities);
		for (Element mesh : elems) {
			entitiesToLoad.get(MeshLoader.meshLoader).add(new Tuple<Element, Entity>(mesh, parent));
		}

		// Point light
		elems = getListElementByName("pointLight", entities);
		for (Element mesh : elems) {
			entitiesToLoad.get(PointLightLoader.pointLightLoader).add(new Tuple<Element, Entity>(mesh, parent));
		}

		// Directional light
		elems = getListElementByName("directionalLight", entities);
		for (Element mesh : elems) {
			entitiesToLoad.get(DirectionalLightLoader.directionalLightLoader)
					.add(new Tuple<Element, Entity>(mesh, parent));
		}

		// Text
		elems = getListElementByName("text", entities);
		for (Element text : elems) {
			entitiesToLoad.get(TextLoader.textLoader).add(new Tuple<Element, Entity>(text, parent));
		}

		// Physic joints loaded at last
		// JointPin
		elems = getListElementByName("jointPin", entities);
		for (Element pin : elems) {
			joinPinToLoad.add(new Tuple<Element, Entity>(pin, parent));
		}
	}

	/**
	 * Load a pin joint
	 * 
	 * @param joint
	 * @param parent
	 */
	private void loadJointPin(Element joint, Entity parent) {
		Entity entityA = parent.getChild(joint.getAttribute("bodyA"));
		Entity entityB = parent.getChild(joint.getAttribute("bodyB"));

		if (!(entityA instanceof StaticBodyContainer) && !(entityA instanceof RigidBodyContainer)
				&& !(entityA instanceof KinematicBodyContainer)) {
			log.error("Invalid bodyA attribute in joint definition: \"" + joint.getAttribute("bodyA") + "\"");
			return;
		}
		if (!(entityB instanceof StaticBodyContainer) && !(entityB instanceof RigidBodyContainer)
				&& !(entityB instanceof KinematicBodyContainer)) {
			log.error("Invalid bodyB attribute in joint definition: \"" + joint.getAttribute("bodyB") + "\"");
			return;
		}

		PhysicBody bodyA = null, bodyB = null;
		if (entityA instanceof RigidBodyContainer)
			bodyA = ((RigidBodyContainer) entityA).getBody();
		else if (entityA instanceof StaticBodyContainer)
			bodyA = ((StaticBodyContainer) entityA).getBody();
		else
			bodyA = ((KinematicBodyContainer) entityA).getBody();

		if (entityB instanceof RigidBodyContainer)
			bodyB = ((RigidBodyContainer) entityB).getBody();
		else if (entityB instanceof StaticBodyContainer)
			bodyB = ((StaticBodyContainer) entityB).getBody();
		else
			bodyB = ((KinematicBodyContainer) entityB).getBody();

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
		if (angleLimit != null)
			pin.enableLimit(angleLimit.x, angleLimit.y);
		pin.addToWorld(entityA.getArena().physic);
	}
}
