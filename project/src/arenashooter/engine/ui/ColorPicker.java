package arenashooter.engine.ui;

import java.util.function.Consumer;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.engine.ui.simpleElement.UiSimpleElementNavigable;

public class ColorPicker extends UiElement {

	private static final Vec2f defaultSize = new Vec2f(15, 10);
	private UiImage background = new UiImage(0, defaultSize.clone(), new Vec4f(0.5, 0.5, 0.5, 0.3));
	
	private UiImage lumiSat, hueImg;
	private Vec3f rgb = new Vec3f();
	private float hue, saturation, value;
	private float alpha = 1;
	private boolean hasAlpha = false;

	private Trigger trigger = new Trigger() {

		@Override
		public void make() {
			// Nothing
		}
	};

	public ColorPicker(boolean hasAlpha) {
		super(0, defaultSize.clone());
		background.setPosition(new Vec2f());
		
		this.hasAlpha = hasAlpha;
		
		Material lumiSatMat = new Material("data/shaders/ui/ui_gradient_4");
		lumiSat = new UiImage(0, new Vec2f(), lumiSatMat);
		
		lumiSat.setPosition(new Vec2f(-2.5, 0));
		
		hueImg = new UiImage(Math.PI/4, new Vec2f(10, 2), Texture.loadTexture("data/sprites/interface/hue.png"));
		
		updateMaterials();
	}
	
	private void updateMaterials() {
		Vec3f.hsvToRgb(hue, saturation, value, rgb);
		
		lumiSat.getMaterial().setParamVec4f("colorA", new Vec4f(1, 1, 1, alpha));
		lumiSat.getMaterial().setParamVec4f("colorB", new Vec4f(rgb.x, rgb.y, rgb.z, alpha));
		lumiSat.getMaterial().setParamVec4f("colorC", new Vec4f(0, 0, 0, alpha));
		lumiSat.getMaterial().setParamVec4f("colorD", new Vec4f(0, 0, 0, alpha));
	}
	
	protected void actionOnAll(Consumer<UiSimpleElementNavigable> c) {
//		c.accept(input);
//		c.accept(background);
//		for (Label label : word) {
//			c.accept(label);
//		}
	}

	@Override
	public void setPosition(Vec2f pos) {
		Vec2f dif = Vec2f.subtract(pos, getPosition());
		super.setPosition(pos);
		actionOnAll(e -> e.setPosition(Vec2f.add(e.getPosition(), dif)));
	}

	@Override
	public void setPositionLerp(Vec2f pos, double lerp) {
		Vec2f dif = Vec2f.subtract(pos, getPosition());
		super.setPositionLerp(pos, lerp);
		actionOnAll(e -> e.setPositionLerp(Vec2f.add(e.getPosition(), dif), 10));
	}

	@Override
	public void setScale(Vec2f scale) {
		super.setScale(scale);
		Vec2f bgScale = scale.clone();
		background.setScale(bgScale);
	}

	@Override
	public void setScaleLerp(Vec2f scale) {
		super.setScaleLerp(scale);
		Vec2f bgScale = scale.clone();
		background.setScaleLerp(bgScale);
	}

	@Override
	public void setVisible(boolean visible) {
		actionOnAll(e -> e.setVisible(visible));
		super.setVisible(visible);
	}

	@Override
	public boolean upAction() {
		return true;
	}

	@Override
	public boolean downAction() {
		return true;
	}
	
	@Override
	public boolean continueAction() {
		trigger.make();
		return true;
	}

	@Override
	public boolean rightAction() {
		return true;
	}

	@Override
	public boolean leftAction() {
		return true;
	}

	@Override
	public boolean selectAction() {
		return true;
	}

	@Override
	public boolean isSelected() {
		return true;
	}

	@Override
	public void unSelec() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		if (visible) {
			background.draw();
			lumiSat.draw();
			hueImg.draw();
		}
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		actionOnAll(e -> e.update(delta));
	}
	
	public Vec4f getColorRGBA() {
		updateMaterials();
		return new Vec4f(rgb.x, rgb.y, rgb.z, alpha);
	}
	
	public void setColorRGBA(Vec4f color) {
		alpha = color.w;
		//TODO: Convert RGB to HSV
		updateMaterials();
	}

	public void setOnFinish(Trigger t) {
		trigger = t;
	}

	public void launchFinishTrigger() {
		trigger.make();
	}

}
