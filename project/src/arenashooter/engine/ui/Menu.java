package arenashooter.engine.ui;

import java.util.ArrayList;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class Menu {
	private ArrayList<UiElement> elems = new ArrayList<>();
	
	private UiElement focus = null;

	public Menu() {
		elems.add(new Rectangle(this, new Vec2f(), 0, new Vec2f(45, 60), new Vec4f(0, 0, 0, .25)));
		elems.add(new Label(this, new Vec2f(0, -30), 0, new Vec2f(50, 50), "PAUSE"));
	
		elems.add(new Button(this, new Vec2f(0, -15), 0, new Vec2f(28, 8), "Reprendre"));
				
		elems.add(new Button(this, new Vec2f(0, -5), 0, new Vec2f(28, 8), "Score"));
	
		elems.add(new Button(this, new Vec2f(0, 5), 0, new Vec2f(28, 8), "Options"));

		elems.add(new Button(this, new Vec2f(0, 15), 0, new Vec2f(35, 8), "Quit : Alt+f4"));
	
	}
	
	public void update() {
		for(UiElement elem : elems)
			elem.update();
	}
	
	public void draw() {
		Window.beginUi();
		for(UiElement elem : elems)
			elem.draw();
		Window.endUi();
	}
}
