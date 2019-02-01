package arenashooter.engine.input;

/**
 * Represents the state of an action
 */
public enum ActionState {
	/** The action is not pressed */
	RELEASED,
	/** The action has just been pressed */
	JUST_PRESSED, 
	/** The action is currently pressed */
	PRESSED, 
	/** The action has just been released */
	JUST_RELEASED;
}