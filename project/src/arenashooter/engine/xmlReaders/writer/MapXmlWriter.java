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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.Spawner;

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

		Element info = doc.createElement("information");
		parentBalise.appendChild(info);

		Element entities = doc.createElement("entities");
		parentBalise.appendChild(entities);
		
		for (String str : parent.getChildren().keySet()) {
			System.out.println("cp");
			Entity child = parent.getChildren().get(str);
//			if (child instanceof Spawner) {
//				System.out.println("lol");
//				Spawner p = (Spawner) child;
//				SpawnXml p1 = new SpawnXml(doc, parent, info, true, p.getCooldown());
//			}
			if (child instanceof Mesh) {
				Mesh p = (Mesh) child;
				MeshXml p1 = new MeshXml(doc, entities);
				p1.xPosition = p.parentPosition.x;
				p1.yPosition = p.parentPosition.y;
				p1.zPosition = p.parentPosition.z;
			}
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
			
			Mesh mesh = new Mesh(new Vec3f(), "data/meshes/item_pickup/weapon_pickup.obj");
			mesh.attachToParent(arena, "meshTest");

			transformer.transform(source, result);
			System.out.println("File saved!");
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}