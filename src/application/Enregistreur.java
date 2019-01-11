package application;

import java.io.File;
import java.io.IOException;

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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Enregistreur {
	/**
	 * Ouvre une fenêtre d'enregistrement de fichier
	 */
	public static void enregistrer() {
		Stage stage = new Stage();
		FileChooser f = new FileChooser();
		f.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"),
				new FileChooser.ExtensionFilter("All", "*.*"));
		File file = f.showSaveDialog(stage);
		if(file != null) {
			remplissageFichier(file);
		} else {
			popupErreur("Enregistrement annul�");
		}
		try {
			if (file != null) {
				file.createNewFile();
			} else {
				popupErreur("Enregistrement annul�");
			}
		} catch (IOException e) {
			e.printStackTrace();
			erreur();
		}
	}

	/**
	 * Ouvre une fen�tre signalant une erreur
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
	 * Ouvre une fen�tre signalant une erreur 
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
		
		// TODO : remplissage entit�s
	}

	private static void remplissageInfomation(Document document, Element information) {
		// Gravity
		Element gravity = document.createElement("gravity");
		remplissageVecteur(document, gravity, 0, Informations.gravity);
		information.appendChild(gravity);
		
		// TODO : Camera Bound
		
		// TODO : Spawns
		
	}
	
	private static void remplissageVecteur(Document document , Element e , double x , double y) {
		Element vecteur = document.createElement("vecteur");
		vecteur.setAttribute("x", ""+x);
		vecteur.setAttribute("y", ""+y);
		e.appendChild(vecteur);
	}
}
