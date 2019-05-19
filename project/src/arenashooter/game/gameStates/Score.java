package arenashooter.game.gameStates;

import java.util.ArrayList;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.UiImage;
import arenashooter.entities.Controller;
import arenashooter.game.GameMaster;

public class Score extends GameState {
	
	@Override
	public void init(){
		menu = new Menu(10);
		
		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Score.png");
		texture1.setFilter(false);

		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
		texture2.setFilter(false);
		
		new UiImage(menu, new Vec2f(), 0, new Vec2f(177.78, 100), texture1, new Vec4f(1, 1, 1, 1) , 0);
		
		super.init();
	}
	
	@Override
	public void update(double d) {
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {
			GameMaster.gm.requestNextState(new CharacterChooser(), GameMaster.mapEmpty);
		}
		super.update(d);
	}
}