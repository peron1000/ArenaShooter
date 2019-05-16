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
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiImage;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Param extends GameState {

	public GameParam gameParam;
	private Menu menu = new Menu();
	private UiImage selec;
	private Label param1, param2, param3, param4;
	private boolean activated = false;

	@Override
	public void init() {
		gameParam = new GameParam();

		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu.png");
		texture1.setFilter(false);

		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
		texture2.setFilter(false);

		new UiImage(menu, new Vec2f(), 0, new Vec2f(177.78, 100), texture1, new Vec4f(1, 1, 1, 1));

		final float x = -57, y = -40;
		param1 = new Label(menu, new Vec2f(x, y), 0, new Vec2f(20), gameParam.getStringGameMode());
		param2 = new Label(menu, new Vec2f(x, y + 10), 0, new Vec2f(30), "Parametre");
		param3 = new Label(menu, new Vec2f(x, y + 20), 0, new Vec2f(30), "Parametre");
		param4 = new Label(menu, new Vec2f(x, y + 30), 0, new Vec2f(30), "Parametre");

		param1.down = param2;
		param1.up = param4;
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

		param2.down = param3;
		param2.up = param1;

		param3.down = param4;
		param3.up = param2;

		param4.down = param1;
		param4.up = param3;

		menu.focus = param1;

		selec = new UiImage(menu, new Vec2f(menu.focus.pos.x, menu.focus.pos.y + 2.25f), 0, new Vec2f(40, 8), texture2,
				new Vec4f(1, 1, 1, 1));
		super.init();
	}

	@Override
	public void update(double delta) {
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_UP)) {
			menu.focusUp();
			activated = false;
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_DOWN)) {
			menu.focusDown();
			activated = false;
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_LEFT)) {
			if (activated)
				menu.focus.lunchAction("left");
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_RIGHT)) {
			if (activated)
				menu.focus.lunchAction("right");
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.JUMP)) {
			activated = !activated;
		}
		selec.pos.x = menu.focus.pos.x;
		selec.pos.y = Utils.lerpF(selec.pos.y, menu.focus.pos.y + 2.25f, Utils.clampD(delta * 20, 0, 1));
		menu.update();
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		menu.draw();
	}

}
