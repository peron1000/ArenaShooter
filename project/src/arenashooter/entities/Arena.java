package arenashooter.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import arenashooter.engine.annotation.JsonRoot;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.Main;

@JsonRoot(extension = ".arena", directory = "data/arena")
public class Arena extends Entity implements Jsonable {

	/** Character spawn points */
	public List<Spawner> playerSpawns = new ArrayList<>();

	/** World gravity vector */
	public Vec2f gravity = new Vec2f(0);

	public Vec3f ambientLight = new Vec3f(.48, .48, .5);

	public Vec3f fogColor = new Vec3f(0.929, 0.906, 0.753);

	public float fogDistance = 3000;

	// Rendering
	/** Entities to render during transparency pass */
	public List<Entity> transparent = new LinkedList<>();

	// Music
	public String musicPath = "";
	public float musicVolume = 1;
	public float musicPitch = 1;

	public PhysicWorld physic;

	/** Map of all items available to item spawners */
	public Map<String, Item> spawnList = new HashMap<>();

	/** Character spawns that have been used */
	public List<Vec2f> usedSpawns = new ArrayList<>();

	/** All items currently on the map */
	public List<Item> items = new ArrayList<>();

	/** Kill every Spatial that touches these bounds (min X, min Y, max X, max Y) */
	public Vec4f killBound = new Vec4f(-100, -100, 100, 100);

	/**
	 * Base camera position, camera movement will be restricted into this field of
	 * view
	 */
	public Vec3f cameraBasePos = new Vec3f(0, 0, 8);

	public Set<Light> lights = new HashSet<>();

//	Timer spawnWeapon = new Timer(4); // TODO : verifier que c'est bien inutile

	public Arena() {
		physic = new PhysicWorld(this);
	}

	/**
	 * Get this Arena
	 * 
	 * @return this
	 */
	@Override
	public Arena getArena() {
		return this;
	}

	@Override
	public void step(double d) {
		super.step(d);
		physic.step(d);
	}

	// TODO : remove ?
//	/**
//	 * Get a spawn location that is not currently occupied by a Character <br/>
//	 * Use this to give a random spawn to each Character
//	 * 
//	 * @author Shervin
//	 * @return Vec2f
//	 */
//	public Vec2f GetRandomRespawnch() {
//		Vec2f rand = new Vec2f(0, 0);
//		return rand;
//	}

	public Vec2f GetRandomRespawnch2() {
		Vec2f randi = new Vec2f();

		try {
			int rand = (int) (Math.random() * playerSpawns.size());

			randi = playerSpawns.get(rand).getWorldPos();
			int max = 100;
			int etapes = 0;
			while (usedSpawns.contains(randi) && etapes < max) {
				rand = (int) (Math.random() * playerSpawns.size());
				randi = playerSpawns.get(rand).getWorldPos();
				etapes++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Main.log.error("Failed to find a spawn point: " + e.getLocalizedMessage());
		}
		usedSpawns.add(randi);
		return randi;
	}

	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return gravity;
			}

			@Override
			public String getKey() {
				return "gravity";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				JsonArray g = json.getCollection(this);
				if (g != null)
					gravity = Vec2f.jsonImport(g);
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return ambientLight;
			}

			@Override
			public String getKey() {
				return "ambient light";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				JsonArray a = json.getCollection(this);
				if (a != null)
					ambientLight = Vec3f.jsonImport(a);
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return new JsonArray(playerSpawns);
			}

			@Override
			public String getKey() {
				return "player spawn";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				List<Spawner> a = json.getCollection(this);
				if (a != null)
					playerSpawns = a;
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return fogColor;
			}

			@Override
			public String getKey() {
				return "fog color";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				JsonArray a = json.getCollection(this);
				if (a != null)
					fogColor = Vec3f.jsonImport(a);
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return fogDistance;
			}

			@Override
			public String getKey() {
				return "fog distance";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				fogDistance = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return musicPath;
			}

			@Override
			public String getKey() {
				return "music path";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				musicPath = json.getString(this);
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return musicPitch;
			}

			@Override
			public String getKey() {
				return "music pitch";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				musicPitch = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return musicVolume;
			}

			@Override
			public String getKey() {
				return "music volume";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				musicVolume = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return new JsonArray(items);
			}

			@Override
			public String getKey() {
				return "items available";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				items = json.getCollection(this);
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return killBound;
			}

			@Override
			public String getKey() {
				return "kill bound";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				killBound = Vec4f.jsonImport(json.getCollection(this));
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return cameraBasePos;
			}

			@Override
			public String getKey() {
				return "camera base position";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				cameraBasePos = Vec3f.jsonImport(json.getCollection(this));
			}
		});
		return set;
	}

	public static Arena fromJson(JsonObject json) throws Exception {
		Arena arena = new Arena();
		useKeys(arena, json);
		return arena;
	}
}
