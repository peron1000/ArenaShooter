package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import arenashooter.entities.Entity;
import arenashooter.entities.Game;

public class TestGame {
	private static Game game = Game.game;
	
	@Test
	public void testConstructeur() {
		Entity player = game.getMap().children.get("Player 1");
		assertTrue(player != null);
	}
}
