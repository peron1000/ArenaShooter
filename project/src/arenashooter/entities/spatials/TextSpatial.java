package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
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
	
	public void setText(String str) {
		text = new Text(text.getFont(), text.getAlignH(), str);
	}
	
	public void setAlign(TextAlignH align) {
		text = new Text(text.getFont(), align, text.getText());
	}
	
	public void setFont(Font font) {
		text = new Text(font, text.getAlignH(), text.getText());
	}
	
	@Override
	public void draw() {
		Profiler.startTimer(Profiler.MESHES);
		
		material.setParamTex("distanceField", text.getFont().getTexture());
		material.model = Mat4f.transform(getWorldPos(), getWorldRot(), scale);
		material.view = Window.getView();
		material.proj = Window.proj;
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
