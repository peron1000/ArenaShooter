package arenashooter.engine.physic;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Map;

public class PhysicWorld {
	/** Box-2d world */
	private World world;
	
	/** Map represented by this simulation */
	private Map map;
	
	public PhysicWorld(Map map) {
		this.map = map;
		
		world = new World(map.gravity.toB2Vec());
		
	}
	
	public void step(double d) {
		Profiler.startTimer(Profiler.PHYSIC);
		
//		world.setGravity(map.gravity.toB2Vec());
		world.setGravity(new Vec2f(0, -9.807*100).toB2Vec()); //TODO: Remove this
		world.step((float) d, 9, 4);
		
		Profiler.endTimer(Profiler.PHYSIC);
	}
	
	public World getB2World() { return world; }
	
	public void createStaticBody(PhysicShape shape, Vec2f pos, double rot) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x, pos.y);

		Body body = world.createBody(bodyDef);
		body.createFixture(shape.b2Shape, 0);
	}
	
}
