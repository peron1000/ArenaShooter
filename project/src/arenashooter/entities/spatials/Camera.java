package arenashooter.entities.spatials;

import java.util.List;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class Camera extends Spatial3 {
	/** View matrix (updated at every step) */
	public Mat4f viewMatrix = Mat4f.identity();
	private Vec3f targetLoc;
	private Vec2f margin = new Vec2f(5, 5);
	private float zoomMin = 18;
	
	/** Vertical field of view */
	private float fov = 55;
	
	/** Does this camera interpolates its position towards its targetLoc */
	public boolean interpolate = true;
	
	private float shakeIntensity = 0;
	private double time = 0;
	
	public Camera(Vec3f position) {
		super(position);
		Mat4f.viewMatrix(getWorldPos(), localRotation, viewMatrix);
		this.targetLoc = localPosition.clone();
	}
	
	@Override
	public void step(double d) {
		//Camera shake
		float shakeX = (float) (Math.sin(120.48*time)*shakeIntensity);
		float shakeY = (float) (Math.cos(110.36*time)*shakeIntensity);
		float shakeZ = (float) (Math.sin(100.55*time+.1)*shakeIntensity);
		shakeIntensity = Utils.lerpF(shakeIntensity, 0, Math.min( 1, 7*d ));
		time += d;
		
		if(interpolate) { //TODO: Change this to work with attachment
			localPosition.x = Utils.lerpF( localPosition.x, targetLoc.x, Math.min(1, 15*d) );
			localPosition.y = Utils.lerpF( localPosition.y, targetLoc.y, Math.min(1, 15*d) );
			localPosition.z = Utils.lerpF( localPosition.z, targetLoc.z, Math.min(1, 20*d) );
		}
		Mat4f.viewMatrix(new Vec3f(getWorldPos().x+shakeX, getWorldPos().y+shakeY, getWorldPos().z+shakeZ), localRotation, viewMatrix);
		
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
	 * @param basePos base camera position used to restrict movement
	 * @param d delta time
	 */
	public void center( List<Character> centerTargets, Vec3f basePos, double d ) { //TODO: Improve bounds support
		if(centerTargets == null || centerTargets.isEmpty() ) {
			targetLoc.set(basePos);
			return;
		}
		
		float boundsXL = centerTargets.get(0).getWorldPos().x; //-x
		float boundsXR = centerTargets.get(0).getWorldPos().x; //+x
		float boundsYU = centerTargets.get(0).getWorldPos().y; //-y
		float boundsYD = centerTargets.get(0).getWorldPos().y; //+y
		
		//Rectangle including all characters
		for( int i=1; i<centerTargets.size(); i++ ) {
			boundsXL = Math.min(boundsXL, centerTargets.get(i).getWorldPos().x);
			boundsXR = Math.max(boundsXR, centerTargets.get(i).getWorldPos().x);

			boundsYU = Math.min(boundsYU, centerTargets.get(i).getWorldPos().y);
			boundsYD = Math.max(boundsYD, centerTargets.get(i).getWorldPos().y);
		}

		//Add margin to rectangle
		boundsXL -= margin.x;
		boundsXR += margin.x;
		boundsYU -= margin.y;
		boundsYD += margin.y;
		
		//Clamp rectangle to camera bounds
		boundsXL = Math.max(boundsXL, getLowerX(basePos, 0));
		boundsXR = Math.min(boundsXR, getUpperX(basePos, 0));
		boundsYU = Math.max(boundsYU, getLowerY(basePos, 0));
		boundsYD = Math.min(boundsYD, getUpperY(basePos, 0));
		
		//Set camera target to the center of the rectangle
		targetLoc.x = Utils.lerpF(boundsXL, boundsXR, .5f);
		targetLoc.y = Utils.lerpF(boundsYU, boundsYD, .5f);
		
		//Get rectangle size
		float boundsW = Math.max(0, boundsXR - boundsXL);
		float boundsH = Math.max(0, boundsYD - boundsYU);
		
		float newZ;
		if( boundsW/boundsH > Window.getRatio() ) { //TODO: Test with different window sizes and aspects
			newZ = zoomMin+(350/((800*Window.getRatio())/boundsW));
		} else {
			newZ = zoomMin+(350/(800/boundsH));
		}
		
		//Slow zoom-in
		if( newZ < targetLoc.z )
			targetLoc.z = Utils.lerpF(targetLoc.z, newZ, Math.min(1, 1.3*d) );
		else
			targetLoc.z = newZ;
		
		targetLoc.z = Utils.clampF(targetLoc.z, zoomMin, basePos.z);

		targetLoc.x = Utils.clampF(targetLoc.x, getLowerX(basePos, targetLoc.z), getUpperX(basePos, targetLoc.z));
		targetLoc.y = Utils.clampF(targetLoc.y, getLowerY(basePos, targetLoc.z), getUpperY(basePos, targetLoc.z));
	}
	
	float getHorizontalFov() {
		return (float)( 2*Math.atan(Math.tan(getFOV()/2)*(1/Window.getRatio())) );
	}
	
	float getUpperX(Vec3f basePos, float z) {
		float rightVecLen = (float)( (basePos.z-z)/Math.cos(getHorizontalFov()/2) );

		float y = (float) (Math.sin(getHorizontalFov()/2)*rightVecLen);
		
		return basePos.x - y;
	}
	
	float getLowerX(Vec3f basePos, float z) {
		float leftVecLen = (float)( (basePos.z-z)/Math.cos(-getHorizontalFov()/2) );

		float y = (float) (Math.sin(-getHorizontalFov()/2)*leftVecLen);

		return basePos.x - y;
	}
	
	float getUpperY(Vec3f basePos, float z) {
		float topVecLen = (float)( (basePos.z-z)/Math.cos(getFOV()/2) );

		float y = (float) (Math.sin(getFOV()/2)*topVecLen);
		
		return basePos.y - y;
	}
	
	float getLowerY(Vec3f basePos, float z) {
		float botVecLen = (float)( (basePos.z-z)/Math.cos(-getFOV()/2) );

		float y = (float) (Math.sin(-getFOV()/2)*botVecLen);

		return basePos.y - y;
	}
}
