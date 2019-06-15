package arenashooter.engine.xmlReaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import arenashooter.engine.audio.SoundBuffer;
import arenashooter.engine.math.Quat;

public abstract class XmlReader {
	private final static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	public static final Logger log = LogManager.getLogger("Xml");

	// document
	private static DocumentBuilder builder = null;
	protected static Document document;
	protected static Element root;

	static {
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println("Xml builder construction has failed");
			e.printStackTrace();
		}
	}

	protected XmlReader() {
		// Can not be instantiate
	}

	protected static void parse(String path) {
		try {
			document = builder.parse(path);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		root = document.getDocumentElement();
	}
	
	/**
	 * Preload a sound from a file path
	 * @param path
	 * @return path (unchanged)
	 */
	protected static String preloadSound(String path) {
		SoundBuffer.loadSound(path);
		return path;
	}
	
	/**
	 * Read a rotation as a quaternion or euler angles
	 * @param element
	 * @return
	 */
	public static Quat readRotation(Element element) {
		double x = 0, y = 0, z = 0, w = 0;
		if(element.hasAttribute("x")) {
			x = Double.parseDouble(element.getAttribute("x"));
		} else
			log.error("Missing x value in rotation");
		if(element.hasAttribute("y")) {
			y = Double.parseDouble(element.getAttribute("y"));
		} else
			log.error("Missing y value in rotation");
		if(element.hasAttribute("z")) {
			z = Double.parseDouble(element.getAttribute("z"));
		} else
			log.error("Missing z value in rotation");
		if(element.hasAttribute("w")) {
			w = Double.parseDouble(element.getAttribute("w"));
			return Quat.normalize(new Quat((float)x, (float)y, (float)z, (float)w));
		}
		return Quat.fromEuler(x, y, z);
	}

	public static List<Element> getListElementByName(String name, Element parent) {
		ArrayList<Element> array = new ArrayList<>();
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase(name)) {
				array.add((Element) list.item(i));
			}
		}
		return array;
	}
}
