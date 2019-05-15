package arenashooter.engine.ui;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class UiImage extends UiElement {
	private Shader shader;
	private static Model model;
	private Texture texture;
	Vec4f color = new Vec4f(0, 0, 0, .8);

	public UiImage(Menu owner, Vec2f pos, double rot, Vec2f scale, Texture texture, Vec4f color) {
		super(owner, pos, rot, scale);

		this.texture = texture;
		this.color = color.clone();
		
		shader = Shader.loadShader("data/shaders/ui/ui_rectangle");
		if(model == null) model = Model.loadQuad();
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void draw() {
		shader.bind();
		
		//Create matrices
		shader.setUniformM4("model", Mat4f.transform(pos, rotation, scale));
		shader.setUniformM4("projection", Window.projOrtho);
		
		model.bindToShader(shader);
		
		//Texture
		texture.bind();
		shader.setUniformI("image", 0);
		
		//Color change
		shader.setUniformV4("color", color);
		
		model.bind();
		model.draw();
	}

}
