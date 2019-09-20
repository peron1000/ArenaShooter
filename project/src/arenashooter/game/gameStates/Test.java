package arenashooter.game.gameStates;

import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.graphics.fonts.Text.TextAlignV;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Main;

public class Test extends GameState {
	
	
	private TextSpatial ts;

	public Test() {
		super("data/arena/menu_intro.arena");
		Text text = new Text(Main.font, TextAlignH.CENTER, TextAlignV.CENTER, "nb children: ");
		ts = new TextSpatial(new Vec3f(), new Vec3f(3), text );
		ts.attachToParent(current, "info");
	}
	
	@Override
	public void update(double delta) {
		ts.setText("nb children: "+current.getChildren().size());
		super.update(delta);
	}
}
