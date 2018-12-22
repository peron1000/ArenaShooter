package arenashooter.entities;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class Camera extends Spatial3 {
	public Mat4f viewMatrix = Mat4f.identity();
	private Vec3f targetLoc;
	private Vec2f margin = new Vec2f(200, 200);
	
	public Camera(Vec3f position) {
		super(position);
		this.targetLoc = position.clone();
	}
	
	@Override
	public void step(double d) {
		position.x = Utils.lerpF( position.x, targetLoc.x, Math.min(1, 8*(float)d) );
		position.y = Utils.lerpF( position.y, targetLoc.y, Math.min(1, 8*(float)d) );
		position.z = Utils.lerpF( position.z, targetLoc.z, Math.min(1, 10*(float)d) );
		viewMatrix = Mat4f.viewMatrix(position, rotation);
		
		super.step(d);
	}
	
	/**
	 * Move the camera target to fit all the targets in the view
	 * @param targets
	 * @param d delta time
	 */
	public void center( Spatial[] targets, double d ) {
		if(targets.length == 0 ) return;
		
		Vec2f boundsX = new Vec2f(targets[0].position.x, targets[0].position.x);
		Vec2f boundsY = new Vec2f(targets[0].position.y, targets[0].position.y);
		
		for( int i=1; i<targets.length; i++ ) {
			boundsX.x = Math.min(boundsX.x, targets[i].position.x);
			boundsX.y = Math.max(boundsX.y, targets[i].position.x);

			boundsY.x = Math.min(boundsY.x, targets[i].position.y);
			boundsY.y = Math.max(boundsY.y, targets[i].position.y);
		}

		float boundsW = boundsX.y - boundsX.x + margin.x;
		float boundsH = boundsY.y - boundsY.x + margin.y;
		
		targetLoc.x = Utils.lerpF(boundsX.x, boundsX.y, .5f);
		targetLoc.y = Utils.lerpF(boundsY.x, boundsY.y, .5f);
		
		float newZ;
		if( boundsW/boundsH > Window.getRatio() ) { //TODO: Test with different window sizes and aspects
			newZ = Utils.clampF(200+350/((800*Window.getRatio())/boundsW), 200, 1000);
		} else {
			newZ = Utils.clampF(200+350/(800/boundsH), 200, 1000);
		}
		
		//Slow zoom-in
		if( newZ < targetLoc.z )
			targetLoc.z = Utils.lerpF(targetLoc.z, newZ, Math.min(1, 1*(float)d) );
		else
			targetLoc.z = newZ;
	}
}
