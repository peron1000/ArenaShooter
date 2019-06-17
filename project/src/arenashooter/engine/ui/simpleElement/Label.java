package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.graphics.fonts.Text.TextAlignV;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.UiElement;
import arenashooter.game.Main;

public class Label extends UiElement {
	private Material material;
	private Text text;
	private Mat4f modelM = new Mat4f();

	public Label(String text) {
		this(text, Text.TextAlignH.CENTER);
	}

	public Label(String text, Text.TextAlignH alignH) {
		this.text = new Text(Main.font, alignH , TextAlignV.CENTER, text);
		this.material = new Material("data/shaders/ui/ui_text_distance_field");
		material.setParamTex("distanceField", this.text.getFont().getTexture());

		setThickness(.3f);
		setColor(new Vec4f(1));
		setShadowThickness(.001f);
		setShadowColor(new Vec4f(0));
	}

	public Label(String text, double xScale, double yScale) {
		this(text);
		setScale(xScale, yScale);
	}
	
	public Label(String text, double scale) {
		this(text);
		setScale(scale);
	}

	public Label(String text, Text.TextAlignH alignH, double xScale, double yScale) {
		this(text , alignH);
		setScale(xScale, yScale);
	}

	public void setText(String newText) {
		this.text = new Text(text.getFont(), this.text.getAlignH() , this.text.getAlignV(), newText);
	}

	public String getText() {
		return text.getText();
	}

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
	
	public TextAlignH getAlignH() {
		return text.getAlignH();
	}

	public void setAlignH(TextAlignH alignH) {
		text = new Text(text.getFont(), alignH, this.text.getAlignV(), text.getText());
	}
	
	public void setAlignV(TextAlignV alignV) {
		text = new Text(text.getFont(), this.text.getAlignH(), alignV, text.getText());
	}
	
	/**
	 * @return width of the text, influenced by scale.x
	 */
	public float getTextWidth() {
		return (getScale().x*text.getWidth())/text.getHeight();
	}
	
	@Override
	public void draw() {
		if (isVisible()) {
			float ratio = 1/text.getHeight();
			material.model = Mat4f.transform(getPosition(), getRotation(), Vec2f.multiply(getScale(), ratio), modelM);
			material.proj = Window.projOrtho;
			material.bind(text.getModel());

			text.getModel().bind();
			text.getModel().draw();
		}
	}
}
