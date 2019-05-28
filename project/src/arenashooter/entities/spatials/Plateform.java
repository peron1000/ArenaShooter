package arenashooter.entities.spatials;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;

@Deprecated
public class Plateform extends Spatial {
	
	@Deprecated
	public Plateform(Vec2f position, Vec2f extent) {
		super(position);

		Sprite spr = new Sprite(position, Texture.default_tex);
		Vec2f.multiply(extent, 2, spr.size);
		spr.attachToParent(this, "sprite");
	}
}
