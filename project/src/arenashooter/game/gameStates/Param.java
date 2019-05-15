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
import arenashooter.engine.ui.UiImage;
import arenashooter.entities.spatials.Sprite;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Param extends GameState {

	private GameParam gameParam;
	private int index = 0;
	public static final Sprite[] selectorArray = new Sprite[7];
	private Menu menu = new Menu();
	private UiImage selec;
	private Label param1;
	private Label param2;
	private Label param3;
	private Label param4;

	@Override
	public void init() {
		for (int i = 0; i < selectorArray.length; i++) {
			Texture texture = Texture.loadTexture("data/sprites/interface/Selector.png");
			double with = 8.5 * texture.getWidth(), height = 7 * texture.getHeight();
			Sprite selector = new Sprite(new Vec2f(-700, -400 + i * height), texture);
			selector.getTexture().setFilter(false);
			selector.size = new Vec2f(with, height);
			selectorArray[i] = selector;
		}
		selectorArray[0].attachToParent(map, selectorArray[0].genName());
		
		gameParam = new GameParam(map);
		
//		Sprite background = new Sprite(new Vec2f(), "data/sprites/interface/Fond Menu.png");
//		background.getTexture().setFilter(false);
//		final int scale = 6;
//		background.size = new Vec2f(background.getTexture().getWidth()*scale, background.getTexture().getHeight()*scale);
//		background.attachToParent(map, background.genName());
		
		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu.png");
		texture1.setFilter(false);
		
		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
		texture2.setFilter(false);
		
		
		UiImage image = new UiImage(menu, new Vec2f(), 0, new Vec2f(177.78, 100), texture1 , new Vec4f(1 ,1 , 1, 1));
		
		final float x = -57 , y = -40;		
		param1 = new Label(menu, new Vec2f(x, y), 0, new Vec2f(30), "Paramètre");
		param2 = new Label(menu, new Vec2f(x, y+10), 0, new Vec2f(30), "Parametre");
		param3 = new Label(menu, new Vec2f(x, y+20), 0, new Vec2f(30), "Parametre");
		param4 = new Label(menu, new Vec2f(x, y+30), 0, new Vec2f(30), "Parametre");
		
		param1.down = param2;
		param1.up = param4;
		
		param2.down = param3;
		param2.up = param1;
		
		param3.down = param4;
		param3.up = param2;
		
		param4.down = param1;
		param4.up = param3;
		
		menu.focus = param1;
		
		selec = new UiImage(menu, new Vec2f(menu.focus.pos.x, menu.focus.pos.y+2.25f), 0, new Vec2f(40 , 8), texture2, new Vec4f(1, 1, 1, 1));
		super.init();
	}

	@Override
	public void update(double delta) {
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_UP))
			menu.focusUp();
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_DOWN))
			menu.focusDown();
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_LEFT))
			menu.focusLeft();
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_RIGHT))
			menu.focusRight();
		selec.pos.x = menu.focus.pos.x;
		selec.pos.y = Utils.lerpF(selec.pos.y, menu.focus.pos.y+2.25f, Utils.clampD(delta*20, 0, 1) );
		menu.update();
		super.update(delta);
	}
	
	@Override
	public void draw() {
		super.draw();
		menu.draw();
	}

}
