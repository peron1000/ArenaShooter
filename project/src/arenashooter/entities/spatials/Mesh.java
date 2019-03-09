package arenashooter.entities.spatials;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.ModelsData;
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
		
		ModelsData data = ModelsData.loadModel(modelPath);
		
		models = data.models;
		textures = data.textures;
		
		//TODO: Load materials from model file
		Shader shaderBase = Shader.loadShader("data/shaders/mesh_simple");
		shaders = new Shader[models.length];
		for( int i=0; i<models.length; i++ )
			shaders[i] = shaderBase;
	}
	
	private Mesh(Vec3f position, Quat rotation, Vec3f scale, Model[] models, Shader[] shaders, Texture[] textures) {
		super(position);
		
		this.rotation = rotation;
		this.scale = scale.clone();
		
		this.models = models;
		
		this.shaders = shaders;
		this.textures = textures;
	}
	
	public static Mesh quad(Vec3f position, Quat rotation, Vec3f scale, Shader shader, Texture texture) {
		return new Mesh(position, rotation, scale, new Model[] {Model.loadQuad()}, new Shader[] {shader}, new Texture[] {texture});
	}
	
	@Override
	public void draw() {
		Profiler.startTimer(Profiler.MESHES);
		
		for( int i=0; i<models.length; i++ ) {
			shaders[i].bind();
			shaders[i].setUniformM4("model", Mat4f.transform(pos(), rotation, scale));
			shaders[i].setUniformM4("view", Window.getView());
			shaders[i].setUniformM4("projection", Window.proj);
			
			models[i].bindToShader(shaders[i]);
			
			glActiveTexture(GL_TEXTURE0);
			textures[i].bind();
			shaders[i].setUniformI("baseColor", 0);
			
			models[i].bind();
			models[i].draw();
		}
		
		Profiler.endTimer(Profiler.MESHES);
		
		super.draw();
	}
}
