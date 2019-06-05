package arenashooter.game.gameStates.editor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.animation.AnimationDataEditable;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.AnimEditorTimeline;
import arenashooter.engine.ui.Navigable;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;

public class AnimEditor implements Navigable {
	DecimalFormat df = new DecimalFormat("#0.000");
	double currentTime = 0;
	AnimationDataEditable data;
	
	List<UiElement> elems = new ArrayList<>();
	
	Label lblTimeCurrent;
	Label lblTimeLength;
	AnimEditorTimeline timeline;
	

	public AnimEditor() {
		data = new AnimationDataEditable();
		
		UiImage bg = new UiImage(0, new Vec2f(screenWidth(), 100), new Vec4f(.133, .204, 961, 1));
		elems.add(bg);
		
		UiImage mainPanel = new UiImage(0, new Vec2f(screenWidth(), 30), new Vec4f(0, 0, 0, .8));
		mainPanel.setPosition(new Vec2f(0, 35));
		elems.add(mainPanel);
		
		lblTimeCurrent = new Label(0, new Vec2f(25), "Time: ", Text.TextAlignH.LEFT);
		lblTimeCurrent.setPosition(new Vec2f(screenLeft()+2, 50-3));
		elems.add(lblTimeCurrent);
		
		lblTimeLength = new Label(0, new Vec2f(25), "Length: ", Text.TextAlignH.LEFT);
		lblTimeLength.setPosition(new Vec2f(screenLeft()+32, 50-3));
		elems.add(lblTimeLength);
		
		timeline = new AnimEditorTimeline(0, new Vec2f(screenWidth()-25, 20));
		timeline.setPosition(new Vec2f(11, 32));
		elems.add(timeline);
	}
	
	private float screenWidth() { return Window.getRatio()*100; }
	
	private float screenLeft() { return -screenWidth()/2; }
	
	private float screenRight() { return screenWidth()/2; }

	@Override
	public boolean upAction() {
		return false;
	}

	@Override
	public boolean downAction() {
		return false;
	}

	@Override
	public boolean rightAction() {
		return false;
	}

	@Override
	public boolean leftAction() {
		return false;
	}

	@Override
	public boolean selectAction() {
		return false;
	}

	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public void unSelec() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(double delta) {
		lblTimeCurrent.setText("Time: "+df.format(currentTime));
		lblTimeLength.setText("Length: "+df.format(data.length)); //TODO: Only update this when needed
		
		for(UiElement elem : elems)
			elem.update(delta);
	}

	@Override
	public void draw() {
		for(UiElement elem : elems)
			elem.draw();
	}

	@Override
	public void setPositionLerp(Vec2f position, double lerp) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean continueAction() {
		return false;
	}

	@Override
	public void setPosition(Vec2f newPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vec2f getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
