package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.UiElement;

public class UiImage extends UiElement {
	private static Model model;
	private Material material;
	private Mat4f modelM = new Mat4f();
	
	public static final UiImage selector = new UiImage(Texture.loadTexture("data/sprites/interface/Selector.png"));
	
	static {
		selector.setScale(45, 10);
	}

	/**
	 * Create a colored rectangle using the ui_rectangle shader
	 * 
	 * @param color
	 */
	public UiImage(Vec4f color) {
		this(new Material("data/shaders/ui/ui_rectangle"));
		material.setParamVec4f("color", color.clone());
	}

	/**
	 * Create a colored rectangle using the ui_rectangle shader
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @param transparency
	 */
	public UiImage(double red, double green, double blue, double transparency) {
		this(new Material("data/shaders/ui/ui_rectangle"));
		if (red > 1 || red < 0) {
			Exception e = new Exception("Color value red given is not valid");
			e.printStackTrace();
		}
		if (green > 1 || green < 0) {
			Exception e = new Exception("Color value green given is not valid");
			e.printStackTrace();
		}
		if (blue > 1 || blue < 0) {
			Exception e = new Exception("Color value blue given is not valid");
			e.printStackTrace();
		}
		if (transparency > 1 || transparency < 0) {
			Exception e = new Exception("Color value transparency given is not valid");
			e.printStackTrace();
		}
		material.setParamVec4f("color", new Vec4f(red, green, blue, transparency));
	}

	/**
	 * Create a textured rectangle using the ui_image shader
	 * 
	 * @param texture
	 */
	public UiImage(Texture texture) {
		this(new Material("data/shaders/ui/ui_image"));
		texture.setFilter(false);
		material.setParamTex("image", texture);
	}

	/**
	 * Create a colored textured rectangle using the ui_image shader
	 * 
	 * @param texture
	 * @param color
	 */
	public UiImage(Texture texture, Vec4f color) {
		this(new Material("data/shaders/ui/ui_image"));
		texture.setFilter(false);
		material.setParamTex("image", texture);
		material.setParamVec4f("color", color.clone());
	}

	public UiImage(Material material) {
		if (model == null)
			model = Model.loadQuad();

		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	@Override
	public void draw() {
		if (isVisible()) {
			material.model = Mat4f.transform(getPosition(), getRotation(), getScale(), modelM);
			material.proj = Window.projOrtho;

			material.bind(model);

			model.bind();
			model.draw();
		}
	}

}
