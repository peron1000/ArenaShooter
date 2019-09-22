package arenashooter.engine.json;

import java.util.Map.Entry;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.Light;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.graphics.fonts.Text.TextAlignV;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.KinematicBody;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.PhysicShape;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.KinematicBodyContainer;
import arenashooter.entities.spatials.LightContainer;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.StaticBodyContainer;
import arenashooter.entities.spatials.TextSpatial;

public enum EntityTypes {
	MESH {

		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			Vec3f position = Vec3f.jsonImport(tryToGet("position", json, JsonArray.class));
			String path = tryToGet("model path", json, String.class);
			if (json.containsKey("rotation")) {
				Quat rotation = Quat.jsonImport((JsonArray) json.get("rotation"));
				if (json.containsKey("scale")) {
					Vec3f scale = Vec3f.jsonImport((JsonArray) json.get("scale"));
					return setChildren(new Mesh(position, rotation, scale, path), (JsonObject) json.get("children"));
				}
				return setChildren(new Mesh(position, rotation, path), (JsonObject) json.get("children"));
			} else {
				return setChildren(new Mesh(position, path), (JsonObject) json.get("children"));
			}
		}

	},
	RIGID_BOX {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			Vec2f extent = Vec2f.jsonImport(tryToGet("extent", json, JsonArray.class));
			return setChildren(makeRigid(new ShapeBox(extent), json), (JsonObject) json.get("children"));
		}
	},
	RIGID_DISK {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			double radius = tryToGet("radius", json, Number.class).doubleValue();
			return setChildren(makeRigid(new ShapeDisk(radius), json), (JsonObject) json.get("children"));
		}
	},
	STATIC_BOX {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			Vec2f extent = Vec2f.jsonImport(tryToGet("extent", json, JsonArray.class));
			return setChildren(makeStatic(new ShapeBox(extent), json), (JsonObject) json.get("children"));
		}
	},
	STATIC_DISK {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			double radius = tryToGet("radius", json, Number.class).doubleValue();
			return setChildren(makeStatic(new ShapeDisk(radius), json), (JsonObject) json.get("children"));
		}
	},
	KINEMATIC_BOX {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			Vec2f extent = Vec2f.jsonImport(tryToGet("extent", json, JsonArray.class));
			return setChildren(makeKinematic(new ShapeBox(extent), json), (JsonObject) json.get("children"));
		}
	},
	KINEMATIC_DISK {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			double radius = tryToGet("radius", json, Number.class).doubleValue();
			return setChildren(makeKinematic(new ShapeDisk(radius), json), (JsonObject) json.get("children"));
		}
	},
	ENTITY {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			return setChildren(new Entity(), (JsonObject) json.get("children"));
		}
	},
	TEXT {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			Vec3f localPosition = Vec3f.jsonImport(tryToGet("position", json, JsonArray.class));
			Vec3f scale = Vec3f.jsonImport(tryToGet("scale", json, JsonArray.class));
			JsonObject jsonText = tryToGet("text", json, JsonObject.class);
			Font font = Font.loadFont(tryToGet("font path", jsonText, String.class));
			TextAlignH alignH = TextAlignH.valueOf(tryToGet("align H", jsonText, String.class));
			TextAlignV alignV = TextAlignV.valueOf(tryToGet("align V", jsonText, String.class));
			String str = tryToGet("text", jsonText, String.class);
			Text text = new Text(font, alignH, alignV, str);
			TextSpatial ts = new TextSpatial(localPosition, scale, text);

			if (json.get("rotation") != null) {
				ts.localRotation = Quat.jsonImport(tryToGet("rotation", json, JsonArray.class));
			}
			return setChildren(ts, (JsonObject) json.get("children"));
		}
	},
	SPAWN {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			Vec2f localPosition = Vec2f.jsonImport(tryToGet("position", json, JsonArray.class));
			double cooldown = tryToGet("cooldown", json, Number.class).doubleValue();
			Spawner spawner = new Spawner(localPosition, cooldown);

			if (json.get("rotation") != null) {
				spawner.localRotation = tryToGet("rotation", json, Number.class).doubleValue();
			}
			return setChildren(spawner, (JsonObject) json.get("children"));
		}
	},
	LIGHT {
		@Override
		public Entity createEntity(JsonObject json) throws Exception {
			Vec3f localPosition = new Vec3f();
			Light l = new Light();
			LightContainer light = new LightContainer(localPosition, l);
			return setChildren(light, (JsonObject) json.get("children"));
		}
	};
	public abstract Entity createEntity(JsonObject json) throws Exception;

	private static <E> E tryToGet(String tag, JsonObject json, Class<E> classType) throws Exception {
		Object o = json.get(tag);
		if (o == null)
			throw new Exception(tag + " not found");
		return classType.cast(o);
	}

	private static Entity setChildren(Entity parent, JsonObject children) throws Exception {
		if (children != null) {
			for (Entry<String, Object> entry : children.entrySet()) {
				System.out.println("import entity -> "+entry.getKey());
				JsonObject jsonChild = (JsonObject) entry.getValue();
				Entity child = EntityTypes.valueOf((String) jsonChild.get("type")).createEntity(jsonChild);
				child.attachToParent(parent, entry.getKey());
			}
		}
		return parent;
	}

	private static RigidBodyContainer makeRigid(PhysicShape shape, JsonObject json) throws Exception {
		Vec2f worldPosition = Vec2f.jsonImport(tryToGet("world position", json, JsonArray.class));
		double worldRotation = tryToGet("world rotation", json, Number.class).doubleValue();
		float density = tryToGet("density", json, Number.class).floatValue();
		float friction = tryToGet("friction", json, Number.class).floatValue();

		RigidBody body = new RigidBody(shape, worldPosition, worldRotation, CollisionFlags.RIGIDBODY, density,
				friction);
		return new RigidBodyContainer(body);
	}

	private static StaticBodyContainer makeStatic(PhysicShape shape, JsonObject json) throws Exception {
		Vec2f worldPosition = Vec2f.jsonImport(tryToGet("world position", json, JsonArray.class));
		double worldRotation = tryToGet("world rotation", json, Number.class).doubleValue();

		StaticBody body = new StaticBody(shape, worldPosition, worldRotation, CollisionFlags.LANDSCAPE);
		return new StaticBodyContainer(body);
	}

	private static KinematicBodyContainer makeKinematic(PhysicShape shape, JsonObject json) throws Exception {
		Vec2f worldPosition = Vec2f.jsonImport(tryToGet("world position", json, JsonArray.class));
		double worldRotation = tryToGet("world rotation", json, Number.class).doubleValue();
		float density = tryToGet("density", json, Number.class).floatValue();

		KinematicBody body = new KinematicBody(shape, worldPosition, worldRotation, CollisionFlags.ARENA_KINEMATIC,
				density);
		return new KinematicBodyContainer(body);
	}
}
