package arenashooter.engine.ui;

import java.util.ArrayList;
import java.util.function.Consumer;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.engine.ui.simpleElement.UiSimpleElementNavigable;

public class TextInput extends UiElement {

	private static final Vec2f defaultSize = new Vec2f(10);
	private static final float labelProportion = 6;
	private Rectangle background = new Rectangle(0, defaultSize.clone(), new Vec4f(0.5, 0.5, 0.5, 0.3));
	private Label input = new Label(0, defaultSize.multiply(labelProportion), "A");
	ArrayList<Label> word = new ArrayList<>();
	private char c = 'A';
	int index = 0;
	private Vec4f selecColor = new Vec4f(1, 1, 0, 1), white = new Vec4f(1, 1, 1, 1);
	private Type type = Type.UPPERCASE;

	private enum Type {
		UPPERCASE, LOWERCASE, NUM, SPECIAL;
	}

	public TextInput() {
		super(0, defaultSize.clone());
		background.setPos(new Vec2f());
		input.setPos(new Vec2f());
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
	public void setPos(Vec2f pos) {
		Vec2f dif = Vec2f.subtract(pos, getPos());
		super.setPos(pos);
		actionOnAll(e -> e.setPos(Vec2f.add(e.getPos(), dif)));
	}

	@Override
	public void setPositionLerp(Vec2f pos, double lerp) {
		Vec2f dif = Vec2f.subtract(pos, getPos());
		super.setPositionLerp(pos, lerp);
		actionOnAll(e -> e.setPositionLerp(Vec2f.add(e.getPos(), dif), 10));
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
	public void upAction() {
		if (index == word.size()) {
			c = decrease(c);
			input.setText(String.valueOf(c));
		} else {
			Label l = word.get(index);
			char c = l.getText().charAt(0);
			c = decrease(c);
			l.setText(String.valueOf(c));
		}
	}

	private char decrease(char c) {
		c--;
		switch (type) {
		case UPPERCASE:
			if (!Character.isAlphabetic(c)) {
				c = 'Z';
			}
			break;
		case LOWERCASE:
			if (!Character.isAlphabetic(c)) {
				c = 'z';
			}
			break;
		case NUM:
			if (c!= '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8'
					&& c != '9') {
				c = '9';
			}
			break;
		case SPECIAL:
			if(c != '_' && c != '`') {
				c = '`';
			}
			break;
		default:
			break;
		}
		return c;
	}

	@Override
	public void downAction() {
		if (index == word.size()) {
			c = increase(c);
			input.setText(String.valueOf(c));
		} else {
			Label l = word.get(index);
			char c = l.getText().charAt(0);
			c = increase(c);
			l.setText(String.valueOf(c));
		}
	}

	private char increase(char c) {
		c++;
		switch (type) {
		case UPPERCASE:
			if (!Character.isAlphabetic(c)) {
				c = 'A';
			}
			break;
		case LOWERCASE:
			if (!Character.isAlphabetic(c)) {
				c = 'a';
			}
			break;
		case NUM:
			if (c!= '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8'
					&& c != '9') {
				c = '0';
			}
			break;
		case SPECIAL:
			if(c != '_' && c != '`') {
				c = '_';
			}
			break;
		default:
			break;
		}
		return c;
	}

	@Override
	public void rightAction() {
		if (index < word.size()) {
			word.get(index).setColor(white);
			index++;
			if (index != word.size()) {
				word.get(index).setColor(selecColor);
			}
		}
	}

	@Override
	public void leftAction() {
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
	}

	@Override
	public void selectAction() {
		if (index == word.size()) {
			addNewChar();
			index = word.size();
		}
	}

	private void addNewChar() {
		Label newChar;
		if(c == '_') {
			newChar = new Label(0, getScale().clone().multiply(labelProportion), String.valueOf(' '));
		} else {
			newChar = new Label(0, getScale().clone().multiply(labelProportion), String.valueOf(c));
		}
		newChar.setPos(input.getPos());
		word.add(newChar);
		Vec2f goalScale = background.getScale().clone(), goalPos = background.getPos().clone(),
				goalInput = input.getPos().clone();
		goalScale.x += getMovement();
		goalPos.x += getMovement() / 2;
		goalInput.x += getMovement();
		background.setScaleLerp(goalScale);
		background.setPositionLerp(goalPos, 10);
		input.setPositionLerp(goalInput, 10);
	}

	private double getMovement() {
		return getScale().x * 0.48;
	}

	@Override
	public boolean isSelected() {
		return true;
	}

	@Override
	public void unSelec() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		background.draw();
		for (Label label : word) {
			label.draw();
		}
		input.draw();
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		actionOnAll(e -> e.update(delta));
	}

	public String getText() {
		String result = "";
		for (Label label : word) {
			result += label.getText();
		}
		return result;
	}

	public void cancelChar() {
		if (word.size() > 0) {
			background.setScaleLerp(Vec2f.add(background.getScale(), new Vec2f(-4.25, 0)));
			background.setPositionLerp(Vec2f.add(background.getPos(), new Vec2f(-2, 0)), 10);
			input.setPositionLerp(word.get(word.size() - 1).getPos(), 10);
			word.remove(word.size() - 1);
			if (index > 0 && index == word.size() + 1) {
				index--;
			}
		}
	}

	public void reset() {
		word.clear();
		c = 'A';
		index = 0;
	}

	public void changeType() {
		int index = type.ordinal();
		index++;
		if (index >= Type.values().length) {
			index = 0;
		}
		type = Type.values()[index];
		switch (type) {
		case UPPERCASE:
			c = 'A';
			break;
		case LOWERCASE:
			c = 'a';
			break;
		case NUM:
			c = '0';
			break;
		case SPECIAL:
			c = '_';
			break;
		default:
			break;
		}
		input.setText(String.valueOf(c));
	}

}
