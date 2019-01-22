package application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gamedata.GameMap;
import gamedata.entities.Entity;
import gamedata.entities.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import math.Vec2;

public class Enregistreur {
	
	/**
	 * Ouvre une fenêtre d'enregistrement de fichier
	 */
	public static void enregistrer() {
		if(Main.map.spawns.size()<GameMap.MIN_SPAWNS) {
			popupErreur("Not enough spawns! ("+GameMap.MIN_SPAWNS+" minimum)");
			return;
		}
		Stage stage = new Stage();
		FileChooser f = new FileChooser();
		f.setInitialFileName("map.xml");
		f.setInitialDirectory(new File("export"));
		f.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"),
				new FileChooser.ExtensionFilter("All", "*.*"));
		File file = f.showSaveDialog(stage);
		if(file != null) {
			remplissageFichier(file);
		} else {
//			popupErreur("Enregistrement annulé");
			return;
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			erreur();
		}
	}

	/**
	 * Ouvre une fenêtre signalant une erreur
	 */
	public static void erreur() {
		Stage erreur = new Stage();
		erreur.setWidth(200);
		erreur.setHeight(150);
		Button b = new Button("ok");
		VBox root = new VBox(10);
		Scene scene = new Scene(root);
		erreur.setScene(scene);
		Text text = new Text("Erreur");
		text.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().addAll(text, b);
		root.setAlignment(Pos.CENTER);
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				erreur.close();
			}
		});
		erreur.show();

	}

	/**
	 * Ouvre une fenêtre signalant une erreur 
	 * @param message Explication de l'erreur
	 */
	public static void popupErreur(String message) {
		Stage erreur = new Stage();
		erreur.setWidth(200);
		erreur.setHeight(150);
		Button b = new Button("ok");
		VBox root = new VBox(10);
		Scene scene = new Scene(root);
		erreur.setScene(scene);
		Text text = new Text(message);
		text.setTextAlignment(TextAlignment.CENTER);
		root.getChildren().addAll(text, b);
		root.setAlignment(Pos.CENTER);
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				erreur.close();
			}
		});
		erreur.show();

	}

	private static void remplissageFichier(File f) {

		// factory
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;

		// document
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			popupErreur("XML builder erreur");
			e.printStackTrace();
		}
		Document document = null;
		if (builder != null) {
			document = builder.newDocument();
		} else {
			popupErreur("builder null");
		}
		try {
			transformer = transformerFactory.newTransformer();
			//Set file encoding
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			//Enable indentation
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		} catch (Exception e) {
			popupErreur("transformer erreur");
		}
		
		remplissageDocument(document);
		// ecriture
		try {
			transformer.transform(new DOMSource(document), new StreamResult(f));
		} catch (TransformerException e) {
			popupErreur("ecriture fichier fail");
			e.printStackTrace();
		}
	}

	private static void remplissageDocument(Document document) {
		Element root = document.createElement("map");
		document.appendChild(root);
		Element information = document.createElement("information");
		root.appendChild(information);
		remplissageInfomation(document , information);
		
		Element entities = document.createElement("entities");
		root.appendChild(entities);
		remplissageEntitees(document, entities, Main.map.children);
	}

	private static void remplissageEntitees(Document document, Element parentElem, HashMap<String, Entity> entites) {
		for (Entity e : entites.values()) {
			Element elem = null;
			if(e instanceof Platform) { //Platforms
				elem = document.createElement("plateform");
				elem.setAttribute("name", e.name);
				Element position = document.createElement("vecteur");
				Element extent = document.createElement("vecteur");
				
				position.setAttribute("x", String.valueOf( ((Platform)e).position.x ));
				position.setAttribute("y", String.valueOf( ((Platform)e).position.y ));
				position.setAttribute("use", "position");
				
				extent.setAttribute("x", String.valueOf( ((Platform)e).extent.x ));
				extent.setAttribute("y", String.valueOf( ((Platform)e).extent.y ));
				extent.setAttribute("use", "extent");
				
				elem.appendChild(position);
				elem.appendChild(extent);
				
				parentElem.appendChild(elem);
			} else if(e instanceof Entity) { //Entities
				elem = document.createElement("entity");
				elem.setAttribute("name", e.name);
				
				parentElem.appendChild(elem);
			}
			
			//Add children
			if(elem != null && !e.children.isEmpty()) {
				Element children = document.createElement("entities");
				elem.appendChild(children);
				remplissageEntitees(document, children, Main.map.children);
			}
		}
	}

	private static void remplissageInfomation(Document document, Element information) {
		remplissageSpawns(document , information);
		
		// Gravity
		Element gravity = document.createElement("gravity");
		gravity.appendChild(remplissageVec2(document, Main.map.gravity));
		information.appendChild(gravity);
		
		// TODO : Camera Bound
		Element cameraBound = document.createElement("cameraBound");
		cameraBound.setAttribute("x", String.valueOf(-5000));
		cameraBound.setAttribute("y", String.valueOf(-1000));
		cameraBound.setAttribute("z", String.valueOf( 5000));
		cameraBound.setAttribute("w", String.valueOf( 1000));
		information.appendChild(cameraBound);
		
		//Sky
		Element sky = document.createElement("sky");
		Element vecteurT = document.createElement("vecteur");
		Color cTop = Main.map.propertiesTab.skyTop.getValue();
		vecteurT.setAttribute("x", String.valueOf(cTop.getRed()));
		vecteurT.setAttribute("y", String.valueOf(cTop.getGreen()));
		vecteurT.setAttribute("z", String.valueOf(cTop.getBlue()));
		sky.appendChild(vecteurT);
		vecteurT.setAttribute("use", "top");
		
		Element vecteurB = document.createElement("vecteur");
		Color cB = Main.map.propertiesTab.skyBot.getValue();
		vecteurB.setAttribute("x", String.valueOf(cB.getRed()));
		vecteurB.setAttribute("y", String.valueOf(cB.getGreen()));
		vecteurB.setAttribute("z", String.valueOf(cB.getBlue()));
		sky.appendChild(vecteurB);
		vecteurB.setAttribute("use", "bottom");
		information.appendChild(sky);
	}
	
	private static void remplissageSpawns(Document document, Element information) {
		for (Vec2 spawn : Main.map.spawns) {
			Element e = document.createElement("spawn");
			e.appendChild(remplissageVec2(document, spawn));
			information.appendChild(e);
		}
	}

	private static Element remplissageVec2(Document document, double x, double y) {
		Element vecteur = document.createElement("vecteur");
		vecteur.setAttribute("x", String.valueOf(x));
		vecteur.setAttribute("y", String.valueOf(y));
		return vecteur;
	}
	
	private static Element remplissageVec2(Document document, Vec2 vec) {
		return remplissageVec2(document, vec.x, vec.y);
	}
	
	private static Element remplissageVec3(Document document, double x, double y, double z) {
		Element vecteur = document.createElement("vecteur");
		vecteur.setAttribute("x", String.valueOf(x));
		vecteur.setAttribute("y", String.valueOf(y));
		vecteur.setAttribute("z", String.valueOf(z));
		return vecteur;
	}
	
	private static Element remplissageVec4(Document document, double x, double y, double z, double w) {
		Element vecteur = document.createElement("vecteur");
		vecteur.setAttribute("x", String.valueOf(x));
		vecteur.setAttribute("y", String.valueOf(y));
		vecteur.setAttribute("z", String.valueOf(z));
		vecteur.setAttribute("w", String.valueOf(w));
		return vecteur;
	}
}
