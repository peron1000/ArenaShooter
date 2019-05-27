package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GunXml extends AbstractElement {

	private String name = "";
	private int weight = 0;
	private String pathSprite = "";
	private String soundPickup = "";
	private double cooldown = 0;
	private int uses = 0;
	private String animPath = "";
	private double warmup = 0;
	private String soundWarmup = "";
	private String attackSound = "";
	private String noAmmoSound = "";
	private float damage = 0f;
	private double size = 0;
	private int bulletType = 0;
	private float bulletSpeed = 0f;
	private double cannonLength = 0;
	private double recoil = 0;
	private double thrust = 0;
	private int proba = 0;
	
	private float xpos = 0f;
	private float ypos = 0f;
	
	private Element gun;

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
		
		VecteurXml vecteurGun = new VecteurXml(doc, gun);
		vecteurGun.addVecteur("", xpos , ypos);
		for (Element e : vecteurGun.getVecteurs()) {
			gun.appendChild(e);
		}
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @param pathSprite the pathSprite to set
	 */
	public void setPathSprite(String pathSprite) {
		this.pathSprite = pathSprite;
	}

	/**
	 * @param soundPickup the soundPickup to set
	 */
	public void setSoundPickup(String soundPickup) {
		this.soundPickup = soundPickup;
	}

	/**
	 * @param cooldown the cooldown to set
	 */
	public void setCooldown(double cooldown) {
		this.cooldown = cooldown;
	}

	/**
	 * @param uses the uses to set
	 */
	public void setUses(int uses) {
		this.uses = uses;
	}

	/**
	 * @param animPath the animPath to set
	 */
	public void setAnimPath(String animPath) {
		this.animPath = animPath;
	}

	/**
	 * @param warmup the warmup to set
	 */
	public void setWarmup(double warmup) {
		this.warmup = warmup;
	}

	/**
	 * @param soundWarmup the soundWarmup to set
	 */
	public void setSoundWarmup(String soundWarmup) {
		this.soundWarmup = soundWarmup;
	}

	/**
	 * @param attackSound the attackSound to set
	 */
	public void setAttackSound(String attackSound) {
		this.attackSound = attackSound;
	}

	/**
	 * @param noAmmoSound the noAmmoSound to set
	 */
	public void setNoAmmoSound(String noAmmoSound) {
		this.noAmmoSound = noAmmoSound;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(double size) {
		this.size = size;
	}

	/**
	 * @param bulletType the bulletType to set
	 */
	public void setBulletType(int bulletType) {
		this.bulletType = bulletType;
	}

	/**
	 * @param bulletSpeed the bulletSpeed to set
	 */
	public void setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	/**
	 * @param cannonLength the cannonLength to set
	 */
	public void setCannonLength(double cannonLength) {
		this.cannonLength = cannonLength;
	}

	/**
	 * @param recoil the recoil to set
	 */
	public void setRecoil(double recoil) {
		this.recoil = recoil;
	}

	/**
	 * @param thrust the thrust to set
	 */
	public void setThrust(double thrust) {
		this.thrust = thrust;
	}

	/**
	 * @param proba the proba to set
	 */
	public void setProba(int proba) {
		this.proba = proba;
	}

	/**
	 * @param xpos the xpos to set
	 */
	public void setXpos(float xpos) {
		this.xpos = xpos;
	}

	/**
	 * @param ypos the ypos to set
	 */
	public void setYpos(float ypos) {
		this.ypos = ypos;
	}

	@Override
	public Element getElement() {
		// TODO Auto-generated method stub
		return null;
	}
}
