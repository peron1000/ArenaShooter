package arenashooter.entities.spatials;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class TextSpatial extends Spatial3 {
	private Text text;
	private Shader shader;
	
//	public float thickness = .3f;
//	public Vec4f color = new Vec4f(1, 1, .5, 1);
	
	public Vec3f scale;

	public TextSpatial(Vec3f position, Vec3f scale, Text text) {
		super(position);
		this.scale = scale.clone();
		this.text = text;
		this.shader = Shader.loadShader("data/shaders/text_distance_field");
		
		setThickness(.3f);
		shader.setUniformV4("baseColor", new Vec4f(1, 1, .5, 1));
	}
	
	public void setThickness(float value) {
		shader.bind();
		shader.setUniformF("thickness", value);
	}
	
	public void setColor(Vec4f value) {
		shader.bind();
		shader.setUniformV4("baseColor", value);
	}
	
	public Shader getShader() { return shader; }
	
	@Override
	public void draw() {
		Profiler.startTimer(Profiler.MESHES);
		
		Model model = text.getModel();
		
		shader.bind();
		shader.setUniformM4("model", Mat4f.transform(pos(), rotation, scale));
		shader.setUniformM4("view", Window.getView());
		shader.setUniformM4("projection", Window.proj);
		
//		shader.setUniformV4("baseColor", color);
		
//		shader.setUniformF("thickness", thickness);

		model.bindToShader(shader);

		glActiveTexture(GL_TEXTURE0);
		text.getFont().tex.bind();
		shader.setUniformI("distanceField", 0);

		model.bind();
		model.draw();
		
		Profiler.endTimer(Profiler.MESHES);
		
		super.draw();
	}
}
