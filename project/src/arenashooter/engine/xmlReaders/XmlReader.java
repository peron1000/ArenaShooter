package arenashooter.engine.xmlReaders;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;

public abstract class XmlReader {
	private final static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	// document
	private static DocumentBuilder builder = null;
	protected static Document document;
	protected static Element root;

	static {
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("Xml builder construction has failed");
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

	public static Map readMap(String path) {
		parse(path);

		ArrayList<Entity> entities = new ArrayList<>();
		ArrayList<Vec2f> spawn = new ArrayList<>();
		Vec2f gravity = new Vec2f(0, 9.807);
		Vec4f cameraBounds = new Vec4f();

		// Navigation into Information part
		NodeList infos = root.getElementsByTagName("information");
		for (int i = 0; i < infos.getLength(); i++) {
			if (infos.item(i).getNodeName() == "spawn") {

				// Navigation into Spawn part

				NodeList spawns = infos.item(i).getChildNodes();
				for (int j = 0; j < spawns.getLength(); j++) {
					if (spawns.item(j).getNodeName() == "vecteur") {
						Element position = (Element) spawns.item(j);
						float x = Float.parseFloat(position.getAttribute("x"));
						float y = Float.parseFloat(position.getAttribute("y"));
						spawn.add(new Vec2f(x, y));
					}
				}
			} else if (infos.item(i).getNodeName() == "gravity") {
				// Navigation into Vector part
				NodeList vector = infos.item(i).getChildNodes();
				for (int j = 0; j < vector.getLength(); j++) {
					if (vector.item(j).getNodeName() == "vecteur") {
						Element position = (Element) vector.item(j);
						gravity.x = Float.parseFloat(position.getAttribute("x"));
						gravity.y = Float.parseFloat(position.getAttribute("y"));
					}
				}
			} else if (infos.item(i).getNodeName() == "cameraBound") {
				// Navigation into CameraBound part

				Element cameraBound = (Element) infos.item(i);
				cameraBounds = new Vec4f(Float.parseFloat(cameraBound.getAttribute("x")),
						Float.parseFloat(cameraBound.getAttribute("y")),
						Float.parseFloat(cameraBound.getAttribute("z")),
						Float.parseFloat(cameraBound.getAttribute("w")));
			}
		}

		return new Map(0);
	}
	
	private void mapInformationReader() {
		
	}
}
