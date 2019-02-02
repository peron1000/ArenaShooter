package application;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import math.Vec2;

public final class MapImporter {
	private final static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	private static DocumentBuilder builder = null;
	protected static Document document;
	protected static Element root;
	
	// Entities variables
	private int iteratorEntite = 0;
	private NodeList entitiesNodeList;

	// Informations variables
	private int iteratorInfo = 0;
//	private Vec4f cameraBounds = new Vec4f();
	private NodeList infoNodeList;

	static {
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println("Xml builder construction has failed");
			e.printStackTrace();
		}
	}
	
	public static void load() {
		Stage stage = new Stage();
		FileChooser f = new FileChooser();
		f.setInitialDirectory(new File("export"));
		f.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"),
				new FileChooser.ExtensionFilter("All", "*.*"));
		File file = f.showOpenDialog(stage);
		
		if(file != null)
			if(Main.popupConfirmation("Warning", "Unsaved changes will be lost, proceed?"))
				new MapImporter(file.getPath());
	}

	private MapImporter(String path) { 
		Main.clear();
		
		try {
			document = builder.parse(path);
			
			NodeList infoTag = document.getElementsByTagName("information");
			infoNodeList = infoTag.item(0).getChildNodes();
			iteratorInfo = 0;

			NodeList entitieTag = document.getElementsByTagName("entities");
			entitiesNodeList = entitieTag.item(0).getChildNodes();
			iteratorEntite = 0;
			
			root = document.getDocumentElement();
			
			//Load map info
			while(!loadNextInformation());

			//Load entities
			while(!loadNextEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load a new entity
	 * 
	 * @return <i>true</i> if all entities are already loaded
	 */
	public boolean loadNextEntity() {
		if (iteratorEntite < entitiesNodeList.getLength() && entitiesNodeList.item(iteratorEntite).getNodeType() == Node.ELEMENT_NODE) {
			Element entity = (Element) entitiesNodeList.item(iteratorEntite);

			switch(entity.getNodeName()) {
			case "entity":
				loadEntity(entity);
				break;
			case "plateform":
				loadPlateform(entity);
				break;
			case "weapon":
				break;
			case "mesh":
				break;
			case "text":
				break;
			default:
				break;
			}
			
		}
		
		iteratorEntite++;
		return !(iteratorEntite < entitiesNodeList.getLength());
	}
	
	/**
	 * Load a new information
	 * 
	 * @return <i>true</i> if all informations are already loaded
	 */
	public boolean loadNextInformation() {
		if (iteratorInfo < infoNodeList.getLength()
				&& infoNodeList.item(iteratorInfo).getNodeType() == Node.ELEMENT_NODE) {
			Element info = (Element) infoNodeList.item(iteratorInfo);

			if (info.getNodeName() == "spawn") {

				NodeList spawns = info.getChildNodes();
				for (int i = 0; i < spawns.getLength(); i++) {
					if (spawns.item(i).getNodeName() == "vecteur") {
						Element position = (Element) spawns.item(i);
						float x = Float.parseFloat(position.getAttribute("x"));
						float y = Float.parseFloat(position.getAttribute("y"));
						Main.map.addSpawn(new Vec2(x, y));
					}
				}
			} else if (info.getNodeName() == "gravity") {
				// Navigation into Vector part
				NodeList vector = info.getChildNodes();
				for (int i = 0; i < vector.getLength(); i++) {
					if (vector.item(i).getNodeName() == "vecteur") {
						Element position = (Element) vector.item(i);
						Main.map.gravity.x = Float.parseFloat(position.getAttribute("x"));
						Main.map.gravity.y = Float.parseFloat(position.getAttribute("y"));
					}
				}
			} else if (info.getNodeName() == "cameraBound") {
				// Navigation into CameraBound part
//				cameraBounds.x = Float.parseFloat(info.getAttribute("x"));
//				cameraBounds.y = Float.parseFloat(info.getAttribute("y"));
//				cameraBounds.z = Float.parseFloat(info.getAttribute("z"));
//				cameraBounds.w = Float.parseFloat(info.getAttribute("w"));
			} else if (info.getNodeName() == "sky") {
				Color bottom = new Color(1, 1, 1, 1);
				Color top = new Color(1, 1, 1, 1);
				NodeList vectors = info.getChildNodes();
				for (int i = 0; i < vectors.getLength(); i++) {
					if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element vector = (Element) vectors.item(i);
						if (vector.hasAttribute("use") && vector.getAttribute("use").equals("bottom")) {
							float r = Float.parseFloat(vector.getAttribute("x"));
							float g = Float.parseFloat(vector.getAttribute("y"));
							float b = Float.parseFloat(vector.getAttribute("z"));
							bottom = new Color(r, g, b, 1);
						}
						if (vector.hasAttribute("use") && vector.getAttribute("use").equals("top")) {
							float r = Float.parseFloat(vector.getAttribute("x"));
							float g = Float.parseFloat(vector.getAttribute("y"));
							float b = Float.parseFloat(vector.getAttribute("z"));
							top = new Color(r, g, b, 1);
						}
					}
				}

				Main.map.propertiesTab.skyBot.setValue(bottom);
				Main.map.propertiesTab.skyTop.setValue(top);
			}

		}
		iteratorInfo++;
		return !(iteratorInfo < infoNodeList.getLength());
	}
	
	/**
	 * Change position and extent to match with the vectors given in the NodeList
	 * 
	 * @param vectors
	 * @param position
	 * @param extent
	 */
	private void getVectorsEntity(NodeList vectors, Vec2 position, Vec2 extent) {
		for (int i = 0; i < vectors.getLength(); i++) {
			if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element vector = (Element) vectors.item(i);
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
	}
	
	private void loadEntity(Element entity) {
		if( entity.hasAttribute("name") )
			ListEntite.newEntity( entity.getAttribute("name") );
		else
			ListEntite.newEntity();
	}
	
	private void loadPlateform(Element entity) {
		Vec2 position = new Vec2();
		Vec2 extent = new Vec2();
		NodeList vectors = entity.getChildNodes();
		getVectorsEntity(vectors, position, extent);
		
		if( entity.hasAttribute("name") )
			ListEntite.newPlatform(position, extent, entity.getAttribute("name"));
		else
			ListEntite.newPlatform(position, extent);
	}
	
//	private Weapon loadWeapon(Element entity) {
//		Vec2f position = new Vec2f();
//		NodeList weapVecs = entity.getChildNodes();
//		getVectorsEntity(weapVecs, position, new Vec2f());
//		return new Weapon(position, SpritePath.iongun); //TODO: Weapon import
//	}
//	
//	private Mesh loadMesh(Element entity) {
//		Vec3f position = new Vec3f();
//		Quat rot = Quat.fromAngle(0);
//		Vec3f scale = new Vec3f(1);
//		String src = entity.getAttribute("src");
//		
//		NodeList vectors = entity.getChildNodes();
//		for (int i = 0; i < vectors.getLength(); i++) {
//			if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
//				Element vector = (Element) vectors.item(i);
//				if(vector.hasAttribute("use") && vector.getAttribute("use").equals("position")) {
//					position.x = Float.parseFloat(vector.getAttribute("x"));
//					position.y = Float.parseFloat(vector.getAttribute("y"));
//					position.z = Float.parseFloat(vector.getAttribute("z"));
//				} else if(vector.hasAttribute("use") && vector.getAttribute("use").equals("rotation")) {
//					float w = Float.parseFloat(vector.getAttribute("w"));
//					float x = Float.parseFloat(vector.getAttribute("x"));
//					float y = Float.parseFloat(vector.getAttribute("y"));
//					float z = Float.parseFloat(vector.getAttribute("z"));
//					rot = new Quat(x, y, z, w);
//				} else if(vector.hasAttribute("use") && vector.getAttribute("use").equals("scale")) {
//					scale.x = Float.parseFloat(vector.getAttribute("x"));
//					scale.y = Float.parseFloat(vector.getAttribute("y"));
//					scale.z = Float.parseFloat(vector.getAttribute("z"));
//				}
//			}
//		}
//		
//		return new Mesh(position, rot, scale, src);
//	}
//	
//	private TextSpatial loadText(Element entity) {
//		Font font = Font.loadFont(entity.getAttribute("font"));
//		if( font != null ) {
//			Vec3f position = new Vec3f();
//			Vec3f size = new Vec3f(1);
//			String content = entity.getAttribute("content");
//			NodeList vectors = entity.getChildNodes();
//			for (int i = 0; i < vectors.getLength(); i++) {
//				if (vectors.item(i).getNodeType() == Node.ELEMENT_NODE) {
//					Element vector = (Element) vectors.item(i);
//					if(vector.hasAttribute("use") && vector.getAttribute("use").equals("position")) {
//						position.x = Float.parseFloat(vector.getAttribute("x"));
//						position.y = Float.parseFloat(vector.getAttribute("y"));
//						position.y = Float.parseFloat(vector.getAttribute("z"));
//					} else if(vector.hasAttribute("use") && vector.getAttribute("use").equals("scale")) {
//						size.x = Float.parseFloat(vector.getAttribute("x"));
//						size.y = Float.parseFloat(vector.getAttribute("y"));
//					}
//				}
//			}
//			Text text = new Text(font, Text.TextAlignH.CENTER, content); //TODO: Load text align from XML
//			return new TextSpatial(position, size, text);
//		}
//		return null;
//	}
}
