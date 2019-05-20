package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.menus.MenuEventExit;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Button;
import arenashooter.engine.ui.MenuSelection;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.Rectangle;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiImage;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.engineParam.GameMode;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Param extends GameState {

	private GameParam gameParam;
	private MenuSelectionV<Button> menuParam = new MenuSelectionV<Button>(10);
	private MenuSelection<Rectangle> menuMap = new MenuSelection<>(10);
	private UiImage selec;
	private Button param1, param2, param3, param4;
	private boolean activated = false;
	private Vec4f color1 = new Vec4f(0.02f, 0.05f, 0.9f, 1);

	@Override
	public void init() {
		gameParam = new GameParam();

		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu.png");
		texture1.setFilter(false);

		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
		texture2.setFilter(false);

		UiImage bg = new UiImage(0, new Vec2f(177.78, 100), texture1, new Vec4f(1, 1, 1, 1));
		menuParam.setBackground(bg);

		final float scaleY = 5.5f, scaleX = 50f;
		param1 = new Button(0, new Vec2f(scaleX, scaleY), gameParam.getStringGameMode());
		param2 = new Button(0, new Vec2f(scaleX, scaleY), gameParam.getStringRound());
		param3 = new Button(0, new Vec2f(scaleX, scaleY), gameParam.getStringTeam());
		param4 = new Button(0, new Vec2f(scaleX, scaleY), "Parametre");

		param1.addAction("right", new Trigger() {

			@Override
			public void make() {
				gameParam.nextGameMode();
				param1.setText(gameParam.getStringGameMode());
			}
		});
		param1.addAction("left", new Trigger() {

			@Override
			public void make() {
				gameParam.previousGameMode();
				param1.setText(gameParam.getStringGameMode());
			}
		});

		param2.addAction("right", new Trigger() {

			@Override
			public void make() {
				gameParam.nextRound();
				param2.setText(gameParam.getStringRound());
			}
		});
		param2.addAction("left", new Trigger() {

			@Override
			public void make() {
				gameParam.previousRound();
				param2.setText(gameParam.getStringRound());
			}
		});

		param3.addAction("right", new Trigger() {

			@Override
			public void make() {
				gameParam.nextTeam();
				param3.setText(gameParam.getStringTeam());
			}
		});
		param3.addAction("left", new Trigger() {

			@Override
			public void make() {
				gameParam.previousTeam();
				param3.setText(gameParam.getStringTeam());
			}
		});

		param3.visible = false;
		param4.visible = false;

		selec = new UiImage(0, new Vec2f(65, 10), texture2, new Vec4f(1, 1, 1, 1));
		menuParam.ecartement = 8;
		menuParam.setImageSelec(selec, 2);
		menuParam.setPosition(new Vec2f(-57, -40));
		menuParam.addElementInListOfChoices(param1, 1);
		menuParam.addElementInListOfChoices(param2, 1);

		menuParam.focus = param1;

		// test
		menuMap.setImageSelec(new UiImage(0, new Vec2f(3), texture2, new Vec4f(1, 1, 1, 1)), 3);
		menuMap.setPosition(new Vec2f(10, -30));
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				menuMap.put(new Rectangle(0, new Vec2f(2), color1), 2, i, j);
			}
		}
		menuMap.active.setValue(false);

		menuParam.exit = new EventListener<MenuEventExit>() {

			@Override
			public void action(MenuEventExit e) {
				switch (e.getSide()) {
				case Down:
				case Up:
					break;
				case Right:
				case Left:
				default:
					menuParam.active = false;
					menuMap.active.setValue(true);
					menuMap.restart();
					break;
				}
			}
		};
		menuMap.exit = new EventListener<MenuEventExit>() {

			@Override
			public void action(MenuEventExit e) {
				switch (e.getSide()) {
				case Down:
				case Up:
					break;
				case Right:
				case Left:
				default:
					menuParam.active = true;
					menuMap.active.setValue(false);
					menuParam.restart();
					break;
				}
			}
		};

		super.init();
	}

	@Override
	public void update(double delta) {
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_UP)) {
			if (!activated && menuParam.active) {
				menuParam.up(delta);
			} else if (menuMap.active.getValue()) {
				menuMap.up();
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_DOWN)) {
			if (!activated && menuParam.active) {
				menuParam.down(delta);
			} else if (menuMap.active.getValue()) {
				menuMap.down();
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_LEFT)) {
			if (activated && menuParam.active) {
				menuParam.getTarget().lunchAction("left");
			} else if(menuParam.active){
				menuParam.left();
			} else if (menuMap.active.getValue()) {
				menuMap.left();
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_RIGHT)) {
			if (activated && menuParam.active) {
				menuParam.getTarget().lunchAction("right");
			} else if(menuParam.active){
				menuParam.right();
			} else if (menuMap.active.getValue()) {
				menuMap.right();
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.JUMP)) {
			activated = !activated;
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {
			GameMaster.gm.requestNextState(new CharacterChooser(), GameMaster.mapEmpty);
		}

		if (GameParam.getGameMode() == GameMode.Rixe) {
			menuParam.addElementInListOfChoices(param3, 1);
		} else {
			menuParam.removeElementInListOfChoices(param3);
		}

		menuParam.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		menuParam.draw();
		menuMap.draw();
	}

}
