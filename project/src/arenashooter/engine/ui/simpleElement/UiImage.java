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
		this(color.x, color.y, color.z, color.w);
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
		this(Material.loadMaterial("data/materials/ui/ui_rectangle.material"));
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
			Exception e = new Exception("Color value opacity given is not valid");
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
		this(texture, new Vec4f(1));
	}

	/**
	 * Create a colored textured rectangle using the ui_image shader
	 * 
	 * @param texture
	 * @param color
	 */
	public UiImage(Texture texture, Vec4f color) {
		this(Material.loadMaterial("data/materials/ui/ui_image.material"));
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
	
	public void setColor(double red , double green , double blue , double transparency) {
		material.setParamVec4f("color", new Vec4f(red, green, blue, transparency));
	}
	
	public void setColor(Vec4f color) {
		material.setParamVec4f("color", color.clone());
	}

	@Override
	public void draw() {
		if (isVisible()) {
			material.setParamMat4f("model", Mat4f.transform(getPosition(), getRotation(), getScale(), modelM));
			material.setParamMat4f("projection", Window.projOrtho);

			if(material.bind(model)) {
				model.bind();
				model.draw();
			}
		}
	}

}
