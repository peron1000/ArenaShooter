package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Physic;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.game.Main;

public class StaticBodyContainer extends Spatial {
	
	private StaticBody body;

	public StaticBodyContainer(Vec2f position, StaticBody body) {
		super(position);
		this.body = body;
		Physic.registerStaticBody(body);
	}

	@Override
	public void draw() {
		if(Main.drawCollisions) body.shape.debugDraw();
		
		super.draw();
	}
}
