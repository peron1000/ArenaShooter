package arenashooter.game;

public class ControllerAi extends Controller {
	public ControllerAi() {
		super();
	}

	@Override
	public void step(double d) {
		if(getCharacter() == null) return;
		
		getCharacter().movementInputX = 1;
	}
}
