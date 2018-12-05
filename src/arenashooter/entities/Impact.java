package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public class Impact {

	public enum ImpactLocation {
		Left, Right, Top, Bottom, NoImpact;
	}

	private boolean collision;
	private float entryTime;
	private ImpactLocation impactLocation;
	private Vec2f response;

	public Impact(Collider c1, Collider c2, Vec2f vel) {
		float x = xReponse(c1 , c2 , vel);
		float y = yReponse(c1 , c2 , vel);
		response = new Vec2f(x, y);
	}

	private float yReponse(Collider c1, Collider c2, Vec2f vel) {
		boolean xImpact = Collider.isX1IncluedInX2(c1, c2);
		if(xImpact && vel.y != 0) {
			if(vel.y > 0) {
				if(c1.getYTop() >= c2.getYBottom()) {
					return 1;
				} else {
					float dist = c2.getYTop() - c1.getYBottom();
					dist = dist / vel.y;
					if(dist < 1) {
						return dist;
					} else {
						return 1;
					}
				}
			} else {
				if(c1.getYBottom() <= c2.getYTop()) {
					return 1;
				} else {
					float dist = c1.getYTop() - c2.getYBottom();
					dist = - dist / vel.y;
					if(dist < 1) {
						return dist;
					} else {
						return 1;
					}
				}
			}
		}
		return 1;
	}

	private float xReponse(Collider c1, Collider c2, Vec2f vel) {
		boolean yImpact = Collider.isY1IncluedInY2(c1, c2);
		if(yImpact && vel.x != 0) {
			if(vel.x > 0) {
				if(c1.getXLeft() >= c2.getXRight()) {
					return 1;
				} else {
					float dist = c2.getXLeft() - c1.getXRight();
					dist = dist / vel.x;
					if(dist < 1) {
						return dist;
					} else {
						return 1;
					}
				}
			} else {
				if(c1.getXRight() <= c2.getXLeft()) {
					return 1;
				} else {
					float dist = c1.getXLeft() - c2.getXRight();
					dist = - dist / vel.x;
					if(dist < 1) {
						return dist;
					} else {
						return 1;
					}
				}
			}
		}
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
	
	public Vec2f getResponse() {
		return response;
	}
}
