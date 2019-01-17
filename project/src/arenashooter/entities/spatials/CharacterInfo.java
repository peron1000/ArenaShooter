package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;

public class CharacterInfo {
	public Character createNewCharacter(Vec2f spawn) {
		return new Character(spawn);
	}
}
