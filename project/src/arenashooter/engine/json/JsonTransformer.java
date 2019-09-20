package arenashooter.engine.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;

import arenashooter.engine.FileUtils;
import arenashooter.engine.annotation.JsonField;
import arenashooter.engine.annotation.JsonParam;
import arenashooter.engine.annotation.JsonRef;
import arenashooter.engine.annotation.JsonRoot;
import arenashooter.engine.annotation.JsonType;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.xmlReaders.reader.MapXmlReader;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.items.Gun;

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
	public static void jsonExport(Jsonable object, String fileName) throws Exception {
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

//	public static void setChildren(Entity parent, JsonObject jsonParent)
//			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
//			NoSuchMethodException, SecurityException {
//		JsonObject children = (JsonObject) jsonParent.get("children");
//		if (children == null)
//			return;
//		for (Entry<String, Object> entry : children.entrySet()) {
//			JsonObject child = (JsonObject) entry.getValue();
//			String className = child.getStringOrDefault(new JsonKey() {
//
//				@Override
//				public Object getValue() {
//					return Entity.class.getName();
//				}
//
//				@Override
//				public String getKey() {
//					return "type";
//				}
//			});
//			Class<?> classType = Class.forName(className);
//			Method m = classType.getMethod("fromJson", JsonObject.class);
//			Entity e = (Entity) m.invoke(null, child);
//			System.out.println(parent.toJson() + " -> "+e.toJson());
//		}
//	}

	public static Arena importArena(String path) throws Exception {
		Reader readableDeserializable = new FileReader(path);
		JsonObject o = (JsonObject) Jsoner.deserialize(readableDeserializable);
		Arena arena = Arena.fromJson(o);
		return arena;
	}

	
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
//		(new Entity()).attachToParent(m, "testChild");
//		jsonExport(arena, "testArena");
		
		List<File> list = FileUtils.listFilesByType(new File("data/mapXML"), ".xml");
		for (File file : list) {
			String name = file.getName();
			name = name.substring(0, name.length()-4);
			System.out.println("begining convertion : "+name);
			xmlToJson(name);
			System.out.println("file converted");
		}

//		Arena a = jsonImportArena("data/arena/testArena.arena");
//		Field[] fields = a.getClass().getDeclaredFields();
//		for (Field field : fields) {
//			System.out.println(field.getName() + " : " + field.get(a));
//		}
//		Arena a = importArena("data/arena/testArena.arena");
//		System.out.println(Jsoner.prettyPrint(a.toJson()));
	}
	
	private static void xmlToJson(String name) throws Exception {
		File file = new File("data/mapXML/"+name+".xml");
		if(!file.exists() || !file.isFile()) {
			throw new FileNotFoundException();
		}
		MapXmlReader reader = new MapXmlReader(file.getPath());
		Arena a = new Arena();
		reader.load(a);
		while (!reader.loadNextEntity())
			;

		jsonExport(a , name);
		
		importArena("data/arena/"+name+".arena");
	}
}
