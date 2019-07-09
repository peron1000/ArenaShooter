package arenashooter.entities;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
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

import arenashooter.engine.annotation.JsonElement;
import arenashooter.engine.annotation.JsonRoot;
import arenashooter.engine.annotation.JsonType;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.Main;

@JsonRoot(extension = ".arena", directory = "data/arena")
public class Arena extends Entity implements Jsonable {
	@JsonElement(tagName = "players spawn" , type = JsonType.LIST)
	/** Character spawn points */
	public List<Spawner> playerSpawns = new ArrayList<>();

	/** World gravity vector */
	@JsonElement(tagName = "gravity" , type = JsonType.VEC2F)
	public Vec2f gravity = new Vec2f(0);

	@JsonElement(tagName = "ambient light" , type = JsonType.VEC3F)
	public Vec3f ambientLight = new Vec3f(.48, .48, .5);

	@JsonElement(tagName = "fog color" , type = JsonType.VEC3F)
	public Vec3f fogColor = new Vec3f(0.929, 0.906, 0.753);

	@JsonElement(tagName = "fog distance" , type = JsonType.FLOAT)
	public float fogDistance = 3000;

	// Rendering
	/** Entities to render during transparency pass */
	public List<Entity> transparent = new LinkedList<>();

	// Music
	@JsonElement(tagName = "music path" , type = JsonType.STRING)
	public String musicPath = "";
	@JsonElement(tagName = "musicVolume" , type = JsonType.FLOAT)
	public float musicVolume = 1;
	@JsonElement(tagName = "musicPitch" , type = JsonType.FLOAT)
	public float musicPitch = 1;

	public PhysicWorld physic;

	@JsonElement(tagName = "spawn items" , type = JsonType.MAP)
	/** Map of all items available to item spawners */
	public Map<String, Item> spawnList = new HashMap<>();

	/** Character spawns that have been used */
	public List<Vec2f> usedSpawns = new ArrayList<>();

	/** All items currently on the map */
	public List<Item> items = new ArrayList<>();

	@JsonElement(tagName = "killBound", type = JsonType.VEC4F)
	/** Kill every Spatial that touches these bounds (min X, min Y, max X, max Y) */
	public Vec4f killBound = new Vec4f(-100, -100, 100, 100);

	@JsonElement(tagName = "cameraBasePos", type = JsonType.VEC3F)
	/**
	 * Base camera position, camera movement will be restricted into this field of
	 * view
	 */
	public Vec3f cameraBasePos = new Vec3f(0, 0, 8);

	@JsonElement(tagName = "lights" , type = JsonType.SET)
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

	private JsonObject arenaToJson() {
		JsonObject arena = new JsonObject();

		Field[] fields = getClass().getDeclaredFields();
		for (Field field : fields) {
			JsonElement jsonElement = field.getAnnotation(JsonElement.class);
			try {
				if (jsonElement != null) {
					arena.putChain(jsonElement.tagName(), field.get(this));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		}

		JsonObject spawnList = new JsonObject(this.spawnList);
		arena.putChain("spawnList", spawnList);

		JsonArray lights = new JsonArray(this.lights);
		arena.putChain("lights", lights);

		JsonObject children = new JsonObject(getChildren());
		arena.putChain("children", children);

		return arena;
	}

	@Override
	public String toJson() {
		return arenaToJson().toJson();
	}

	@Override
	public void toJson(Writer writable) throws IOException {
		writable.write(toJson());
	}
}
