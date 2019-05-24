package arenashooter.engine.ui;

public interface Actionable extends Navigable {
	void setOnArm(Trigger t);
	void arm();
}
