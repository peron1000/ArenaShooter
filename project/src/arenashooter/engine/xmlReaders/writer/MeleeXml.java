package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.entities.spatials.items.Melee;

public class MeleeXml extends AbstractElement {
	private String handl = "handPosL", handr = "handPosR";
	private Element shot;

	public MeleeXml(Document doc, Element elementParent, Melee melee) {
		super(doc, elementParent);
		shot = doc.createElement("melee");
		elementParent.appendChild(shot);

		Attr name = doc.createAttribute("name");
		name.setValue(melee.name + "");
		shot.setAttributeNode(name);

		Attr weight = doc.createAttribute("weight");
		weight.setValue(melee.getWeight() + "");
		shot.setAttributeNode(weight);

		Attr pathSprite = doc.createAttribute("pathSprite");
		pathSprite.setValue(melee.getPathSprite() + "");
		shot.setAttributeNode(pathSprite);

		Attr soundPickup = doc.createAttribute("soundPickup");
		soundPickup.setValue(melee.soundPickup + "");
		shot.setAttributeNode(soundPickup);

		Attr cooldown = doc.createAttribute("cooldown");
		cooldown.setValue(melee.getFireRate() + "");
		shot.setAttributeNode(cooldown);

		Attr uses = doc.createAttribute("uses");
		uses.setValue(melee.getUses() + "");
		shot.setAttributeNode(uses);

		Attr animPath = doc.createAttribute("animPath");
		animPath.setValue(melee.getAnimPath() + "");
		shot.setAttributeNode(animPath);

		Attr warmup = doc.createAttribute("warmupDuration");
		warmup.setValue(melee.getWarmup() + "");
		shot.setAttributeNode(warmup);

		Attr soundWarmup = doc.createAttribute("soundWarmup");
		soundWarmup.setValue(melee.getSoundWarmup() + "");
		shot.setAttributeNode(soundWarmup);

		Attr attackSound = doc.createAttribute("bangSound");
		attackSound.setValue(melee.getSoundFire() + "");
		shot.setAttributeNode(attackSound);

		Attr damage = doc.createAttribute("damage");
		damage.setValue(melee.getDamage() + "");
		shot.setAttributeNode(damage);

		Attr size = doc.createAttribute("size");
		size.setValue(melee.getSize() + "");
		shot.setAttributeNode(size);
	

		VectorXml vectorshotgun = new VectorXml(doc, shot);
		if (melee.handPosL != null)
			vectorshotgun.addVector(handl, melee.handPosL.x, melee.handPosL.y);
		if (melee.handPosR != null)
			vectorshotgun.addVector(handr, melee.handPosR.x, melee.handPosR.y);
		for (Element e : vectorshotgun.getVectors()) {
			shot.appendChild(e);
		}
	}

	/**
	 * @param handl the handl to set
	 */
	public void setHandl(String handl) {
		this.handl = handl;
	}

	/**
	 * @param handr the handr to set
	 */
	public void setHandr(String handr) {
		this.handr = handr;
	}

	/**
	 * @param gun the gun to set
	 */
	public void setshootgun(Element shot) {
		this.shot = shot;
	}

	@Override
	public Element getElement() {
		// TODO Auto-generated method stub
		return null;
	}
}
