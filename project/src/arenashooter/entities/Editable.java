package arenashooter.entities;

import arenashooter.engine.math.Vec2fi;

public interface Editable {
	public boolean isEditorTarget();
	public void setEditorTarget(boolean editorTarget);
	public void editorAddPosition(Vec2fi position);
	public void editorAddScale(Vec2fi scale);
	public void editorAddRotationX(double angle);
	public void editorAddRotationY(double angle);
	public void editorAddRotationZ(double angle);
	public void editorAddDepth(float depth);
	public void editorDraw();
}
