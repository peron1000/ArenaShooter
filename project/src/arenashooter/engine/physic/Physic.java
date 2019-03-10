package arenashooter.engine.physic;

import java.util.ArrayList;
import java.util.HashSet;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.Disk;
import arenashooter.engine.physic.shapes.Rectangle;
import arenashooter.entities.Map;

public class Physic {
	private ArrayList<Body> bodies = new ArrayList<Body>();
	private ArrayList<StaticBody> staticBodies = new ArrayList<StaticBody>();
	private ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>();
	
	HashSet<BodiesCouple> couplesToTest = new HashSet<BodiesCouple>();
	
	/** Map represented by this simulation */
	private Map map;
	
	public Physic(Map map) {
		this.map = map;
	}
	
	public void step(double d) {
		Profiler.startTimer(Profiler.PHYSIC);
		
		for( RigidBody rigid : rigidBodies ) {
			rigid.preprocess(d, map.gravity);
		}
		
		couplesToTest.clear();
		for(RigidBody b : rigidBodies)
			for(Body other : bodies)
				if(b != other && mayCollide(b.shape, other.shape))
					couplesToTest.add(new BodiesCouple(b, other));
		
		for(BodiesCouple c : couplesToTest) {
			if( isColliding(c.a.shape, c.b.shape) ) {
				if(c.a instanceof RigidBody) {
					c.a.position = Vec2f.subtract(c.a.position, Vec2f.multiply(((RigidBody)c.a).velocity, d));
					((RigidBody)c.a).velocity = Vec2f.multiply(((RigidBody)c.a).velocity, -0.6);
					((RigidBody)c.a).angularVel = ((RigidBody)c.a).angularVel*.6;
				}
				if(c.b instanceof RigidBody) {
					c.b.position = Vec2f.subtract(c.b.position, Vec2f.multiply(((RigidBody)c.b).velocity, d));
					((RigidBody)c.b).velocity = Vec2f.multiply(((RigidBody)c.b).velocity, -0.6);
					((RigidBody)c.b).angularVel = ((RigidBody)c.b).angularVel*.6;
				}
			}
		}
		
		for( RigidBody rigid : rigidBodies ) {
			rigid.process(d);
		}
		
		Profiler.endTimer(Profiler.PHYSIC);
	}
	
	public void registerRigidBody(RigidBody body) {
		bodies.add(body);
		rigidBodies.add(body);
	}
	
	public void registerStaticBody(StaticBody body) {
		bodies.add(body);
		staticBodies.add(body);
	}
	
	public void unregisterRigidBody(RigidBody body) {
		bodies.remove(body);
		rigidBodies.remove(body);
	}
	
	public void unregisterStaticBody(StaticBody body) {
		bodies.remove(body);
		staticBodies.remove(body);
	}
	
	/**
	 * Test if two shapes might be colliding using their AABB
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean mayCollide(Shape a, Shape b) {
		Vec2f extA = a.getAABBextent();
		Vec2f extB = b.getAABBextent();
		
		return  (a.body.position.x - extA.x < b.body.position.x + extB.x && a.body.position.x + extA.x > b.body.position.x - extB.x) &&
				(a.body.position.y + extA.y > b.body.position.y - extB.y && a.body.position.y - extA.y < b.body.position.y + extB.y);
	}
	
	public boolean isColliding(Shape a, Shape b) {
		if(a instanceof Disk) //a is a Disk
			if(b instanceof Rectangle) //b is a Rectangle
				return( diskVsRect((Disk)a, (Rectangle)b) );
			else //b is a Disk
				return( diskVsDisk((Disk)a, (Disk)b) );
		else //a is a Rectangle
			if(b instanceof Rectangle) //b is a Rectangle
				return( rectVsRect((Rectangle)b, (Rectangle)a) );
			else //b is a Disk
				return( diskVsRect((Disk)b, (Rectangle)a) );
	}
	
	public boolean diskVsDisk(Disk a, Disk b) {
		double distSqr = Vec2f.subtract(a.body.position, b.body.position).lengthSquared();
		double radiiSqr = (a.radius+b.radius)*(a.radius+b.radius);
		return distSqr <= radiiSqr;
	}
	
	public boolean rectVsRect(Rectangle a, Rectangle b) { //TODO: Fix
		Vec2f normal = Vec2f.fromAngle(a.body.rotation);
		Vec2f projA = a.project(normal);
//		Vec2f projA = a.projectSelfX();
		Vec2f projB = b.project(normal);
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		normal = Vec2f.rotate90(normal);
		projA = a.project(normal);
//		projA = a.projectSelfY();
		projB = b.project(normal);
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		normal = Vec2f.fromAngle(b.body.rotation);
		projA = a.project(normal);
		projB = b.project(normal);
//		projB = b.projectSelfX();
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		normal = Vec2f.rotate90(normal);
		projA = a.project(normal);
		projB = b.project(normal);
//		projB = b.projectSelfY();
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		return true;
	}

	public boolean diskVsRect(Disk a, Rectangle b) { //TODO: Fix
		Vec2f normal = Vec2f.fromAngle(b.body.rotation);
		Vec2f projA = a.project(normal);
		Vec2f projB = b.project(normal);
//		Vec2f projB = b.projectSelfX();
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		normal = Vec2f.rotate90(normal);
		projA = a.project(normal);
		projB = b.project(normal);
//		projB = b.projectSelfY();
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		
		return true;
	}
	
	private class BodiesCouple {
		Body a, b;
		private BodiesCouple(Body a, Body b) {
			this.a = a;
			this.b = b;
		}
		
		@Override
		public String toString() {
			return a+" and "+b;
		}
	}
}
