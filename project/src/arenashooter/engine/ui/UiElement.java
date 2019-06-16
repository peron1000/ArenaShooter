package arenashooter.engine.ui;

import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public abstract class UiElement implements Navigable, NoStatic {
	public static final double defaultLerp = 5;
	private Map<String, Trigger> actions = new HashMap<>();

	private Vec2f pos = new Vec2f(), scale = new Vec2f(10), rePos = pos.clone(), reScale = scale.clone();
	private double rotation = 0, lerp = defaultLerp;
	private boolean visible = true;

	public UiElement(float xPos, float yPos, float xScale, float yScale, double rot) {
		rotation = rot;
		pos.set(xPos, yPos);
		scale.set(xScale, yScale);
		rePos.set(xPos, yPos);
		reScale.set(xScale, yScale);
	}

	public UiElement() {
		// keep default values
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void addAction(String name, Trigger trigger) {
		actions.put(name, trigger);
	}

	public boolean hasAction(String name) {
		return actions.containsKey(name);
	}

	public void lunchAction(String name) {
		try {
			actions.get(name).make();
		} catch (Exception e) {
			System.err.println("Action " + name + " doesn't exist");
		}
	}

	public void removeAction(String name) {
		actions.remove(name);
	}

	public Vec2f getPosition() {
		return pos;
	}

	public void setPosition(Vec2f position) {
		setPosition(position.x, position.y);
	}

	public void setPosition(double x, double y) {
		pos.set(x, y);
		rePos.set(x, y);
	}

	public void setPositionLerp(double x, double y, double lerp) {
		pos.set(x, y);
		rePos.set(x, y);
		this.lerp = lerp;
	}

	public void addToPosition(double x, double y) {
		setPosition(pos.x + x, pos.y + y);
	}

	public void addToPositionLerp(double x, double y, double lerp) {
		setPositionLerp(x + pos.x, y + pos.y, lerp);
	}

	public Vec2f getScale() {
		return scale;
	}

	public void setScale(Vec2f scale) {
		setScale(scale.x, scale.y);
	}

	public void setScale(double x, double y) {
		scale.set(x, y);
		reScale.set(x, y);
	}

	public void setScale(double square) {
		setScale(square, square);
	}

	public void setScaleLerp(double x, double y, double lerp) {
		reScale.set(x, y);
		this.lerp = lerp;
	}

	public void addToScale(double x, double y) {
		setScale(scale.x + x, scale.y + y);
	}

	public void addToScaleLerp(double x, double y, double lerp) {
		setScaleLerp(x + scale.x, y + scale.y, lerp);
	}

	public void addToScaleLerp(double add, double lerp) {
		setScaleLerp(add + scale.x, add + scale.y, lerp);
	}

	/**
	 * call <i>addToScale(double x , double y)</i>
	 * 
	 * @param square
	 */
	public void addToScale(double square) {
		setScale(square + scale.x, square + scale.y);
	}

	@Override
	public boolean continueAction() {
		return false;
	}

	@Override
	public boolean backAction() {
		return false;
	}

	@Override
	public boolean downAction() {
		return false;
	}

	@Override
	public boolean leftAction() {
		return false;
	}

	@Override
	public boolean rightAction() {
		return false;
	}

	@Override
	public boolean upAction() {
		return false;
	}

	@Override
	public boolean selectAction() {
		return false;
	}

	@Override
	public boolean cancelAction() {
		return false;
	}

	@Override
	public boolean changeAction() {
		return false;
	}

	public void update(double delta) {
		scale.set(Vec2f.lerp(scale, reScale, Utils.clampD(delta * 5, 0, 1)));
		pos.set(Vec2f.lerp(pos, rePos, Utils.clampD(delta * lerp, 0, 1)));
	}

}
