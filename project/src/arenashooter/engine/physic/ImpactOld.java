package arenashooter.engine.physic;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Collider;

public class ImpactOld {

	public enum ImpactLocation {
		Left, Right, Top, Bottom, NoImpact;
	}

	private boolean collision;
	private float entryTime;
	private ImpactLocation impactLocation;
	private Vec2f velMod;

	public ImpactOld(Collider c1, Collider c2, Vec2f vel) {
		float x = xColliding(c1, c2, vel);
		float y = yColliding(c1, c2, vel);
		velMod = new Vec2f(x, y);
		collision = true;
		if(y < x) { // Vertical impact first
			if(y > 0) {
				impactLocation = ImpactLocation.Bottom;
			} else {
				impactLocation = ImpactLocation.Top;
			}
		} else if(x < y) { // Horizontal impact first
			if(x > 0) {
				impactLocation = ImpactLocation.Right;
			} else {
				impactLocation = ImpactLocation.Left;
			}
		} else { // x = y
			if(x == 1) {
				impactLocation = ImpactLocation.NoImpact;
				collision = false;
			} else {
				impactLocation = ImpactLocation.Bottom; // By default
			}
		}
		entryTime = Math.min(x, y);
	}

	/**
	 * @param c1
	 * @param c2
	 * @param vel
	 * @return The <code>float</code> which is the representation of the distance
	 *         betwenn {@link Collider} <code>c1</code> and {@link Collider}
	 *         <code>c2</code> on Y axe.</br>
	 *         The <code>float</code> given is a % so its value stay between 0 and 1
	 */
	private float yColliding(Collider c1, Collider c2, Vec2f vel) {

		// check if c1 and c2 can have a collision on X axe (no matter their Y position)
		boolean xImpact = Collider.isX1IncluedInX2(c1, c2);

		if (xImpact && vel.y != 0) { // a collision could be possible in Y axe

			if (vel.y > 0) { // c1 is going down
				if (c1.getYTop() >= c2.getYBottom()) {// c1 is below c2 so a collision is impossible
					return 1;
				} else { // c1 is above c2
					float dist = c2.getYTop() - c1.getYBottom();
					dist = dist / vel.y;
					if (dist < 1) { // there will be a collision in Y axe during the frame
						return dist;
					} else { // c2 is to far from c1 for a collision during the frame
						return 1;
					}
				}
			} else { // c1 is going up
				if (c1.getYBottom() <= c2.getYTop()) { // c1 is above c2 so a collision is impossible
					return 1;
				} else { // c1 is below c2
					float dist = c1.getYTop() - c2.getYBottom();
					dist = -dist / vel.y;
					if (dist < 1) { // there will be a collision in Y axe during the frame
						return dist;
					} else { // c2 is to far from c1 for a collision during the frame
						return 1;
					}
				}
			}
		}

		// Collision is impossible, no matter how the Y velocity is high
		return 1;
	}

	/**
	 * @param c1
	 * @param c2
	 * @param vel
	 * @return The <code>float</code> which is the representation of the distance
	 *         betwenn {@link Collider} <code>c1</code> and {@link Collider}
	 *         <code>c2</code> on X axe.</br>
	 *         The <code>float</code> given is a % so its value stay between 0 and 1
	 */
	private float xColliding(Collider c1, Collider c2, Vec2f vel) {

		boolean yImpact = Collider.isY1IncluedInY2(c1, c2); // check if c1 and c2 can have a collision on Y axe (no
															// matter their X position)
		if (yImpact && vel.x != 0) { // a collision could be possible in X axe
			if (vel.x > 0) { // c1 is going right
				if (c1.getXLeft() >= c2.getXRight()) { // c1 is already on the right of c2
					return 1;
				} else { // c1 is on the left of c2
					float dist = c2.getXLeft() - c1.getXRight();
					dist = dist / vel.x;
					if (dist < 1) { // there will be a collision in X axe during the frame
						return dist;
					} else { // c2 is to far from c1 for a collision during the frame
						return 1;
					}
				}
			} else { // c1 is going left
				if (c1.getXRight() <= c2.getXLeft()) { // c1 is already on the left of c2
					return 1;
				} else {
					float dist = c1.getXLeft() - c2.getXRight();
					dist = -dist / vel.x;
					if (dist < 1) { // there will be a collision in X axe during the frame
						return dist;
					} else { // c2 is to far from c1 for a collision during the frame
						return 1;
					}
				}
			}
		}
		
		// Collision is impossible, no matter how the X velocity is high
		return 1;
	}

	public boolean isColliding() {
		return collision;
	}

	public float getTimeToCollision() {
		return entryTime;
	}

	public ImpactLocation getImpactLocation() {
		return impactLocation;
	}

	/**
	 * On purpose to be multiplied with the velocity to reduce it so the first {@link Collider}
	 * will be well placed in case of a colliding
	 * @return A {@link Vec2f} which is a % before impact for each axe
	 */
	public Vec2f getVelMod() {
		return velMod;
	}
}
