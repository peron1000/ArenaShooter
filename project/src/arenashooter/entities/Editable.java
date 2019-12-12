package arenashooter.entities;

import arenashooter.engine.math.Vec2fi;

public interface Editable {
	/**
	 * @return true if this is the current editor selection
	 */
	public boolean isEditorTarget();
	public void setEditorTarget(boolean editorTarget);
	public void editorAddPosition(Vec2fi position);
	public void editorAddScale(Vec2fi scale);
	public void editorAddRotationX(double angle);
	public void editorAddRotationY(double angle);
	public void editorAddRotationZ(double angle);
	public void editorAddDepth(float depth);
	/**
	 * This is used to draw additional elements such as icons in arena editor
	 */
	public void editorDraw();
}
