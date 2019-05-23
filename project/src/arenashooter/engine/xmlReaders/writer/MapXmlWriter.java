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

import arenashooter.entities.spatials.items.Gun;

public class MapXmlWriter {
	Element gun;

	public static void loadGun(Gun gunar, Document doc, Element spawn) {
		Element gun = doc.createElement("gun");
		spawn.appendChild(gun);
	}
	
//	public static void loadPlatform(Document doc, Element entities, float xpos, float ypos, float xext, float yext) {
//		Element plateforme = doc.createElement("plateform");
//		entities.appendChild(plateforme);
//		VecteurXml vecteurPlateforme = new VecteurXml(doc, plateforme);
//		vecteurPlateforme.addVecteur("position", xpos , ypos);
//		vecteurPlateforme.addVecteur("extent", xext , yext);
//		for (Element e : vecteurPlateforme.getVecteurs()) {
//			plateforme.appendChild(e);
//		}
//	}
	
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
			if(!file.exists()) {
				file.mkdirs();
			}
			StreamResult result = new StreamResult(new File("data/testMap/"+line+".xml"));
			
//root elements
			Element map = doc.createElement("map");
			doc.appendChild(map);
//staff elements
			Element info = doc.createElement("information");
			map.appendChild(info);

			Element entities = doc.createElement("entities");
			map.appendChild(entities);
			
			//loadPlatform(doc, entities, 0f, 8f, 1f, 2f);

			SpawnXml spawn = new SpawnXml(doc, info, true, 10);
			
			spawn.addGun();

			transformer.transform(source, result);
			System.out.println("File saved!");
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}