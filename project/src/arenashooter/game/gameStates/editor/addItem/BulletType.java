package arenashooter.game.gameStates.editor.addItem;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Sprite;

enum BulletType {
	DEFAULT("data/sprites/Bullet.png" , 0 , .018), CIRCLE("data/sprites/Ion_Bullet.png",1, .018), STAR("data/sprites/star.png" , 3 , .018),
	GRENADE("data/sprites/grenade_01.png" , 2 , .06);

	private Sprite sprite;
	private int id;

	private BulletType(String path , int id , double sizeScale) {
		this.id = id;
		sprite = new Sprite(new Vec2f(), path);
		sprite.size = new Vec2f(sprite.getTexture().getWidth() * sizeScale, sprite.getTexture().getHeight() * sizeScale);
		sprite.getTexture().setFilter(false);
	}

	public Sprite getSprite() {
		return sprite;
	}
	
	public int getId() {
		return id;
	}
}
