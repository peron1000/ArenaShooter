package arenashooter.game.gameStates;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Label;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.UiImage;
import arenashooter.game.Controller;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Score extends GameState {

	private Menu menu = new Menu(5);

	public Score() {
		super(1);
	}

	@Override
	public void init() {

		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Score.png");
		texture1.setFilter(false);
		UiImage bg = new UiImage(0, new Vec2f(177.78, 100), texture1, new Vec4f(1, 1, 1, 1));
		menu.setBackground(bg);

		Texture texButtonA = Texture.loadTexture("data/sprites/interface/Button_A.png");
		Texture texButtonY = Texture.loadTexture("data/sprites/interface/Button_Y.png");
		Texture texButtonB = Texture.loadTexture("data/sprites/interface/Button_B.png");

		texButtonA.setFilter(false);
		texButtonY.setFilter(false);
		texButtonB.setFilter(false);
		UiImage butA = new UiImage(0, new Vec2f(texButtonA.getWidth() / 2, texButtonA.getHeight() / 2), texButtonA,
				new Vec4f(1, 1, 1, 1));
		UiImage butY = new UiImage(0, new Vec2f(texButtonY.getWidth() / 2, texButtonY.getHeight() / 2), texButtonY,
				new Vec4f(1, 1, 1, 1));
		UiImage butB = new UiImage(0, new Vec2f(texButtonB.getWidth() / 2, texButtonB.getHeight() / 2), texButtonB,
				new Vec4f(1, 1, 1, 1));
		menu.addUiElement(butA, 2);
		menu.addUiElement(butY, 2);
		menu.addUiElement(butB, 2);
		butA.setPos(new Vec2f(-67, 41));
		butY.setPos(new Vec2f(-30, 41));
		butB.setPos(new Vec2f(7, 41));

		Label labelA = new Label(0, new Vec2f(25, 25), "Rematch"), labelY = new Label(0, new Vec2f(25, 25), "New Game"),
				labelB = new Label(0, new Vec2f(25, 25), "Back to Menu"),
				labelAInfo = new Label(0, new Vec2f(20, 20), "(Space)"),
				labelYInfo = new Label(0, new Vec2f(20, 20), "(R)"),
				labelBInfo = new Label(0, new Vec2f(20, 20), "(Esc)");
		labelAInfo.setColor(new Vec4f(0, 0, 0, 0.25));
		labelYInfo.setColor(new Vec4f(0, 0, 0, 0.25));
		labelBInfo.setColor(new Vec4f(0, 0, 0, 0.25));
		menu.addUiElement(labelA, 4);
		menu.addUiElement(labelY, 4);
		menu.addUiElement(labelB, 4);
		menu.addUiElement(labelAInfo, 4);
		menu.addUiElement(labelYInfo, 4);
		menu.addUiElement(labelBInfo, 4);
		labelA.setPos(new Vec2f(-67, 40));
		labelY.setPos(new Vec2f(-30, 40));
		labelB.setPos(new Vec2f(7, 40));
		labelAInfo.setPos(new Vec2f(-67, 43));
		labelYInfo.setPos(new Vec2f(-30, 43));
		labelBInfo.setPos(new Vec2f(7, 43));

		super.init();
	}

	@Override
	public void update(double d) {

		// Detect controls
		for (Controller controller : GameMaster.gm.controllers) {
			if (Input.actionJustPressed(controller.getDevice(), Action.JUMP)) {
				Texture tex = Texture.loadTexture("data/sprites/interface/Button_A_Activated.png");
				tex.setFilter(false);
				UiImage but = new UiImage(0, new Vec2f(tex.getWidth() / 2, tex.getHeight() / 2), tex,
						new Vec4f(1, 1, 1, 1));
				menu.addUiElement(but, 3);
				but.setPos(new Vec2f(-67, 41));
			} else if (Input.actionJustReleased(controller.getDevice(), Action.JUMP)) {
				Object[] variable = GameParam.maps.toArray();
				String[] chosenMaps = new String[variable.length];
				for (int i = 0; i < variable.length; i++) {
					chosenMaps[i] = (String) variable[i];
				}
				GameMaster.gm.requestNextState(new Game(GameParam.maps.size()), chosenMaps);
			} else if (Input.actionJustPressed(controller.getDevice(), Action.DROP_ITEM)) {
				Texture tex = Texture.loadTexture("data/sprites/interface/Button_Y_Activated.png");
				tex.setFilter(false);
				UiImage but = new UiImage(0, new Vec2f(tex.getWidth() / 2, tex.getHeight() / 2), tex,
						new Vec4f(1, 1, 1, 1));
				menu.addUiElement(but, 3);
				but.setPos(new Vec2f(-30, 41));				
			} else if (Input.actionJustReleased(controller.getDevice(), Action.DROP_ITEM)) {
				GameMaster.gm.requestNextState(new Config(), GameMaster.mapEmpty);
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_BACK)) {
				Texture tex = Texture.loadTexture("data/sprites/interface/Button_B_Activated.png");
				tex.setFilter(false);
				UiImage but = new UiImage(0, new Vec2f(tex.getWidth() / 2, tex.getHeight() / 2), tex,
						new Vec4f(1, 1, 1, 1));
				menu.addUiElement(but, 3);
				but.setPos(new Vec2f(7, 41));
			} else if (Input.actionJustReleased(controller.getDevice(), Action.UI_BACK)) {
				GameMaster.gm.requestNextState(new Start(), GameMaster.mapEmpty);
			}
		}

		super.update(d);
		menu.update(d);
	}

	public void draw() {
		super.draw();
		menu.draw();
	}
}