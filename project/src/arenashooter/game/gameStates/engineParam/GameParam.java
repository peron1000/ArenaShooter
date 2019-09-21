package arenashooter.game.gameStates.engineParam;

import java.util.Set;
import java.util.TreeSet;

public class GameParam {

	// GameMode
	private ParamElement<GameMode> gameMode = new ParamElement<GameMode>("Game Mode", GameMode.values()) {

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

	public GameMode getGameMode() {
		return gameMode.getValue();
	}

	// Rounds
	private Integer nbRound = Integer.valueOf(1);

	public void setNbRound(Integer nbRound) {
		this.nbRound = nbRound;
	}

	public int getRound() {
		return nbRound;
	}

	// Team
	private ParamElement<Boolean> team = new ParamElement<Boolean>("Team", true, false) {

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

	public boolean getTeam() {
		return team.getValue();
	}

	// Maps
	public Set<String> maps = new TreeSet<>();
	
	public String[] mapsString() {
		return maps.toArray(new String[maps.size()]);
	}

}
