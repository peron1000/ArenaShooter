package arenashooter.engine.ui;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import arenashooter.engine.events.BooleanProperty;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.NewValueEvent;
import arenashooter.engine.events.menus.MenuExitEvent;
import arenashooter.engine.events.menus.MenuExitEvent.Side;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.simpleElement.UiImage;

public class MenuSelection<E extends UiElement> extends Menu {

	private UiImage selec;
	private float ecartementX = 5, ecartementY = 5;
	private Vec2f positionRelative = getPosition();
	private TreeMap<Coordonnees, E> mesElements = new TreeMap<>();
	private int x = 0, y = 0;
	public BooleanProperty active = new BooleanProperty();
	public EventListener<MenuExitEvent> exit = new EventListener<MenuExitEvent>() {

		@Override
		public void launch(MenuExitEvent e) {
			// Nothing
		}
	};

	private class Coordonnees implements Comparable<Coordonnees> {
		private int x, y;

		public Coordonnees(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(MenuSelection<E>.Coordonnees o) {
			if (o.y < y) {
				return 1;
			} else if (o.y > y) {
				return -1;
			} else {
				if (o.x < x) {
					return 1;
				} else if (o.x > x) {
					return -1;
				} else {
					return 0;
				}
			}

		}
	}

	public MenuSelection(int maxLayout) {
		super(maxLayout);
		active.listener.add(new EventListener<NewValueEvent<Boolean>>() {

			@Override
			public void launch(NewValueEvent<Boolean> e) {
				if (selec != null) {
					selec.setVisible(e.getNewValue());
				}
			}
		});
	}

	public float getEcartementX() {
		return ecartementX;
	}

	public void setEcartementX(float ecartementX) {
		this.ecartementX = ecartementX;
	}

	public float getEcartementY() {
		return ecartementY;
	}

	public void setEcartementY(float ecartementY) {
		this.ecartementY = ecartementY;
	}

	public void put(E element, int layout, int x, int y) {
		Coordonnees c = new Coordonnees(x, y);
		mesElements.put(c, element);
		addUiElement(element, layout);
		Vec2f pos = new Vec2f(positionRelative.x + (x * ecartementX), positionRelative.y + (y * ecartementY));
		element.setPosLerp(pos, 3);
		element.setVisible(true);
		if (mesElements.size() == 1) {
			majSelecPosition();
		}
	}

	public void remove(E element) {
		for (MenuSelection<E>.Coordonnees c : mesElements.keySet()) {
			mesElements.remove(c, element);
		}
	}

	public void setImageSelec(UiImage image, int layout) {
		addUiElement(image, layout);
		selec = image;
		selec.setPos(positionRelative.clone());
		majSelecPosition();
	}

	public void rightAction() {
		Set<MenuSelection<E>.Coordonnees> c = new TreeSet<>(mesElements.keySet());
		c.removeIf(e -> e.y != this.y);
		x++;
		if (x == c.size()) {
			x = 0;
			exit.launch(new MenuExitEvent(Side.Right));
		}
		majSelecPosition();
	}

	public void leftAction() {
		Set<MenuSelection<E>.Coordonnees> c = new TreeSet<>(mesElements.keySet());
		c.removeIf(e -> e.y != this.y);
		x--;
		if (x < 0) {
			x = c.size() - 1;
			exit.launch(new MenuExitEvent(Side.Left));
		}
		majSelecPosition();
	}

	public void upAction() {
		Set<MenuSelection<E>.Coordonnees> c = new TreeSet<>(mesElements.keySet());
		c.removeIf(e -> e.x != this.x);
		y--;
		if (y < 0) {
			y = c.size() - 1;
			exit.launch(new MenuExitEvent(Side.Up));
		}
		majSelecPosition();
	}

	public void downAction() {
		Set<MenuSelection<E>.Coordonnees> c = new TreeSet<>(mesElements.keySet());
		c.removeIf(e -> e.x != this.x);
		y++;
		if (y == c.size()) {
			y = 0;
			exit.launch(new MenuExitEvent(Side.Down));
		}
		majSelecPosition();
	}

	public E getElemSelec() {
		return mesElements.get(new Coordonnees(x, y));
	}

	public void setPositionRelative(Vec2f newPosition) {
		this.positionRelative = newPosition;
		majSelecPosition();
	}

	@Override
	public void setPosition(Vec2f newPosition) {
		Vec2f dif = Vec2f.subtract(newPosition, getPosition());
		super.setPosition(newPosition);
		setPositionRelative(Vec2f.add(positionRelative, dif));
	}

	private void majSelecPosition() {
		E element = mesElements.get(new Coordonnees(x, y));
		if (element != null && selec != null) {
			selec.setPosLerp(element.getPos(), 40);
		}
	}

	public void restart() {
		x = 0;
		y = 0;
		majSelecPosition();
	}

	@Override
	public void update(double delta) {
		mesElements.values().forEach(v -> v.visible = this.isVisible());
		super.update(delta);
	}
}
