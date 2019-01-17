package arenashooter.engine.graphics.particles;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.ArrayList;

import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.game.Game;
import arenashooter.game.GameMaster;

class EmitterSparks extends Emitter {
	ArrayList<Vec2f> positions;
	ArrayList<Vec2f> velocities;
	ArrayList<Float> lives;
	ArrayList<Float> livesTotal;

	protected EmitterSparks( ParticleSystem owner, Texture texture, float duration, float delay, float rate, 
			float lifetimeMin, float lifetimeMax, 
			Vec4f colorStart, Vec4f colorEnd, 
			float angleMin, float angleMax, 
			float velocityMin, float velocityMax  ) {
		
		super(owner, texture, duration, delay, rate, 
				lifetimeMin, lifetimeMax, 
				colorStart, colorEnd, 
				angleMin, angleMax, 
				velocityMin, velocityMax );
		
		shader = new Shader("data/shaders/particle_simple");
		
		int capacity = (remaining > 0) ? remaining : (int)(rate*lifetimeMax)+1 ;
		positions = new ArrayList<Vec2f>(capacity);
		velocities = new ArrayList<Vec2f>(capacity);
		lives = new ArrayList<Float>(capacity);
		livesTotal = new ArrayList<Float>(capacity);
	}
	
	@Override
	boolean update(double delta) {
		super.update(delta);
		
		//Force = current map gravity
		Vec2f force = Vec2f.multiply(GameMaster.gm.getMap().gravity, 50);
		
		for( int i=positions.size()-1; i>=0; i-- ) {
			if( lives.get(i) > 0 ) {
				velocities.get(i).add(Vec2f.multiply(force, (float)delta));
				positions.get(i).add( Vec2f.multiply(velocities.get(i), (float) delta) );
				lives.set(i, lives.get(i)-(float)delta);
			} else {
				positions.remove(i);
				velocities.remove(i);
				lives.remove(i);
				livesTotal.remove(i);
			}
		}
		
		//Return true if all particles are dead and none are left to spawn
		return remaining == 0 && positions.isEmpty();
	}
	
	@Override
	void spawn(int count) {
		for(int i=0; i<count; i++) {
			positions.add(new Vec2f( owner.position.x, owner.position.y ));
//			velocities.add(new Vec2f( 0,0 ));

			//TODO: temp random velocity
			Vec2f vel = Vec2f.fromAngle( (float) (Math.random()*Math.PI*-.4f) );
			vel.multiply(1000);
			velocities.add( vel );

			float life = (float) ((Math.random()*(lifetimeMax-lifetimeMin))+lifetimeMin);
			lives.add(life);
			livesTotal.add(life);
		}
		
	}

	@Override
	void draw() {
		shader.bind();
		
		//Get matrices
		shader.setUniformM4("view", Game.camera.viewMatrix);
		shader.setUniformM4("projection", Window.proj); //TODO: Get projection matrix properly
		
		model.bindToShader(shader);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		shader.setUniformI("baseColor", 0);
		
		model.bind();
		
		for( int i=0; i<positions.size(); i++ ) {
			float lifetime = lives.get(i)/livesTotal.get(i);
			
			Vec3f pos = new Vec3f( positions.get(i).x, positions.get(i).y, owner.position.z );
			Quat rot = Quat.fromAngle( velocities.get(i).angle() );
			Vec3f scale = new Vec3f( 160*lifetime, 35*lifetime, 1 );
			Mat4f modelM = Mat4f.transform(pos, rot, scale);
			
			shader.setUniformM4("model", modelM);
			
			
			shader.setUniformV4("baseColorMod", Vec4f.lerp(colorEnd, colorStart, lifetime));
			
			model.draw();
		}
	}

}
