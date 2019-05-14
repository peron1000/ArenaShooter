package arenashooter.game.gameParam;

public enum GameMode {
	Death_Match , Battle_Royal , King_OfT_heHill , Time , Stock_Match, Capture_The_Flag , Rixe;
	
	public GameMode getNext() {
		int index = this.ordinal();
		index++;
		if(index >= GameMode.values().length) {
			index = 0;
		}
		return GameMode.values()[index];
	}
	
	public GameMode getPrevious() {
		int index = this.ordinal();
		index--;
		if(index < 0) {
			index = GameMode.values().length-1;
		}
		return GameMode.values()[index];
	}
}
