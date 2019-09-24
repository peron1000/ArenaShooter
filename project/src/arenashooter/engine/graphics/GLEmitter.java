package arenashooter.engine.graphics;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.graphics.particles.EmitterTemplate;
import arenashooter.engine.graphics.particles.ParticleSystem;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.game.Main;

public class GLEmitter extends Emitter {
	
	private GLShader shader;
	
	private Vec3f pos = new Vec3f();
	private Quat rot = new Quat();
	private Vec3f scale = new Vec3f(1);
	private Mat4f modelM = new Mat4f();
	
	public GLEmitter(ParticleSystem owner, EmitterTemplate data) {
		super(owner, data);
		
		shader = GLShader.loadShader("data/shaders/particle_simple.vert", "data/shaders/particle_simple.frag");
	}

	@Override
	public void draw() {
		shader.bind();
		
		//Get matrices
		shader.setUniformM4("view", Main.getRenderer().getView());
		shader.setUniformM4("projection", Main.getRenderer().getProj());
		
		model.bindToShader(shader);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		data.getTexture().bind();
		shader.setUniformI("baseColor", 0);
		
		model.bind();
		
		pos.z = owner.position.z;
		
		for( int i=0; i<positions.size(); i++ ) {
			pos.x = positions.get(i).x;
			pos.y = positions.get(i).y;
			Quat.fromAngle(rotations.get(i), rot);
			scale.x = scales.get(i).y;
			scale.y = scales.get(i).y;
			
			shader.setUniformM4( "model", Mat4f.transform(pos, rot, scale, modelM) );
			
			shader.setUniformV4("baseColorMod", colors.get(i));
			
			model.draw();
		}
	}
}
