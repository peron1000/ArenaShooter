package arenashooter.game.gameStates;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Label;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.Rectangle;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiImage;
import arenashooter.game.gameStates.engineParam.GameMode;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Param extends GameState {

	public GameParam gameParam;
	private Menu menu = new Menu();
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

		new UiImage(menu, new Vec2f(), 0, new Vec2f(177.78, 100), texture1, new Vec4f(1, 1, 1, 1));

		final float x = -57, y = -40, scale = 20f;
		param1 = new Label(menu, new Vec2f(x, y), 0, new Vec2f(scale), gameParam.getStringGameMode());
		param2 = new Label(menu, new Vec2f(x, y + 10), 0, new Vec2f(scale), gameParam.getStringRound());
		param3 = new Label(menu, new Vec2f(x, y + 20), 0, new Vec2f(scale), gameParam.getStringTeam());
		param4 = new Label(menu, new Vec2f(x, y + 30), 0, new Vec2f(scale), "Parametre");
		carre = new Rectangle(menu, new Vec2f(0, y), 0, new Vec2f(scale), color1);

		param1.ui_Pointation(param2, param2, carre, null);
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

		param2.ui_Pointation(param1, param1, carre, null);
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

		param3.ui_Pointation(param2, param1, carre, null);
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

		param4.ui_Pointation(param3, param1, carre, null);

		param3.visible = false;
		param4.visible = false;

		carre.ui_Pointation(null, null, null, param1);

		menu.focus = param1;

		selec = new UiImage(menu, new Vec2f(menu.focus.pos.x, menu.focus.pos.y + 2.25f), 0, new Vec2f(40, 8), texture2,
				new Vec4f(1, 1, 1, 1));
		super.init();
	}

	@Override
	public void update(double delta) {
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_UP)) {
			if (!activated) {
				menu.focusUp();
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_DOWN)) {
			if (!activated) {
				menu.focusDown();
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_LEFT)) {
			if (activated) {
				menu.focus.lunchAction("left");
			} else {
				menu.focusLeft();
				if (menu.focus == carre) {
					carre.setColor(color2);
				} else {
					carre.setColor(color1);
				}
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_RIGHT)) {
			if (activated) {
				menu.focus.lunchAction("right");
			} else {
				menu.focusRight();
				if (menu.focus == carre) {
					carre.setColor(color2);
				} else {
					carre.setColor(color1);
				}
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.JUMP)) {
			activated = !activated;
		}

		if (GameParam.getGameMode() == GameMode.Rixe) {
			param3.visible = true;
			param2.down = param3;
			param1.up = param3;
		} else {
			param3.visible = false;
			param2.down = param1;
			param1.up = param2;
		}

		selec.pos = Vec2f.lerp(selec.pos, menu.focus.pos, Utils.clampD(delta * 20, 0, 1));
		menu.update();
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		menu.draw();
	}

}
