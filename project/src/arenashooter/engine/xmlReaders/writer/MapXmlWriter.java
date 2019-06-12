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

import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.StaticBodyContainer;
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

//			int etapes = 0;
//			for (Entry<String, Entity> entry : arena.getChildren().entrySet()) {
//				System.out.println("cp " + entry.getValue());
//			
			// for (Item it : arena.items) {

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
//			<vecteur x="7" y="1" use="position" />
//			<itemRef item="kata" proba="1" />
//			<entities>
//				<mesh src="data/meshes/item_pickup/weapon_pickup.obj">
//					<vecteur use="position" x="0" y="1" z="0" />
//					<vecteur use="rotation" x="0" y="0" z="0" w="1" />
//					<vecteur use="scale" x=".5" y=".5" z=".5" />
//				</mesh>
//			</entities>
//		</spawn>
			// for (Vec2f ps : arena.playerSpawns) {

			for (Entry<String, Entity> entry : arena.getChildren().entrySet()) {
//				<spawn playerSpawn="false" cooldown="2"> 
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
//					<vecteur x="7" y="1" use="position" />
					Element vecteur = doc.createElement("vecteur");
					VecteurAttr2(vecteur, "position", ((Spawner) entry.getValue()).localPosition.x,
							((Spawner) entry.getValue()).localPosition.y);
					spawn.appendChild(vecteur);

//					<itemRef item="minigun" proba="1" />
					for (Entry<String, Integer> itemavaible : ((Spawner) entry.getValue()).getAvailableItems()
							.entrySet()) {
						Element itemRef = doc.createElement("itemRef");
						itemRef.setAttribute("item", itemavaible.getKey() + "");
						itemRef.setAttribute("proba", itemavaible.getValue() + "");
						spawn.appendChild(itemRef);
					}
					// <entities>
//				<mesh src="data/meshes/item_pickup/weapon_pickup.obj">
//				<vecteur use="position" x="0" y="1" z="0" />
//				<vecteur use="rotation" x="0" y="0" z="0" w="1" />
//				<vecteur use="scale" x=".5" y=".5" z=".5" />
//			</mesh>
//		</entities>
					for (Entry<String, Entity> mesh : ((Spawner) entry.getValue()).getChildren().entrySet()) {
						if (mesh instanceof Mesh) {
							Element mesheux = doc.createElement("mesh");
							mesheux.setAttribute("src", ((Mesh) mesh.getValue()).getModelPath() + "");
							spawn.appendChild(mesheux);
							VecteurAttr2(vecteur, "position", ((Mesh) entry.getValue()).localPosition.x,
									((Mesh) entry.getValue()).localPosition.y);
							spawn.appendChild(vecteur);
							VecteurAttr2(vecteur, "rotation", ((Mesh) entry.getValue()).localRotation.toEuler().x,
									((Mesh) entry.getValue()).localRotation.toEuler().y);
							spawn.appendChild(vecteur);
							VecteurAttr2(vecteur, "scale", ((Mesh) entry.getValue()).scale.x,
									((Mesh) entry.getValue()).scale.y);
							spawn.appendChild(vecteur);
						}

					}

				}

			}

//				for(Entry<String, Item> sp : ) {
//					
//				}

//				if (entry.getValue() instanceof Spawner) {
//					Spawner spp = (Spawner) entry.getValue();
//					etapes = 1;
//					Element spawn = doc.createElement("spawn");
//					info.appendChild(spawn);
//					spawn.setAttribute("cooldown", "" + spp.getCooldown());
//					VecteurXml vecteurGun = new VecteurXml(doc, spawn);
//					vecteurGun.addVecteur("position", spp.localPosition.x, spp.localPosition.y);
//
////					for (Element e : vecteurGun.getVecteurs()) {
////						new GunXml(doc, spawn);
////					}

			/**/
//			if (bm) {
//				Element entities1 = doc.createElement("entities");
//				spawn.appendChild(entities1);
//				Element mesh1 = doc.createElement("mesh");
//				entities1.appendChild(mesh1);
//				mesh1.setAttribute("src", "data/meshes/item_pickup/weapon_pickup_ceiling.obj");
//				// <vecteur use="position" x="0" y="-1" z="0" />
//				// <vecteur use="rotation" x="0" y="0" z="0" />
//				// <vecteur use="scale" x=".75" y=".75" z=".75" />
//				Element vecteur1 = doc.createElement("vecteur");
//				Element vecteur2 = doc.createElement("vecteur");
//				Element vecteur3 = doc.createElement("vecteur");
//
//				VecteurAttr3Mesh(vecteur1, "position", 0, -1, 0);
//				VecteurAttr3Mesh(vecteur2, "rotation", 0, 0, 0);
//				VecteurAttr3Mesh(vecteur3, "scale", 0.75, 0.75, 0.75);
//
//				mesh1.appendChild(vecteur1);
//				mesh1.appendChild(vecteur2);
//				mesh1.appendChild(vecteur3);
//			}
//					if (!vecteurGun.getElement().getElementsByTagName("gun").equals(null)) {
//						for (int j = 0; j < vecteurGun.getElement().getElementsByTagName("gun").getLength(); j++) {
//							new GunXml(doc, spawn);
//						}
//					}

