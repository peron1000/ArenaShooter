package arenashooter.engine.ui;

import java.util.ArrayList;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;

public class Menu {
	private ArrayList<UiElement> elems = new ArrayList<>();
	
	private UiElement focus = null;

	public Menu() {
		elems.add(new Label(new Vec2f(), 0, new Vec2f(50, 50), "PAUSE"));
	}
	
	public void update() {
		
	}
	
	public void draw() {
		Window.beginUi();
		for(UiElement elem : elems)
			elem.draw();
		Window.endUi();
	}
}
