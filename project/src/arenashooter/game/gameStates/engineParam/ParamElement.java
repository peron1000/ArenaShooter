package arenashooter.game.gameStates.engineParam;

import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Map;
import arenashooter.entities.spatials.TextSpatial;

class ParamElement {
	
	private String[] choices;
	private String title;
	private int index = 0;
	TextSpatial text;
	public Vec3f position = new Vec3f();
	
	public ParamElement(String title , String...choices) {
		this.title = title;
		this.choices = choices;
	}
	
	public void afficherH(Map map , Vec3f position , Vec3f scale , Font font) {
		text = new TextSpatial(position, scale, new Text(font, TextAlignH.LEFT, title+" : "+choices[index]));
		text.attachToParent(map, text.genName());
		this.position = position;
	}
	
	public String getValue() {
		return choices[index];
	}
	
	public void next() {
		index++;
		if(index >= choices.length) {
			index = 0;
		}
	}
	
	public void previous() {
		index--;
		if(index < 0) {
			index = choices.length-1;
		}
	}
	
}