//					for(Element e :) {
//						
//					}

			// }
			// }
//			if (etapes == 0) {
//				Element spawn = doc.createElement("spawn");
//				info.appendChild(spawn);
//				spawn.setAttribute("cooldown", "2");
//				Element vecteur = doc.createElement("vecteur");
//				spawn.appendChild(vecteur);
//				vecteur.setAttribute("x", "" + 0.0);
//				vecteur.setAttribute("y", "" + 0.0);
//			}

			/* <gravity> <vecteur x="0" y="9.807" /> </gravity> */
			Element gravity = doc.createElement("gravity");
			info.appendChild(gravity);
			Element vecteur = doc.createElement("vecteur");
			gravity.appendChild(vecteur);
			vecteur.setAttribute("x", "" + arena.gravity.x);
			vecteur.setAttribute("y", "" + arena.gravity.y);

//			for (Entry<String, Entity> entry : arena.getChildren().entrySet()) {
//				System.out.println("cp " + entry.getValue());
//				/* camera */
//				if (entry.getValue() instanceof Camera) {
//					Camera c = (Camera) entry.getValue();
			Element camera = doc.createElement("cameraPos");
			info.appendChild(camera);
//					camera.setAttribute("x", "" + c.getWorldPos().x);
//					camera.setAttribute("y", "" + c.getWorldPos().y);
//					camera.setAttribute("z", "" + c.getWorldPos().z);

			camera.setAttribute("x", "" + arena.cameraBasePos.x);
			camera.setAttribute("y", "" + arena.cameraBasePos.y);
			camera.setAttribute("z", "" + arena.cameraBasePos.z);

//				}
//
//			}

			/* <killBounds minX="-200" minY="-100" maxX="200" maxY="20" /> */
			Element killbounds = doc.createElement("killBounds");
			info.appendChild(killbounds);
			killbounds.setAttribute("minX", "" + arena.killBound.x);
			killbounds.setAttribute("minY", "" + arena.killBound.y);
			killbounds.setAttribute("maxX", "" + arena.killBound.z);
			killbounds.setAttribute("maxY", "" + arena.killBound.w);

			for (Entry<String, Entity> entry : arena.getChildren().entrySet()) {
				System.out.println("cp " + entry.getValue());
				if (entry.getValue() instanceof Sky) {
					Sky s = (Sky) entry.getValue();
					Element sky = doc.createElement("sky");
					info.appendChild(sky);
					VecteurXml vecs = new VecteurXml(doc, sky);
					vecs.addVecteur("bottom", s.material.getParamVec3f("colorBot").x,
							s.material.getParamVec3f("colorBot").y, s.material.getParamVec3f("colorBot").z);
					vecs.addVecteur("top", s.material.getParamVec3f("colorTop").x,
							s.material.getParamVec3f("colorTop").y, s.material.getParamVec3f("colorTop").z);
				}
			}

			/*
			 * <static name="large_platform"> <vecteur x="0" y="2.25" use="position" />
			 * <vecteur x="50" y="0.25" use="extent" />
			 */
			for (Entry<String, Entity> entry : arena.getChildren().entrySet()) {
				System.out.println("cp " + entry.getValue());

				if (entry.getValue() instanceof StaticBodyContainer) {
					Element stat = doc.createElement("static");
					entities.appendChild(stat);
					Element vec1 = doc.createElement("vecteur");
					Element vec2 = doc.createElement("vecteur");
					stat.appendChild(vec1);
					stat.appendChild(vec2);
					Element entities34 = doc.createElement("entities");
					stat.appendChild(entities34);

					for (Entry<String, Entity> mesh : entry.getValue().getChildren().entrySet()) {
						if (mesh instanceof Mesh) {
//					MeshXml s = new MeshXml(doc, entities);

							Element mesh12 = doc.createElement("mesh");
							entities34.appendChild(mesh12);
							Mesh meu = ((Mesh) mesh);
							mesh12.setAttribute("src", meu.getModelPath());
							Element vecteur12 = doc.createElement("vecteur");
							Element vecteur22 = doc.createElement("vecteur");
							Element vecteur32 = doc.createElement("vecteur");
							Vec3f vm = ((Mesh) mesh).localPosition;
							Vec3f vms = ((Mesh) mesh).scale;
							Vec3f qm = meu.localRotation.toEuler();
							VecteurAttr3(vecteur12, "position", vm.x, vm.y, vm.z);
							VecteurAttr3(vecteur22, "rotation", qm.x, qm.y, qm.z);
							VecteurAttr3(vecteur32, "scale", vms.x, vms.y, vms.z);

							mesh12.appendChild(vecteur12);
							mesh12.appendChild(vecteur22);
							mesh12.appendChild(vecteur32);

							VecteurAttr2(vec1, "position", 0, 2.25);
							VecteurAttr2(vec2, "extent", 5, 0.25);

						}
					}
					if (entry.getValue() instanceof Mesh) {
//						MeshXml s = new MeshXml(doc, entities);

						Element mesh12 = doc.createElement("mesh");
						entities34.appendChild(mesh12);
						Mesh meu = ((Mesh) entry.getValue());
						mesh12.setAttribute("src", meu.getModelPath());
						Element vecteur12 = doc.createElement("vecteur");
						Element vecteur22 = doc.createElement("vecteur");
						Element vecteur32 = doc.createElement("vecteur");
						Vec3f vm = ((Mesh) entry.getValue()).localPosition;
						Vec3f vms = ((Mesh) entry.getValue()).scale;
						Vec3f qm = meu.localRotation.toEuler();
						VecteurAttr3(vecteur12, "position", vm.x, vm.y, vm.z);
						VecteurAttr3(vecteur22, "rotation", qm.x, qm.y, qm.z);
						VecteurAttr3(vecteur32, "scale", vms.x, vms.y, vms.z);

						mesh12.appendChild(vecteur12);
						mesh12.appendChild(vecteur22);
						mesh12.appendChild(vecteur32);

						VecteurAttr2(vec1, "position", 0, 2.25);
						VecteurAttr2(vec2, "extent", 5, 0.25);

					}
				}
			}

			// if (entry.getValue() instanceof Mesh) {
