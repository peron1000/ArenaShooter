package arenashooter.engine.ui;

import arenashooter.engine.graphics.MaterialI;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.Main;

public class ColorPicker extends UiElement {

	private static enum selection {LUMISAT, HUE, ALPHA};
	
	private float speed = .05f;
	
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
		
		MaterialI lumiSatMat = Main.getRenderer().loadMaterial("data/materials/ui/ui_gradient_4.material");
		lumiSat = new UiImage(lumiSatMat);
		
		hueImg = new UiImage(Main.getRenderer().loadTexture("data/sprites/interface/hue.png"));
		hueImg.setRotation(-Math.PI/2);
		hueImg.setPosition(6, 0);
		hueImg.setScale(10, 2);
		
		lumiSatSelector = new UiImage( Main.getRenderer().loadTexture("data/sprites/interface/selector_round.png").setFilter(true) );
		lumiSatSelector.setScale(2.5, 2.5);
		
		hueSelector = new UiImage(new Vec4f(1));
		hueSelector.setScale(hueImg.getScale().y+.2, .45);
		
		updateMaterials();
	}
	
	private void updateMaterials() {
		Vec3f.hsvToRgb(hue, saturation, value, rgb);
		
		Vec3f hueColor = new Vec3f();
		Vec3f.hsvToRgb(hue, 1, 1, hueColor);
		
		lumiSat.getMaterial().setParamVec4f("colorA", new Vec4f(1, 1, 1, 1));
		lumiSat.getMaterial().setParamVec4f("colorB", new Vec4f(hueColor.x, hueColor.y, hueColor.z, 1));
		lumiSat.getMaterial().setParamVec4f("colorC", new Vec4f(0, 0, 0, 1));
		lumiSat.getMaterial().setParamVec4f("colorD", new Vec4f(0, 0, 0, 1));
	}
	
	@Override
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		background.setPosition(x, y);
		hueImg.setPosition(x+3.75, y);
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
			value += speed;
			break;
		case HUE:
			hue += speed;
			break;
		case ALPHA:
			alpha += speed;
			break;
		}
		return true;
	}

	@Override
	public boolean downAction() {
		switch(current) {
		case LUMISAT:
			value -= speed;
			break;
		case HUE:
			hue -= speed;
			break;
		case ALPHA:
			alpha -= speed;
			break;
		}
		return true;
	}
	
	@Override
	public boolean leftAction() {
		switch(current) {
		case LUMISAT:
			saturation -= speed;
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
			saturation += speed;
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

			if(current == selection.LUMISAT)
				lumiSatSelector.getMaterial().setParamVec4f("color", new Vec4f(1, 1, .2, 1));
			else
				lumiSatSelector.getMaterial().setParamVec4f("color", new Vec4f(1, 1, 1, .7));
			
			if(current == selection.HUE)
				hueSelector.getMaterial().setParamVec4f("color", new Vec4f(1, 1, .2, 1));
			else
				hueSelector.getMaterial().setParamVec4f("color", new Vec4f(1, 1, 1, .7));
			
			hueSelector.draw();
			lumiSatSelector.draw();
		}
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		hue = Utils.clampF(hue, 0, 1);
		value = Utils.clampF(value, 0, 1); //Remove this clamp?
		saturation = Utils.clampF(saturation, 0, 1);
		alpha = hasAlpha ? Utils.clampF(alpha, 0, 1) : 1;
		
		float selectorX = lumiSat.getPosition().x;
		float selectorY = lumiSat.getPosition().y;
		float halfWidth = lumiSat.getScale().x/2;
		float halfHeight = lumiSat.getScale().y/2;
		selectorX = Utils.lerpF(selectorX-halfWidth, selectorX+halfWidth, saturation);
		selectorY = Utils.lerpF(selectorY+halfHeight, selectorY-halfHeight, value);
		lumiSatSelector.setPosition(selectorX, selectorY);

		selectorY = hueImg.getPosition().y;
		halfHeight = hueImg.getScale().x/2; //hueImg is rotated so we use scale.x
		selectorY = Utils.lerpF(selectorY+halfHeight, selectorY-halfHeight, hue);
		hueSelector.setPosition(hueImg.getPosition().x, selectorY);
		
		updateMaterials();
	}
	
	public Vec3f getColorRGB() {
		updateMaterials();
		return new Vec3f(rgb.x, rgb.y, rgb.z);
	}
	
	public void setColorRGB(Vec3fi rgb) {
		Vec3f hsv = new Vec3f();
		Vec3f.rgbToHsv(rgb.x(), rgb.y(), rgb.z(), hsv);
		hue = hsv.x;
		saturation = hsv.y;
		value = hsv.z;
		updateMaterials();
	}
	
	public Vec4f getColorRGBA() {
		updateMaterials();
		return new Vec4f(rgb.x, rgb.y, rgb.z, alpha);
	}
	
	public void setColorRGBA(Vec4f rgba) {
		alpha = rgba.w;
		Vec3f hsv = new Vec3f();
		Vec3f.rgbToHsv(rgba.x, rgba.y, rgba.z, hsv);
		hue = hsv.x;
		saturation = hsv.y;
		value = hsv.z;
		updateMaterials();
	}

	public void setOnFinish(Trigger t) {
		trigger = t;
	}

	public void launchFinishTrigger() {
		trigger.make();
	}

}
