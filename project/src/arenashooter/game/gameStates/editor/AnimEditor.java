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
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;

public class AnimEditor extends UiElement {
	DecimalFormat df = new DecimalFormat("#0.000");
	double currentTime = 0;
	AnimationDataEditable data;
	
	List<UiElement> elems = new ArrayList<>();
	
	Label lblTimeCurrent;
	Label lblTimeLength;
	AnimEditorTimeline timeline;
	

	public AnimEditor() {
		data = new AnimationDataEditable();
		
		UiImage bg = new UiImage(new Vec4f(.133, .204, 961, 1));
		bg.setScale(screenWidth(), 100);
		elems.add(bg);
		
		UiImage mainPanel = new UiImage(new Vec4f(0, 0, 0, .8));
		mainPanel.setPosition(0, 35);
		mainPanel.setScale(screenWidth(), 30);
		elems.add(mainPanel);
		
		lblTimeCurrent = new Label("Time: ", Text.TextAlignH.LEFT);
		lblTimeCurrent.setScale(4);
		lblTimeCurrent.setPosition(screenLeft()+2, 50-3);
		elems.add(lblTimeCurrent);
		
		lblTimeLength = new Label("Length: ", Text.TextAlignH.LEFT);
		lblTimeLength.setScale(4);
		lblTimeLength.setPosition(screenLeft()+32, 50-3);
		elems.add(lblTimeLength);
		
		timeline = new AnimEditorTimeline(0, new Vec2f(screenWidth()-25, 20));
		timeline.setPosition(11, 32);
		elems.add(timeline);
	}
	
	private float screenWidth() { return Window.getRatio()*100; }
	
	private float screenLeft() { return -screenWidth()/2; }
	
	@SuppressWarnings("unused")
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
	public boolean continueAction() {
		return false;
	}

	@Override
	public boolean cancelAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean backAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
