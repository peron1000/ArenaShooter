package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class UiImage extends UiSimpleElementNavigable {
	private static Model model;
	private Material material;
	private Mat4f modelM = new Mat4f();

	public UiImage(double rot, Vec2f scale, Texture texture, Vec4f color) {
		super(rot, scale);
		
		if(model == null) model = Model.loadQuad();

		this.material = new Material("data/shaders/ui/ui_image");
		this.material.setParamTex("image", texture);
		this.material.setParamVec4f("color", color.clone());
	}
	
	@Override
	public void draw() {
		material.model = Mat4f.transform(getPos(), rotation, getScale(), modelM);
		material.proj = Window.projOrtho;
		
		material.bind(model);
		
		model.bind();
		model.draw();
	}

}
