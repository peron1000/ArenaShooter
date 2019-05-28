/**
 * 
 */
package arenashooter.engine.ui;

/**
 * @author Nathan
 *
 */
public interface Valuable<T extends Enum<T>> extends Navigable {
	public T get();
	public T get(int i);
	public void add(T element);
}
