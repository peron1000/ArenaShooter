package arenashooter.engine.xmlReaders;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class ParticlesXmlReader extends XmlReader {

	private static List<EmitterTemplate> data;
	
	private ParticlesXmlReader() { }

	public static EmitterTemplate[] read(String path) {
		parse(path);
		
		data = new ArrayList<>();
		
		readEmitters(getListElementByName("emitter", root));
		
		return data.toArray( new EmitterTemplate[data.size()] );
	}

	private static void readEmitters(List<Element> emitters) {
		for(Element emitter : emitters) {
			float duration = Float.valueOf(emitter.getAttribute("duration"));
			float delay = Float.valueOf(emitter.getAttribute("delay"));
			float rate = Float.valueOf(emitter.getAttribute("rate"));
			float lifetimeMin = Float.valueOf(emitter.getAttribute("lifetimeMin"));
			float lifetimeMax = Float.valueOf(emitter.getAttribute("lifetimeMax"));
			float initialRotMin = Float.valueOf(emitter.getAttribute("initialRotMin"));
			float initialRotMax = Float.valueOf(emitter.getAttribute("initialRotMax"));

			Element texElem = getFirstElementByName("texture", emitter);
			if(texElem == null) continue;
			String path = texElem.getAttribute("src");
			boolean filtered = Boolean.parseBoolean(texElem.getAttribute("filtered"));
			Texture tex = Texture.loadTexture(path).setFilter(filtered);
			
			List<ParticleModule> modules = readModules(getFirstElementByName("modules", emitter).getChildNodes());

			data.add(new EmitterTemplate(tex, delay, duration, rate, lifetimeMin, lifetimeMax, initialRotMin, initialRotMax, modules));
		}
	}

	private static List<ParticleModule> readModules(NodeList modules) {
		List<ParticleModule> res = new LinkedList<>();
		
		for(int i=0; i<modules.getLength(); i++) {
			if(modules.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
			Element elem = (Element)modules.item(i);
			
			ParticleModule module = null;
			
			switch(elem.getTagName()) {
			case "accelConstant":
				module = accelConstant(elem);
				break;
			case "colorOverLife":
				module = colorOverLife(elem);
				break;
			case "drag":
				module = drag(elem);
				break;
			case "gravity":
				module = gravity(elem);
				break;
			case "initialPosCircle":
				module = initialPosCircle(elem);
				break;
			case "initialVelCone":
				module = initialVelCone(elem);
				break;
			case "sizeOverLife":
				module = sizeOverLife(elem);
				break;
			default:
				log.error("Unknown particle module: "+elem.getNodeType());
				break;
			}
			
			if(module != null)
				res.add(module);
		}
		return res;
	}
	
	private static ParticleModule accelConstant(Element elem) {
		Vec2f accel = new Vec2f();
		readVec2f(getFirstElementByName("vec2", elem), accel);
		return new AccelConstant(accel);
	}
	
	private static ParticleModule colorOverLife(Element elem) {
		List<Element> vectors = getListElementByName("vec4", elem);
		if(vectors.size() != 2) {
			log.error("Invalid vector count in colorOverLife module: "+vectors.size());
			return null;
		}
		Vec4f start = new Vec4f(), end = new Vec4f();
		
		if(vectors.get(0).getAttribute("use").equals("colorStart")) {
			readVec4f(vectors.get(0), start);
			readVec4f(vectors.get(1), end);
		} else {
			readVec4f(vectors.get(1), start);
			readVec4f(vectors.get(0), end);
		}
		
		return new ColorOverLife(start, end);
	}
	
	private static ParticleModule drag(Element elem) {
		float strength = Float.parseFloat(elem.getAttribute("strength"));
		return new Drag(strength);
	}
	
	private static ParticleModule gravity(Element elem) {
		float scale = Float.parseFloat(elem.getAttribute("gravityScale"));
		return new Gravity(scale);
	}
	
	private static ParticleModule initialPosCircle(Element elem) {
		float radiusMin = Float.parseFloat(elem.getAttribute("radiusMin"));
		float radiusMax = Float.parseFloat(elem.getAttribute("radiusMax"));
		
		return new InitialPosCircle(radiusMin, radiusMax);
	}
	
	private static ParticleModule initialVelCone(Element elem) {
		float angleMin = Float.parseFloat(elem.getAttribute("angleMin"));
		float angleMax = Float.parseFloat(elem.getAttribute("angleMax"));
		float velMin = Float.parseFloat(elem.getAttribute("velMin"));
		float velMax = Float.parseFloat(elem.getAttribute("velMax"));
		
		return new InitialVelCone(angleMin, angleMax, velMin, velMax);
	}
	
	private static ParticleModule sizeOverLife(Element elem) {
		List<Element> vectors = getListElementByName("vec2", elem);
		if(vectors.size() != 2) {
			log.error("Invalid vector count in sizeOverLife module: "+vectors.size());
			return null;
		}
		Vec2f start = new Vec2f(), end = new Vec2f();
		
		if(vectors.get(0).getAttribute("use").equals("sizeStart")) {
			readVec2f(vectors.get(0), start);
			readVec2f(vectors.get(1), end);
		} else {
			readVec2f(vectors.get(1), start);
			readVec2f(vectors.get(0), end);
		}
		
		return new SizeOverLife(start, end);
	}
	
	private static void readVec2f(Element element, Vec2f target) {
		target.x = Float.valueOf(element.getAttribute("x"));
		target.y = Float.valueOf(element.getAttribute("y"));
	}
	
	private static void readVec4f(Element element, Vec4f target) {
		target.x = Float.valueOf(element.getAttribute("x"));
		target.y = Float.valueOf(element.getAttribute("y"));
		target.z = Float.valueOf(element.getAttribute("z"));
		target.w = Float.valueOf(element.getAttribute("w"));
	}
}
