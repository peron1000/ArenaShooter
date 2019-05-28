package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.engine.xmlReaders.XmlVector;

public class SpawnXml extends AbstractElement {

	Element spawn;
	float x = 0f;
	float y = 0f;

	public SpawnXml(Document doc, Element elementParent, boolean spawnperso, double Cooldown) {
		super(doc, elementParent);
		spawn = doc.createElement("spawn");
		elementParent.appendChild(spawn);

		Attr spawnpers = doc.createAttribute("spawnperso");
		spawnpers.setValue(spawnperso + "");
		spawn.setAttributeNode(spawnpers);

		Attr cooldown = doc.createAttribute("cooldown");
		cooldown.setValue(Cooldown + "");
		spawn.setAttributeNode(cooldown);
		
		VecteurXml vecteurGun = new VecteurXml(doc, spawn);
		vecteurGun.addVecteur("", x , y);
		for (Element e : vecteurGun.getVecteurs()) {
			spawn.appendChild(e);
		}
		
		//creation du mesh
	}

	public void setVecteur(float x, float y) {
		XmlVector vec = new XmlVector(spawn);
	}

	public void addGun() {
		GunXml gun = new GunXml(doc, spawn);
	}
	
	public void addPlatform() {
		PlateformXml platform = new PlateformXml(doc, spawn);
	}
	
	public void addMesh() {
		MeshXml r = new MeshXml(doc, spawn);
	}

	@Override
	public Element getElement() {
		return (Element) spawn.cloneNode(true);
	}

}
