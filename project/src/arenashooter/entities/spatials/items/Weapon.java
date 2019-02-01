package arenashooter.entities.spatials.items;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.items.Item.SpritePath;
import arenashooter.game.GameMaster;

public class Weapon extends Item {

	public Weapon(Vec2f position, SpritePath itemSprite) {
		super(position, itemSprite);
	}

	public void attackStart() {
			
	}
	
	public void attackStop() {
		
	}

	public void step(double d) {
		if (isEquipped()) {
			rotation = Utils.normalizeAngle(rotation);
			Sprite image = ((Sprite) children.get("Item_Sprite"));
			if (image != null) {
				image.rotation = rotation;
				if (rotation < Math.PI / 2 && rotation > -Math.PI / 2)
					image.flipY = false;
				else
					image.flipY = true;
				getVel().x = Utils.lerpF(getVel().x, 0, Utils.clampD(d * 50, 0, 1));
				getVel().y = Utils.lerpF(getVel().y, 0, Utils.clampD(d * 50, 0, 1));
			}
		}

		super.step(d);
	}
}
