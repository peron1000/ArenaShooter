package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;

public class CharacterInfo {
	public String spriteFolder;
	
	private static final String[] skins = { "chat_01", "chevre_01", "moineau_01", "canard_01", "vache_01" };
	private static int currentSkin = (int)Math.floor(Math.random()*(skins.length-1));
	
	public CharacterInfo() {
		//TODO: Remove automatic sprite chooser
		spriteFolder = "data/sprites/characters/" + skins[currentSkin];
		if (currentSkin >= 4)
			currentSkin = 0;
		else
			currentSkin++;
	}
	
	public void tempSpriteNext() { //TODO: Remove this
		spriteFolder = "data/sprites/characters/" + skins[currentSkin];
		if (currentSkin >= 4)
			currentSkin = 0;
		else
			currentSkin++;
	}
	
	public Character createNewCharacter(Vec2f spawn) {
		return new Character(spawn, this);
	}
}
