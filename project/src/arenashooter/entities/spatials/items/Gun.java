package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.game.Game;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Sprite;

public class Gun extends Item {

	private double dispersion = 0.15;// la non-précision en radians.
	private Timer fire = new Timer(0.15);
	Collider coll;
	private float recul = 0.4f;
	private float damage = 1;

	public Gun(Vec2f position, SpritePath itemSprite) {
		super(position, itemSprite);
		tag = "Arme";
		coll = new Collider(position, new Vec2f(40, 40));
		if (itemSprite == SpritePath.assault) {
			recul = 0.5f;
			dispersion = 0.05;// la non-précision en radians.
			fire = new Timer(0.10);
			fire.attachToParent(this, "attack timer");
			
			damage = 5;
			
			SoundEffect bangSound = new SoundEffect(this.position, "data/sound/Bang1.ogg", 2);
			bangSound.setVolume(3f);
			bangSound.attachToParent(this, "snd_Bang");
			
		} else if (itemSprite == SpritePath.minigun) {
			recul = 0.25f;
			dispersion = 0.15;// la non-précision en radians.
			fire = new Timer(0.05);
			fire.attachToParent(this, "attack timer");
			
			damage = 0;
			
			SoundEffect bangSound = new SoundEffect(this.position, "data/sound/Bang2.ogg", 2);
			bangSound.setVolume(3f);
			bangSound.attachToParent(this, "snd_Bang");
		}

		SoundEffect pickup = new SoundEffect(this.position, "data/sound/GunCock1.ogg", 1);
		pickup.setVolume(0.5f);
		pickup.attachToParent(this, "snd_Pickup");
	}

	public void fire(boolean lookRight) { // Visée uniquement droite et gauche pour l'instant. TODO :
		if (fire.isOver()) {
			float pX = 0;
			float vX = 0;
			if (parent instanceof Character) {
				if (lookRight) {
					pX = position.x + 50;
					vX = 3000;
					((Character) parent).vel.x -= 50;
				} else {
					pX = position.x - 50;
					vX = -3000;
					((Character) parent).vel.x += 50;
				}
			}
			fire.restart();

			double coeff = (2 * Math.random()) - 1;

			Vec2f angle = Vec2f.rotate(new Vec2f(vX, 0), dispersion * coeff);
			angle.x += ((Character) parent).vel.x / 4;
			angle.y += ((Character) parent).vel.y / 4;

			Bullet bul = new Bullet(new Vec2f(pX, position.y), angle, damage);
			bul.attachToParent(Game.game.map, ("bullet" + bul.genName()));

			vel.add(Vec2f.multiply(Vec2f.normalize(Vec2f.rotate(angle, Math.PI)), recul * 5000));
			((SoundEffect) children.get("snd_Bang")).play();

			((Sprite) children.get("item_Sprite")).rotation += ((Math.random()) - 0.5) * recul;
			
			//Add camera shake
			Game.game.camera.setCameraShake(.8f);
		}
	}
	
	/**	public void fire(boolean lookRight) { // Visée par vecteur en cours de construction TODO :
		if (fire.isOver()) {
			float pX = 0;
			float vX = 0;
			if (parent instanceof Character) {
				if (lookRight) {
					pX = position.x + 50;
					vX = 3000;
					Vec2f.
					((Character) parent).vel.x -= 50;
				} else {
					pX = position.x - 50;
					vX = -3000;
					((Character) parent).vel.x += 50;
				}
			}
			fire.restart();

			double coeff = (2 * Math.random()) - 1;

			Vec2f angle = Vec2f.rotate(new Vec2f(vX, 0), dispersion * coeff);
			angle.x += ((Character) parent).vel.x / 4;
			angle.y += ((Character) parent).vel.y / 4;

			Bullet bul = new Bullet(new Vec2f(pX, position.y), angle, damage);
			bul.attachToParent(Game.game.map, ("bullet" + bul.genName()));

			vel.add(Vec2f.multiply(Vec2f.normalize(Vec2f.rotate(angle, Math.PI)), recul * 5000));
			((SoundEffect) children.get("snd_Bang")).play();

			((Sprite) children.get("item_Sprite")).rotation += ((Math.random()) - 0.5) * recul;
			
			//Add camera shake
			Game.game.camera.setCameraShake(.8f);
		}
	}**/

	public void step(double d) {
		if(parent!=null &&parent instanceof Character) {
			rotation = ((Character)parent).aimInput;
		}
		if (isEquipped()) {
			Sprite image = ((Sprite) children.get("item_Sprite"));
			if(image != null) {
				if (((Character) parent).lookRight)
					image.flipX = false;
				else
					image.flipX = true;
				vel.x = Utils.lerpF(vel.x, 0, Utils.clampD(d * 50, 0, 1));
				vel.y = Utils.lerpF(vel.y, 0, Utils.clampD(d * 50, 0, 1));
				double lerpVal = d * ((Math.abs(rotation) > 1) ? 30 : 10);
				image.rotation = Utils.lerpD(image.rotation, 0, Utils.clampD(lerpVal, 0, 1));
			}
		}
		
		super.step(d);
	}
}
