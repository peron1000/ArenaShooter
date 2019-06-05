package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public interface Editable {
	public boolean isEditorTarget();
	public void setEditorTarget(boolean editorTarget);
	public void editorAddPosition(Vec2f position);
	public void editorAddScale(Vec2f scale);
	public void editorAddRotation(double angle);
	public void editorDraw();
}
