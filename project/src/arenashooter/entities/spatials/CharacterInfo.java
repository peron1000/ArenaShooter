package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.game.CharacterClass;

public class CharacterInfo {
	public CharacterClass charClass = CharacterClass.agile;
	public int skin;
	
	public CharacterInfo() {

	}
	
	public void classNext() {
		switch(charClass.name) {
		case "Bird":
			charClass = CharacterClass.agile;
			break;
		case "Agile":
			charClass = CharacterClass.heavy;
			break;
		default:
			charClass = CharacterClass.bird;
			break;
		}
	}
	
	public void classPrev() {
		switch(charClass.name) {
		case "Bird":
			charClass = CharacterClass.heavy;
			break;
		case "Agile":
			charClass = CharacterClass.bird;
			break;
		default:
			charClass = CharacterClass.agile;
			break;
		}
	}
	
	public void skinNext() {
		skin = (skin+1)%charClass.skins.length;
	}
	
	public void skinPrev() {
		if(skin <= 0) skin = charClass.skins.length-1;
		else skin -= 1;
	}
	
	public Character createNewCharacter(Vec2f spawn) {
		return new Character(spawn, this);
	}
}
