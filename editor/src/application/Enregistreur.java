package application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
		f.setInitialFileName("blep");
		f.setInitialDirectory(new File("icons"));
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
		final TransformerFactory tranformerFactory = TransformerFactory.newInstance();
		Transformer tranformer = null;

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
			tranformer = tranformerFactory.newTransformer();
		} catch (Exception e) {
			popupErreur("transformer erreur");
		}
		
		remplissageDocument(document);
		// ecriture
		try {
			tranformer.transform(new DOMSource(document), new StreamResult(f));
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
		remplissageEntitees(document , entities);
	}

	private static void remplissageEntitees(Document document, Element entities) {
		HashMap<String, Entity> entites = Main.map.children;
		for (Entity entity : entites.values()) {
			if(entity instanceof Platform) {
				Element plateforme = document.createElement("plateform");
				plateforme.setAttribute("name", entity.name);
				Element position = document.createElement("vecteur");
				Element extent = document.createElement("vecteur");
				
				position.setAttribute("x", String.valueOf( ((Platform)entity).position.x ));
				position.setAttribute("y", String.valueOf( ((Platform)entity).position.y ));
				position.setAttribute("use", "position");
				
				extent.setAttribute("x", String.valueOf( ((Platform)entity).extent.x ));
				extent.setAttribute("y", String.valueOf( ((Platform)entity).extent.y ));
				extent.setAttribute("use", "extent");
				
				plateforme.appendChild(position);
				plateforme.appendChild(extent);
				
				entities.appendChild(plateforme);
			}
		}
	}

	private static void remplissageInfomation(Document document, Element information) {
		// Gravity
		Element gravity = document.createElement("gravity");
		gravity.appendChild(remplissageVecteur(document, Main.map.gravity));
		information.appendChild(gravity);
		
		// TODO : Camera Bound
		
		// TODO : Spawns
		
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
	
	private static Element remplissageVecteur(Document document, double x, double y) {
		Element vecteur = document.createElement("vecteur");
		vecteur.setAttribute("x", String.valueOf(x));
		vecteur.setAttribute("y", String.valueOf(y));
		return vecteur;
	}
	
	private static Element remplissageVecteur(Document document, Vec2 vec) {
		return remplissageVecteur(document, vec.x, vec.y);
	}
}
