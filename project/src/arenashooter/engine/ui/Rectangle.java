package arenashooter.engine.ui;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class Rectangle extends UiElement {
	private static Model model;
	private Material material;

	public Rectangle(Menu owner, Vec2f pos, double rot, Vec2f scale, Vec4f color) {
		super(owner, pos, rot, scale);

		if(model == null) model = Model.loadQuad();

		this.material = new Material("data/shaders/ui/ui_rectangle");
		setColor(color.clone());
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
	
	public void setColor(Vec4f newColor) {
		material.setParamVec4f("color", newColor.clone());
	}

}
