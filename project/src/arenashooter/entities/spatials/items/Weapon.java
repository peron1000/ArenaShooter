package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Sprite;

public class Weapon extends Item {
	
	public Vec2f handPosL = null;
	public Vec2f handPosR = null;	

	public Weapon(Vec2f position, String itemSprite, double size) {
		super(position, itemSprite, size);
	}

	public void attackStart() { }
	
	public void attackStop() { }

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
