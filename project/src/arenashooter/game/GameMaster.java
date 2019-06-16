package arenashooter.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import arenashooter.engine.Profiler;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.ActionV2;
import arenashooter.engine.ui.Trigger;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.Intro;
import arenashooter.game.gameStates.Loading;
import arenashooter.game.gameStates.Start;

public class GameMaster {

	public static List<Controller> controllers = new ArrayList<>();

	private static Stack<GameState> stateStack = new Stack<>();
	private static GameState current = new Start();

	public static final GameMaster gm = new GameMaster();

	public static final String mapEmpty = "data/mapXML/menu_empty.xml";
	public static final String mapCharChooser = "data/mapXML/menu_character_chooser.xml";
	
	private static Loading loading = Loading.loading;
	
	private InputListener inputs = new InputListener();

	private GameMaster() {
		inputs.actions.add(new EventListener<InputActionEvent>() {
			@Override
			public void launch(InputActionEvent event) {
				if(event.getAction() == ActionV2.DEBUG_SHOW_COLLISION && event.getActionState() == ActionState.JUST_PRESSED)
					Main.drawCollisions = !Main.drawCollisions;
				if(event.getAction() == ActionV2.DEBUG_SKIP_TRANSPARENCY && event.getActionState() == ActionState.JUST_PRESSED)
					Main.skipTransparency = !Main.skipTransparency;
				if(event.getAction() == ActionV2.DEBUG_TOGGLE_PROFILER && event.getActionState() == ActionState.JUST_PRESSED)
					Profiler.toggle();
			}
		});
	}
	
	public static GameState getCurrent() {
		return current;
	}

	public void requestNextState(GameState nextState, String... nextStateMap) {
		stateStack.push(current);
		loading.init();
		
		current = loading;
		loading.setOnFinish(new Trigger() {
			
			@Override
			public void make() {
				current = nextState;
				current.init();
			}
		});
		
		loading.setNextState(nextState, nextStateMap);

//		if (current == Loading.loading) { // Loading
//			current = Loading.loading.getNextState();
//		} else {
//			if (current instanceof Start) { // Start
//			} else if (current instanceof Intro) { // Intro movie
//			} else if (current instanceof CharacterChooser) { // Character chooser
//			} else if(current instanceof Config) {
//			} else if(current instanceof Game) {
//			} else if(current instanceof Score) {
//				goBackTo((Class<GameState>) nextState.getClass());
//			}
//			stateStack.push(current);
//			current = Loading.loading;
//			current.init();
//			Loading.loading.setNextState(nextState, nextStateMap);
//		}
	}

	public void requestPreviousState() {
		 current = stateStack.pop();
		 if(current instanceof Intro) {
			 current = new Start();
		 }
	}

	private void goBackTo(Class<GameState> stateClass) {
		boolean boo = false;
		for (GameState gameState : stateStack) {
			if (gameState.getClass().equals(stateClass)) {
				boo = true;
			}
		}
		if(boo)
			while(!(current.getClass().equals(stateClass))){
				System.out.println(current.getClass().getName());
				requestPreviousState();
			}
		requestPreviousState();
	}

	public void draw() {
		if (current.getMap() != null)
			current.draw();
	}

	public void update(double delta) {
		inputs.step(delta);
		current.update(delta);
	}
	
	public List<ControllerPlayer> getPlayerControllers() {
		List<ControllerPlayer> res = new ArrayList<>();
		for(Controller c : controllers) {
			if(c instanceof ControllerPlayer)
				res.add((ControllerPlayer)c);
		}
		return res;
	}
}
