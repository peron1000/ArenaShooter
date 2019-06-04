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
import arenashooter.engine.ui.simpleElement.Rectangle;

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
		
		Rectangle bg = new Rectangle(0, new Vec2f(screenWidth(), 100), new Vec4f(.133, .204, 961, 1));
		elems.add(bg);
		
		Rectangle mainPanel = new Rectangle(0, new Vec2f(screenWidth(), 30), new Vec4f(0, 0, 0, .8));
		mainPanel.setPos(new Vec2f(0, 35));
		elems.add(mainPanel);
		
		lblTimeCurrent = new Label(0, new Vec2f(25), "Time: ", Text.TextAlignH.LEFT);
		lblTimeCurrent.setPos(new Vec2f(screenLeft()+2, 50-3));
		elems.add(lblTimeCurrent);
		
		lblTimeLength = new Label(0, new Vec2f(25), "Length: ", Text.TextAlignH.LEFT);
		lblTimeLength.setPos(new Vec2f(screenLeft()+32, 50-3));
		elems.add(lblTimeLength);
		
		timeline = new AnimEditorTimeline(0, new Vec2f(screenWidth()-25, 20));
		timeline.setPos(new Vec2f(11, 32));
		elems.add(timeline);
	}
	
	private float screenWidth() { return Window.getRatio()*100; }
	
	private float screenLeft() { return -screenWidth()/2; }
	
	private float screenRight() { return screenWidth()/2; }

	@Override
	public void upAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void downAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rightAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void leftAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
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

}
