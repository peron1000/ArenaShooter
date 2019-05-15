package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class MenuPause extends Menu {
	public MenuPause() {
		new Rectangle(this, new Vec2f(), 0, new Vec2f(45, 60), new Vec4f(0, 0, 0, .25));
		new Label(this, new Vec2f(0, -30), 0, new Vec2f(50, 50), "PAUSE");
	
		new Button(this, new Vec2f(0, -15), 0, new Vec2f(28, 8), "Reprendre");
				
		new Button(this, new Vec2f(0, -5), 0, new Vec2f(28, 8), "Score");
	
		new Button(this, new Vec2f(0, 5), 0, new Vec2f(28, 8), "Options");

		new Button(this, new Vec2f(0, 15), 0, new Vec2f(35, 8), "Quit : Alt+f4");
	}
}
