package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;

/**
 * extends {@link Spatial}</br>
 * Part of the entity which can colliding with an other entity with a <code>Collider</code>
 */
public class Collider extends Spatial {
	public Vec2f extent;

	public Collider(Vec2f position, Vec2f extent) {
		super(position);
		this.extent = extent;
	}

	/**
	 * @param other
	 * @return <i>true</i> if <code>this</code> and <code>other</code> are colliding
	 */
	public boolean isColliding(Collider other) {
		return Math.abs(pos().x - other.pos().x) < extent.x + other.extent.x
				&& Math.abs(pos().y - other.pos().y) < extent.y + other.extent.y;
	}

	/**
	 * @return The position -<code>float</code>- which is the most toward the left
	 *         on X axe
	 */
	public float getXLeft() {
		return pos().x - extent.x;
	}

	/**
	 * @return The position -<code>float</code>- which is the most toward the right
	 *         on X axe
	 */
	public float getXRight() {
		return pos().x + extent.x;
	}

	/**
	 * @return The position -<code>float</code>- which is the most toward the top on
	 *         Y axe
	 */
	public float getYTop() {
		return pos().y - extent.y;
	}

	/**
	 * @return The position -<code>float</code>- which is the most toward the bottom
	 *         on Y axe
	 */
	public float getYBottom() {
		return pos().y + extent.y;
	}

	/**
	 * Test if there is a colliding between {@link Collider} <code>c1</code>
	 * {@link Collider} <code>c2</code> only considering the Y axe
	 * 
	 * @param c1
	 * @param c2
	 * @return <i>true</i> if there is a collidong on Y axe 
	 */
	public static boolean isY1IncluedInY2(Collider c1, Collider c2) {
		return c1.getYBottom() > c2.getYTop() && c1.getYTop() < c2.getYBottom();
	}

	/**
	 * Test if there is a colliding between {@link Collider} <code>c1</code>
	 * {@link Collider} <code>c2</code> only considering the X axe
	 * 
	 * @param c1
	 * @param c2
	 * @return <i>true</i> if there is a collidong on X axe 
	 */
	public static boolean isX1IncluedInX2(Collider c1, Collider c2) {
		return c1.getXLeft() < c2.getXRight() && c1.getXRight() > c2.getXLeft();
	}
}
