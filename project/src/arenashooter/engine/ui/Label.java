package arenashooter.engine.ui;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.game.Main;

public class Label extends UiElement {
	private Shader shader;
	private Text text;
	
	public float thickness = .3f;
	public Vec4f color = new Vec4f(1, 1, 1, 1);
	
	public Label(Menu owner, Vec2f pos, double rot, Vec2f scale, String text) {
		super(owner, pos, rot, scale);
		this.text = new Text(Main.font, Text.TextAlignH.CENTER, text);
		this.shader = Shader.loadShader("data/shaders/ui/ui_text_distance_field");
	}
	
	public void setText(String newText) {
		this.text = new Text(text.getFont(), Text.TextAlignH.CENTER, newText);
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void draw() {
		Model model = text.getModel();
		
		shader.bind();
		shader.setUniformM4("model", Mat4f.transform(pos, rotation, scale));
		shader.setUniformM4("projection", Window.projOrtho);
		
		shader.setUniformV4("baseColor", color);
		
		shader.setUniformF("thickness", thickness);

		model.bindToShader(shader);

		glActiveTexture(GL_TEXTURE0);
		text.getFont().tex.bind();
		shader.setUniformI("distanceField", 0);

		model.bind();
		model.draw();
	}
	
}
