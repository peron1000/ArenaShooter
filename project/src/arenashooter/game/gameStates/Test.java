package arenashooter.game.gameStates;

import arenashooter.engine.json.JsonTransformer;

public class Test extends GameState {
	public Test() {
		try {
			current = JsonTransformer.jsonImportArena("data/arena/mapXML.arena");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end");
	}
}
