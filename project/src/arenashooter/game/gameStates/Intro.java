package arenashooter.game.gameStates;

import arenashooter.engine.animation.Test;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.entities.Entity;
import arenashooter.entities.Music;
import arenashooter.game.GameMaster;

public class Intro extends GameState {
	public Intro() { 
		super(1);
	}
	
	@Override
	public void init() {
		super.init();
		current = new Test();
	}

	@Override
	public void update(double delta) {
		current.step(delta);
		if(Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)||Input.actionJustPressed(Device.CONTROLLER01, Action.UI_OK)) {
			Entity bgm = current.getChild("bgm");
			if(bgm instanceof Music) ((Music)bgm).stop();
			
			GameMaster.gm.requestNextState(new Config(), "data/mapXML/menu_empty.xml");
		}
	}

}
