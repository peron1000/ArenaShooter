package arenashooter.engine.ui;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.game.Main;

public class Label extends UiElement {
	private Material material;
	private Text text;
	
	public Label(Menu owner, Vec2f pos, double rot, Vec2f scale, String text) {
		super(owner, pos, rot, scale);
		this.text = new Text(Main.font, Text.TextAlignH.CENTER, text);
		this.material = new Material("data/shaders/ui/ui_text_distance_field");
		
		setThickness(.3f);
		setColor(new Vec4f(1, 1, 1, 1));
	}
	
	public void setText(String newText) {
		this.text = new Text(text.getFont(), Text.TextAlignH.CENTER, newText);
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
	}
	
	public void setThickness(float value) {
		material.setParamF("thickness", value);
	}
	
	public void setColor(Vec4f value) {
		material.setParamVec4f("baseColor", value);
	}

	@Override
	protected void draw() {
		material.setParamTex("distanceField", text.getFont().getTexture());
		material.model = Mat4f.transform(pos, rotation, scale);
		material.proj = Window.projOrtho;
		material.bind(text.getModel());
		
		text.getModel().bind();
		text.getModel().draw();
	}
	
}
