package arenashooter.engine.xmlReaders.writer;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import arenashooter.engine.animation.AnimationDataEditable;
import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.animevents.AnimEventCustom;
import arenashooter.engine.animation.animevents.AnimEventSound;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class AnimXmlWriter {

	public static final Logger log = LogManager.getLogger("Xml");

	public static final AnimXmlWriter writer = new AnimXmlWriter();
	public static Document doc;

	private AnimXmlWriter() {
	}
	
	public static void exportArena(AnimationDataEditable anim, String name) {
		try {
			// Creation / instantiation file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// creation Animation
			// Document
			doc = docBuilder.newDocument();

			Element animation = doc.createElement("animation");
			animation.setAttribute("length", anim.length + "");
			animation.setAttribute("loop", anim.loop + "");
			doc.appendChild(animation);

			if (anim.trackEvents != null) {
				/*
				 * <!ELEMENT trackEvent (keyframeEvent+)> <!ELEMENT keyframeEvent
				 * (eventCustom|eventSound)> <!ATTLIST keyframeEvent time CDATA #REQUIRED>
				 * <!ELEMENT eventCustom EMPTY> <!ATTLIST eventCustom data CDATA #REQUIRED>
				 * <!ELEMENT eventSound EMPTY> <!ATTLIST eventSound path CDATA #REQUIRED>
				 * <!ATTLIST eventSound channel (SFX|MUSIC|UI) #REQUIRED> <!ATTLIST eventSound
				 * volume CDATA #REQUIRED> <!ATTLIST eventSound pitch CDATA #REQUIRED> <!ATTLIST
				 * eventSound localized (true|false) #REQUIRED>
				 */
				for (Entry<Double, AnimEvent> te : anim.trackEvents.entrySet()) {
					Element trackEvent = doc.createElement("trackEvent");
					animation.appendChild(trackEvent);

					Element keyframeEvent = doc.createElement("keyframeEvent");
					keyframeEvent.setAttribute("time", te.getKey() + "");
					trackEvent.appendChild(keyframeEvent);
					// <eventCustom data=""/>

					if (te.getValue() instanceof AnimEventCustom) {
						Element eventCustom = doc.createElement("eventCustom");
						eventCustom.setAttribute("data", ((AnimEventCustom) te.getValue()).data);
						keyframeEvent.appendChild(eventCustom);
					}
					// <eventSound localized="true" volume="" channel="SFX" pitch="" path=""/>
					if (te.getValue() instanceof AnimEventSound) {
						Element eventSound = doc.createElement("eventSound");
						eventSound.setAttribute("localized", ((AnimEventSound) te.getValue()).spatialized + "");
						eventSound.setAttribute("volume", ((AnimEventSound) te.getValue()).volume + "");
						eventSound.setAttribute("channel", ((AnimEventSound) te.getValue()).channel + "");
						eventSound.setAttribute("pitch", ((AnimEventSound) te.getValue()).pitch + "");
						eventSound.setAttribute("path", ((AnimEventSound) te.getValue()).path + "");
						keyframeEvent.appendChild(eventSound);
					}
				}

			}

			if (anim.tracksD != null) {

				/*
				 * <!ELEMENT trackD (keyframeD+)> <!ATTLIST trackD name ID #REQUIRED> <!ATTLIST
				 * trackD interpolation (linear|cubic) "cubic"> <!ELEMENT keyframeD EMPTY>
				 * <!ATTLIST keyframeD time CDATA #REQUIRED> <!ATTLIST keyframeD value CDATA
				 * #REQUIRED>
				 */

				for (Entry<String, Map<Double, Double>> tD : anim.tracksD.entrySet()) {
					Element trackD = doc.createElement("trackD");
					animation.appendChild(trackD);

					trackD.setAttribute("name", tD.getKey());
					// TODO
					// trackD.setAttribute("interpolation", + "");

					for (Entry<Double, Double> AuDD : tD.getValue().entrySet()) {
						Element keyframeD = doc.createElement("keyframeD");
						keyframeD.setAttribute("time", AuDD.getKey() + "");
						keyframeD.setAttribute("value", AuDD.getValue() + "");
						trackD.appendChild(keyframeD);
					}
				}

			}

			if (anim.tracksT != null) {
				/*
				 * <!ELEMENT trackT (keyframeT+)> <!ATTLIST trackT name ID #REQUIRED> <!ELEMENT
				 * keyframeT EMPTY> <!ATTLIST keyframeT time CDATA #REQUIRED> <!ATTLIST
				 * keyframeT value CDATA #REQUIRED>
				 */
				for (Entry<String, Map<Double, Texture>> tT : anim.tracksT.entrySet()) {
					Element tracksT = doc.createElement("tracksT");
					tracksT.setAttribute("name", tT.getKey());
					animation.appendChild(tracksT);
					
					for (Entry<Double, Texture> AuDD : tT.getValue().entrySet()) {
						Element keyframeT = doc.createElement("keyframeT");
						keyframeT.setAttribute("time", AuDD.getKey() + "");
						keyframeT.setAttribute("value", AuDD.getValue() + "");
						tracksT.appendChild(keyframeT);
					}
				}
				if (anim.tracksVec2f != null) {
					/*
					 * <!ELEMENT trackVec2f (keyframeVec2f+)> <!ATTLIST trackVec2f name ID
					 * #REQUIRED> <!ATTLIST trackVec2f interpolation (linear|cubic) "cubic">
					 * <!ELEMENT keyframeVec2f EMPTY> <!ATTLIST keyframeVec2f time CDATA #REQUIRED>
					 * <!ATTLIST keyframeVec2f x CDATA #REQUIRED> <!ATTLIST keyframeVec2f y CDATA
					 * #REQUIRED>
					 * 
					 */
					for (Entry<String, Map<Double, Vec2f>> tV2 : anim.tracksVec2f.entrySet()) {
						Element tracksVec2f = doc.createElement("tracksVec2f");
						tracksVec2f.setAttribute("name", tV2.getKey());
						animation.appendChild(tracksVec2f);
						// TODO interpolation

						for (Entry<Double, Vec2f> AuDD : tV2.getValue().entrySet()) {
							Element keyframeVec2f = doc.createElement("keyframeVec2f");
							keyframeVec2f.setAttribute("time", AuDD.getKey() + "");
							keyframeVec2f.setAttribute("x", AuDD.getValue().x + "");
							keyframeVec2f.setAttribute("y", AuDD.getValue().y + "");
							tracksVec2f.appendChild(keyframeVec2f);
						}

					}
				}
				if (anim.tracksVec3f != null) {
					/*
					 * <!ELEMENT trackVec3f (keyframeVec3f+)> <!ATTLIST trackVec3f name ID
					 * #REQUIRED> <!ATTLIST trackVec3f interpolation (linear|cubic) "cubic">
					 * <!ELEMENT keyframeVec3f EMPTY> <!ATTLIST keyframeVec3f time CDATA #REQUIRED>
					 * <!ATTLIST keyframeVec3f x CDATA #REQUIRED> <!ATTLIST keyframeVec3f y CDATA
					 * #REQUIRED> <!ATTLIST keyframeVec3f z CDATA #REQUIRED>
					 */

					for (Entry<String, Map<Double, Vec3f>> tV3 : anim.tracksVec3f.entrySet()) {
						Element tracksVec3f = doc.createElement("tracksVec3f");
						animation.appendChild(tracksVec3f);
						tracksVec3f.setAttribute("name", tV3.getKey());

						// TODO interpolation

						for (Entry<Double, Vec3f> AuDD : tV3.getValue().entrySet()) {
							Element keyframeVec3f = doc.createElement("keyframeVec3f");
							keyframeVec3f.setAttribute("time", AuDD.getKey() + "");
							keyframeVec3f.setAttribute("x", AuDD.getValue().x + "");
							keyframeVec3f.setAttribute("y", AuDD.getValue().y + "");
							keyframeVec3f.setAttribute("z", AuDD.getValue().z + "");
							tracksVec3f.appendChild(keyframeVec3f);
						}
					}
				}

			}

			// Create arenas folder if necessary
			File file1 = new File("data/animations");
			if (!file1.exists()) {
				file1.mkdirs();
			}

			File file = new File("data/animations/" + name + ".xml");

			// Set file encoding
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			// Enable indentation
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			// Add DOCTYPE
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType docType = domImpl.createDocumentType("doctype", "", "animationDTD.dtd");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());

			// Write file
			StreamResult resultat = new StreamResult(file);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, resultat);

			log.info("Successfully exported your Animation to " + file.getPath());

		} catch (

		ParserConfigurationException pce) {
			pce.printStackTrace();
			System.out.println(pce);
		} catch (TransformerException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

}


