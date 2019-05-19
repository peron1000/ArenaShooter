package arenashooter.game.gameStates;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Label;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.Rectangle;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiImage;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.engineParam.GameMode;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Param extends GameState {

	private GameParam gameParam;
	private MenuSelectionV<UiElement> menu = new MenuSelectionV<UiElement>(10);
	private UiImage selec;
	private Label param1, param2, param3, param4;
	private boolean activated = false;
	private Rectangle carre;
	private Vec4f color1 = new Vec4f(0.02f, 0.05f, 0.9f, 1), color2 = new Vec4f(0.8f, 0.1f, 0.4f, 1);

	@Override
	public void init() {
		gameParam = new GameParam();

		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu.png");
		texture1.setFilter(false);

		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
		texture2.setFilter(false);

		new UiImage(menu, new Vec2f(), 0, new Vec2f(177.78, 100), texture1, new Vec4f(1, 1, 1, 1) , 0);

		final float y = -40, scale = 20f;
		param1 = new Label(menu, new Vec2f(), 0, new Vec2f(scale), gameParam.getStringGameMode() , 1);
		param2 = new Label(menu, new Vec2f(), 0, new Vec2f(scale), gameParam.getStringRound() , 1);
		param3 = new Label(menu, new Vec2f(), 0, new Vec2f(scale), gameParam.getStringTeam() , 1);
		param4 = new Label(menu, new Vec2f(), 0, new Vec2f(scale), "Parametre" , 1);
		carre = new Rectangle(menu, new Vec2f(0, y), 0, new Vec2f(scale), color1 , 1);

		param1.ui_Pointation(null, null, carre, null);
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

		param2.ui_Pointation(null, null, carre, null);
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

		param3.ui_Pointation(null, null, carre, null);
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

		param4.ui_Pointation(null, null, carre, null);
		
		param3.visible = false;
		param4.visible = false;
		
		selec = new UiImage(menu, new Vec2f(), 0, new Vec2f(40, 8), texture2,
				new Vec4f(1, 1, 1, 1) , 2);
		menu.ecartement = 5;
		menu.setImageSelec(selec);
		menu.setPositionRef(new Vec2f(-57, -40));
		menu.addElement(param1);
		menu.addElement(param2);

		carre.ui_Pointation(null, null, param1, param1);

		menu.focus = param1;

		super.init();
	}

	@Override
	public void update(double delta) {
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_UP)) {
			if (!activated) {
				menu.previous(delta);
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_DOWN)) {
			if (!activated) {
				menu.next(delta);
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_LEFT)) {
			if (activated) {
				menu.getTarget().lunchAction("left");
			} else {
				menu.focusLeft();
				if (menu.focus == carre) {
					carre.setColor(color2);
					menu.active = false;
				} else {
					carre.setColor(color1);
					menu.active = true;
				}
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_RIGHT)) {
			if (activated) {
				menu.getTarget().lunchAction("right");
			} else {
				menu.focusRight();
				if (menu.focus == carre) {
					carre.setColor(color2);
					menu.active = false;
				} else {
					carre.setColor(color1);
					menu.active = true;
				}
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.JUMP)) {
			activated = !activated;
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {
			GameMaster.gm.requestNextState(new CharacterChooser(), GameMaster.mapEmpty);
		}

		if (GameParam.getGameMode() == GameMode.Rixe) {
			menu.addElement(param3);
		} else {
			menu.removeElement(param3);
		}

		menu.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		menu.draw();
	}

}
