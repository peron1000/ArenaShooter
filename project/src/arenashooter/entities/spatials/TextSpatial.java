package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class TextSpatial extends Spatial3 {
	private Text text;
	public Material material;
	
	public Vec3f scale;

	public TextSpatial(Vec3f position, Vec3f scale, Text text) {
		super(position);
		this.scale = scale.clone();
		this.text = text;
		this.material = new Material("data/shaders/text_distance_field");
		
		setThickness(.3f);
		setColor(new Vec4f(1, 1, .5, 1));
	}
	
	public void setThickness(float value) {
		material.setParamF("thickness", value);
	}
	
	public void setColor(Vec4f value) {
		material.setParamVec4f("baseColor", value);
	}
	
	@Override
	public void draw() {
		Profiler.startTimer(Profiler.MESHES);
		
		material.setParamTex("distanceField", text.getFont().tex);
		material.model = Mat4f.transform(pos(), rotation, scale);
		material.view = Window.getView();
		material.proj = Window.proj;
		material.bind(text.getModel());
		
		text.getModel().bind();
		text.getModel().draw();
		
		Profiler.endTimer(Profiler.MESHES);
		
		super.draw();
	}
}
