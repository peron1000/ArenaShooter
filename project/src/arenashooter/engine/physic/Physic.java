package arenashooter.engine.physic;

import java.util.ArrayList;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.Disk;
import arenashooter.engine.physic.shapes.Rectangle;

public final class Physic {
	private static ArrayList<Body> bodies = new ArrayList<Body>();
	private static ArrayList<StaticBody> staticBodies = new ArrayList<StaticBody>();
	private static ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>();
	
	public static Vec2f globalForce = new Vec2f();
	
	//This class cannot be instantiated
	private Physic() {}
	
	public static void step(double d) {
		for(RigidBody b : rigidBodies) {
			System.out.println("Hit:");
			for(Body other : bodies) {
				if(b != other) {
					if(mayCollide(b.shape, other.shape)) System.out.println(other);
				}
			}
		}
		
		for( RigidBody rigid : rigidBodies ) {
			rigid.process(d);
		}
	}
	
	public static void registerRigidBody(RigidBody body) {
		bodies.add(body);
		rigidBodies.add(body);
	}
	
	public static void registerStaticBody(StaticBody body) {
		bodies.add(body);
		staticBodies.add(body);
	}
	
	/**
	 * Test if two shapes might be colliding using their AABB
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean mayCollide(Shape a, Shape b) {
		Vec2f extA = a.getAABBextent();
		Vec2f extB = b.getAABBextent();
		
		return  (a.body.position.x - extA.x < b.body.position.x + extB.x && a.body.position.x + extA.x > b.body.position.x - extB.x) &&
				(a.body.position.y + extA.y > b.body.position.y - extB.y && a.body.position.y - extA.y < b.body.position.y + extB.y);
	}
	
	public static boolean isColliding(Shape a, Shape b) {
		if(a instanceof Disk) //a is a Disk
			if(b instanceof Rectangle) //b is a Rectangle
				return( isCollidingDiskVsRect((Disk)a, (Rectangle)b) );
			else //b is a Disk
				return( isCollidingDiskVsDisk((Disk)a, (Disk)b) );
		else //a is a Rectangle
			if(b instanceof Rectangle) //b is a Rectangle
				return( isCollidingRectVsRect((Rectangle)b, (Rectangle)a) );
			else //b is a Disk
				return( isCollidingDiskVsRect((Disk)b, (Rectangle)a) );
	}
	
	public static boolean isCollidingDiskVsDisk(Disk a, Disk b) {
		double distSqr = Vec2f.subtract(a.body.position, b.body.position).lengthSquared();
		double radiiSqr = (a.radius+b.radius)*(a.radius+b.radius);
		return distSqr <= radiiSqr;
	}
	
	public static boolean isCollidingRectVsRect(Rectangle a, Rectangle b) {
		Vec2f normal = Vec2f.fromAngle(a.body.rotation);
		Vec2f projA = a.project(normal);
		Vec2f projB = b.project(normal);
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		normal = Vec2f.fromAngle(a.body.rotation+Math.PI);
		projA = a.project(normal);
		projB = b.project(normal);
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		normal = Vec2f.fromAngle(b.body.rotation);
		projA = a.project(normal);
		projB = b.project(normal);
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		normal = Vec2f.fromAngle(b.body.rotation+Math.PI);
		projA = a.project(normal);
		projB = b.project(normal);
		if( projB.x >= projA.y || projA.x >= projB.y ) return false;
		
		return true;
	}

	public static boolean isCollidingDiskVsRect(Disk a, Rectangle b) {
		return false;
	}
}
