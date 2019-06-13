package arenashooter.engine.ui;

import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.UiImage;

public class ColorPicker extends UiElement {

	private static enum selection {LUMISAT, HUE, ALPHA};
	
	private UiImage background = new UiImage(new Vec4f(0, 0, 0, 0.8));
	
	private UiImage lumiSat, hueImg, lumiSatSelector, hueSelector, alphaSelector;
	private Vec3f rgb = new Vec3f();
	private float hue, saturation, value;
	private float alpha = 1;
	private boolean hasAlpha = false;
	
	private selection current = selection.LUMISAT;

	private Trigger trigger = new Trigger() {
		@Override
		public void make() {
			// Nothing
		}
	};

	public ColorPicker(boolean hasAlpha) {
		
		this.hasAlpha = hasAlpha;
		
		Material lumiSatMat = new Material("data/shaders/ui/ui_gradient_4");
		lumiSat = new UiImage(lumiSatMat);
		
		lumiSat.setPosition(-2.5, 0);
		
		hueImg = new UiImage(Texture.loadTexture("data/sprites/interface/hue.png"));
		hueImg.setRotation(Math.PI/2);
		hueImg.setPosition(10, 0);
		hueImg.setScale(10, 2);
		
		updateMaterials();
	}
	
	private void updateMaterials() {
		Vec3f.hsvToRgb(hue, saturation, value, rgb);
		
		lumiSat.getMaterial().setParamVec4f("colorA", new Vec4f(1, 1, 1, alpha));
		lumiSat.getMaterial().setParamVec4f("colorB", new Vec4f(rgb.x, rgb.y, rgb.z, alpha));
		lumiSat.getMaterial().setParamVec4f("colorC", new Vec4f(0, 0, 0, alpha));
		lumiSat.getMaterial().setParamVec4f("colorD", new Vec4f(0, 0, 0, alpha));
	}
	
	@Override
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		background.setPosition(x, y);
		hueImg.setPosition(x+10, y);
		lumiSat.setPosition(x-2.5, y);
	}

	@Override
	public void setScale(double x , double y) {
		super.setScale(x , y);
		background.setScale(x, y);
	}

	@Override
	public void setScaleLerp(double x , double y , double lerp) {
		super.setScaleLerp( x ,  y , lerp);
		background.setScaleLerp( x ,  y, lerp);
	}

	@Override
	public boolean upAction() {
		switch(current) {
		case LUMISAT:
			value += .1;
			break;
		case HUE:
			hue += .1;
			break;
		case ALPHA:
			alpha += .1;
			break;
		}
		return true;
	}

	@Override
	public boolean downAction() {
		switch(current) {
		case LUMISAT:
			value -= .1;
			break;
		case HUE:
			hue -= .1;
			break;
		case ALPHA:
			alpha -= .1;
			break;
		}
		return true;
	}
	
	@Override
	public boolean leftAction() {
		switch(current) {
		case LUMISAT:
			saturation -= .1;
			break;
		case HUE:
			current = selection.LUMISAT;
			break;
		case ALPHA:
			current = selection.HUE;
			break;
		}
		return true;
	}

	@Override
	public boolean rightAction() {
		switch(current) {
		case LUMISAT:
			saturation += .1;
			break;
		case HUE:
			current = hasAlpha ? selection.ALPHA : selection.LUMISAT;
			break;
		case ALPHA:
			current = selection.LUMISAT;
			break;
		}
		return true;
	}

	
	@Override
	public boolean continueAction() {
		trigger.make();
		return true;
	}
	
	@Override
	public boolean changeAction() {
		switch(current) {
		case LUMISAT:
			current = selection.HUE;
			break;
		case HUE:
			current = hasAlpha ? selection.ALPHA : selection.LUMISAT;
			break;
		case ALPHA:
			current = selection.LUMISAT;
			break;
		}
		return super.changeAction();
	}
	
	@Override
	public boolean cancelAction() {
		// TODO Auto-generated method stub
		return super.cancelAction();
	}

	@Override
	public boolean selectAction() {
		return true;
	}

	@Override
	public void draw() {
		if (isVisible()) {
			background.draw();
			lumiSat.draw();
			hueImg.draw();
		}
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		hue = Utils.clampF(hue, 0, 1);
		value = Utils.clampF(value, 0, 1); //Remove this clamp?
		saturation = Utils.clampF(saturation, 0, 1);
		alpha = hasAlpha ? Utils.clampF(alpha, 0, 1) : 1;
		updateMaterials();
	}
	
	public Vec3f getColorRGB() {
		updateMaterials();
		return new Vec3f(rgb.x, rgb.y, rgb.z);
	}
	
	public void setColorRGB(Vec3f color) {
		//TODO: Convert RGB to HSV
		updateMaterials();
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
