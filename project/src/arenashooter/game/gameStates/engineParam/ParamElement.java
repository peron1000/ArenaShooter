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
	
	public ParamElement(String title , String...choices) {
		this.title = title;
		this.choices = choices;
	}
	
	public void afficherH(Map map , Vec3f position , Vec3f scale , Font font , TextAlignH align) {
		TextSpatial text = new TextSpatial(position, scale, new Text(font, align, title+" : "+choices[index]));
		text.attachToParent(map, text.genName());
	}
	
	public void afficherV(Map map , Vec3f position1 , Vec3f position2 , Vec3f scale , Font font , TextAlignH align) {
		TextSpatial text1 = new TextSpatial(position1, scale, new Text(font, align, title));
		text1.attachToParent(map, text1.genName());
		
		TextSpatial text2 = new TextSpatial(position2, scale, new Text(font, align, choices[index]));
		text2.attachToParent(map, text2.genName());
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
