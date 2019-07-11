package arenashooter.engine.json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import arenashooter.engine.annotation.JsonField;
import arenashooter.engine.annotation.JsonRoot;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.xmlReaders.reader.MapXmlReader;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Mesh;

public class JsonTransformer {

	/**
	 * Create the file and his parent directory if they doesn't exist
	 * 
	 * @param parent
	 * @param name
	 * @param extension
	 * @return The file to transform
	 * @throws IOException
	 */
	private static File fileCreation(String parent, String name, String extension) throws IOException {
		File parentFile = new File(parent);
		if (!parentFile.exists() || !parentFile.isDirectory()) {
			parentFile.mkdir();
		}
		File newFile = new File(parentFile, name + extension);
		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		return newFile;
	}

	/**
	 * Create a Json file for the <code>object</code>
	 * 
	 * @param object   The object to convert into file. <b>Make sure it has the
	 *                 Annotation <code>JsonRoot</code></b>
	 * @param fileName The simple name to give to the file without informations like
	 *                 path or extension. Example: <code>"MyArena"</code>
	 * @throws Exception
	 */
	public static void jsonExport(Object object, String fileName) throws Exception {
		JsonRoot a = object.getClass().getAnnotation(JsonRoot.class);
		if (a == null) {
			throw new Exception("Impossible to find the JsonRoot annotation");
		}

		File file = fileCreation(a.directory(), fileName, a.extension());

		String json = Jsoner.serialize(object);
		Writer writable = new FileWriter(file);
		Reader reader = new StringReader(json);
		Jsoner.prettyPrint(reader, writable, "\t", "\n");
	}

	private static float getFloat(String key, JsonObject jsonObject) {
		return ((Number) jsonObject.get(key)).floatValue();
	}

	private static String getString(String key, JsonObject jsonObject) {
		return (String) jsonObject.get(key);
	}

	private static Object getVec(String key, JsonObject jsonObject) throws Exception {
		JsonArray array = (JsonArray) jsonObject.get(key);
		if (array.size() == 2) {
			return new Vec2f(((Number) array.get(0)).doubleValue(), ((Number) array.get(1)).doubleValue());
		} else if (array.size() == 3) {
			return new Vec3f(((Number) array.get(0)).doubleValue(), ((Number) array.get(1)).doubleValue(),
					((Number) array.get(2)).doubleValue());
		} else if (array.size() == 4) {
			return new Vec4f(((Number) array.get(0)).doubleValue(), ((Number) array.get(1)).doubleValue(),
					((Number) array.get(2)).doubleValue(), ((Number) array.get(3)).doubleValue());
		} else {
			throw new Exception("array size given is not correct (expected: 2 or 3 or 4 ; given: " + array.size());
		}
	}

	private static Object getMap(String key, JsonObject jsonObject) {
		return jsonObject.get(key);
	}

	private static void setField(Entity e, Field field, JsonObject jsonObject, JsonField a)
			throws IllegalArgumentException, IllegalAccessException {
		switch (a.type()) {
		case FLOAT:
			field.set(e, getFloat(a.tagName(), jsonObject));
			System.out.println(field.getName() + ": " + field.get(e));
			break;
		case STRING:
			field.set(e, getString(a.tagName(), jsonObject));
			System.out.println(field.getName() + ": " + field.get(e));
			break;
		case VECTOR:
			try {
				field.set(e, getVec(a.tagName(), jsonObject));
				System.out.println(field.getName() + ": " + field.get(e));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		case MAP:
			field.set(e, getMap(a.tagName(), jsonObject));
			System.out.println(field.getName() + ": " + field.get(e));
			break;
		default:
			break;
		}
	}

	public static Arena importArena(String path) throws Exception {
		Reader readableDeserializable = new FileReader(path);
		Arena arena = new Arena();
		JsonObject o = (JsonObject) Jsoner.deserialize(readableDeserializable);
		Field[] fields = Arena.class.getFields();
		for (Field field : fields) {
			JsonField a = field.getAnnotation(JsonField.class);
			if (a != null) {
				setField(arena, field, o, a);
			}
		}
		JsonObject children = (JsonObject) o.get("children");
		for (Entry<String, Object> e : children.entrySet()) {
			JsonObject child = (JsonObject) e.getValue();
			EntityTypes.valueOf((String) child.get("type")).createEntity(child).attachToParent(arena, e.getKey());
			System.out.println(e.getKey());
			// Entity entity = setChildren((JsonObject) e.getValue());
			// entity.attachToParent(arena, e.getKey());
		}
		return arena;
	}

//	public static <E extends Entity> E entityFactory(JsonObject jsonObject, Class<E> classType)
//			throws InstantiationException, IllegalAccessException {
//		Field[] fields = classType.getFields();
//		System.out.println(jsonObject);
//		for (Field field : fields) {
//			JsonElement a = field.getAnnotation(JsonElement.class);
//			if (a != null) {
//				jsonObject.get(a.tagName());
//				System.out.println(a.tagName() + ": "+jsonObject.get(a.tagName()));
//			}
//		}
//		fields = classType.getDeclaredFields();
//		for (Field field : fields) {
//			JsonElement a = field.getAnnotation(JsonElement.class);
//			if (a != null && field.getModifiers() == 2)
//				System.out.println(a.tagName());
//		}
//		return null;
//	}

	public static void main(String[] args) throws Exception {
//		Arena arena = new Arena();
//		arena.playerSpawns.add(new Spawner(new Vec2f(), 2));
//		arena.usedSpawns.add(new Vec2f(6, 5));
//		arena.gravity = new Vec2f(9.8, 6.3);
//		arena.fogColor = new Vec3f(6, 21, 13);
//		arena.ambientLight = new Vec3f(51, 65, 651);
//		arena.fogDistance = 656511;
//		arena.cameraBasePos = new Vec3f(48, 5818, 15);
//		arena.killBound = new Vec4f(51, 1600, 345, 15);
//		arena.musicPath = "kjbsef";
//		arena.spawnList.put("Name Item", new Gun("data/sprites/BouleMagique.png"));
//		arena.lights.add(new Light());
//		Entity e = new Entity();
//		e.attachToParent(arena, "mon entite");
//		Mesh m = new Mesh(new Vec3f(), "data/meshes/arrow.obj");
//		m.attachToParent(arena, "Mon mesh");
//		jsonExport(arena, "testArena");

//		MapXmlReader reader = new MapXmlReader("data/mapXML/mapXML.xml");
//		Arena a = new Arena();
//		reader.load(a);
//		while (!reader.loadNextEntity())
//			;
//
//		jsonExport(a, "mapXML");

//		Arena a = jsonImportArena("data/arena/testArena.arena");
//		Field[] fields = a.getClass().getDeclaredFields();
//		for (Field field : fields) {
//			System.out.println(field.getName() + " : " + field.get(a));
//		}
		Arena a = importArena("data/arena/testArena.arena");
		System.out.println(Jsoner.prettyPrint(a.toJson()));
	}
}
