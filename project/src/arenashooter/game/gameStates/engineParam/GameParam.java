package arenashooter.game.gameStates.engineParam;

public class GameParam {
	private static ParamElement<GameMode> gameMode = new ParamElement<GameMode>("Game Mode" , GameMode.values()) {
		
		@Override
		String getStringValue() {
			return getValue().name();
		}
	};
	
	public void nextGameMode() {
		gameMode.next();
	}
	
	public void previousGameMode() {
		gameMode.previous();
	}
	
	public String getStringGameMode() {
		return gameMode.getTitle()+" : "+gameMode.getStringValue();
	}
	
	public static GameMode getGameMode() {
		return gameMode.getValue();
	}
	
}
