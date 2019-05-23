package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.engine.xmlReaders.XmlVecteur;

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

//		VecteurXml vec = new VecteurXml(doc, spawn);
//		vec.addVecteur("l", x, y);
//		for (Element e : vec.getVecteurs()) {
//			spawn.appendChild(e);
//		}

	}

	public void setVecteur(float x, float y) {
		XmlVecteur vec = new XmlVecteur(spawn);
	}

	public void addGun() {
		GunXml gun = new GunXml(doc, spawn);
	}

	@Override
	public Element getElement() {
		return (Element) spawn.cloneNode(true);
	}

}
