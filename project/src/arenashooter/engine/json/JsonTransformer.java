package arenashooter.engine.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import arenashooter.engine.annotation.JsonElement;
import arenashooter.engine.annotation.JsonPrimitive;
import arenashooter.engine.annotation.JsonRoot;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.xmlReaders.reader.MapXmlReader;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.items.Gun;

public class JsonTransformer {


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

	private static double getVectorElement(String field, JsonObject jsonObj) {
		return ((BigDecimal) jsonObj.get(field)).doubleValue();
	}

	public static Arena jsonImportArena(String path) throws Exception {
		Reader readableDeserializable = new FileReader(path);
		JsonObject json = (JsonObject) Jsoner.deserialize(readableDeserializable);
		Arena arena = Arena.class.newInstance();
		Field[] fields = arena.getClass().getDeclaredFields();
		for (Field field : fields) {
			JsonElement jsonElement = field.getAnnotation(JsonElement.class);
			if (jsonElement != null) {
				JsonObject vec;
				switch (jsonElement.type()) {
				case FLOAT:
					BigDecimal bd = (BigDecimal) json.get(jsonElement.tagName());
					field.set(arena, bd.floatValue());
					break;
				case STRING:
					field.set(arena, json.get(jsonElement.tagName()));
					break;
				case VEC2F:
					vec = (JsonObject) json.get(jsonElement.tagName());
					field.set(arena, new Vec2f(getVectorElement("x", vec), getVectorElement("y", vec)));
					break;
				case VEC3F:
					vec = (JsonObject) json.get(jsonElement.tagName());
					field.set(arena, new Vec3f(getVectorElement("x", vec), getVectorElement("y", vec),
							getVectorElement("z", vec)));
					break;
				case VEC4F:
					vec = (JsonObject) json.get(jsonElement.tagName());
					field.set(arena, new Vec4f(getVectorElement("x", vec), getVectorElement("y", vec),
							getVectorElement("z", vec), getVectorElement("w", vec)));
					break;
				case MAP:
					field.set(arena, json.get(jsonElement.tagName()));
					break;
				case SET:
					JsonArray array = (JsonArray) json.get(jsonElement.tagName());
					HashSet<Object> set = new HashSet<>();
					set.addAll(array);
					field.set(arena, set);
					break;
				default:
					break;
				}
			}
		}
		

		return arena;
	}
	
	private static Map<String, Entity> getChildren(JsonObject parent) {
		return null;
	}

	public static void main(String[] args) throws Exception {
		Arena arena = new Arena();
		arena.playerSpawns.add(new Spawner(new Vec2f(), 2));
		arena.usedSpawns.add(new Vec2f(6, 5));
		arena.gravity = new Vec2f(9.8, 6.3);
		arena.fogColor = new Vec3f(6, 21, 13);
		arena.ambientLight = new Vec3f(51, 65, 651);
		arena.fogDistance = 656511;
		arena.cameraBasePos = new Vec3f(48, 5818, 15);
		arena.killBound = new Vec4f(51, 1600, 345, 15);
		arena.musicPath = "kjbsef";
		arena.spawnList.put("Name Item", new Gun("data/sprites/BouleMagique.png"));
		arena.lights.add(new Light());
		Entity e = new Entity();
		e.attachToParent(arena, "mon entite");
		jsonExport(arena, "testArena");
		
		
		MapXmlReader reader = new MapXmlReader("data/mapXML/mapXML.xml");
		Arena a = new Arena();
		reader.load(a);
		while(!reader.loadNextEntity());
		
		jsonExport(a, "mapXML");

//		Arena a = jsonImportArena("data/arena/testArena.arena");
//		Field[] fields = a.getClass().getDeclaredFields();
//		for (Field field : fields) {
//			System.out.println(field.getName() + " : " + field.get(a));
//		}
	}
}
