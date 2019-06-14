package arenashooter.engine.xmlReaders.writer;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lwjgl.opengl.WGL;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.graphics.Light.LightType;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.physic.bodies.KinematicBody;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.KinematicBodyContainer;
import arenashooter.entities.spatials.LightContainer;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.StaticBodyContainer;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Melee;
import arenashooter.entities.spatials.items.Shotgun;

public class MapXmlWriter {
	public static final MapXmlWriter writer = new MapXmlWriter();
	public static Document doc;
	private static Element info;

	private MapXmlWriter() {

	}

	public static void writerMap(Arena arena, String name) {
		try {
			// Creation / instantiation file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// creation Arena dans Xml
			// Document
			doc = docBuilder.newDocument();

			Element map = doc.createElement("map");
			doc.appendChild(map);

			Element info = doc.createElement("information");
			map.appendChild(info);

			Element entities = doc.createElement("entities");
			map.appendChild(entities);

			// items de la map
			for (Entry<String, Item> it : arena.spawnList.entrySet()) {
				Element item = doc.createElement("item");
				info.appendChild(item);
				if (it instanceof Shotgun) {
					new ShootgunXml(doc, item, (Shotgun) it.getValue());
				}
				if (it instanceof Gun) {
					new GunXml(doc, item, (Gun) it.getValue());
				}

				if (it instanceof Melee) {
					new MeleeXml(doc, item, (Melee) it.getValue());
				}
			}

//			<spawn playerSpawn="false" cooldown="2"> 
//			<vector x="7" y="1" use="position" />
//			<itemRef item="kata" proba="1" />
//			<entities>
//				<mesh src="data/meshes/item_pickup/weapon_pickup.obj">
//					<vector use="position" x="0" y="1" z="0" />
//					<vector use="rotation" x="0" y="0" z="0" w="1" />
//					<vector use="scale" x=".5" y=".5" z=".5" />
//				</mesh>
//			</entities>
//		</spawn>
			// </entities>

			for (Entry<String, Entity> entry : arena.getChildren().entrySet()) {

				/*
				 * Spawn* / /*-
				 */

				// <spawn playerSpawn="false" cooldown="2">
				if (entry.getValue() instanceof Spawner) {
					Element spawn = doc.createElement("spawn");
					entities.appendChild(spawn);
					Attr spawnpers = doc.createAttribute("playerSpawn");
					if (arena.playerSpawns.contains(((Spawner) entry.getValue()).localPosition)) {
						spawnpers.setValue(true + "");
						spawn.setAttributeNode(spawnpers);
					} else {
						spawnpers.setValue(false + "");
						spawn.setAttributeNode(spawnpers);
					}
					Attr cooldown = doc.createAttribute("cooldown");
					cooldown.setValue(((Spawner) entry.getValue()).getCooldown() + "");
					spawn.setAttributeNode(cooldown);
					// <vector x="7" y="1" use="position" />
					Element vector = doc.createElement("vector");
					VectorAttr2(vector, "position", ((Spawner) entry.getValue()).localPosition.x,
							((Spawner) entry.getValue()).localPosition.y);
					spawn.appendChild(vector);

					// <itemRef item="minigun" proba="1" />
					for (Entry<String, Integer> itemavaible : ((Spawner) entry.getValue()).getAvailableItems()
							.entrySet()) {
						Element itemRef = doc.createElement("itemRef");
						itemRef.setAttribute("item", itemavaible.getKey() + "");
						itemRef.setAttribute("proba", itemavaible.getValue() + "");
						spawn.appendChild(itemRef);
					}
//		 <entities>
//				<mesh src="data/meshes/item_pickup/weapon_pickup.obj">
//				<vector use="position" x="0" y="1" z="0" />
//				<vector use="rotation" x="0" y="0" z="0" w="1" />
//				<vector use="scale" x=".5" y=".5" z=".5" />
//			</mesh>
//		</entities>
					Element entitiespawn = doc.createElement("entities");
					spawn.appendChild(entitiespawn);
					ADDelemnts(spawn, entry.getValue());
				}

				/* Rigid */
				/*
				 * <vector x="-5" y="-3" use="position" /> <vector x=".5" y=".5" use="extent" />
				 */
				if (entry.getValue() instanceof RigidBodyContainer) {
					Element rigid = doc.createElement("rigid");
					entities.appendChild(rigid);
					RigidBodyContainer rbc = (RigidBodyContainer) entry.getValue();
					Element vec1 = doc.createElement("vector");
					VectorAttr2(vec1, "position", rbc.getWorldPos().x, rbc.getWorldPos().y);
					if (rbc.getBody().getShape() instanceof ShapeBox) {
						Element vec2 = doc.createElement("vector");
						VectorAttr2(vec2, "extent", ((ShapeBox) rbc.getBody().getShape()).getExtent().x,
								((ShapeBox) rbc.getBody().getShape()).getExtent().y);// TODO LA
						rigid.appendChild(vec2);
					} else if (rbc.getBody().getShape() instanceof ShapeDisk) {
						rigid.setAttribute("radius", ((ShapeDisk) rbc.getBody().getShape()).getRadius() + "");
					}
					rigid.setAttribute("rotation", rbc.getWorldRot() + "");
					rigid.appendChild(vec1);

					ADDelemnts(rigid, entry.getValue());
				}

				/*
				 * <mesh src="data/meshes/item_pickup/weapon_pickup.obj"> <vector use="position"
				 * x="0" y="1" z="0" /> <vector use="rotation" x="0" y="0" z="0" w="1" />
				 * <vector use="scale" x=".5" y=".5" z=".5" /> </mesh>
				 */
				if (entry.getValue() instanceof Mesh) {

					Mesh meu = ((Mesh) entry.getValue());

					if (meu.getModelPath() == null)
						continue;
					Element mesh12 = doc.createElement("mesh");
					entities.appendChild(mesh12);
					mesh12.setAttribute("src", meu.getModelPath());
					Element vector12 = doc.createElement("vector");
					Element vector22 = doc.createElement("vector");
					Element vector32 = doc.createElement("vector");
					Vec3f vm = ((Mesh) entry.getValue()).localPosition;
					Vec3f vms = ((Mesh) entry.getValue()).scale;
					Vec3f qm = meu.localRotation.toEuler();
					VectorAttr3(vector12, "position", vm.x, vm.y, vm.z);
					VectorAttr3(vector22, "rotation", qm.x, qm.y, qm.z);
					VectorAttr3(vector32, "scale", vms.x, vms.y, vms.z);

					mesh12.appendChild(vector12);
					mesh12.appendChild(vector22);
					mesh12.appendChild(vector32);

					ADDelemnts(mesh12, meu);
				}

				/* Light */
				// directionalLight|pointLight
				/*
				 * <!ELEMENT directionalLight (vector, vector)> <!ATTLIST directionalLight name
				 * CDATA #IMPLIED> <!ELEMENT pointLight (vector, vector)> <!ATTLIST pointLight
				 * name CDATA #IMPLIED> <!ATTLIST pointLight radius CDATA #REQUIRED>
				 */

				if (entry.getValue() instanceof LightContainer) {

					Element vec1 = doc.createElement("vector");
					Element vec2 = doc.createElement("vector");
					Element Light = null;
					LightContainer lc = ((LightContainer) entry.getValue());
					VectorAttr3(vec1, "color", lc.getLight().color.x, lc.getLight().color.y, lc.getLight().color.z);
					if (lc.getLight().getType() == LightType.DIRECTIONAL) {
						Light = doc.createElement("directionalLight");
						entities.appendChild(Light);
						VectorAttr3(vec2, "rotation", lc.localRotation.toEuler().x, lc.localRotation.toEuler().y,
								lc.localRotation.toEuler().z);
						/*
						 * <vector use="color" x="1" y="1" z="0.99" /> <vector use="rotation" x="-.2"
						 * y="-.8" z="0" />
						 */
					} else if (((LightContainer) entry.getValue()).getLight().getType() == LightType.POINT) {
						Light = doc.createElement("pointLight");
						entities.appendChild(Light);
						Light.setAttribute("radius", lc.getLight().radius + "");
						VectorAttr3(vec2, "position", lc.localPosition.x, lc.localPosition.y, lc.localPosition.z);
					} /*
						 * <pointLight radius="10"> <vector use="color" x="1" y="1" z=".5" /> <vector
						 * use="position" x="0" y="2" z="0" /> </pointLight>
						 */
					Light.appendChild(vec1);
					Light.appendChild(vec2);
					ADDelemnts(Light, entry.getValue());
				}
				/*
				 * <!ELEMENT text (vector, vector, entities?)> <!ATTLIST text name CDATA
				 * #IMPLIED> <!ATTLIST text content CDATA #REQUIRED> <!ATTLIST text font CDATA
				 * #REQUIRED>
				 */
				if (entry.getValue() instanceof TextSpatial) {
					Element Text = doc.createElement("text");
					TextSpatial txtspe = (TextSpatial) entry.getValue();
					Text.setAttribute("content", txtspe.getText() + "");
					Text.setAttribute("font", txtspe.getText().getFont() + "");
					ADDelemnts(Text, entry.getValue());
				}

				/*
				 * <static name="..."> <vector x="0" y="2.25" use="position" /> <vector x="50"
				 * y="0.25" use="extent" />...
				 */
				if (entry.getValue() instanceof StaticBodyContainer) {
					Element stat = doc.createElement("static");
					entities.appendChild(stat);
					Element vec1 = doc.createElement("vector");
					StaticBodyContainer staticbc = ((StaticBodyContainer) entry.getValue());
					VectorAttr2(vec1, "position", staticbc.getWorldPos().x, staticbc.getWorldPos().y);
					stat.appendChild(vec1);
					if (staticbc.getBody().getShape() instanceof ShapeBox) {
						Element vec2 = doc.createElement("vector");
						VectorAttr2(vec2, "extent", ((ShapeBox) staticbc.getBody().getShape()).getExtent().x,
								((ShapeBox) staticbc.getBody().getShape()).getExtent().y);// TODO LA
						stat.appendChild(vec2);
					} else if (staticbc.getBody().getShape() instanceof ShapeDisk) {
						stat.setAttribute("radius", ((ShapeDisk) staticbc.getBody().getShape()).getRadius() + "");
					}
					stat.setAttribute("rotation", staticbc.getWorldRot() + "");
					ADDelemnts(stat, entry.getValue());
				}

				/*
				 * <kinematic animation="data/animations/MapXML/MapXmlPlatformMov2.xml"> <vector
				 * x="-10.0" y="-5.0" use="position" /> <vector x="0.125" y="1.0" use="extent"
				 * /> <entities> <mesh src="data/meshes/catwalk/catwalk_10.obj"> <vector
				 * use="position" x="0.0" y="0.0" z="0" /> <vector use="rotation" x="0.0" y="0"
				 * z="1.57" /> <vector use="scale" x="0.25" y="0.25" z="1" /> </mesh>
				 * </entities> </kinematic>
				 */
				if (entry.getValue() instanceof KinematicBodyContainer) {
					Element KinematicBodyContainer = doc.createElement("Kinematic");
					entities.appendChild(KinematicBodyContainer);
					Element vec1 = doc.createElement("vector");

					KinematicBodyContainer statikbc = ((KinematicBodyContainer) entry.getValue());

					VectorAttr2(vec1, "position", statikbc.localPosition.x, statikbc.localPosition.y);

					if (statikbc.getBody().getShape() instanceof ShapeBox) {
						Element vec2 = doc.createElement("vector");
						VectorAttr2(vec2, "extent", ((ShapeBox) statikbc.getBody().getShape()).getExtent().x,
								((ShapeBox) statikbc.getBody().getShape()).getExtent().y);

						KinematicBodyContainer.appendChild(vec2);
					} else if (statikbc.getBody().getShape() instanceof ShapeDisk) {
						KinematicBodyContainer.setAttribute("radius",
								((ShapeDisk) statikbc.getBody().getShape()).getRadius() + "");
					}

					KinematicBodyContainer.appendChild(vec1);

					ADDelemnts(KinematicBodyContainer, entry.getValue());
				}

			}

			/* <gravity> <vector x="0" y="9.807" /> </gravity> */
			Element gravity = doc.createElement("gravity");
			info.appendChild(gravity);
			gravity.setAttribute("x", "" + arena.gravity.x);
			gravity.setAttribute("y", "" + arena.gravity.y);

			// <ambientLight r="0.48" g="0.48" b="0.5" />
			Element ambientLight = doc.createElement("ambientLight");
			info.appendChild(ambientLight);
			ambientLight.setAttribute("r", "" + arena.ambientLight.x);
			ambientLight.setAttribute("g", "" + arena.ambientLight.y);
			ambientLight.setAttribute("b", "" + arena.ambientLight.z);

			// <cameraPos x="0" y="-15" z="100" />
			Element camera = doc.createElement("cameraPos");
			info.appendChild(camera);
			camera.setAttribute("x", "" + arena.cameraBasePos.x);
			camera.setAttribute("y", "" + arena.cameraBasePos.y);
			camera.setAttribute("z", "" + arena.cameraBasePos.z);

			/* <killBounds minX="-200" minY="-100" maxX="200" maxY="20" /> */
			Element killbounds = doc.createElement("killBounds");
			info.appendChild(killbounds);
			killbounds.setAttribute("minX", "" + arena.killBound.x);
			killbounds.setAttribute("minY", "" + arena.killBound.y);
			killbounds.setAttribute("maxX", "" + arena.killBound.z);
			killbounds.setAttribute("maxY", "" + arena.killBound.w);

			/*
			 * <sky> <vector x="0.659" y="0.835" z="0.996" use="bottom" /> <vector x="0.016"
			 * y="0.145" z="0.565" use="top" /> </sky>
			 **/
			Element sky = doc.createElement("sky");
			info.appendChild(sky);
			int skyint = 0;
			for (Entry<String, Entity> entry : arena.getChildren().entrySet()) {
				if (entry.getValue() instanceof Sky) {
					if (skyint == 0) {
						Sky s = (Sky) entry.getValue();
						Element vec1 = doc.createElement("vector");
						Element vec2 = doc.createElement("vector");
						VectorAttr3(vec1, "bottom", s.material.getParamVec3f("colorBot").x,
								s.material.getParamVec3f("colorBot").y, s.material.getParamVec3f("colorBot").z);
						VectorAttr3(vec2, "top", s.material.getParamVec3f("colorTop").x,
								s.material.getParamVec3f("colorTop").y, s.material.getParamVec3f("colorTop").z);
						sky.appendChild(vec1);
						sky.appendChild(vec2);
					}
					skyint++;
				}
			}

			DOMSource source = new DOMSource(doc);
			/* _verify the existance of the file and create it if it doesn't exist_ */
			File file1 = new File("data/mapXML");
			if (!file1.exists()) {
				file1.mkdirs();
			}

			File file = new File("data/mapXML/" + name + ".xml");

			/* DTD */
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("doctype", "", "mapDTD.dtd");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			/* Save */
			StreamResult resultat = new StreamResult(file);
			transformer.transform(source, resultat);
			System.out.println("Fichier sauvegardé avec succès!");

		} catch (

		ParserConfigurationException pce) {
			pce.printStackTrace();
			System.out.println(pce);
		} catch (TransformerException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	private static void VectorAttr3(Element vector, String s, double x, double y, double z) {
		vector.setAttribute("use", s);
		vector.setAttribute("x", "" + x);
		vector.setAttribute("y", "" + y);
		vector.setAttribute("z", "" + z);

	}

	private static void VectorAttr2(Element vector, String s, double x, double y) {
		vector.setAttribute("use", s);
		vector.setAttribute("x", "" + x);
		vector.setAttribute("y", "" + y);
	}

	// <!ELEMENT entities
	// (entity|spawn|rigid|mesh|directionalLight|pointLight|text|static|kinematic|jointPin)*>
	private static void ADDelemnts(Element elem, Entity entity) {
		if (entity.getChildren() != null) {
			for (Entry<String, Entity> entry : entity.getChildren().entrySet()) {
				if (entry.getValue() instanceof Mesh) {
					Element vec1 = doc.createElement("vector");
					Element vec2 = doc.createElement("vector");

					Element mesh12 = doc.createElement("mesh");
					elem.appendChild(mesh12);
					Mesh meu = ((Mesh) entry.getValue());
					if (((Mesh) meu).getModelPath() == null)
						continue;
					mesh12.setAttribute("src", meu.getModelPath());
					Element vector12 = doc.createElement("vector");
					Element vector22 = doc.createElement("vector");
					Element vector32 = doc.createElement("vector");
					Vec3f vm = ((Mesh) entry.getValue()).localPosition;
					Vec3f vms = ((Mesh) entry.getValue()).scale;
					Vec3f qm = meu.localRotation.toEuler();
					VectorAttr3(vector12, "position", vm.x, vm.y, vm.z);
					VectorAttr3(vector22, "rotation", qm.x, qm.y, qm.z);
					VectorAttr3(vector32, "scale", vms.x, vms.y, vms.z);

					mesh12.appendChild(vector12);
					mesh12.appendChild(vector22);
					mesh12.appendChild(vector32);

					VectorAttr2(vec1, "position", 0, 2.25);
					VectorAttr2(vec2, "extent", 5, 0.25);
					ADDelemnts(mesh12, meu);
				}
				if (entry.getValue() instanceof StaticBodyContainer) {
					Element stat = doc.createElement("static");
					elem.appendChild(stat);
					Element vec1 = doc.createElement("vector");
					Element vec2 = doc.createElement("vector");
					stat.appendChild(vec1);
					stat.appendChild(vec2);
					Element entities34 = doc.createElement("entities");
					stat.appendChild(entities34);

					for (Entry<String, Entity> mesh : entry.getValue().getChildren().entrySet()) {
						if (mesh.getValue() instanceof Mesh) {
							if (((Mesh) mesh.getValue()).getModelPath() == null)
								continue;
							Element mesh12 = doc.createElement("mesh");
							entities34.appendChild(mesh12);
							Mesh meu = ((Mesh) mesh.getValue());
							mesh12.setAttribute("src", meu.getModelPath());
							Element vector12 = doc.createElement("vector");
							Element vector22 = doc.createElement("vector");
							Element vector32 = doc.createElement("vector");
							Vec3f vm = ((Mesh) meu).localPosition;
							Vec3f vms = ((Mesh) meu).scale;
							Vec3f qm = meu.localRotation.toEuler();
							VectorAttr3(vector12, "position", vm.x, vm.y, vm.z);
							VectorAttr3(vector22, "rotation", qm.x, qm.y, qm.z);
							VectorAttr3(vector32, "scale", vms.x, vms.y, vms.z);

							mesh12.appendChild(vector12);
							mesh12.appendChild(vector22);
							mesh12.appendChild(vector32);

							VectorAttr2(vec1, "position", 0, 2.25);
							VectorAttr2(vec2, "extent", 5, 0.25);
							ADDelemnts(mesh12, meu);
						}
						ADDelemnts(stat, entry.getValue());
					}
				}
				if (entry.getValue() instanceof RigidBodyContainer) {
					Element rigid = doc.createElement("rigid");
					elem.appendChild(rigid);
					Element vec1 = doc.createElement("vector");
					Element vec2 = doc.createElement("vector");
					rigid.appendChild(vec1);
					rigid.appendChild(vec2);
					Element entities34 = doc.createElement("entities");
					rigid.appendChild(entities34);

					for (Entry<String, Entity> mesh : entry.getValue().getChildren().entrySet()) {
						if (mesh.getValue() instanceof Mesh) {
//					MeshXml s = new MeshXml(doc, entities);
							if (((Mesh) mesh.getValue()).getModelPath() == null)
								continue;
							Element mesh12 = doc.createElement("mesh");
							entities34.appendChild(mesh12);
							Mesh meu = ((Mesh) mesh.getValue());
							mesh12.setAttribute("src", meu.getModelPath());
							Element vector12 = doc.createElement("vector");
							Element vector22 = doc.createElement("vector");
							Element vector32 = doc.createElement("vector");
							Vec3f vm = ((Mesh) meu).localPosition;
							Vec3f vms = ((Mesh) meu).scale;
							Vec3f qm = meu.localRotation.toEuler();
							VectorAttr3(vector12, "position", vm.x, vm.y, vm.z);
							VectorAttr3(vector22, "rotation", qm.x, qm.y, qm.z);
							VectorAttr3(vector32, "scale", vms.x, vms.y, vms.z);

							mesh12.appendChild(vector12);
							mesh12.appendChild(vector22);
							mesh12.appendChild(vector32);

							VectorAttr2(vec1, "position", 0, 2.25);
							VectorAttr2(vec2, "extent", 5, 0.25);
							ADDelemnts(mesh12, meu);
						}
						ADDelemnts(rigid, entry.getValue());
					}
				}

			}
		}

	}

//	public static void main(String argv[]) {
//		try {
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//			Document doc = docBuilder.newDocument();
//			System.out.print("Nom map : ");
//			Scanner sc = new Scanner(System.in);
//			String line = sc.nextLine();
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			DOMSource source = new DOMSource(doc);
//			File file = new File("data/testMap");
//			if (!file.exists()) {
//				file.mkdirs();
//			}
//			StreamResult result = new StreamResult(new File("data/testMap/" + line + ".xml"));
//
//			// Creation of the test arena
//			Arena arena = new Arena();
//			Mesh mesh = new Mesh(new Vec3f(), "data/meshes/item_pickup/weapon_pickup.obj");
//			mesh.attachToParent(arena, "meshTest");
//
//			transformer.transform(source, result);
//			System.out.println("File saved!");
//		} catch (ParserConfigurationException pce) {
//			pce.printStackTrace();
//		} catch (TransformerException tfe) {
//			tfe.printStackTrace();
//		}
//	}
}