package arenashooter.engine.json;

import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class JsonReader {
	protected enum TextureKeys implements JsonKey {
		path {
			@Override
			public String getKey() {
				return "path";
			}

			@Override
			public Object getValue() {
				return Texture.default_tex.getPath();
			}
		}, 
		filtered {
			@Override
			public String getKey() {
				return "filtered";
			}

			@Override
			public Object getValue() {
				return false;
			}
		}
	}
	
	protected static final Logger log = LogManager.getLogger("Json");

	public JsonReader(String path) {
	}
	
	protected static float readFloat(JsonArray array) {
		try {
			float val = array.getFloat(0);
			return val;
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(0).toString());
			e.printStackTrace();
			return 0;
		}
	}
	
	protected static Vec2f readVec2f(JsonArray array) {
		float x, y;
		try {
			x = array.getFloat(0);
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(0).toString());
			e.printStackTrace();
			return null;
		}
		try {
			y = array.getFloat(1);
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(1).toString());
			e.printStackTrace();
			return null;
		}
		return new Vec2f(x, y);
	}
	
	protected static Vec3f readVec3f(JsonArray array) {
		float x, y, z;
		try {
			x = array.getFloat(0);
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(0).toString());
			e.printStackTrace();
			return null;
		}
		try {
			y = array.getFloat(1);
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(1).toString());
			e.printStackTrace();
			return null;
		}
		try {
			z = array.getFloat(2);
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(2).toString());
			e.printStackTrace();
			return null;
		}
		return new Vec3f(x, y, z);
	}
	
	protected static Vec4f readVec4f(JsonArray array) {
		float x, y, z, w;
		try {
			x = array.getFloat(0);
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(0).toString());
			e.printStackTrace();
			return null;
		}
		try {
			y = array.getFloat(1);
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(1).toString());
			e.printStackTrace();
			return null;
		}
		try {
			z = array.getFloat(2);
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(2).toString());
			e.printStackTrace();
			return null;
		}
		try {
			w = array.getFloat(3);
		} catch(Exception e) {
			log.error("Value is not a float: "+array.get(3).toString());
			e.printStackTrace();
			return null;
		}
		return new Vec4f(x, y, z, w);
	}
	
	protected static Texture readTexture(JsonObject obj) {
		try {
		obj.requireKeys(TextureKeys.values());
		} catch(NoSuchElementException e) {
			log.error("Missing element in texture definition "+e.getLocalizedMessage());
			e.printStackTrace();
			return Texture.default_tex;
		}
		
		return Texture.loadTexture(obj.getStringOrDefault(TextureKeys.path)).setFilter(obj.getBooleanOrDefault(TextureKeys.filtered));
	}

}
