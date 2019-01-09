package arenashooter.engine;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Map;
import arenashooter.entities.spatials.Plateform;

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

		ArrayList<Plateform> plateforms = new ArrayList<>();
		ArrayList<Vec2f> spawn = new ArrayList<>();
		Vec4f cameraBounds = new Vec4f();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeName() == "information") {
				NodeList infos = children.item(i).getChildNodes();
				for (int j = 0; j < infos.getLength(); j++) {
					if (infos.item(j).getNodeName() == "spawn") {
						NodeList spawns = infos.item(j).getChildNodes();
						for (int k = 0; k < spawns.getLength(); k++) {
							if (spawns.item(k).getNodeName() == "vecteur") {
								Element position = (Element) spawns.item(k);
								float x = Float.parseFloat(position.getAttribute("x"));
								float y = Float.parseFloat(position.getAttribute("y"));
								spawn.add(new Vec2f(x, y));
							}
						}
					}
					if (infos.item(j).getNodeName() == "cameraBound") {
						Element cameraBound = (Element) infos.item(j);
						cameraBounds = new Vec4f(Float.parseFloat(cameraBound.getAttribute("x")),
								Float.parseFloat(cameraBound.getAttribute("y")),
								Float.parseFloat(cameraBound.getAttribute("z")),
								Float.parseFloat(cameraBound.getAttribute("w")));
					}
				}
			}
			if (children.item(i).getNodeName() == "entity") {
				NodeList entities = children.item(i).getChildNodes();
				for (int j = 0; j < entities.getLength(); j++) {
					if (entities.item(j).getNodeName() == "plateform") {
						Vec2f position = new Vec2f();
						Vec2f extent = new Vec2f();
						NodeList vectors = entities.item(j).getChildNodes();
						for (int k = 0; k < vectors.getLength(); k++) {
							if (vectors.item(k).getNodeType() == Node.ELEMENT_NODE) {
								Element vector = (Element) vectors.item(k);
								if (vector.hasAttribute("use") && vector.getAttribute("use").equals("position")) {
									position.x = Float.parseFloat(vector.getAttribute("x"));
									position.y = Float.parseFloat(vector.getAttribute("y"));
								}
								if (vector.hasAttribute("use") && vector.getAttribute("use").equals("extent")) {
									extent.x = Float.parseFloat(vector.getAttribute("x"));
									extent.y = Float.parseFloat(vector.getAttribute("y"));
								}
							}
						}
						plateforms.add(new Plateform(position, extent));
					}
				}
			}
		}
		Map map = new Map(plateforms);
		map.cameraBounds = cameraBounds;
		map.spawn = spawn;
		return map;
	}

}
