package arenashooter.engine.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;

public class DoubleInput extends UiElement {

	private UiImage background = new UiImage(new Vec4f(0.5, 0.5, 0.5, 0.3));
	private Label input = new Label("0");
	List<Label> word = new ArrayList<>();
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
		input.setColor(new Vec4f(1, 0.1, 0.1, 0.48));
		setScale(8);
	}

	protected void actionOnAll(Consumer<UiElement> c) {
		c.accept(input);
		c.accept(background);
		for (Label label : word) {
			c.accept(label);
		}
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPosition(x, y);
		actionOnAll(e -> e.addToPosition(xDif, yDif));
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPositionLerp(x, y, lerp);
		actionOnAll(e -> e.setPositionLerp(e.getPosition().x + xDif, e.getPosition().y + yDif, 10));
	}

	@Override
	public void setScale(double x, double y) {
		super.setScale(x, y);
		double max = Math.max(x, y);
		input.setScale(max);
		for (Label label : word) {
			label.setScale(max);
		}
		background.setScale(x + getMovement() * word.size(), y);
	}

	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		super.setScaleLerp(x, y, lerp);
		double max = Math.max(x, y);
		input.setScaleLerp(max, max, lerp);
		for (Label label : word) {
			label.setScaleLerp(max, max, lerp);
		}
		background.setScaleLerp(x + getMovement() * word.size(), y, lerp);
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
		int nbNoNumber = 0;
		for (Label label : word) {
			if (label.getText().charAt(0) == '.') {
				nbNoNumber++;
			}
		}
		if (nbNoNumber < 2) {
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

	@Override
	public boolean cancelAction() {
		if (word.size() > 0) {
			word.remove(word.size() - 1);
			index--;
			background.addToScaleLerp(-(getMovement() + 1), 0, 10);
			background.addToPositionLerp(-getMovement() / 2, 0, 10);
			input.addToPositionLerp(-getMovement(), 0, 10);
		}
		return true;
	}

	private void addNewChar() {
		Label newChar;
		if (c == '_') {
			newChar = new Label(String.valueOf(' '));
		} else {
			newChar = new Label(String.valueOf(c));
		}
		newChar.setScale(getScale().x, getScale().y);
		newChar.setPosition(input.getPosition().x, input.getPosition().y);
		word.add(newChar);

		background.addToScaleLerp(getMovement() + 1, 0, 10);
		background.addToPositionLerp(getMovement() / 2, 0, 10);
		input.addToPositionLerp(getMovement(), 0, 10);
	}

	private double getMovement() {
		return getScale().x * 0.55;
	}

	@Override
	public void draw() {
		if (isVisible()) {
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
		background.setScale(getScale().x, getScale().y);
		background.setPosition(getPosition().x, getPosition().y);
		input.setText(String.valueOf(c));
		input.setPosition(getPosition().x, getPosition().y);
		isNum = true;
	}

	@Override
	public boolean changeAction() {
		isNum = !isNum;
		if (isNum) {
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
