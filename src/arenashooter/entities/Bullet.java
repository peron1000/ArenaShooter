package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public class Bullet extends Projectile{
	
	public Bullet(Vec2f position, Vec2f vel) {
		super(position);
		damage = 10;
		rotation = 0;
		
		collider = new Collider(this.position, new Vec2f(25, 60));
		collider.attachToParent(this, "coll_Body");
		
		Sprite bod = new Sprite(position, "data/sprites/Bouboule.png");
		bod.size = new Vec2f(bod.tex.getWidth() * 3, bod.tex.getHeight() * 3);
		
		SoundEffect touche = new SoundEffect( this.position, "data/sound/Ptou.ogg" );
		touche.setVolume(.7f);
		touche.attachToParent(this, "snd_touche");
	}
	
	public void step(double d) {
		for (Entity bump : getParent().children.values()) {
			if (bump instanceof Plateform) {
				for (Entity coll : ((Plateform) bump).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						if (c.isColliding(collider)) {
							((SoundEffect)children.get("snd_touche")).play();
							detach();
						}
					}
				}
			}
			if(bump instanceof Character) {
				for (Entity coll : ((Character) bump).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						if (c.isColliding(collider)) {
							((SoundEffect)children.get("snd_touche")).play();
							((Character) bump).takeDamage(damage);
							detach();
						}
					}
				}
			}
		}
		
		position.add(Vec2f.multiply(vel, (float) d));
		
		((Spatial)children.get("sprite")).position = position;
		((Spatial)children.get("collider")).position = position;
		
		super.step(d);
	}
}