//				Mesh p = (Mesh) entry.getValue();
//				// NodeList bb = doc.getElementsByTagName("entities");
//				Element bbb = (Element) doc.getElementsByTagName("map").item(0).getLastChild();
//				// getDocumentElement().getChildNodes().item(1).getNodeValue();
//				System.out.println(bbb + "      " + bbb);
//				if (bbb != null) {
//					MeshXml p1 = new MeshXml(doc, bbb);
//					p1.xPosition = p.localPosition.x;
//					p1.yPosition = p.localPosition.y;
//					p1.zPosition = p.localPosition.z;
//				}
//			}

			// loadChildren(doc,arena, map);
			// loadChildren2(doc, arena, map);

			DOMSource source = new DOMSource(doc);
			/* _ */
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

			// Accesseurs :

		} catch (

		ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("aze_aze_aze" + e);
		}
	}

//	private static void loadChildren2(Document doc2, Arena arena, Element map) {
//		// TODO Auto-generated method stub
////		for(Vec2f v : arena.spawn) {
////			Element sp = doc.createElement("spawn");
////			((Node) doc2.getChildNodes()).appendChild(sp);
////		}
//		for (Entry<String, Entity> entry : parent.getChildren().entrySet()) {
//			System.out.println("cp");
//			if (child instanceof Spawner) {
//				System.out.println("lol");
//				Spawner p = (Spawner) child;
//				SpawnXml p1 = new SpawnXml(doc, parent, info, true, p.getCooldown());
//			}
//	}

	private static void loadChildren(Document doc, Entity parent, Element parentBalise) {

//			Element info = doc.createElement("information");
//			parentBalise.appendChild(info);

//			Element entities = doc.createElement("entities");
//			parentBalise.appendChild(entities);

		for (Entry<String, Entity> entry : parent.getChildren().entrySet()) {
			System.out.println("cp " + entry.getValue());
//				if (entry.getValue() instanceof Spawner) {
//					System.out.println("lol");
//					Spawner p = (Spawner) entry.getValue();
//					SpawnXml p1 = new SpawnXml(doc, parent, info, true, p.getCooldown());
//				}
//			if (entry.getValue() instanceof Mesh) {
//				Mesh p = (Mesh) entry.getValue();
//				// NodeList bb = doc.getElementsByTagName("entities");
//				Element bbb = (Element) doc.getElementsByTagName("map").item(0).getLastChild();
//				// getDocumentElement().getChildNodes().item(1).getNodeValue();
//				System.out.println(bbb + "      " + bbb);
//				if (bbb != null) {
//					MeshXml p1 = new MeshXml(doc, bbb);
//					p1.xPosition = p.localPosition.x;
//					p1.yPosition = p.localPosition.y;
//					p1.zPosition = p.localPosition.z;
//				}
//			}

		}

	}

	private static void VecteurAttr3(Element vecteur, String s, double x, double y, double z) {
		vecteur.setAttribute("use", s);
		vecteur.setAttribute("x", "" + x);
		vecteur.setAttribute("y", "" + y);
		vecteur.setAttribute("z", "" + z);

	}

	private static void VecteurAttr2(Element vecteur, String s, double x, double y) {
		vecteur.setAttribute("use", s);
		vecteur.setAttribute("x", "" + x);
		vecteur.setAttribute("y", "" + y);
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