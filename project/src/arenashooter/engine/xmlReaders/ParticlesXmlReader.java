package arenashooter.engine.xmlReaders;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParticlesXmlReader extends XmlReader {

	private ParticlesXmlReader() {
		// TODO Auto-generated constructor stub
	}

	public static void read(String path) {
		parse(path);
		
		readEmitters(document.getElementsByTagName("emitter"));
		
	}

	private static void readEmitters(NodeList emitters) {
		for (int i = 0; i < emitters.getLength(); i++) {
			Node node = emitters.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element emitter = (Element) node;
				String duration = emitter.getAttribute("duration");
				String delay = emitter.getAttribute("delay");
				String rate = emitter.getAttribute("rate");
				String lifetimeMin = emitter.getAttribute("lifetimeMin");
				String lifetimeMax = emitter.getAttribute("lifetimeMax");
				String angleMin = emitter.getAttribute("angleMin");
				String angleMax = emitter.getAttribute("angleMax");
				String velocityMin = emitter.getAttribute("velocityMin");
				String velocityMax = emitter.getAttribute("velocityMax");
				
				readEmitterChildren(emitter.getChildNodes());
			}
		}
	}

	private static void readEmitterChildren(NodeList childNodes) {
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if(node.getNodeName() == "basic") {
				Element basic = (Element) node;
			} else if (node.getNodeName() == "oriented") {
				Element oriented = (Element) node;
			} else if (node.getNodeName() == "vec2") {
				Element vec2 = (Element) node;
			} else if (node.getNodeName() == "vec4") {
				Element vec4 = (Element) node;
			} else if (node.getNodeName() == "texture") {
				Element texture = (Element) node;
			}
		}
	}
}
