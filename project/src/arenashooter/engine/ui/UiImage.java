package arenashooter.engine.ui;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class UiImage extends UiElement {
	private static Model model;
	private Material material;

	public UiImage(double rot, Vec2f scale, Texture texture, Vec4f color) {
		super(rot, scale);
		
		if(model == null) model = Model.loadQuad();

		this.material = new Material("data/shaders/ui/ui_image");
		this.material.setParamTex("image", texture);
		this.material.setParamVec4f("color", color.clone());
	}
	
	@Override
	protected void draw() {
		material.model = Mat4f.transform(getPos(), rotation, getScale());
		material.proj = Window.projOrtho;
		
		material.bind(model);
		
		model.bind();
		model.draw();
	}

}
