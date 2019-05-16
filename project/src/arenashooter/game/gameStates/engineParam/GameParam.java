package arenashooter.game.gameStates.engineParam;

public class GameParam {
	
	// GameMode
	private static ParamElement<GameMode> gameMode = new ParamElement<GameMode>("Game Mode", GameMode.values()) {

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
		return gameMode.getTitle() + " : " + gameMode.getStringValue();
	}

	public static GameMode getGameMode() {
		return gameMode.getValue();
	}
	
	
	// Rounds
	private static ParamElement<Integer> nbRound = new ParamElement<Integer>("Round(s)", 1, 2, 3, 4, 5) {

		@Override
		String getStringValue() {
			return getValue().toString();
		}
	};
	
	public void nextRound() {
		nbRound.next();
	}

	public void previousRound() {
		nbRound.previous();
	}

	public String getStringRound() {
		return nbRound.getTitle() + " : " + nbRound.getStringValue();
	}

	public static int getRound() {
		return nbRound.getValue();
	}
	
	// Team
	private static ParamElement<Boolean> team = new ParamElement<Boolean>("Team" , true , false) {
		
		@Override
		String getStringValue() {
			if(getValue()) {
				return "yes";
			} else {
				return "no";
			}
		}
	};
	
	public void nextTeam() {
		team.next();
	}

	public void previousTeam() {
		team.previous();
	}

	public String getStringTeam() {
		return team.getTitle() + " : " + team.getStringValue();
	}

	public static boolean getTeam() {
		return team.getValue();
	}

}
