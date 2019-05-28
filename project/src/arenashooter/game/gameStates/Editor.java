package arenashooter.game.gameStates;

import java.util.Stack;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.events.menus.MenuExitEvent;
import arenashooter.engine.events.menus.MenuExitEvent.Side;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.entities.spatials.Camera;
import arenashooter.game.gameStates.editorEnum.Prime;

public class Editor extends GameState {
	/* Prime Menu */
	private Vec2f forVisible = new Vec2f(-64, 0), forNotVisible = new Vec2f(-110, 0);
	private boolean primeVisible = true;
	private MenuSelectionV<UiActionable> menuPrime = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(40, 15), "data/sprites/interface/Selector.png");

	/* Save-quit Menu */
	private MenuSelectionV<UiActionable> saveQuitMenu = new MenuSelectionV<>(5, 0, 0, new Vec2f(30, 10),
			"data/sprites/interface/Selector.png");

	/* Set Menu */
	private MenuSelectionV<UiActionable> setMenu = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");
	
	/* Add Menu */
	private MenuSelectionV<UiActionable> addMenu = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");

	private Stack<MenuSelectionV<UiActionable>> stackMenu = new Stack<>();

	private final Vec2f scale = new Vec2f(50);
	private ScrollerH<Prime> prime = new ScrollerH<>(0, scale, Prime.values());
	private InputListener inputs = new InputListener();
	private Camera cam;

	public Editor() {
		super(1);
		/* Save-Quit Menu */
		saveQuitMenu.setBackground(new Rectangle(0, new Vec2f(30), new Vec4f(0.5, 0.5, 0.8, 0.25)));
		saveQuitMenu.setEcartement(12);
		saveQuitMenu.setPositionRef(new Vec2f(0, -6.5));
		Button save = new Button(0, new Vec2f(25, 10), "Save");
		save.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		Button quit = new Button(0, new Vec2f(25, 10), "Quit");
		quit.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		saveQuitMenu.addElementInListOfChoices(save, 1);
		saveQuitMenu.addElementInListOfChoices(quit, 1);
		saveQuitMenu.setVisible(false);
		saveQuitMenu.active.setValue(false);
		
		/* Add Menu */
		addMenu.setPosition(new Vec2f(forVisible.x, forVisible.y-30));
		addMenu.setLoop(false);
		addMenu.addElementInListOfChoices(new Button(0, new Vec2f(15, 6), "Add"), 1);
		addMenu.active.setValue(false);
		addMenu.exit = new EventListener<MenuExitEvent>() {
			
			@Override
			public void launch(MenuExitEvent event) {
				if(event.getSide() == Side.Up) {
					stackMenu.pop();
					stackMenu.add(menuPrime);
					menuPrime.active.setValue(true);
					addMenu.active.setValue(false);
				}
			}
		};
		
		/* Set Menu */
		setMenu.setPosition(forVisible);
		setMenu.active.setValue(false);
		setMenu.setLoop(false);
		setMenu.exit = new EventListener<MenuExitEvent>() {
			
			@Override
			public void launch(MenuExitEvent event) {
				if(event.getSide() == Side.Up) {
					stackMenu.pop();
					stackMenu.add(menuPrime);
					menuPrime.active.setValue(true);
					setMenu.active.setValue(false);
				}
			}
		};
		
		/* Prime Menu */
		menuPrime.exit = new EventListener<MenuExitEvent>() {
			
			@Override
			public void launch(MenuExitEvent event) {
				if(event.getSide() == Side.Down) {
					switch (prime.get()) {
					case Add:
						stackMenu.pop();
						stackMenu.add(addMenu);
						menuPrime.active.setValue(false);
						addMenu.active.setValue(true);
						setMenu.setVisible(false);
						break;
					case Set:
						stackMenu.pop();
						stackMenu.add(setMenu);
						menuPrime.active.setValue(false);
						setMenu.active.setValue(true);
						addMenu.setVisible(false);
					default:
						break;
					}
				}
			}
		};

		Rectangle bg = new Rectangle(0, new Vec2f(50, 150), new Vec4f(0.5, 0.5, 0.5, 0.2));
		menuPrime.setBackground(bg);
		menuPrime.setPositionRef(new Vec2f(forVisible.x, forVisible.y - 40));
		menuPrime.addElementInListOfChoices(prime, 1);
		menuPrime.setLoop(false);

		stackMenu.add(menuPrime);

		prime.setAlwaysScrollable(true);

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						stackMenu.peek().left();
						break;
					case UI_RIGHT:
						stackMenu.peek().right();
						break;
					case UI_UP:
						stackMenu.peek().up();
						break;
					case UI_DOWN:
						stackMenu.peek().down();
						break;
					case UI_OK:
						stackMenu.peek().getTarget().selectAction();
						break;
					case UI_CONTINUE:
						setPrimeVisible(!primeVisible);
						break;
					case UI_PAUSE:
						if (stackMenu.peek() == saveQuitMenu) {
							stackMenu.pop();
						} else {
							stackMenu.push(saveQuitMenu);
							saveQuitMenu.restart();
						}
						break;
					default:
						break;
					} 
				}
			}
		});

	}

	@Override
	public void init() {
		super.init();
		cam = (Camera) current.getChild("camera");
	}

	private void setPrimeVisible(boolean visible) {
		if (visible) {
			menuPrime.setPositionLerp(forVisible);
			addMenu.setPositionLerp(new Vec2f(forVisible.x, forVisible.y-30));
			setMenu.setPositionLerp(forVisible);
		} else {
			menuPrime.setPositionLerp(forNotVisible);
			addMenu.setPositionLerp(forNotVisible);
			setMenu.setPositionLerp(forNotVisible);
		}
		primeVisible = visible;
	}
	
	private void updateVisibleMenus() {
		if(stackMenu.peek() == menuPrime) {
			menuPrime.setVisible(true);
			if(!menuPrime.active.getValue()) {
				menuPrime.active.setValue(true);
			}
			if(saveQuitMenu.isVisible()) {
				saveQuitMenu.setVisible(false);
			}
			switch (prime.get()) {
			case Add:
				addMenu.setVisible(true);
				setMenu.setVisible(false);
				break;
			case Set:
				addMenu.setVisible(false);
				setMenu.setVisible(true);
			default:
				break;
			}
			addMenu.active.setValue(false);
			setMenu.active.setValue(false);
			saveQuitMenu.setVisible(false);
		} else if(stackMenu.peek() == saveQuitMenu) {
			saveQuitMenu.setVisible(true);
		}
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		// TODO : Ajouter les entites au setMenu
		menuPrime.update(delta);
		saveQuitMenu.update(delta);
		addMenu.update(delta);
		setMenu.update(delta);
		updateVisibleMenus();
	}

	@Override
	public void draw() {
		super.draw();
		Window.beginUi();
		menuPrime.draw();
		addMenu.draw();
		setMenu.draw();
		saveQuitMenu.draw();
		Window.endUi();
	}

}
