package arenashooter.entities.spatials;

import java.util.ArrayList;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class Camera extends Spatial3 {
	/** View matrix (updated at every step) */
	public Mat4f viewMatrix = Mat4f.identity();
	private Vec3f targetLoc;
	private Vec2f margin = new Vec2f(5, 5);
	private float zoomMin = 10, zoomMax = 100;
	
	/** Vertical field of view */
	private float fov = 70;
	
	/** Does this camera interpolates its position towards its targetLoc */
	public boolean interpolate = true;
	
	private float shakeIntensity = 0;
	private double time = 0;
	
	public Camera(Vec3f position) {
		super(position);
		viewMatrix = Mat4f.viewMatrix(pos(), rotation);
		this.targetLoc = localPosition.clone();
	}
	
	@Override
	public void step(double d) {
		//Camera shake
		float shakeX = (float) (Math.sin(1.48*time)*shakeIntensity);
		float shakeY = (float) (Math.cos(1.36*time)*shakeIntensity);
		float shakeZ = (float) (Math.sin(1.55*time+.1)*shakeIntensity);
		shakeIntensity = Utils.lerpF(shakeIntensity, 0, Math.min( 1, 7*d ));
		time += d;
		
		if(interpolate) { //TODO: Change this to work with attachment
			localPosition.x = Utils.lerpF( localPosition.x, targetLoc.x, Math.min(1, 15*d) );
			localPosition.y = Utils.lerpF( localPosition.y, targetLoc.y, Math.min(1, 15*d) );
			localPosition.z = Utils.lerpF( localPosition.z, targetLoc.z, Math.min(1, 20*d) );
		}
		viewMatrix = Mat4f.viewMatrix(new Vec3f(pos().x+shakeX, pos().y+shakeY, pos().z+shakeZ), rotation);
		
		super.step(d);
	}
	
	/**
	 * Set camera shake intensity.</br>
	 * Ignored if new value < current value.
	 * @param intensity new shake intensity value
	 */
	public void setCameraShake( float intensity ) {
		shakeIntensity = Math.max( shakeIntensity, intensity );
	}
	
	/**
	 * @return vertical field of view
	 */
	public float getFOV() { return fov; }
	
	/**
	 * Set the vertical field of view for this camera
	 * @param fov
	 */
	public void setFOV( float fov ) {
		this.fov = fov;
		
		//Update projection matrix if this is the current camera
		if(Window.getCamera() == this)
			Window.createProjectionMatrix();
	}
	
	/**
	 * Move the camera target to fit all the targets in the view
	 * @param players
	 * @param bounds world camera bounds (min x, min y, max x, max y), null if none
	 * @param d delta time
	 */
	public void center( ArrayList<Character> players, Vec4f bounds, double d ) { //TODO: Improve bounds support
		if(players.size() == 0 ) return;
		
		Vec2f boundsX = new Vec2f(players.get(0).getWorldPos().x, players.get(0).getWorldPos().x);
		Vec2f boundsY = new Vec2f(players.get(0).getWorldPos().y, players.get(0).getWorldPos().y);
		
		for( int i=1; i<players.size(); i++ ) {
			boundsX.x = Math.min(boundsX.x, players.get(i).getWorldPos().x);
			boundsX.y = Math.max(boundsX.y, players.get(i).getWorldPos().x);

			boundsY.x = Math.min(boundsY.x, players.get(i).getWorldPos().y);
			boundsY.y = Math.max(boundsY.y, players.get(i).getWorldPos().y);
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
