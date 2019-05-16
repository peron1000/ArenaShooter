package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.ModelsData;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;

public class Mesh extends Spatial3 {

	private Model[] models;
	private Material[] materials;
	
	private int timeMs = 0;
	
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
		materials = data.materials;
	}
	
	private Mesh(Vec3f position, Quat rotation, Vec3f scale, Model[] models, Material[] materials) {
		super(position);
		
		this.rotation = rotation;
		this.scale = scale.clone();
		
		this.models = models;
		
		this.materials = materials;
	}
	
	public static Mesh quad(Vec3f position, Quat rotation, Vec3f scale, Material material) {
		return new Mesh(position, rotation, scale, new Model[] {Model.loadQuad()}, new Material[] {material});
	}
	
	@Override
	public void step(double d) {
		timeMs += d*1000;
		
		super.step(d);
	}
	
	@Override
	public void draw() {
		Profiler.startTimer(Profiler.MESHES);
		
		for( int i=0; i<models.length; i++ ) {
			materials[i].model = Mat4f.transform(pos(), rotation, scale);
			materials[i].view = Window.getView();
			materials[i].proj = Window.proj;

			materials[i].setParamI("time", timeMs);

			materials[i].bind(models[i]);
			
			models[i].bind();
			models[i].draw();
		}
		
		Profiler.endTimer(Profiler.MESHES);
		
		super.draw();
	}
}
