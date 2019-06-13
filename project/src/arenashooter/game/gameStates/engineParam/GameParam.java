package arenashooter.game.gameStates.engineParam;

import java.util.TreeSet;

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
	private static Integer nbRound = Integer.valueOf(1);

	public void setNbRound(Integer nbRound) {
		GameParam.nbRound = nbRound;
	}

	public static int getRound() {
		return nbRound;
	}

	// Team
	private static ParamElement<Boolean> team = new ParamElement<Boolean>("Team", true, false) {

		@Override
		String getStringValue() {
			if (getValue()) {
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

	// Maps
	public static TreeSet<String> maps = new TreeSet<>();
	
	public static String[] mapsString() {
		String[] ret = new String[maps.size()];
		int i =0;
		for (String map : maps) {
			ret[i] = map;
			i++;
		}
		return ret;
	}

}
