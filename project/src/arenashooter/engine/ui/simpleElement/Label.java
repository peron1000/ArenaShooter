package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.game.Main;

public class Label extends UiSimpleElementNavigable {
	private Material material;
	private Text text;
	private Mat4f modelM = new Mat4f();

	public Label(double rot, Vec2f scale, String text) {
		this(rot, scale, text, Text.TextAlignH.CENTER);
	}

	public Label(double rot, Vec2f scale, String text, Text.TextAlignH alignH) {
		super(rot, scale);
		this.text = new Text(Main.font, alignH, text);
		this.material = new Material("data/shaders/ui/ui_text_distance_field");

		setThickness(.3f);
		setColor(new Vec4f(1, 1, 1, 1));
		setShadowThickness(.001f);
		setShadowColor(new Vec4f(0, 0, 0, 0));
	}

	public void setText(String newText) {
		this.text = new Text(text.getFont(), this.text.getAlignH(), newText);
	}

	public String getText() { return text.getText(); }

	public void setThickness(float value) {
		material.setParamF("thickness", value);
	}

	public void setColor(Vec4f value) {
		material.setParamVec4f("baseColor", value);
	}

	public void setShadowThickness(float value) {
		material.setParamF("shadowThickness", value);
	}

	public void setShadowColor(Vec4f value) {
		material.setParamVec4f("shadowColor", value);
	}

	@Override
	public void draw() {
		if (isVisible()) {
			material.setParamTex("distanceField", text.getFont().getTexture());
			material.model = Mat4f.transform(Vec2f.subtract(getPosition(), new Vec2f(0, getScale().y * 0.07)), rotation,
					getScale(), modelM);
			material.proj = Window.projOrtho;
			material.bind(text.getModel());

			text.getModel().bind();
			text.getModel().draw();
		}
	}
}
