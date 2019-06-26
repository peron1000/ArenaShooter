package arenashooter.engine.ui;

import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public abstract class UiElement implements Navigable, NoStatic {
	public static final double defaultLerp = 5;
	private Map<String, Trigger> actions = new HashMap<>();

	private Vec2f pos = new Vec2f(), scale = new Vec2f(10), toLerpPos = pos.clone(), reScale = scale.clone(),
			toSuperLerpPos = pos.clone();
	private double rotation = 0;
	private double lerp = defaultLerp;
	private boolean visible = true, onSuperLerp = false, scissorOk = true;
	private UiElement delegation = null;

	public UiElement(float xPos, float yPos, float xScale, float yScale, double rot) {
		rotation = rot;
		pos.set(xPos, yPos);
		scale.set(xScale, yScale);
		toLerpPos.set(xPos, yPos);
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

	/**
	 * @return the scissorOk
	 */
	public boolean isScissorOk() {
		return scissorOk;
	}

	/**
	 * Set to false to avoid scissor conflicts
	 * 
	 * @param scissorOk the scissorOk to set
	 */
	public void setScissorOk(boolean scissorOk) {
		this.scissorOk = scissorOk;
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
	
	/**
	 * The actions implemented from {@link Navigable} will be the actions of <i>element</i>. </br>
	 * Put <i>element</i> to <code>null</code> to cancel the delegation
	 * @param element
	 */
	protected void delegationActionsTo(UiElement element) {
		delegation = element;
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
		toLerpPos.set(x, y);
	}

	public void setPositionLerp(double x, double y, double lerp) {
		toLerpPos.set(x, y);
		this.lerp = lerp;
	}

	public void setPositionSuperLerp(double x, double y, double lerp) {
		if (!onSuperLerp) {
			setPositionLerp(x, y, lerp);
			toSuperLerpPos.set(pos);
			onSuperLerp = true;
		}
	}

	public void addToPosition(double x, double y) {
		setPosition(pos.x + x, pos.y + y);
	}

	public void addToPositionLerp(double x, double y, double lerp) {
		setPositionLerp(x + pos.x, y + pos.y, lerp);
	}

	/**
	 * Don't break a setPositionLerp call
	 * 
	 * @param x
	 * @param y
	 * @param lerp
	 */
	public void addToPositionSafely(double x, double y) {
		if (onSuperLerp) {
			toLerpPos.x += x;
			toLerpPos.y += y;
			toSuperLerpPos.x += x;
			toSuperLerpPos.y += y;
		} else if (isOnlerp()) {
			toLerpPos.x += x;
			toLerpPos.y += y;
		} else {
			pos.x += x;
			pos.y += y;
			toLerpPos.set(pos);
			toSuperLerpPos.set(pos);
		}

	}

	public void addToPositionSuperLerp(double x, double y, double lerp) {
		setPositionSuperLerp(x + pos.x, y + pos.y, lerp);
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

	public boolean isOnlerp() {
		return !toLerpPos.equals(pos, 0.05f);
	}

	@Override
	public boolean continueAction() {
		if(delegation != null) {
			return delegation.continueAction();
		}
		return false;
	}

	@Override
	public boolean backAction() {
		if(delegation != null) {
			return delegation.backAction();
		}
		return false;
	}

	@Override
	public boolean downAction() {
		if(delegation != null) {
			return delegation.downAction();
		}
		return false;
	}

	@Override
	public boolean leftAction() {
		if(delegation != null) {
			return delegation.leftAction();
		}
		return false;
	}

	@Override
	public boolean rightAction() {
		if(delegation != null) {
			return delegation.rightAction();
		}
		return false;
	}

	@Override
	public boolean upAction() {
		if(delegation != null) {
			return delegation.upAction();
		}
		return false;
	}

	@Override
	public boolean selectAction() {
		if(delegation != null) {
			return delegation.selectAction();
		}
		return false;
	}

	@Override
	public boolean cancelAction() {
		if(delegation != null) {
			return delegation.cancelAction();
		}
		return false;
	}

	@Override
	public boolean changeAction() {
		if(delegation != null) {
			return delegation.changeAction();
		}
		return false;
	}

	public float getLeft() {
		return getPosition().x - getScale().x / 2;
	}

	public float getRight() {
		return getPosition().x + getScale().x / 2;
	}

	public float getTop() {
		return getPosition().y - getScale().y / 2;
	}

	public float getBottom() {
		return getPosition().y + getScale().y / 2;
	}

	public void update(double delta) {
		scale.set(Vec2f.lerp(scale, reScale, Utils.clampD(delta * 5, 0, 1)));
		pos.set(Vec2f.lerp(pos, toLerpPos, Utils.clampD(delta * lerp, 0, 1)));
		if (onSuperLerp) {
			if (pos.equals(toSuperLerpPos, .05f)) {
				onSuperLerp = false;
			} else if (pos.equals(toLerpPos, .05f)) {
				setPositionLerp(toSuperLerpPos.x, toSuperLerpPos.y, lerp);
			}
		}
	}

}
