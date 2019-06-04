package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class Rectangle extends UiSimpleElementNavigable {
	private static Model model;
	private Material material;
	private Mat4f modelM = new Mat4f();

	public Rectangle(double rot, Vec2f scale, Vec4f color) {
		super(rot, scale);

		if(model == null) model = Model.loadQuad();

		this.material = new Material("data/shaders/ui/ui_rectangle");
		setColor(color.clone());
	}

	@Override
	public void draw() {
		if(isVisible()) {
			material.model = Mat4f.transform(getPosition(), rotation, getScale(), modelM);
			material.proj = Window.projOrtho;
			
			material.bind(model);
			
			model.bind();
			model.draw();
		}
	}
	
	public void setColor(Vec4f newColor) {
		material.setParamVec4f("color", newColor.clone());
	}

}
