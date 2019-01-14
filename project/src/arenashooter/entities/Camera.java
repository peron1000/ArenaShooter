package arenashooter.entities;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Spatial3;

public class Camera extends Spatial3 {
	public Mat4f viewMatrix = Mat4f.identity();
	private Vec3f targetLoc;
	private Vec2f margin = new Vec2f(200, 200);
	private float zoomMin = 300, zoomMax = 3000;
	
	private float shakeIntensity = 0;
	private double time = 0;
	
	public Camera(Vec3f position) {
		super(position);
		this.targetLoc = position.clone();
	}
	
	@Override
	public void step(double d) {
		//Camera shake
		float shakeX = (float) (Math.sin(148*time)*shakeIntensity);
		float shakeY = (float) (Math.cos(136*time)*shakeIntensity);
		float shakeZ = (float) (Math.sin(155*time+.1)*shakeIntensity);
		shakeIntensity = Utils.lerpF(shakeIntensity, 0, Math.min( 1, 6*d ));
		time += d;
		
		position.x = Utils.lerpF( position.x, targetLoc.x, Math.min(1, 8*d) );
		position.y = Utils.lerpF( position.y, targetLoc.y, Math.min(1, 8*d) );
		position.z = Utils.lerpF( position.z, targetLoc.z, Math.min(1, 10*d) );
		viewMatrix = Mat4f.viewMatrix(new Vec3f(position.x+shakeX, position.y+shakeY, position.z+shakeZ), rotation);
		
		super.step(d);
	}
	
	/**
	 * Set camera shake intensity. </br>
	 * Ignored if new value < current value.
	 * @param intensity new shake intensity value
	 */
	public void setCameraShake( float intensity ) {
		shakeIntensity = Math.max( shakeIntensity, intensity );
	}
	
	/**
	 * Move the camera target to fit all the targets in the view
	 * @param targets
	 * @param bounds world camera bounds (min x, min y, max x, max y), null if none
	 * @param d delta time
	 */
	public void center( Spatial[] targets, Vec4f bounds, double d ) { //TODO: Improve bounds support
		if(targets.length == 0 ) return;
		
		Vec2f boundsX = new Vec2f(targets[0].position.x, targets[0].position.x);
		Vec2f boundsY = new Vec2f(targets[0].position.y, targets[0].position.y);
		
		for( int i=1; i<targets.length; i++ ) {
			boundsX.x = Math.min(boundsX.x, targets[i].position.x);
			boundsX.y = Math.max(boundsX.y, targets[i].position.x);

			boundsY.x = Math.min(boundsY.x, targets[i].position.y);
			boundsY.y = Math.max(boundsY.y, targets[i].position.y);
		}
		
		if(bounds != null) {
			boundsX.x = Utils.clampF(boundsX.x, bounds.x, bounds.z);
			boundsX.y = Utils.clampF(boundsX.y, bounds.x, bounds.z);
			boundsY.x = Utils.clampF(boundsY.x, bounds.y, bounds.w);
			boundsY.y = Utils.clampF(boundsY.y, bounds.y, bounds.w);
		}

		float boundsW = Math.max(0, boundsX.y - boundsX.x) + margin.x;
		float boundsH = Math.max(0, boundsY.y - boundsY.x) + margin.y;
		
		targetLoc.x = Utils.lerpF(boundsX.x, boundsX.y, .5f);
		targetLoc.y = Utils.lerpF(boundsY.x, boundsY.y, .5f);
		
		float newZ;
		if( boundsW/boundsH > Window.getRatio() ) { //TODO: Test with different window sizes and aspects
			newZ = zoomMin+Utils.clampF(350/((800*Window.getRatio())/boundsW), 0, zoomMax-zoomMin);
		} else {
			newZ = zoomMin+Utils.clampF(350/(800/boundsH), 0, zoomMax-zoomMin);
		}
		
		//Slow zoom-in
		if( newZ < targetLoc.z )
			targetLoc.z = Utils.lerpF(targetLoc.z, newZ, Math.min(1, 1.5*d) );
		else
			targetLoc.z = newZ;
	}
}
