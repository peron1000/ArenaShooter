package arenashooter.game;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;

public enum CharacterClass {
	Bird("moineau_01", "canard_01", "canard_02"), 
	Agile ("chat_01", "renard_01", "renard_02", "renard_03", "renard_04", "ecureuil_01"),
	Heavy ("chevre_01", "vache_01"),
	Aqua ("poisson_01", "manchot_01", "manchot_02");
	
	private String[] skins;
	private int indexSkin;

	private CharacterClass (String... skins) {
		this.skins = skins;
		indexSkin = 0;
	}
	
	public void nextSkin () {
		indexSkin = (indexSkin+1)%skins.length;
	}
	
	public void previousSkin () {
		indexSkin--;
		if(indexSkin < 0 ) {
			indexSkin = skins.length-1;
		}
	}
	
	public String getSkin() {
		return skins[indexSkin];
	}
	
	public CharacterClass nextClass() {
		int next = (this.ordinal()+1)%values().length;
		return values()[next];
	}
	
	public CharacterClass previousClass() {
		int next = (this.ordinal()-1)%values().length;
		if(next < 0)next = values().length-1;
		return values()[next];
	}

	public Character createNewCharacter(Vec2f spawn) {
		return new Character(spawn, this);
	}
}