/*
 * 
 * <?xml version="1.0" encoding="UTF-8"?> <!ELEMENT animation
 * (trackEvent?,(trackD|trackT|trackVec2f|trackVec3f)*)> <!ATTLIST animation
 * length CDATA #REQUIRED> <!ATTLIST animation loop (true|false) "false">
 * 
 * <!ELEMENT trackD (keyframeD+)> <!ATTLIST trackD name ID #REQUIRED> <!ATTLIST
 * trackD interpolation (linear|cubic) "cubic"> <!ELEMENT keyframeD EMPTY>
 * <!ATTLIST keyframeD time CDATA #REQUIRED> <!ATTLIST keyframeD value CDATA
 * #REQUIRED>
 * 
 * <!ELEMENT trackT (keyframeT+)> <!ATTLIST trackT name ID #REQUIRED> <!ELEMENT
 * keyframeT EMPTY> <!ATTLIST keyframeT time CDATA #REQUIRED> <!ATTLIST
 * keyframeT value CDATA #REQUIRED>
 * 
 * <!ELEMENT trackVec2f (keyframeVec2f+)> <!ATTLIST trackVec2f name ID
 * #REQUIRED> <!ATTLIST trackVec2f interpolation (linear|cubic) "cubic">
 * <!ELEMENT keyframeVec2f EMPTY> <!ATTLIST keyframeVec2f time CDATA #REQUIRED>
 * <!ATTLIST keyframeVec2f x CDATA #REQUIRED> <!ATTLIST keyframeVec2f y CDATA
 * #REQUIRED>
 * 
 * <!ELEMENT trackVec3f (keyframeVec3f+)> <!ATTLIST trackVec3f name ID
 * #REQUIRED> <!ATTLIST trackVec3f interpolation (linear|cubic) "cubic">
 * <!ELEMENT keyframeVec3f EMPTY> <!ATTLIST keyframeVec3f time CDATA #REQUIRED>
 * <!ATTLIST keyframeVec3f x CDATA #REQUIRED> <!ATTLIST keyframeVec3f y CDATA
 * #REQUIRED> <!ATTLIST keyframeVec3f z CDATA #REQUIRED>
 * 
 * <!ELEMENT trackEvent (keyframeEvent+)> <!ELEMENT keyframeEvent
 * (eventCustom|eventSound)> <!ATTLIST keyframeEvent time CDATA #REQUIRED>
 * <!ELEMENT eventCustom EMPTY> <!ATTLIST eventCustom data CDATA #REQUIRED>
 * <!ELEMENT eventSound EMPTY> <!ATTLIST eventSound path CDATA #REQUIRED>
 * <!ATTLIST eventSound channel (SFX|MUSIC|UI) #REQUIRED> <!ATTLIST eventSound
 * volume CDATA #REQUIRED> <!ATTLIST eventSound pitch CDATA #REQUIRED> <!ATTLIST
 * eventSound localized (true|false) #REQUIRED>
 * 
 * 
 */
