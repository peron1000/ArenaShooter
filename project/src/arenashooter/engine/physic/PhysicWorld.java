package arenashooter.engine.physic;

import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.PhysicBody;
import arenashooter.engine.physic.joints.PhysicJoint;
import arenashooter.engine.physic.shapes.PhysicShape;
import arenashooter.entities.Arena;
import arenashooter.entities.spatials.Projectile;
import arenashooter.entities.spatials.Spatial;

public class PhysicWorld {
	public static final Logger log = LogManager.getLogger("Phys");
	
	/** Box-2d world */
	private World world;
	
	/** Arena represented by this simulation */
	private Arena arena;
	
	private MyContactListener contactListener = new MyContactListener();
	
	private LinkedList<PhysicBody> bToDestroy = new LinkedList<>(), bToCreate = new LinkedList<>();
	private LinkedList<PhysicJoint> jToDestroy = new LinkedList<>(), jToCreate = new LinkedList<>();
	
	public PhysicWorld(Arena arena) {
		this.arena = arena;
		
		world = new World(arena.gravity.toB2Vec());
		
		world.setSleepingAllowed(false);
		
		world.setContactListener(contactListener);
	}
	
	public void step(double d) {
		Profiler.startTimer(Profiler.PHYSIC);
		
		world.setGravity(arena.gravity.toB2Vec());
		world.step((float) d, 9, 4);

		//Safely destroy joints after step
		for(PhysicJoint joint : jToDestroy)
			joint.destroy();
		jToDestroy.clear();
		
		//Safely destroy bodies after step
		for(PhysicBody body : bToDestroy)
			body.destroy();
		bToDestroy.clear();

		//Safely create bodies after step
		for(PhysicBody body : bToCreate)
			body.create();
		bToCreate.clear();
		
		//Safely create joints after step
		for(PhysicJoint joint : jToCreate)
			joint.create();
		jToCreate.clear();
		
		Profiler.endTimer(Profiler.PHYSIC);
	}
	
	/**
	 * Mark a body for destruction
	 * @param body
	 */
	public void destroyBody(PhysicBody body) {
		bToDestroy.add(body);
	}
	
	/**
	 * Mark a body for creation
	 * @param body
	 */
	public void createBody(PhysicBody body) {
		bToCreate.add(body);
	}
	
	/**
	 * Mark a joint for destruction
	 * @param joint
	 */
	public void destroyJoint(PhysicJoint joint) {
		jToDestroy.add(joint);
	}
	
	/**
	 * Mark a joint for creation
	 * @param joint
	 */
	public void createJoint(PhysicJoint joint) {
		jToCreate.add(joint);
	}
	
	public World getB2World() { return world; }
	
	public void createStaticBody(PhysicShape shape, Vec2f pos, double rot) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos.x, pos.y*-1);

		Body body = world.createBody(bodyDef);
		body.createFixture(shape.getB2Shape(), 0);
	}
	
	public Contact getContacts() {
		return world.getContactList();
	}
	
	private class MyContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			if( contact.getFixtureA().getUserData() instanceof Spatial
					&& contact.getFixtureB().getUserData() instanceof Spatial ) {
					//A is a projectile
					if( (contact.getFixtureA().getUserData()) instanceof Projectile ) {
						((Projectile)contact.getFixtureA().getUserData()).impact((Spatial) contact.getFixtureB().getUserData());
					}
					//B is a projectile
					if( (contact.getFixtureB().getUserData()) instanceof Projectile ) {
						((Projectile)contact.getFixtureB().getUserData()).impact((Spatial) contact.getFixtureA().getUserData());
					}
			}
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
//			if(impulse.normalImpulses[0] > .7f) {
//				System.out.println(impulse.normalImpulses[0]);
//				float volume = Math.min(1, (impulse.normalImpulses[0]-.5f)*.1f);
//				Audio.playSound2D("data/sound/explosion_01.ogg", volume, Utils.lerpF(.8f, 1.2f, Math.random()), new Vec2f(contact.getManifold().localPoint));
//			}
		}
		
	}
}
