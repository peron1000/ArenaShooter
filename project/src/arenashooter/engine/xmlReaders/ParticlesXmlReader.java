package arenashooter.engine.xmlReaders;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.particles.EmitterTemplate;
import arenashooter.engine.graphics.particles.EmitterTemplateBasic;
import arenashooter.engine.graphics.particles.EmitterTemplateSparks;
import arenashooter.engine.math.Vec4f;

public class ParticlesXmlReader extends XmlReader {

	private static List<EmitterTemplate> data;
	private static EmitterTemplate currentEmitter;
	private static Element emitterType;
	private static Texture texture;
	private static Vec4f colorStart, colorEnd;
	
	private ParticlesXmlReader() { }

	public static EmitterTemplate[] read(String path) {
		parse(path);
		
		data = new ArrayList<>();
		
		readEmitters(document.getElementsByTagName("emitter"));
		
		return data.toArray( new EmitterTemplate[data.size()] );
	}

	private static void readEmitters(NodeList emitters) {
		for (int i = 0; i < emitters.getLength(); i++) {
			Node node = emitters.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				currentEmitter = null;
				colorStart = new Vec4f(1);
				colorEnd = new Vec4f(1);
				
				Element emitter = (Element) node;
				
				float duration = Float.valueOf(emitter.getAttribute("duration"));
				float delay = Float.valueOf(emitter.getAttribute("delay"));
				float rate = Float.valueOf(emitter.getAttribute("rate"));
				float lifetimeMin = Float.valueOf(emitter.getAttribute("lifetimeMin"));
				float lifetimeMax = Float.valueOf(emitter.getAttribute("lifetimeMax"));
				float angleMin = Float.valueOf(emitter.getAttribute("angleMin"));
				float angleMax = Float.valueOf(emitter.getAttribute("angleMax"));
				float velocityMin = Float.valueOf(emitter.getAttribute("velocityMin"));
				float velocityMax = Float.valueOf(emitter.getAttribute("velocityMax"));
				float gravityScale = Float.valueOf(emitter.getAttribute("gravityScale"));

				readEmitterChildren(emitter.getChildNodes());
				
				if(emitterType.getTagName().equals("basic")) {
					float sizeInitial = Float.valueOf(emitterType.getAttribute("sizeInitial"));
					float sizeEnd = Float.valueOf(emitterType.getAttribute("sizeEnd"));
					currentEmitter = new EmitterTemplateBasic(texture, duration, delay, rate, 
							lifetimeMin, lifetimeMax, 
							colorStart, colorEnd, 
							angleMin, angleMax, 
							velocityMin, velocityMax,
							sizeInitial, sizeEnd, 
							gravityScale );
				} else if(emitterType.getTagName().equals("sparks")) {
					currentEmitter = new EmitterTemplateSparks(texture, duration, delay, rate, 
							lifetimeMin, lifetimeMax, 
							colorStart, colorEnd, 
							angleMin, angleMax, 
							velocityMin, velocityMax, 
							gravityScale );
				}
				
				if(currentEmitter != null)
					data.add(currentEmitter);
			}
		}
	}

	private static void readEmitterChildren(NodeList childNodes) {
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if( node.getNodeName().equals("basic") || node.getNodeName().equals("oriented") ) {
				emitterType = (Element) node;
			} else if (node.getNodeName().equals("vec4")) { //Read colors
				Element vec4 = (Element) node;
				
				if(vec4.getAttribute("use").equals("colorStart"))
					readVec4f(vec4, colorStart);
				else if(vec4.getAttribute("use").equals("colorEnd"))
					readVec4f(vec4, colorEnd);
						
			} else if (node.getNodeName().equals("texture")) { //Read texture
				Element textureElem = (Element) node;
				
				texture = Texture.loadTexture(textureElem.getAttribute("src"));
				texture.setFilter(textureElem.getAttribute("filtered").equals("true"));
			}
		}
	}
	
	private static void readVec4f(Element element, Vec4f target) {
		target.w = Float.valueOf(element.getAttribute("w"));
		target.x = Float.valueOf(element.getAttribute("x"));
		target.y = Float.valueOf(element.getAttribute("y"));
		target.z = Float.valueOf(element.getAttribute("z"));
	}
}
