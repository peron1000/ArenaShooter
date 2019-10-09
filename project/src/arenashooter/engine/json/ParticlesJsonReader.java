package arenashooter.engine.json;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.particles.EmitterTemplate;
import arenashooter.engine.graphics.particles.modules.AccelConstant;
import arenashooter.engine.graphics.particles.modules.ColorOverLife;
import arenashooter.engine.graphics.particles.modules.Drag;
import arenashooter.engine.graphics.particles.modules.Gravity;
import arenashooter.engine.graphics.particles.modules.InitialPosCircle;
import arenashooter.engine.graphics.particles.modules.InitialVelCone;
import arenashooter.engine.graphics.particles.modules.ParticleModule;
import arenashooter.engine.graphics.particles.modules.SizeOverLife;
import arenashooter.game.Main;

public class ParticlesJsonReader extends JsonReader {
	private enum Keys implements JsonKey {
		type {
			@Override
			public String getKey() {
				return "type";
			}
			@Override
			public String getValue() {
				return "";
			}
		},
		emitters {
			@Override
			public String getKey() {
				return "emitters";
			}
			@Override
			public JsonArray getValue() {
				return new JsonArray();
			}
		}
	}

	private final String path;
	private List<EmitterTemplate> data = new ArrayList<>();

	public ParticlesJsonReader(String path) {
		super(path);
		
		this.path = path;

		try (FileReader fileReader = new FileReader((path))) {

			JsonObject deserialize = (JsonObject) Jsoner.deserialize(fileReader);

			try {
				deserialize.requireKeys(Keys.values());
			} catch(NoSuchElementException e) {
				log.error("Missing element in particle system definition "+e.getLocalizedMessage());
				e.printStackTrace();
			}

			if( !deserialize.getStringOrDefault(Keys.type).equals("particles") )
				log.error(path+" is not a particle system.");

			readEmitters(deserialize.getCollectionOrDefault(Keys.emitters));
		} catch(Exception e) {
			log.error("Error parsing "+path);
			e.printStackTrace();
		}
	}
	
	public EmitterTemplate[] getData() {
		return data.toArray( new EmitterTemplate[data.size()] );
	}
	
	private void readEmitters(JsonArray emitters) {
		for(Object emitterO : emitters) {
			if( !(emitterO instanceof JsonObject) ) {
				log.error("Error loading particle: not an emitter (in "+path+")");
				continue;
			}
			
			JsonObject emitter = (JsonObject)emitterO;
			
			float duration = emitter.getFloatOrDefault(EmitterKeys.duration);
			float delay = emitter.getFloatOrDefault(EmitterKeys.delay);
			float rate = emitter.getFloatOrDefault(EmitterKeys.rate);
			float lifetimeMin = emitter.getFloatOrDefault(EmitterKeys.lifetimeMin);
			float lifetimeMax = emitter.getFloatOrDefault(EmitterKeys.lifetimeMax);
			float initialRotMin = emitter.getFloatOrDefault(EmitterKeys.initialRotMin);
			float initialRotMax = emitter.getFloatOrDefault(EmitterKeys.initialRotMax);

			Texture tex = readTexture(emitter.getMapOrDefault(EmitterKeys.texture));
			
			List<ParticleModule> modules = readModules(emitter.getCollectionOrDefault(EmitterKeys.modules));

			data.add(new EmitterTemplate(tex, delay, duration, rate, lifetimeMin, lifetimeMax, initialRotMin, initialRotMax, modules));
		}
	}
	
	private enum EmitterKeys implements JsonKey {
		duration {
			@Override
			public String getKey() {
				return "duration";
			}
			@Override
			public Float getValue() {
				return 1.0f;
			}
		},
		delay {
			@Override
			public String getKey() {
				return "delay";
			}
			@Override
			public Float getValue() {
				return 0.0f;
			}
		},
		rate {
			@Override
			public String getKey() {
				return "rate";
			}
			@Override
			public Float getValue() {
				return 5.0f;
			}
		},
		lifetimeMin {
			@Override
			public String getKey() {
				return "lifetimeMin";
			}
			@Override
			public Float getValue() {
				return 1.0f;
			}
		},
		lifetimeMax {
			@Override
			public String getKey() {
				return "lifetimeMax";
			}
			@Override
			public Float getValue() {
				return 2.0f;
			}
		},
		initialRotMin {
			@Override
			public String getKey() {
				return "initialRotMin";
			}
			@Override
			public Float getValue() {
				return 0.0f;
			}
		},
		initialRotMax {
			@Override
			public String getKey() {
				return "initialRotMax";
			}
			@Override
			public Float getValue() {
				return 3.14f;
			}
		},
		texture {
			@Override
			public String getKey() {
				return "texture";
			}
			@Override
			public JsonObject getValue() {
				return new JsonObject().putChain("path", Main.getRenderer().getDefaultTexture().getPath()).putChain("filtered", false);
			}
		},
		modules {
			@Override
			public String getKey() {
				return "modules";
			}
			@Override
			public JsonArray getValue() {
				return new JsonArray();
			}
		}
	}
	
	private List<ParticleModule> readModules(JsonArray modules) {
		List<ParticleModule> res = new LinkedList<>();

		for( Object obj : modules ) {
			if( !(obj instanceof JsonObject) ) {
				log.error("Error loading particle: not a module (in "+path+")");
				continue;
			}
			JsonObject jsonModule = (JsonObject)obj;
			ParticleModule module = null;
			
			switch( jsonModule.getStringOrDefault(ModulesKey.type) ) {
			case AccelConstant.jsonType:
				module = AccelConstant.fromJson(jsonModule);
				break;
			case ColorOverLife.jsonType:
				module = ColorOverLife.fromJson(jsonModule);
				break;
			case Drag.jsonType:
				module = Drag.fromJson(jsonModule);
				break;
			case Gravity.jsonType:
				module = Gravity.fromJson(jsonModule);
				break;
			case InitialPosCircle.jsonType:
				module = InitialPosCircle.fromJson(jsonModule);
				break;
			case InitialVelCone.jsonType:
				module = InitialVelCone.fromJson(jsonModule);
				break;
			case SizeOverLife.jsonType:
				module = SizeOverLife.fromJson(jsonModule);
				break;
			default:
				log.error("Error loading particle: not a module (in "+path+")");
				break;
			}
			
			if(module != null)
				res.add(module);
		}
		return res;
	}
	
	private enum ModulesKey implements JsonKey {
		type {
			@Override
			public String getKey() {
				return "type";
			}
			@Override
			public String getValue() {
				return "";
			}
		}
	}

}
