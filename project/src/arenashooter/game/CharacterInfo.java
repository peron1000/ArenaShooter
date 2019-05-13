package arenashooter.game;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;

public class CharacterInfo {
	public CharacterClass charClass = CharacterClass.agile;
	public int skin;
	
	public CharacterInfo() {

	}
	
	public void classNext() {
		skin = 0;
		switch(charClass.name) {
		case "Bird":
			charClass = CharacterClass.agile;
			break;
		case "Agile":
			charClass = CharacterClass.heavy;
			break;
		case "Heavy" :
			charClass = CharacterClass.aqua;
			break;
		default:
			charClass = CharacterClass.bird;
			break;
		}
	}
	
	public void classPrev() {
		skin = 0;
		switch(charClass.name) {
		case "Bird":
			charClass = CharacterClass.aqua;
			break;
		case "Aqua":
			charClass = CharacterClass.heavy;
			break;
		case "Heavy":
			charClass = CharacterClass.agile;
			break;
		default:
			charClass = CharacterClass.bird;
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
