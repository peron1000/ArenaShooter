package arenashooter.engine.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CircleList<T> implements Iterable<T>, Collection<T> {

	private List<T> circleList = new LinkedList<>();
	private int index = 0;

	@Override
	public boolean add(T arg0) {
		return circleList.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		return circleList.addAll(arg0);
	}

	@Override
	public void clear() {
		circleList.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return circleList.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return circleList.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return circleList.isEmpty();
	}

	@Override
	public boolean remove(Object arg0) {
		return circleList.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return circleList.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return circleList.retainAll(arg0);
	}

	@Override
	public int size() {
		return circleList.size();
	}

	@Override
	public Object[] toArray() {
		return circleList.toArray();
	}

	@Override
	public <E> E[] toArray(E[] arg0) {
		return circleList.toArray(arg0);
	}

	@Override
	public Iterator<T> iterator() {
		return circleList.iterator();
	}

	public void next() {
		index++;
		index %= circleList.size();
	}

	public void previous() {
		index--;
		if (index < 0) {
			index = circleList.size()-1;
		}
	}

	public T get() {
		return circleList.get(index);
	}
	
	public T get(int i) {
		return circleList.get(i);
	}

	public void restart() {
		index = 0;
	}
	
}
