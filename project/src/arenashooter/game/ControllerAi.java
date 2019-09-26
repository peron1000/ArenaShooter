package arenashooter.game;

import arenashooter.engine.math.Utils;

public class ControllerAi extends Controller {
	double punchTime = 0;
	boolean punching = false;
	
	public ControllerAi() {
		super();
	}

	@Override
	public void step(double d) {
		if(getCharacter() == null) return;
		
		if(punching) {
			punchTime += d;
			if(punchTime >= 1) {
				getCharacter().attackStop();
				punching = false;
			}
		}
		
		
		Controller target = Main.getGameMaster().getPlayerControllers().get(0);
		
		if(target == null) {
			
		} else {
			float xDiff = target.getCharacter().getWorldPos().x() - getCharacter().getWorldPos().x();
			
			if( Math.abs(xDiff) > 2 )
				getCharacter().movementInputX = Utils.clampF(xDiff*0.8f, -1, 1);
			else {
				if(!punching) {
					getCharacter().attackStart(true);
					punchTime = 0;
					punching = true;
				}
			}
		}
		
		getCharacter().jump();
	}
}
