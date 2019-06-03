package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public interface Editable {
	public boolean isEditorTarget();
	public void setEditorTarget(boolean editorTarget);
	public void addPosition(Vec2f position);
	public void addScale(Vec2f extent);
	public void addRotation(double angle);
}
