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

	public UiImage(Menu owner, Vec2f pos, double rot, Vec2f scale, Texture texture, Vec4f color , int layout) {
		super(owner, pos, rot, scale , layout);
		
		if(model == null) model = Model.loadQuad();

		this.material = new Material("data/shaders/ui/ui_image");
		this.material.setParamTex("image", texture);
		this.material.setParamVec4f("color", color.clone());
	}
	
	@Override
	protected void update() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void draw() {
		material.model = Mat4f.transform(pos, rotation, scale);
		material.proj = Window.projOrtho;
		
		material.bind(model);
		
		model.bind();
		model.draw();
	}

}
