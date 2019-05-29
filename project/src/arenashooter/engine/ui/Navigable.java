package arenashooter.engine.ui;

public interface Navigable {
	public void upAction();

	public void downAction();

	public void rightAction();

	public void leftAction();
	
	public void selectAction();
	
	public boolean isSelected();
	
	public void unSelec();
	
	public void update(double delta);
	
	public void draw();
}
