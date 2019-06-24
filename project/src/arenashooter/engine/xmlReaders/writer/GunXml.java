package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.entities.spatials.items.Gun;

public class GunXml extends AbstractElement {
	/*
	 * 
	 * <gun name="Minigun" weight="43" pathSprite="data/weapons/Minigun_1.png"
	 * soundPickup="GunCock_02" cooldown="0.03" uses="350" animPath=""
	 * warmupDuration="0.25" soundWarmup="warmup_minigun_01" bangSound="Bang1"
	 * noAmmoSound="no_ammo_01" bulletType="0" bulletSpeed="35" damage="1"
	 * cannonLength="1.9" recoil="0.2" thrust="2.6" size="64" proba="1">
	 * 
	 * <vector use="handPosL" x="-.8" y="-.1" /> <vector use="handPosR" x="0"
	 * y="-.28" /> </gun>
	 * 
	 */
	private String handl = "handPosL", handr = "handPosR";
	private Element gun;

	public GunXml(Document doc, Element elementParent, Gun g) {
		super(doc, elementParent);
		gun = doc.createElement("gun");
		elementParent.appendChild(gun);

		Attr name = doc.createAttribute("name");
		name.setValue(g.name + "");
		gun.setAttributeNode(name);

		Attr weight = doc.createAttribute("weight");
		weight.setValue(g.getWeight() + "");
		gun.setAttributeNode(weight);

		Attr pathSprite = doc.createAttribute("pathSprite");
		pathSprite.setValue(g.getPathSprite() + "");
		gun.setAttributeNode(pathSprite);

		Attr soundPickup = doc.createAttribute("soundPickup");
		soundPickup.setValue(g.soundPickup + "");
		gun.setAttributeNode(soundPickup);

		Attr cooldown = doc.createAttribute("cooldown");
		cooldown.setValue(g.getFireRate() + "");
		gun.setAttributeNode(cooldown);

		Attr uses = doc.createAttribute("uses");
		uses.setValue(g.getUses() + "");
		gun.setAttributeNode(uses);

		Attr animPath = doc.createAttribute("animPath");
		animPath.setValue(g.getAnimPath() + "");
		gun.setAttributeNode(animPath);

		Attr warmup = doc.createAttribute("warmupDuration");
		warmup.setValue(g.getWarmupDuration() + "");
		gun.setAttributeNode(warmup);

		Attr soundWarmup = doc.createAttribute("soundWarmup");
		soundWarmup.setValue(g.getSoundWarmup() + "");
		gun.setAttributeNode(soundWarmup);

		Attr attackSound = doc.createAttribute("bangSound");
		attackSound.setValue(g.getSoundFire() + "");
		gun.setAttributeNode(attackSound);
		Attr noAmmoSound = doc.createAttribute("noAmmoSound");
		noAmmoSound.setValue(g.getSoundNoAmmo() + "");
		gun.setAttributeNode(noAmmoSound);

		Attr damage = doc.createAttribute("damage");
		damage.setValue(g.getDamage() + "");
		gun.setAttributeNode(damage);

		Attr size = doc.createAttribute("size");
		size.setValue(g.getExtent() + "");
		gun.setAttributeNode(size);

		Attr bulletType = doc.createAttribute("bulletType");
		bulletType.setValue(g.getBulletType() + "");
		gun.setAttributeNode(bulletType);

		Attr bulletSpeed = doc.createAttribute("bulletSpeed");
		bulletSpeed.setValue(g.getBulletSpeed() + "");
		gun.setAttributeNode(bulletSpeed);

		Attr cannonLength = doc.createAttribute("cannonLength");
		cannonLength.setValue(g.getCannonLength() + "");
		gun.setAttributeNode(cannonLength);

		Attr recoil = doc.createAttribute("recoil");
		recoil.setValue(g.getRecoil() + "");
		gun.setAttributeNode(recoil);

		Attr thrust = doc.createAttribute("thrust");
		thrust.setValue(g.getThrust() + "");
		gun.setAttributeNode(thrust);

		VectorXml vectorGun = new VectorXml(doc, gun);
		if (g.handPosL != null)
			vectorGun.addVector(handl, g.handPosL.x, g.handPosL.y);
		if (g.handPosR != null)
			vectorGun.addVector(handr, g.handPosR.x, g.handPosR.y);
		for (Element e : vectorGun.getVectors()) {
			gun.appendChild(e);
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
	public void setGun(Element gun) {
		this.gun = gun;
	}

	@Override
	public Element getElement() {
		// TODO Auto-generated method stub
		return null;
	}
}
