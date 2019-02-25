package arenashooter.game.gameStates;

import arenashooter.engine.animation.Test;
import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.entities.Entity;
import arenashooter.entities.Music;
import arenashooter.game.GameMaster;

public class Intro extends GameState {
	public Intro() { }
	
	@Override
	public void init() {
		
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		map = new Test();
	}

	@Override
	public void update(double delta) {
		map.step(delta);
		if(Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {
			Entity bgm = map.children.get("bgm");
			if(bgm instanceof Music) ((Music)bgm).stop();
			
			GameMaster.gm.requestNextState();
		}
	}

}
