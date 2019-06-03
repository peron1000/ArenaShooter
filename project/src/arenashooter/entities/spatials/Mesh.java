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
	
	/** Does this sprite require transparency */
	public boolean useTransparency = false;
	
	private int timeMs = 0;
	
	private boolean isEditorTarget =false;
	
	public Vec3f scale;
	
	public Mesh(Vec3f position, String modelPath) {
		this(position, new Quat(), new Vec3f(1), modelPath);
	}
	
	public Mesh(Vec3f position, Quat rotation, String modelPath) {
		this(position, rotation, new Vec3f(1, 1, 1), modelPath);
	}
	
	public Mesh(Vec3f position, Quat rotation, Vec3f scale, String modelPath) {
		super(position);
		
		this.localRotation = rotation;
		this.scale = scale.clone();
		
		ModelsData data = ModelsData.loadModel(modelPath);
		
		models = data.models;
		materials = data.materials;
	}
	
	private Mesh(Vec3f position, Quat rotation, Vec3f scale, Model[] models, Material[] materials) {
		super(position);
		
		this.localRotation = rotation;
		this.scale = scale.clone();
		
		this.models = models;
		
		this.materials = materials;
	}
	
	public void setEditorTarget(boolean isEditorTarget) {
		this.isEditorTarget = isEditorTarget;
	}
	
	public boolean isEditorTarget() {
		return isEditorTarget;
	}
	
	public static Mesh quad(Vec3f position, Quat rotation, Vec3f scale, Material material) {
		return new Mesh(position, rotation, scale, new Model[] {Model.loadQuad()}, new Material[] {material});
	}
	
	public Material getMaterial(int id) {
		if(id < 0 || id >= materials.length) return null;
		return materials[id];
	}
	
	@Override
	public void step(double d) {
		timeMs += d*1000;
		super.step(d);
	}
	
	@Override
	public boolean drawAsTransparent(){ return useTransparency; }
	
	@Override
	public void draw() {
		Profiler.startTimer(Profiler.MESHES);
		
		for( int i=0; i<models.length; i++ ) {
			
			if(isEditorTarget) {
				materials[i].setParamF("editorFilter", (float) (Math.sin(System.currentTimeMillis()*0.006)+1)/2f);
			} else {
				materials[i].setParamF("editorFilter", 0);
			}
			
			materials[i].model = Mat4f.transform(getWorldPos(), localRotation, scale);
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
