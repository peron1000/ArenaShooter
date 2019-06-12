package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.entities.spatials.items.Melee;
import arenashooter.entities.spatials.items.Shotgun;

public class MeleeXml extends AbstractElement {
//	private String name = "";
//	private int weight = 0;
//	private String pathSprite = "";
//	private String soundPickup = "";
//	private double cooldown = 0;
//	private int uses = 0;
//	private String animPath = "";
//	private double warmup = 0;
//	private String soundWarmup = "";
//	private String attackSound = "";
//	private float damage = 0f;
//	private double size = 0;

//	private float xpos1 = -.8f;
//	private float ypos1 = -.1f;
//	private float xpos2 = 0f;
//	private float ypos2 = -0.28f;
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
	

		VecteurXml vecteurshotgun = new VecteurXml(doc, shot);
		if (melee.handPosL != null)
			vecteurshotgun.addVecteur(handl, melee.handPosL.x, melee.handPosL.y);
		if (melee.handPosR != null)
			vecteurshotgun.addVecteur(handr, melee.handPosR.x, melee.handPosR.y);
		for (Element e : vecteurshotgun.getVecteurs()) {
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
