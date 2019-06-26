package arenashooter.engine.ui;

public interface Valuable<E> {
	public String getStringValue();
	public void setValue(E newValue);
	public E getValue();
}
