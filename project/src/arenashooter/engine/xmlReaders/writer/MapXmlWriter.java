package arenashooter.engine.xmlReaders.writer;

import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lwjgl.system.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.game.GameMaster;

public class MapXmlWriter {
	public static final MapXmlWriter writer = new MapXmlWriter();
	static Document doc;
	private static Element info;
	private MapXmlWriter() {

	}

	public static void writerMap(Arena arena, String name, Document doc) {
		// TODO : Creation / instantiation file

		// TODO : creation Arena dans Xml
		Element map = doc.createElement("map");
		doc.appendChild(map);

		Element info = doc.createElement("information");
		map.appendChild(info);

		Element entities = doc.createElement("entities");
		map.appendChild(entities);

		loadChildren(arena, map);
		
		// Accesseurs :
	}

	private static void loadChildren(Entity parent, Element parentBalise) {
		for (String str : parent.getChildren().keySet()) {
			System.out.println("cp");
			Entity child = parent.getChildren().get(str);
			if (child instanceof Spawner) {
				System.out.println("lol");
				Spawner p = (Spawner) child;
				SpawnXml p1 = new SpawnXml(doc, parent, info, true, p.getCooldown());
			}
			//else if(child instanceof )
		}
	}

	public static void main(String argv[]) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			System.out.print("Nom map : ");
			Scanner sc = new Scanner(System.in);
			String line = sc.nextLine();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File file = new File("data/testMap");
			if (!file.exists()) {
				file.mkdirs();
			}
			StreamResult result = new StreamResult(new File("data/testMap/" + line + ".xml"));
			
			// Creation of the test arena
			Arena arena = new Arena();
			MapXmlWriter.writerMap(arena, "test", doc);
			
			Spawner p = new Spawner(new Vec2f(), 0);
			p.attachToParent(arena, "p");

			transformer.transform(source, result);
			System.out.println("File saved!");
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}