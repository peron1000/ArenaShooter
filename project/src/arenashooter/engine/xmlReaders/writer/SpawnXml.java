package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;

public class SpawnXml extends AbstractElement {

	Element spawn;
	float x = 0f;
	float y = 0f;

	public SpawnXml(Document doc, Entity map , Element elementParent, boolean spawnperso, double Cooldown) {
		super(doc, elementParent);
		System.out.println("ll");
		spawn = doc.createElement("spawn");
		elementParent.appendChild(spawn);

		Attr spawnpers = doc.createAttribute("spawnperso");
		spawnpers.setValue(spawnperso + "");
		spawn.setAttributeNode(spawnpers);

		Attr cooldown = doc.createAttribute("cooldown");
		cooldown.setValue(Cooldown + "");
		spawn.setAttributeNode(cooldown);
		
		VectorXml vectorGun = new VectorXml(doc, spawn);
		vectorGun.addVector("", x , y);
		for (Element e : vectorGun.getVectors()) {
			spawn.appendChild(e);
		}
	}
	
	public void setVector(float x, float y) {
		XmlVector vec = new XmlVector(spawn);
	}

	@Override
	public Element getElement() {
		return (Element) spawn.cloneNode(true);
	}

}
