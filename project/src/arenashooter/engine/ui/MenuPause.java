package arenashooter.engine.ui;

import java.util.function.Consumer;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.ActionV2;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.Game;
import arenashooter.game.gameStates.MenuStart;

public class MenuPause {

	private UiImage selector = new UiImage(Texture.loadTexture("data/sprites/interface/Selector.png")),
			background = new UiImage(0.5, 0.5, 0.5, 0.3);
	private TabList<UiActionable> mainMenu = new TabList<>();
	private TabList<UiActionable> current = mainMenu;
	private InputListener inputs = new InputListener();
	private boolean pause = false;

	public MenuPause(Game game) {

		// background
		background.setScale(40, 60);

		Button resume = new Button("Resume"), options = new Button("option"), back = new Button("Back to menu");

		UiListVertical<Button> uiList = new UiListVertical<>();
		uiList.addElement(resume);
		uiList.addElement(options);
		uiList.addElement(back);
		mainMenu.addBind("Pause", uiList);
		mainMenu.setPosition(0, -8);
		mainMenu.setScale(8);
		uiList.setSpacing(8);
		uiList.setScale(6, 6);

		final double xScaleButton = 30, yScaleButton = 8;
		resume.setScale(xScaleButton, yScaleButton);
		options.setScale(xScaleButton, yScaleButton);
		back.setScale(xScaleButton, yScaleButton);

		resume.setOnArm(new Trigger() {

			@Override
			public void make() {
				setPause(false);
			}
		});
		options.setOnArm(new Trigger() {

			@Override
			public void make() {
				current = new MenuSettings(-10, new Trigger() {

					@Override
					public void make() {
						current = mainMenu;
					}
				});
			}
		});
		back.setOnArm(new Trigger() {

			@Override
			public void make() {
				Main.getGameMaster().requestNextState(new MenuStart());
			}
		});

		selector.setScale(40, 10);
		selector.setPosition(mainMenu.getTarget().getPosition().x, mainMenu.getTarget().getPosition().y);

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					if(isPaused()) {
						switch (event.getAction()) {
						case UI_BACK:
							current.backAction();
							break;
						case UI_CANCEL:
							current.cancelAction();
							break;
						case UI_CHANGE:
							current.changeAction();
							break;
						case UI_CONTINUE:
							current.continueAction();
							break;
						case UI_DOWN:
							current.downAction();
							break;
						case UI_LEFT:
							current.leftAction();
							break;
						case UI_OK:
							current.selectAction();
							break;
						case UI_RIGHT:
							current.rightAction();
							break;
						case UI_UP:
							current.upAction();
							break;
						default:
							break;
						}
						selector.setPositionLerp(current.getTarget().getPosition().x, current.getTarget().getPosition().y, 32);
					} else {
						setPause(event.getAction() == ActionV2.UI_PAUSE);
					}
				}
			}
		});
	}

	private void setPause(boolean b) {
		pause = b;
	}

	private void actionOnAll(Consumer<UiElement> c) {
		c.accept(background);
		c.accept(current);
		c.accept(selector);
	}

	public boolean isPaused() {
		return pause;
	}

	public void update(double delta) {
		inputs.step(delta);
		if (pause) {
			actionOnAll(e -> e.update(delta));
		}
	}

	public void draw() {
		if (pause) {
			actionOnAll(e -> e.draw());
		}
	}

}
