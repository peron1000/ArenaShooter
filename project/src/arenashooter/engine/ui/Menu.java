package arenashooter.engine.ui;

import java.util.ArrayList;

import arenashooter.engine.math.Vec2f;

public class Menu {
	private ArrayList<UiElement> elems = new ArrayList<>();

	public Menu() {
		elems.add(new Label(new Vec2f(), 0, new Vec2f(200, 200), "PAUSE"));
	}
	
	public void update() {
		
	}
	
	public void draw() {
		for(UiElement elem : elems)
			elem.draw();
	}
}
