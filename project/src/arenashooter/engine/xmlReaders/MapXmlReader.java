package arenashooter.engine.xmlReaders;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
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
import arenashooter.game.Main;
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
		loadSky(getFirstElementByName("sky", information), map);
		loadCameraBasePos(getFirstElementByName("cameraPos", information), map);
		loadKillBounds(getFirstElementByName("killBounds", information), map);

		// Load spawns
		List<Element> listSpawn = getListElementByName("spawn", information);
		for (Element element : listSpawn)
			loadSpawns(element, map);
		
		map.spawn = spawner;
		map.spawnperso = spawnerChars;
	}

	private void loadSpawns(Element spawn, Arena map) {

		XmlVector vec = loadVecteur(getFirstElementByName("vecteur", spawn));
		double cooldown = 0;
		Boolean spawnperso = true;

		if (spawn.hasAttribute("cooldown")) {
			cooldown = Double.parseDouble(spawn.getAttribute("cooldown"));
		} else {
			System.err.println("Pas d'attribut cooldown");
		}
		Vec2f position = new Vec2f(vec.x, vec.y);
		Spawner spawner = new Spawner(position, cooldown);
		if (spawn.hasAttribute("spawnperso")) {
			spawnperso = Boolean.parseBoolean(spawn.getAttribute("spawnperso"));
			
		}	
			if(spawnperso) {
				this.spawnerChars.add(position);
				this.spawner.add(position);
			}
		 else {
			this.spawner.add(position);
		}
		// Usables
		List<Element> usables = getListElementByName("usable", spawn);
		for (Element usable : usables) {
			loadUsable(usable, spawner, position);
		}

		// Guns
		List<Element> guns = getListElementByName("gun", spawn);
		for (Element gun : guns) {
			loadGun(gun, spawner, position);
		}

		// shotguns
		List<Element> shotguns = getListElementByName("shotgun", spawn);
		for (Element shotgun : shotguns) {
			loadShotGun(shotgun, spawner, position);
		}

		// Melee
		List<Element> melees = getListElementByName("melee", spawn);
		for (Element melee : melees) {
			loadMelee(melee, spawner, position);
		}

		// UsableTimers
		List<Element> usableTimers = getListElementByName("usabletimer", spawn);
		for (Element usableTimer : usableTimers) {
			loadUsableTimers(usableTimer, spawner, position);
		}

		// Entities
		List<Element> entitiess = getListElementByName("entities", spawn);
		for (Element entities : entitiess) {
			loadEntities(entities, spawner);
		}

		spawner.attachToParent(map, spawner.genName());
	}

	private void loadUsableTimers(Element usableTimer, Spawner spawner, Vec2f position) {
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

		// Vecteurs
		List<Element> vecteurs = getListElementByName("vecteur", usableTimer);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vecteur : vecteurs) {
			XmlVector vec = loadVecteur(vecteur);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				System.err.println("Use érroné");
				break;
			}
		}
		UsableTimer u = new UsableTimer(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown,
				duration, animPath, warmup, soundWarmup, soundAttack);
		spawner.addItem(u, Integer.parseInt(usableTimer.getAttribute("proba")));
	}

	private void loadMelee(Element melee, Spawner spawner, Vec2f position) {
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

		// Vecteurs
		List<Element> vecteurs = getListElementByName("vecteur", melee);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vecteur : vecteurs) {
			XmlVector vec = loadVecteur(vecteur);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				System.err.println("Use érroné");
				break;
			}
		}
		Melee u = new Melee(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses,
				animPath, warmup, soundWarmup, attackSound, damage, size);
		spawner.addItem(u, Integer.parseInt(melee.getAttribute("proba")));
	}

	private void loadGun(Element gun, Spawner spawner, Vec2f position) {
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

		// Vecteurs
		List<Element> vecteurs = getListElementByName("vecteur", gun);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vecteur : vecteurs) {
			XmlVector vec = loadVecteur(vecteur);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				System.err.println("Use érroné");
				break;
			}
		}
		Gun u = new Gun(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses, animPath,
				warmup, soundWarmup, soundAttack, soundNoAmmo, bulletType, bulletSpeed, damage, cannonLength, recoil,
				thrust, size);
		spawner.addItem(u, Integer.parseInt(gun.getAttribute("proba")));
	}

	private void loadShotGun(Element shotgun, Spawner spawner, Vec2f position) {
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

		// Vecteurs
		List<Element> vecteurs = getListElementByName("vecteur", shotgun);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vecteur : vecteurs) {
			XmlVector vec = loadVecteur(vecteur);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				System.err.println("Use érroné");
				break;
			}
		}
		Shotgun u = new Shotgun(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses,
				animPath, warmup, soundWarmup, soundAttack, soundNoAmmo, multiShot, dispersion, bulletType, bulletSpeed,
				damage, cannonLength, recoil, thrust, size);
		spawner.addItem(u, Integer.parseInt(shotgun.getAttribute("proba")));
	}

	private void loadUsable(Element usable, Spawner spawner, Vec2f position) {
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

		// Vecteurs
		List<Element> vecteurs = getListElementByName("vecteur", usable);
		Vec2f handPosL = new Vec2f();
		Vec2f handPosR = new Vec2f();
		for (Element vecteur : vecteurs) {
			XmlVector vec = loadVecteur(vecteur);
			switch (vec.use) {
			case "handPosL":
				handPosL = new Vec2f(vec.x, vec.y);
				break;
			case "handPosR":
				handPosR = new Vec2f(vec.x, vec.y);
				break;
			default:
				System.err.println("Use érroné");
				break;
			}
		}
		Usable u = new Usable(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses,
				animPath, warmup, soundWarmup, soundAttack);
		spawner.addItem(u, Integer.parseInt(usable.getAttribute("proba")));
	}

	private XmlVector loadVecteur(Element vecteur) {
		return new XmlVector(vecteur);
	}

	private void loadKillBounds(Element killBounds, Arena map) {
		double minX = 0, minY = 0, maxX = 0, maxY = 0;
		if (killBounds.hasAttribute("minX")) {
			minX = Double.parseDouble(killBounds.getAttribute("minX"));
		} else {
			System.err.println("Missing min X value in kill bounds");
		}
		if (killBounds.hasAttribute("minY")) {
			minY = Double.parseDouble(killBounds.getAttribute("minY"));
		} else {
			System.err.println("Missing min Y value in kill bounds");
		}
		if (killBounds.hasAttribute("maxX")) {
			maxX = Double.parseDouble(killBounds.getAttribute("maxX"));
		} else {
			System.err.println("Missing max X value in kill bounds");
		}
		if (killBounds.hasAttribute("maxY")) {
			maxY = Double.parseDouble(killBounds.getAttribute("maxY"));
		} else {
			System.err.println("Missing max Y value in kill bounds");
		}
		map.killBound.set(minX, minY, maxX, maxY);
	}
	
	private void loadCameraBasePos(Element cameraPos, Arena map) {
		double x = 0, y = 0, z = 0;
		if (cameraPos.hasAttribute("x")) {
			x = Double.parseDouble(cameraPos.getAttribute("x"));
		} else {
			System.err.println("Missing x coordinate in camera base position");
		}
		if (cameraPos.hasAttribute("y")) {
			y = Double.parseDouble(cameraPos.getAttribute("y"));
		} else {
			System.err.println("Missing y coordinate in camera base position");
		}
		if (cameraPos.hasAttribute("z")) {
			z = Double.parseDouble(cameraPos.getAttribute("z"));
		} else {
			System.err.println("Missing z coordinate in camera base position");
		}
		map.cameraBasePos.set(x, y, z);
	}

	private void loadSky(Element sky, Arena map) {
		List<Element> vecteurs = getListElementByName("vecteur", sky);
		Vec3f top = new Vec3f(), bottom = new Vec3f();
		int nbVec = 2;
		if (vecteurs.size() != nbVec) {
			System.err.println("Balise Sky dans XML ne possÃ¨de pas " + nbVec + " vecteurs");
		} else {
			for (Element vecteur : vecteurs) {
				XmlVector vec = loadVecteur(vecteur);
				switch (vec.use) {
				case "top":
					top = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "bottom":
					bottom = new Vec3f(vec.x, vec.y, vec.z);
					break;

				default:
					System.err.println("Sky mal dÃ©fini");
					break;
				}
			}
		}
		Sky s = new Sky(bottom, top);
		s.attachToParent(map, s.genName());

	}

	private void loadGravity(Element gravity, Arena map) {
		XmlVector vec = new XmlVector(getFirstElementByName("vecteur", gravity));
		map.gravity = new Vec2f(vec.x, vec.y);
	}

	private void loadEntities(Element entities, Entity parent) {
		// Entity
		List<Element> entitys = getListElementByName("entity", entities);
		for (Element entity : entitys)
			loadEntity(entity, parent);

		// Rigid Bodies
		List<Element> rigids = getListElementByName("rigid", entities);
		for (Element rigid : rigids)
			loadRigid(rigid, parent);

		// Kinematic bodies
		List<Element> kinematics = getListElementByName("kinematic", entities);
		for (Element body : kinematics)
			loadKinematicBody(body, parent);

		// Static bodies
		List<Element> statics = getListElementByName("static", entities);
		for (Element body : statics)
			loadStaticBody(body, parent);

		// Mesh
		List<Element> meshs = getListElementByName("mesh", entities);
		for (Element mesh : meshs)
			loadMesh(mesh, parent);

		// Text
		List<Element> texts = getListElementByName("text", entities);
		for (Element text : texts)
			loadText(text, parent);
		
		//Physic joints loaded at last
		// JointPin
		List<Element> jointPins = getListElementByName("jointPin", entities);
		for (Element pin : jointPins)
			loadJointPin(pin, parent);
	}

	private void loadText(Element text, Entity parent) {
		// vecteurs
		List<Element> vecteurs = getListElementByName("vecteur", text);
		Vec3f position = new Vec3f();
		Vec3f scale = new Vec3f();
		int nbVec = 2;
		if (vecteurs.size() != nbVec) {
			System.err.println("Text element needs " + nbVec + " vectors");
		} else {
			for (Element vecteur : vecteurs) {
				XmlVector vec = loadVecteur(vecteur);
				switch (vec.use) {
				case "position":
					position = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "scale":
					scale = new Vec3f(vec.x, vec.y, vec.z);
					break;

				default:
					System.err.println("Invalid vector in Text element");
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
			System.err.println("Mesh element needs " + nbVec + " vectors [src="
					+ mesh.getAttribute("src") + "]");
			System.out.println(vecteurs.size());
		} else {
			for (Element vecteur : vecteurs) {
				XmlVector vec = loadVecteur(vecteur);
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
					System.err.println("Invalid vector in Mesh element");
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

		// Load children
		List<Element> entitiess = getListElementByName("entities", mesh);
		for (Element entities : entitiess) {
			loadEntities(entities, parent);
		}
	}

	private void loadRigid(Element rigid, Entity parent) {
		//Vectors
		List<Element> vecteurs = getListElementByName("vecteur", rigid);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vecteurs.size() != nbVec) {
			System.err.println("RigidBody element needs " + nbVec + " vectors");
		} else {
			for (Element vecteur : vecteurs) {
				XmlVector vec = loadVecteur(vecteur);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				case "extent":
					extent = new Vec2f(vec.x, vec.y);
					break;
				default:
					System.err.println("Invalid vector in RigidBody element");
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
		RigidBodyContainer p = new RigidBodyContainer(position, body);
		if (rigid.hasAttribute("name")) {
			p.attachToParent(parent, rigid.getAttribute("name"));
		} else {
			p.attachToParent(parent, p.genName());
		}

		// Load children
		List<Element> entitiess = getListElementByName("entities", rigid);
		for (Element entities : entitiess) {
			loadEntities(entities, p);
		}
	}

	private void loadStaticBody(Element entity, Entity parent) { // TODO: Add support for circular static bodies
		// Read position and extent values
		List<Element> vecteurs = getListElementByName("vecteur", entity);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vecteurs.size() != nbVec) {
			System.err.println("StaticBody element needs " + nbVec + " vectors");
		} else {
			for (Element vecteur : vecteurs) {
				XmlVector vec = loadVecteur(vecteur);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				case "extent":
					extent = new Vec2f(vec.x, vec.y);
					break;
				default:
					System.err.println("Invalid vector in StaticBody element");
					break;
				}
			}
		}

		// Read optional rotation
		double rotation = 0.0;
		if (entity.hasAttribute("rotation"))
			rotation = Double.parseDouble(entity.getAttribute("rotation"));

		// Create entity
		StaticBodyContainer e = new StaticBodyContainer(position, extent, rotation);

		// Attach new entity
		if (entity.hasAttribute("name"))
			e.attachToParent(parent, entity.getAttribute("name"));
		else
			e.attachToParent(parent, e.genName());

		// Load children
		List<Element> entitiess = getListElementByName("entities", entity);
		for (Element entities : entitiess)
			loadEntities(entities, e);
	}
	
	private void loadKinematicBody(Element entity, Entity parent) { // TODO: Add support for circular kinematic bodies
		// Read position and extent values
		List<Element> vecteurs = getListElementByName("vecteur", entity);
		Vec2f position = new Vec2f(), extent = new Vec2f();
		int nbVec = 2;
		if (vecteurs.size() != nbVec) {
			System.err.println("KinematicBody element needs " + nbVec + " vectors");
		} else {
			for (Element vecteur : vecteurs) {
				XmlVector vec = loadVecteur(vecteur);
				switch (vec.use) {
				case "position":
					position = new Vec2f(vec.x, vec.y);
					break;
				case "extent":
					extent = new Vec2f(vec.x, vec.y);
					break;
				default:
					System.err.println("Invalid vector in KinematicBody element");
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
		KinematicBodyContainer e = new KinematicBodyContainer(position, body);

		// Attach new entity
		if (entity.hasAttribute("name"))
			e.attachToParent(parent, entity.getAttribute("name"));
		else
			e.attachToParent(parent, e.genName());
		
		//Load animation
		if(entity.hasAttribute("animation")) {
			AnimationData animData = AnimationData.loadAnim(entity.getAttribute("animation"));
			e.ignoreKillBounds = true;
			e.setAnim(new Animation(animData));
			e.playAnim();
		}

		// Load children
		List<Element> entitiess = getListElementByName("entities", entity);
		for (Element entities : entitiess)
			loadEntities(entities, e);
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
			Main.log.error("Invalid bodyA attribute in joint definition: \""+joint.getAttribute("bodyA")+"\"");
			return;
		}
		if(!(entityB instanceof StaticBodyContainer)
				&& !(entityB instanceof RigidBodyContainer)
				&& !(entityB instanceof KinematicBodyContainer)) {
			Main.log.error("Invalid bodyB attribute in joint definition: \""+joint.getAttribute("bodyB")+"\"");
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
		
		List<Element> vectors = getListElementByName("vecteur", joint);
		Vec2f anchorA = new Vec2f(), anchorB = new Vec2f(), angleLimit = null;
		if (vectors.size() < 2 || vectors.size() > 3) {
			System.err.println("JointPin element needs 2 or 3 vectors");
		} else {
			for (Element vecteur : vectors) {
				XmlVector vec = loadVecteur(vecteur);
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
					Main.log.error("Invalid vector in JointPin element");
					return;
				}
			}
		}
		
		JointPin pin = new JointPin(bodyA, bodyB, anchorA, anchorB);
		if(angleLimit != null) pin.enableLimit(angleLimit.x, angleLimit.y);
		pin.addToWorld(entityA.getArena().physic);
	}
}
