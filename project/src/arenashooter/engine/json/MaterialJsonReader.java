package arenashooter.engine.json;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class MaterialJsonReader extends JsonReader {
	private enum Keys implements JsonKey {
		type {
			@Override
			public String getKey() {
				return "type";
			}

			@Override
			public Object getValue() {
				return "material";
			}
		}, 
		shaderVertex {
			@Override
			public String getKey() {
				return "shaderVertex";
			}

			@Override
			public Object getValue() {
				return "data/shaders/mesh_simple.vert";
			}
		},
		shaderFragment {
			@Override
			public String getKey() {
				return "shaderFragment";
			}

			@Override
			public Object getValue() {
				return "data/shaders/mesh_simple.frag";
			}
		},
		transparent {
			@Override
			public String getKey() {
				return "transparent";
			}

			@Override
			public Object getValue() {
				return false;
			}
		},
		params {
			@Override
			public String getKey() {
				return "params";
			}

			@Override
			public Object getValue() {
				return new HashMap<String, Object>();
			}
		}
	}
	
	public String vertexShader;
	public String fragmentShader;

	public boolean transparency;

	public Map<String, Integer> paramsI = new HashMap<>();
	public Map<String, Float> paramsF = new HashMap<>();
	public Map<String, Vec2f> paramsVec2f = new HashMap<>();
	public Map<String, Vec3f> paramsVec3f = new HashMap<>();
	public Map<String, Vec4f> paramsVec4f = new HashMap<>();
	public Map<String, Texture> paramsTex = new HashMap<>();

	public MaterialJsonReader(String path) {
		super(path);
		try (FileReader fileReader = new FileReader((path))) {

            JsonObject deserialize = (JsonObject) Jsoner.deserialize(fileReader);
            
            vertexShader = deserialize.getStringOrDefault(Keys.shaderVertex);
            fragmentShader = deserialize.getStringOrDefault(Keys.shaderFragment);
            
            transparency = deserialize.getBooleanOrDefault(Keys.transparent);
            
            // TODO: Improve params parsing
            JsonObject params = deserialize.getMapOrDefault(Keys.params);
            for(Entry<String, Object> entry : params.entrySet()) {
            	if( entry.getValue() instanceof JsonArray ) {
            		JsonArray array = ((JsonArray)entry.getValue());
            		switch ( array.size() ) {
            		case 1:
            			paramsF.put( entry.getKey(), readFloat(array) );
            			break;
            		case 2:
            			paramsVec2f.put( entry.getKey(), readVec2f(array) );
            			break;
            		case 3:
            			paramsVec3f.put( entry.getKey(), readVec3f(array) );
            			break;
            		case 4:
            			paramsVec4f.put( entry.getKey(), readVec4f(array) );
            			break;
            		default:
            			log.error("Invalid vector size: "+array.size());
            		}
            	} else if( entry.getValue() instanceof JsonObject ) {
            		JsonObject object = ((JsonObject)entry.getValue());
            		
            		paramsTex.put( entry.getKey(), readTexture(object) );
            	} else {
            		paramsI.put(entry.getKey(), ((Integer)entry.getValue()));
            		System.out.println(paramsI.get(entry.getKey()));
            	}
            }
        } catch(Exception e) {
        	log.error("Error parsing "+path);
        	e.printStackTrace();
        }
	}

}
