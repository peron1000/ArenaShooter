package arenashooter.engine.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.engine.util.Tuple;
import arenashooter.game.Main;

public class AnimEditorTimeline extends UiElement {
	UiImage bg;
	
	double timeScale = 1;
	double timeOffset = 0;
	
	private Map<String, Track> tracksMap = new HashMap<>();
	private List<String> tracks = new ArrayList<>();

	public AnimEditorTimeline(double rot, Vec2f scale) {
		bg = new UiImage(new Vec4f(0,0,0, .8f));
	}
	
	public double maxVisibleTime() {
		return timeOffset+(getScale().x * timeScale);
	}
	
	@Override
	public void setPosition(double x, double y) {
		bg.setPosition(x , y);
		super.setPosition(x , y);
	}

	public void addTrack(String name) {
		tracks.add(name);
		tracksMap.put(name, new Track());
	}
	
	public void removeTrack(String name) {
		if(!tracks.contains(name) || !tracksMap.containsKey(name)) {
			Main.log.error("Animation editor tried to delete a non existant track");
			return;
		}
		tracks.remove(name);
		tracksMap.remove(name);
	}

	@Override
	public void draw() {
		bg.draw();
		for(Track track : tracksMap.values())
			track.draw();
	}
	
	private class Track {
		List<Tuple<Double, UiImage>> keyframes;
		
		Track() {
			keyframes = new ArrayList<>();
		}

		void update() {
			
		}
		
		void draw() {
			for(Tuple<Double, UiImage> key : keyframes) {
				if(key.x >= timeOffset && key.x <= maxVisibleTime())
					key.y.draw();
			}
		}
	}

}
