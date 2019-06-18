package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.graphics.fonts.Text.TextAlignV;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class TextSpatial extends Spatial3 {
	private Text text;
	public Material material;

	public Vec3f scale;

	public TextSpatial(Vec3f localPosition, Vec3f scale, Text text) {
		super(localPosition);
		this.scale = scale.clone();
		this.text = text;
		this.material = Material.loadMaterial("data/materials/text_distance_field.xml");

		setThickness(.3f);
		setColor(new Vec4f(1, 1, .5, 1));
	}

	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	public void setThickness(float value) {
		material.setParamF("thickness", value);
	}

	public void setColor(Vec4f value) {
		material.setParamVec4f("baseColor", value);
	}

	public void setText(String str) {
		text = new Text(text.getFont(), text.getAlignH(), text.getAlignV(), str);
	}

	public void setAlignH(TextAlignH alignH) {
		text = new Text(text.getFont(), alignH, text.getAlignV(), text.getText());
	}
	
	public void setAlignV(TextAlignV alignV) {
		text = new Text(text.getFont(), text.getAlignH(), alignV, text.getText());
	}

	public void setFont(Font font) {
		text = new Text(font, text.getAlignH(), text.getAlignV(), text.getText());
	}

	@Override
	public void draw() {
		Profiler.startTimer(Profiler.MESHES);

		material.setParamTex("distanceField", text.getFont().getTexture());
		material.setParamMat4f("model", Mat4f.transform(getWorldPos(), getWorldRot(), scale));
		material.setParamMat4f("view", Window.getView());
		material.setParamMat4f("projection", Window.proj);
		material.bind(text.getModel());

		text.getModel().bind();
		text.getModel().draw();

		Profiler.endTimer(Profiler.MESHES);

		super.draw();
	}

	@Override
	public void editorDraw() {
		if (isEditorTarget()) {
			material.setParamF("editorFilter", (float) (Math.sin(System.currentTimeMillis() * 0.006) + 1) / 2f);
		} else {
			material.setParamF("editorFilter", 0);
		}
		draw();
	}

	@Override
	public void editorAddScale(Vec2f scale) {
		this.scale.x += scale.x;
		this.scale.y += scale.y;
	}

	/**
	 * Create a copy of this TextSpatial (cloned transform, text and material)
	 */
	@Override
	public TextSpatial clone() {
		TextSpatial res = new TextSpatial(localPosition, scale, text.clone());
		res.material = material.clone();
		return res;
	}
}
