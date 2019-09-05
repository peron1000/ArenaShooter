package arenashooter.engine.graphics.particles;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.particles.modules.ParticleModule;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class Emitter {
	static final protected Model model = Model.loadQuad();
	
	/** Remaining time before starting this emitter */
	private float delay;
	
	/** Remaining number of particles to spawn */
	int remaining;

	private Shader shader;

	/** World space positions */
	public List<Vec2f> positions = new ArrayList<>();
	/** World space rotations */
	public List<Float> rotations = new ArrayList<>();
	/** World space scales */
	public List<Vec2f> scales = new ArrayList<>();
	public List<Vec2f> velocities = new ArrayList<>();
	public List<Vec4f> colors = new ArrayList<>();
	/** Particles lives, starts at 0, ends at livesTotal */
	public List<Float> lives = new ArrayList<>();
	public List<Float> livesTotal = new ArrayList<>();
	
	public final ParticleSystem owner;
	
	private final EmitterTemplate data;
	
	/** Particles to create */
	private int particlesToSpawn = 0;
	
	public Emitter(ParticleSystem owner, EmitterTemplate data) {
		this.owner = owner;

		this.data = data;
		
		delay = data.delay;
		
		if( data.duration == 0 ) //Burst
			remaining = (int) data.rate;
		else if( data.duration < 0 ) //Infinite
			remaining = -1;
		else
			remaining = (int) Math.max(1, data.rate*data.duration);
		
		shader = Shader.loadShader("data/shaders/particle_simple.vert", "data/shaders/particle_simple.frag");
	}
	
	/**
	 * Update this emitter
	 * @param delta
	 * @param gravity
	 * @param worldRotation
	 * @return true if emitter ended
	 */
	boolean update(double delta, Vec2f gravity) {
		// Delay
		if( delay > 0 ) {
			delay -= delta;
			return false;
		}
		
		if( remaining == -1 ) { //Inifinite
			//Add particle count, depending on spawn rate
			particlesToSpawn += data.rate*delta;
			
			//Floor the value to get the number of full particles
			int newParticles = (int)Math.floor(particlesToSpawn);
			
			//Spawn the particles
			spawn(newParticles);
			
			// Update counters
			particlesToSpawn -= newParticles;
			
		} else if( remaining > 0 ) //Need to spawn more particles
			if( data.duration == 0 ) { //Burst
				spawn(remaining);
				remaining = 0;
			} else { //Spawn over time
				//Add particle count, depending on spawn rate
				particlesToSpawn += data.rate*delta;
				
				//Floor the value to get the number of full particles
				int newParticles = (int)Math.floor(particlesToSpawn);
				
				//Make sure we don't spawn more particles than needed
				newParticles = Math.min( newParticles, remaining );
				
				//Spawn the particles
				spawn(newParticles);
				
				//Update counters
				remaining -= newParticles;
			}
		
		// Update modules
		if(positions.size() > 0)
			for(ParticleModule module : data.modules)
				module.apply(this, delta);
		
		// Update particles and delete dead ones
		Vec2f temp = new Vec2f();
		for( int i=positions.size()-1; i>=0; i-- ) {
			if( lives.get(i) < livesTotal.get(i) ) {
				Vec2f.multiply(velocities.get(i), delta, temp);
				positions.get(i).add(temp);
				lives.set(i, lives.get(i)+(float)delta);
			} else {
				positions.remove(i);
				rotations.remove(i);
				scales.remove(i);
				velocities.remove(i);
				colors.remove(i);
				lives.remove(i);
				livesTotal.remove(i);
			}
		}
		
		// Return true if all particles are dead and none are left to spawn
		return remaining == 0 && positions.isEmpty();
	}
	
	private void spawn(int n) {
		for(int i=0; i<n; i++) {
			positions.add(new Vec2f(owner.position.x, owner.position.y));
			rotations.add(Utils.lerpF(data.initialRotMin, data.initialRotMax, Math.random()));
			scales.add(new Vec2f(1));
			velocities.add(new Vec2f());
			colors.add(new Vec4f(1));
			lives.add(0f);
			livesTotal.add(Utils.lerpF(data.lifetimeMin, data.lifetimeMax, Math.random()));
		}
		particlesToSpawn -= n;
	}

	void draw() {
		shader.bind();
		
		//Get matrices
		shader.setUniformM4("view", Window.getView());
		shader.setUniformM4("projection", Window.getProj());
		
		model.bindToShader(shader);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		data.tex.bind();
		shader.setUniformI("baseColor", 0);
		
		model.bind();
		
		Vec3f pos = new Vec3f(0, 0, owner.position.z);
		Quat rot = new Quat();
		Vec3f scale = new Vec3f(1);
		Mat4f modelM = new Mat4f();
		
		for( int i=0; i<positions.size(); i++ ) {
			pos.x = positions.get(i).x;
			pos.y = positions.get(i).y;
			Quat.fromAngle(rotations.get(i), rot);
			scale.x = scales.get(i).y;
			scale.y = scales.get(i).y;
//			Mat4f.transform(pos, rot, scale, modelM);
			modelM = Mat4f.transform(pos, rot, scale); //TODO: replace this to avoid creating a new matrix
			
			shader.setUniformM4("model", modelM);
			
			shader.setUniformV4("baseColorMod", colors.get(i));
			
			model.draw();
		}
	}
}
