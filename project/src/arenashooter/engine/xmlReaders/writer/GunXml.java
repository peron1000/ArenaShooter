package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GunXml extends AbstractElement {

	String name = "lol";
	int weight = 1;
	String pathSprite = "data/weapons/Minigun_1.png";
	String soundPickup = "GunCock_02";
	double cooldown = 1;
	int uses = 30;
	String animPath = "";
	double warmup = 0.25;
	String soundWarmup = "warmup_minigun_01";
	String attackSound = "Bang1";
	String noAmmoSound = "no_ammo_01";
	float damage = 1;
	double size = 35;
	int bulletType = 0;
	float bulletSpeed = 1.9f;
	double cannonLength = 0.2;
	double recoil = 2.6;
	double thrust = 0.5;
	int proba = 1;

	Element gun;

	public GunXml(Document doc, Element elementParent) {
		super(doc, elementParent);
		gun = doc.createElement("gun");
		elementParent.appendChild(gun);

		Attr name = doc.createAttribute("name");
		name.setValue(this.name + "");
		gun.setAttributeNode(name);

		Attr weight = doc.createAttribute("weight");
		weight.setValue(this.weight + "");
		gun.setAttributeNode(weight);
		
		Attr pathSprite = doc.createAttribute("pathSprite");
		pathSprite.setValue(this.pathSprite + "");
		gun.setAttributeNode(pathSprite);
		
		Attr soundPickup = doc.createAttribute("soundPickup");
		soundPickup.setValue(this.soundPickup + "");
		gun.setAttributeNode(soundPickup);
		
		Attr cooldown = doc.createAttribute("cooldown");
		cooldown.setValue(this.cooldown + "");
		gun.setAttributeNode(cooldown);
		
		Attr uses = doc.createAttribute("uses");
		uses.setValue(this.uses + "");
		gun.setAttributeNode(uses);
		
		Attr animPath = doc.createAttribute("animPath");
		animPath.setValue(this.animPath + "");
		gun.setAttributeNode(animPath);
		
		Attr warmup = doc.createAttribute("warmup");
		warmup.setValue(this.warmup + "");
		gun.setAttributeNode(warmup);
		
		Attr soundWarmup = doc.createAttribute("soundWarmup");
		soundWarmup.setValue(this.soundWarmup + "");
		gun.setAttributeNode(soundWarmup);
		
		Attr attackSound = doc.createAttribute("attackSound");
		attackSound.setValue(this.attackSound + "");
		gun.setAttributeNode(attackSound);
		
		Attr noAmmoSound = doc.createAttribute("noAmmoSound");
		noAmmoSound.setValue(this.noAmmoSound + "");
		gun.setAttributeNode(noAmmoSound);
		
		Attr damage = doc.createAttribute("damage");
		damage.setValue(this.damage + "");
		gun.setAttributeNode(damage);
		
		Attr size = doc.createAttribute("size");
		warmup.setValue(this.size + "");
		gun.setAttributeNode(warmup);
		
		Attr bulletType = doc.createAttribute("bulletType");
		bulletType.setValue(this.bulletType + "");
		gun.setAttributeNode(bulletType);
		
		Attr bulletSpeed = doc.createAttribute("bulletSpeed");
		bulletSpeed.setValue(this.bulletSpeed + "");
		gun.setAttributeNode(bulletSpeed);
		
		Attr cannonLength = doc.createAttribute("cannonLength");
		cannonLength.setValue(this.cannonLength + "");
		gun.setAttributeNode(cannonLength);
		
		Attr recoil = doc.createAttribute("recoil");
		recoil.setValue(this.recoil + "");
		gun.setAttributeNode(recoil);
		
		Attr thrust = doc.createAttribute("thrust");
		thrust.setValue(this.thrust + "");
		gun.setAttributeNode(thrust);
		
		Attr proba = doc.createAttribute("proba");
		proba.setValue(this.proba + "");
		gun.setAttributeNode(proba);
		
	}

	@Override
	public Element getElement() {
		// TODO Auto-generated method stub
		return null;
	}
}
