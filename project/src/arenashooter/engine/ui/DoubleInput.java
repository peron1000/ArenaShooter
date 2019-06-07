package arenashooter.engine.ui;

import java.util.ArrayList;
import java.util.function.Consumer;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.engine.ui.simpleElement.UiSimpleElementNavigable;

public class DoubleInput extends UiElement {

	private static final Vec2f defaultSize = new Vec2f(10);
	private static final float labelProportion = 6;
	private UiImage background = new UiImage(0, defaultSize.clone(), new Vec4f(0.5, 0.5, 0.5, 0.3));
	private Label input = new Label(0, defaultSize.multiply(labelProportion), "0");
	ArrayList<Label> word = new ArrayList<>();
	private char c = '0';
	int index = 0;
	private Vec4f selecColor = new Vec4f(1, 1, 0, 1), white = new Vec4f(1);
	private boolean isNum = true;
	private Trigger onFinishGood = new Trigger() {

		@Override
		public void make() {
			// Nothing
		}
	};
	private Trigger onFinishBad = new Trigger() {
		
		@Override
		public void make() {
			// Nothing
		}
	};

	public DoubleInput() {
		super(0, defaultSize.clone());
		background.setPosition(new Vec2f());
		input.setPosition(new Vec2f());
		input.setColor(new Vec4f(1, 0.1, 0.1, 0.48));
	}

	protected void actionOnAll(Consumer<UiSimpleElementNavigable> c) {
		c.accept(input);
		c.accept(background);
		for (Label label : word) {
			c.accept(label);
		}
	}

	@Override
	public void setPosition(Vec2f pos) {
		Vec2f dif = Vec2f.subtract(pos, getPosition());
		super.setPosition(pos);
		actionOnAll(e -> e.setPosition(Vec2f.add(e.getPosition(), dif)));
	}

	@Override
	public void setPositionLerp(Vec2f pos, double lerp) {
		Vec2f dif = Vec2f.subtract(pos, getPosition());
		super.setPositionLerp(pos, lerp);
		actionOnAll(e -> e.setPositionLerp(Vec2f.add(e.getPosition(), dif), 10));
	}

	@Override
	public void setScale(Vec2f scale) {
		super.setScale(scale);
		input.setScale(scale.clone().multiply(labelProportion));
		for (Label label : word) {
			label.setScale(scale.clone().multiply(labelProportion));
		}
		Vec2f bgScale = scale.clone();
		bgScale.x += getMovement() * word.size();
		background.setScale(bgScale);
	}

	@Override
	public void setScaleLerp(Vec2f scale) {
		super.setScaleLerp(scale);
		input.setScaleLerp(scale.clone().multiply(labelProportion));
		for (Label label : word) {
			label.setScaleLerp(scale.clone().multiply(labelProportion));
		}
		Vec2f bgScale = scale.clone();
		bgScale.x += getMovement() * word.size();
		background.setScaleLerp(bgScale);
	}

	@Override
	public void setVisible(boolean visible) {
		actionOnAll(e -> e.setVisible(visible));
		super.setVisible(visible);
	}

	@Override
	public boolean upAction() {
		if (index == word.size()) {
			c = decrease(c);
			input.setText(String.valueOf(c));
		} else {
			Label l = word.get(index);
			char c = l.getText().charAt(0);
			c = decrease(c);
			l.setText(String.valueOf(c));
		}
		return true;
	}

	private char decrease(char c) {
		if (isNum) {
			c--;
			if (c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8'
					&& c != '9') {
				c = '9';
			}
		}
		return c;
	}

	@Override
	public boolean downAction() {
		if (index == word.size()) {
			c = increase(c);
			input.setText(String.valueOf(c));
		} else {
			Label l = word.get(index);
			char c = l.getText().charAt(0);
			c = increase(c);
			l.setText(String.valueOf(c));
		}
		return true;
	}

	@Override
	public boolean continueAction() {
		int nbNoNumber=0;
		for (Label label : word) {
			if(label.getText().charAt(0) == '.') {
				nbNoNumber++;
			}
		}
		if(nbNoNumber < 2) {
			onFinishGood.make();
		}
		return true;
	}
	
	@Override
	public boolean backAction() {
		onFinishBad.make();
		return true;
	}

	private char increase(char c) {
		if (isNum) {
			c++;
			if (c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8'
					&& c != '9') {
				c = '0';
			}
		}
		return c;
	}

	@Override
	public boolean rightAction() {
		if (index < word.size()) {
			word.get(index).setColor(white);
			index++;
			if (index != word.size()) {
				word.get(index).setColor(selecColor);
			}
		}
		return true;
	}

	@Override
	public boolean leftAction() {
		if (index > 0) {
			if (index == word.size()) {
				index--;
				word.get(index).setColor(selecColor);
			} else {
				word.get(index).setColor(white);
				index--;
				word.get(index).setColor(selecColor);
			}
		}
		return true;
	}

	@Override
	public boolean selectAction() {
		if (index == word.size()) {
			addNewChar();
			index = word.size();
		}
		return true;
	}

	private void addNewChar() {
		Label newChar;
		if (c == '_') {
			newChar = new Label(0, getScale().clone().multiply(labelProportion), String.valueOf(' '));
		} else {
			newChar = new Label(0, getScale().clone().multiply(labelProportion), String.valueOf(c));
		}
		newChar.setPosition(input.getPosition());
		word.add(newChar);
		Vec2f goalScale = background.getScale().clone(), goalPos = background.getPosition().clone(),
				goalInput = input.getPosition().clone();
		goalScale.x += getMovement() + 1;
		goalPos.x += getMovement() / 2;
		goalInput.x += getMovement();
		background.setScaleLerp(goalScale);
		background.setPositionLerp(goalPos, 10);
		input.setPositionLerp(goalInput, 10);
	}

	private double getMovement() {
		return getScale().x * 0.55;
	}

	@Override
	public boolean isSelected() {
		return true;
	}

	@Override
	public void unSelec() {
		// ?
	}

	@Override
	public void draw() {
		if (visible) {
			background.draw();
			for (Label label : word) {
				label.draw();
			}
			input.draw();
		}
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		actionOnAll(e -> e.update(delta));
	}

	public double getDouble() {
		String result = "";
		for (Label label : word) {
			result += label.getText();
		}
		double res = 0;
		try {
			res = Double.parseDouble(result);
		} catch (Exception e) {
			System.err.println("The input is not a Double");
			e.printStackTrace();
		}
		return res;
	}

	public void reset() {
		word.clear();
		c = '0';
		index = 0;
		background.setScale(getScale().clone());
		background.setPosition(getPosition());
		input.setText(String.valueOf(c));
		input.setPosition(getPosition());
		isNum = true;
	}

	@Override
	public boolean changeAction() {
		isNum = !isNum;
		if(isNum) {
			c = '0';
		} else {
			c = '.';
		}
		input.setText(String.valueOf(c));
		return true;
	}

	public void setOnFinish(Trigger t) {
		onFinishGood = t;
	}
	
	public void setOnCancel(Trigger t) {
		onFinishBad = t;
	}

}
