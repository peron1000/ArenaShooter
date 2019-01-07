package arenashooter.engine;

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
import arenashooter.entities.Map;

public class MapXMLTranslator {

	// factory
	private final static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	// document
	private static DocumentBuilder builder;
	private static Document document;
	private static Element root;
	private static NodeList children;

	private MapXMLTranslator() {
	}

	public static Map getMap(String pathNomMap) {
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(pathNomMap);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		root = document.getDocumentElement();
		children = root.getChildNodes();

		Map map = new Map();
		map.spawn = new ArrayList<>();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeName() == "information") {
				NodeList infos = children.item(i).getChildNodes();
				for (int j = 0; j < infos.getLength(); j++) {
					if(infos.item(j).getNodeName() == "spawn") {
						NodeList spawns = infos.item(j).getChildNodes();
						for (int k = 0; k < spawns.getLength(); k++) {
							if(spawns.item(k).getNodeName() == "position") {
								Element position = (Element) spawns.item(k);
								float x = Float.parseFloat(position.getAttribute("x"));
								float y = Float.parseFloat(position.getAttribute("y"));
								map.spawn.add(new Vec2f(x, y));
							}
						}
					}
				}
			}
			if (children.item(i).getNodeName() == "entity") {
				//TODO : platforms
			}
		}

		return map;
	}

}
