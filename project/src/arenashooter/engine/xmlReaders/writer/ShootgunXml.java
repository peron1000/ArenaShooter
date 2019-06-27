package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.entities.spatials.items.Shotgun;

public class ShootgunXml extends AbstractElement {
	private String handl = "handPosL", handr = "handPosR";
	private Element shot;

	public ShootgunXml(Document doc, Element elementParent, Shotgun shotgun) {
		super(doc, elementParent);
		shot = doc.createElement("shootgun");
		elementParent.appendChild(shot);

		Attr name = doc.createAttribute("name");
		name.setValue(shotgun.name + "");
		shot.setAttributeNode(name);

		Attr weight = doc.createAttribute("weight");
		weight.setValue(shotgun.getWeight() + "");
		shot.setAttributeNode(weight);

		Attr pathSprite = doc.createAttribute("pathSprite");
		pathSprite.setValue(shotgun.getPathSprite() + "");
		shot.setAttributeNode(pathSprite);

		Attr soundPickup = doc.createAttribute("soundPickup");
		soundPickup.setValue(shotgun.soundPickup + "");
		shot.setAttributeNode(soundPickup);

		Attr cooldown = doc.createAttribute("cooldown");
		cooldown.setValue(shotgun.getFireRate() + "");
		shot.setAttributeNode(cooldown);

		Attr uses = doc.createAttribute("uses");
		uses.setValue(shotgun.getUses() + "");
		shot.setAttributeNode(uses);

		Attr animPath = doc.createAttribute("animPath");
		animPath.setValue(shotgun.getAnimPath() + "");
		shot.setAttributeNode(animPath);

		Attr warmup = doc.createAttribute("warmupDuration");
		warmup.setValue(shotgun.getWarmupDuration() + "");
		shot.setAttributeNode(warmup);

		Attr soundWarmup = doc.createAttribute("soundWarmup");
		soundWarmup.setValue(shotgun.getSoundWarmup() + "");
		shot.setAttributeNode(soundWarmup);

		Attr attackSound = doc.createAttribute("bangSound");
		attackSound.setValue(shotgun.getSoundFire() + "");
		shot.setAttributeNode(attackSound);
		Attr noAmmoSound = doc.createAttribute("noAmmoSound");
		noAmmoSound.setValue(shotgun.getSoundNoAmmo() + "");
		shot.setAttributeNode(noAmmoSound);

		Attr damage = doc.createAttribute("damage");
		damage.setValue(shotgun.getDamage() + "");
		shot.setAttributeNode(damage);

//		Attr size = doc.createAttribute("size");
//		size.setValue(shotgun.getExtent() + "");
//		shot.setAttributeNode(size);

		Attr bulletType = doc.createAttribute("bulletType");
		bulletType.setValue(shotgun.getBulletType() + "");
		shot.setAttributeNode(bulletType);

		Attr bulletSpeed = doc.createAttribute("bulletSpeed");
		bulletSpeed.setValue(shotgun.getBulletSpeed() + "");
		shot.setAttributeNode(bulletSpeed);

		Attr cannonLength = doc.createAttribute("cannonLength");
		cannonLength.setValue(shotgun.getCannonLength() + "");
		shot.setAttributeNode(cannonLength);

		Attr recoil = doc.createAttribute("recoil");
		recoil.setValue(shotgun.getRecoil() + "");
		shot.setAttributeNode(recoil);

		Attr thrust = doc.createAttribute("thrust");
		thrust.setValue(shotgun.getThrust() + "");
		shot.setAttributeNode(thrust);
		
		Attr multiShot = doc.createAttribute("multiShot");
		multiShot.setValue(shotgun.getMultiShot() + "");
		shot.setAttributeNode(thrust);
		
		Attr dispersion = doc.createAttribute("dispersion");
		dispersion.setValue(shotgun.getDispersion() + "");
		shot.setAttributeNode(thrust);
		
		VectorXml vectorshotgun = new VectorXml(doc, shot);
		if (shotgun.handPosL != null)
			vectorshotgun.addVector(handl, shotgun.handPosL.x, shotgun.handPosL.y);
		if (shotgun.handPosR != null)
			vectorshotgun.addVector(handr, shotgun.handPosR.x, shotgun.handPosR.y);
		
		vectorshotgun.addVector("extent", shotgun.extent.x, shotgun.extent.y);
		
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
