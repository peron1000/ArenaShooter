package arenashooter.game;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

import arenashooter.entities.spatials.Character;

public class ControllerAi extends Controller {
	double punchTime = 0;
	boolean punching = false;
	
	public ControllerAi() {
		super();
	}

	@Override
	public void step(double d) {
		if(getCharacter() == null) return;
		
		getCharacter().movementInputX = 0;
		
		if(punching) {
			punchTime += d;
			if(punchTime >= 1) {
				getCharacter().attackStop();
				punching = false;
			}
		}
		
		
		Controller targetController = Main.getGameMaster().getPlayerControllers().get(0);
		Character target = targetController.getCharacter();
		
		if(target == null) {
			
		} else {
			float xDiff = target.getWorldPos().x() - getCharacter().getWorldPos().x();
			
			getCharacter().aimInput = Vec2f.direction(getCharacter().getWorldPos(), target.getWorldPos());
			
			if( Math.abs(xDiff) > 5 ) getCharacter().jump();
			if( Math.abs(xDiff) > 3 )
				getCharacter().movementInputX = Utils.clampF(xDiff*0.6f, -1, 1);
			else {
				if(!punching) {
					getCharacter().attackStart();
					punchTime = 0;
					punching = true;
				}
			}
		}
	}
}
