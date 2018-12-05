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
import arenashooter.entities.Game;

class EmitterBasic extends Emitter {
	//Start angle
	float angleMin, angleMax;
	//Gravity
	Vec2f force;
	
	ArrayList<Vec2f> positions;
	ArrayList<Vec2f> velocities;
	ArrayList<Float> life;
	
	protected EmitterBasic( ParticleSystem owner, Texture texture, float duration, float delay, float rate, float lifetimeMin, float lifetimeMax  ) {
		super(owner, texture, duration, delay, rate, lifetimeMin, lifetimeMax );
		shader = new Shader("data/shaders/particle_simple");
		
		int capacity = (remaining > 0) ? remaining : (int)(rate*lifetimeMax)+1 ;
		positions = new ArrayList<Vec2f>(capacity);
		velocities = new ArrayList<Vec2f>(capacity);
		life = new ArrayList<Float>(capacity);
		
		//TODO: temp force
		force = new Vec2f(0, 9.807f*50);
	}

	@Override
	boolean update(double delta) {
		super.update(delta);
		
		for( int i=positions.size()-1; i>=0; i-- ) {
			if( life.get(i) > 0 ) {
				velocities.get(i).add(Vec2f.multiply(force, (float)delta));
				positions.get(i).add( Vec2f.multiply(velocities.get(i), (float) delta) );
				life.set(i, life.get(i)-(float)delta);
			} else {
				positions.remove(i);
				velocities.remove(i);
				life.remove(i);
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
			Vec2f vel = Vec2f.fromAngle( (float) (Math.random()*Math.PI*2) );
			vel.multiply(200);
			velocities.add( vel );
			
			life.add( (float) ((Math.random()*(lifetimeMax-lifetimeMin))+lifetimeMin) );
		}
	}

	@Override
	void draw() {
		shader.bind();
		
		//Get matrices
		shader.setUniformM4("view", Game.game.camera.viewMatrix);
		shader.setUniformM4("projection", Window.proj); //TODO: Get projection matrix properly
		
		model.bindToShader(shader);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		shader.setUniformI("baseColor", 0);
		
		model.bind();
		
		Quat rot = Quat.fromAngle(0);
		Vec3f scale = new Vec3f( 50, 50, 1 );
		for( int i=0; i<positions.size(); i++ ) {
			Vec3f pos = new Vec3f( positions.get(i).x, positions.get(i).y, owner.position.z );
			
			Mat4f modelM = Mat4f.transform(pos, rot, scale);
			
			shader.setUniformM4("model", modelM);
			
			float lifetime = life.get(i)/lifetimeMax;
			shader.setUniformV4("baseColorMod", new Vec4f(1, 1, 1, lifetime));
			
			model.draw();
		}
	}

}
