package arenashooter.entities.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Impact;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Plateform;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;

public class Item extends Spatial {
	
	protected String tag = "Item";
	
	public String getTag() {
		return tag;
	}
	
	public enum ItemSprite{
		minugun("data/weapons/Minigun_1.png"), assault("data/weapons/Assaut_1.png"), armor("data/armor/shield_of_Pop.png");
		public String sprite;
		private ItemSprite(String sprite) {
			this.sprite = sprite;
		}
	}
	Vec2f vel = new Vec2f();
	Collider collider;
	boolean isOnGround = true;
	public float movementInput = 0;

	public Item(Vec2f position, ItemSprite itemSprite) {
		super(position);
		
		Sprite sprite = new Sprite(position, itemSprite.sprite);
		sprite.attachToParent(this, "item_Sprite");
		sprite.size =  new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight());
		
		collider = new Collider(position, new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight()));
		collider.attachToParent(this, "coll_item");
		
	}

	@Override
	public void step(double d) {
		super.step(d);
	}
}