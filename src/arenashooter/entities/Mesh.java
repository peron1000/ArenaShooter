package arenashooter.entities;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;

public class Mesh extends Spatial3 {

	private Model[] models;
	private Shader[] shaders;
	private Texture[] textures;
	
	public Vec3f scale;
	
	public Mesh(Vec3f position, String modelPath) {
		this(position, new Quat(), new Vec3f(1), modelPath);
	}
	
	public Mesh(Vec3f position, Quat rotation, String modelPath) {
		this(position, rotation, new Vec3f(1, 1, 1), modelPath);
	}
	
	public Mesh(Vec3f position, Quat rotation, Vec3f scale, String modelPath) {
		super(position);
		
		this.rotation = rotation;
		this.scale = scale.clone();
		
		models = Model.loadModel(modelPath);

		//TODO: Load materials from model file
		Shader shaderBase = new Shader("data/shaders/mesh_simple");
		Texture textureBase = Texture.loadTexture("data/default_texture.png");
		
		shaders = new Shader[models.length];
		textures = new Texture[models.length];
		
		for( int i=0; i<models.length; i++ ) {
			shaders[i] = shaderBase;
			textures[i] = textureBase;
		}
	}
	
	@Override
	public void draw() {
		for( int i=0; i<models.length; i++ ) {
			shaders[i].bind();
			shaders[i].setUniformM4("model", Mat4f.transform(position, rotation, scale));
			shaders[i].setUniformM4("view", Game.game.camera.viewMatrix);
			shaders[i].setUniformM4("projection", Window.proj);
			
			models[i].bindToShader(shaders[i]);
			
			glActiveTexture(GL_TEXTURE0);
			textures[i].bind();
			shaders[i].setUniformI("baseColor", 0);
			
			models[i].bind();
			models[i].draw();
		}
		
		super.draw();
	}
}
