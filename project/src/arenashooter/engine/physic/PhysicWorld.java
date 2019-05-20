package arenashooter.engine.physic;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Map;

public class PhysicWorld {
	/** Box-2d world */
	private World world;
	
	/** Map represented by this simulation */
	private Map map;
	
	private MyContactListener contactListener = new MyContactListener();
	
	public PhysicWorld(Map map) {
		this.map = map;
		
		world = new World(map.gravity.toB2Vec());
		
		world.setContactListener(contactListener);
	}
	
	public void step(double d) {
		Profiler.startTimer(Profiler.PHYSIC);
		
		world.setGravity(map.gravity.toB2Vec());
//		world.setGravity(new Vec2f(0, 9.807*100).toB2Vec()); //TODO: Remove this
		world.step((float) d, 9, 4);
		
		Profiler.endTimer(Profiler.PHYSIC);
	}
	
	public World getB2World() { return world; }
	
	public void createStaticBody(PhysicShape shape, Vec2f pos, double rot) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x, pos.y*-1);

		Body body = world.createBody(bodyDef);
		body.createFixture(shape.b2Shape, 0);
	}
	
	public Contact getContacts() {
		return world.getContactList();
	}
	
	private class MyContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			// TODO Auto-generated method stub
		}

		@Override
		public void endContact(Contact contact) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			//Prevent characters from bouncing
			if(contact.getFixtureA().getFilterData().categoryBits == CollisionCategory.CAT_CHARACTER.bits
					|| contact.getFixtureB().getFilterData().categoryBits == CollisionCategory.CAT_CHARACTER.bits)
				contact.setRestitution(0);
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub
		}
		
	}
}
