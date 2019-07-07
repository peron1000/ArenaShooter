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
import arenashooter.game.Main;

public class MaterialJsonReader {
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
	private enum TextureKeys implements JsonKey {
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
            			paramsF.put( entry.getKey(), array.getFloat(0) );
            			break;
            		case 2:
            			paramsVec2f.put( entry.getKey(), new Vec2f(array.getFloat(0), array.getFloat(1)) );
            			break;
            		case 3:
            			paramsVec3f.put( entry.getKey(), new Vec3f(array.getFloat(0), array.getFloat(1), array.getFloat(2)) );
            			break;
            		case 4:
            			paramsVec4f.put( entry.getKey(), new Vec4f(array.getFloat(0), array.getFloat(1), array.getFloat(2), array.getFloat(3)) );
            			break;
            		}
            	} else if( entry.getValue() instanceof JsonObject ) {
            		JsonObject object = ((JsonObject)entry.getValue());
            		
            		paramsTex.put( entry.getKey(), Texture.loadTexture(object.getStringOrDefault(TextureKeys.path)).setFilter(object.getBooleanOrDefault(TextureKeys.filtered)) );
            	} else {
            		paramsI.put(entry.getKey(), ((Integer)entry.getValue()));
            		System.out.println(paramsI.get(entry.getKey()));
            	}
            }
        } catch(Exception e) {
        	Main.log.error("Error parsing "+path);
        	e.printStackTrace();
        }
	}

}
