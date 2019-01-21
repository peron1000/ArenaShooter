package arenashooter.entities.spatials;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class Text extends Spatial3 {
	private String text;
	private Model model;
	private Shader shader;
	private Font font;
	
	public Vec4f color = new Vec4f(1, .5, 1, 1);
	
	public Vec3f scale;

	public Text(Vec3f position, Vec3f scale, Font font, String text) {
		super(position);
		this.scale = scale.clone();
		this.text = text;
		this.font = font;
		this.shader = new Shader("data/shaders/text_distance_field");
		this.model = font.genModel(text);
	}

	@Override
	public void draw() {
		Profiler.startTimer(Profiler.MESHES);
		
		shader.bind();
		shader.setUniformM4("model", Mat4f.transform(position, rotation, scale));
		shader.setUniformM4("view", Window.camera.viewMatrix);
		shader.setUniformM4("projection", Window.proj);
		
		shader.setUniformV4("baseColor", color);

		model.bindToShader(shader);

		glActiveTexture(GL_TEXTURE0);
		font.tex.bind();
		shader.setUniformI("distanceField", 0);

		model.bind();
		model.draw();
		
		Profiler.endTimer(Profiler.MESHES);
		
		super.draw();
	}
}
