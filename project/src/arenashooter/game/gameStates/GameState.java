package arenashooter.game.gameStates;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.ui.Menu;
import arenashooter.entities.Map;
import arenashooter.game.GameMaster;

public abstract class GameState {
	protected Map map;
	
	protected Menu menu = null;

	public GameState() {
		map = new Map();
	}

	public void init() {
	}

	public void update(double delta) {
		if(Input.actionJustPressed(Device.KEYBOARD, Action.UI_BACK) | Input.actionJustPressed(Device.CONTROLLER01, Action.UI_PAUSE) && (GameMaster.current instanceof Game)) {
			if(menu == null)
				menu = new Menu();
			else
				menu = null;
		}
		if(menu != null)
			menu.update();
	}

	public void draw() {
		map.drawSelfAndChildren();
		
		if(menu != null)
			menu.draw();
		
		Window.endTransparency(); // Make sure to end transparency
	}

	public Map getMap() {
		return map;
	}
}
