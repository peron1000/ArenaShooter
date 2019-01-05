package arenashooter.entities;

import arenashooter.Main;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;

/**
 * extends {@link Spatial}</br>
 * Part of the entity which can colliding with an other entity with a <code>Collider</code>
 */
public class Collider extends Spatial {
	Vec2f extent;

	public Collider(Vec2f position, Vec2f extent) {
		super(position);
//		this.position = position;
		this.extent = extent;
	}

	/**
	 * @param other
	 * @return <i>true</i> if <code>this</code> and <code>other</code> are colliding
	 */
	boolean isColliding(Collider other) {
		return Math.abs(position.x - other.position.x) < extent.x + other.extent.x
				&& Math.abs(position.y - other.position.y) < extent.y + other.extent.y;
	}

	Impact colliding(Vec2f vel, Collider other) {
		return new Impact(this, other, vel);
	}

	/**
	 * @return The point on top and left corner
	 */
	public Vec2f getTopLeftVec() {
		return new Vec2f(position.x - extent.x, position.y - extent.y);
	}

	/**
	 * @return The point on top and right corner
	 */
	public Vec2f getTopRightVec() {
		return new Vec2f(position.x + extent.x, position.y - extent.y);
	}

	/**
	 * @return The point on bottom and left corner
	 */
	public Vec2f getBottomLeftVec() {
		return new Vec2f(position.x - extent.x, position.y + extent.y);
	}

	/**
	 * @return The point on bottom and right corner
	 */
	public Vec2f getBottomRightVec() {
		return new Vec2f(position.x + extent.x, position.y + extent.y);
	}

	/**
	 * @return The position -<code>float</code>- which is the most toward the left
	 *         on X axe
	 */
	public float getXLeft() {
		return position.x - extent.x;
	}

	/**
	 * @return The position -<code>float</code>- which is the most toward the right
	 *         on X axe
	 */
	public float getXRight() {
		return position.x + extent.x;
	}

	/**
	 * @return The position -<code>float</code>- which is the most toward the top on
	 *         Y axe
	 */
	public float getYTop() {
		return position.y - extent.y;
	}

	/**
	 * @return The position -<code>float</code>- which is the most toward the bottom
	 *         on Y axe
	 */
	public float getYBottom() {
		return position.y + extent.y;
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
	
	private static final Model quad = Model.loadQuad();
	private static final Shader shader = new Shader("data/shaders/debug_color");
	@Override
	public void draw() {
		if(Main.drawCollisions) {
			shader.bind();
			
			//Create matrices
			Mat4f modelM = Mat4f.transform(position, rotation, new Vec2f(extent.x*2, extent.y*2));
			shader.setUniformM4("model", modelM);
			shader.setUniformM4("view", Game.game.camera.viewMatrix);
			shader.setUniformM4("projection", Window.proj);
			
			shader.setUniformV4("color", new float[]{1,0,0,1});
			
			quad.bindToShader(shader);

			quad.bind();
			quad.draw(true);
		}
		
		super.draw();
	}
}
