package arenashooter.game.gameStates;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.Controller;
import arenashooter.game.ControllerPlayer;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Score extends GameState {

	private Menu menu = new Menu(5);
	private Menu menuscore = new Menu(5);

	public Score() {
		super(1);
	}

	@Override
	public void init() {

		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Score.png");
		texture1.setFilter(false);
		UiImage bg = new UiImage(0, new Vec2f(177.78, 100), texture1, new Vec4f(1));
		menu.setBackground(bg);

		Texture texButtonA = Texture.loadTexture("data/sprites/interface/Button_A.png");
		Texture texButtonY = Texture.loadTexture("data/sprites/interface/Button_Y.png");
		Texture texButtonB = Texture.loadTexture("data/sprites/interface/Button_B.png");

		texButtonA.setFilter(false);
		texButtonY.setFilter(false);
		texButtonB.setFilter(false);
		UiImage butA = new UiImage(0, new Vec2f(texButtonA.getWidth() / 2, texButtonA.getHeight() / 2), texButtonA, new Vec4f(1));
		UiImage butY = new UiImage(0, new Vec2f(texButtonY.getWidth() / 2, texButtonY.getHeight() / 2), texButtonY, new Vec4f(1));
		UiImage butB = new UiImage(0, new Vec2f(texButtonB.getWidth() / 2, texButtonB.getHeight() / 2), texButtonB, new Vec4f(1));
		menu.addUiElement(butA, 2);
		menu.addUiElement(butY, 2);
		menu.addUiElement(butB, 2);
		butA.setPosition(new Vec2f(-67, 41));
		butY.setPosition(new Vec2f(-30, 41));
		butB.setPosition(new Vec2f(7, 41));

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
		labelA.setPosition(new Vec2f(-67, 40));
		labelY.setPosition(new Vec2f(-30, 40));
		labelB.setPosition(new Vec2f(7, 40));
		labelAInfo.setPosition(new Vec2f(-67, 43));
		labelYInfo.setPosition(new Vec2f(-30, 43));
		labelBInfo.setPosition(new Vec2f(7, 43));

		
		//Display scores
		Controller winner = GameMaster.gm.controllers.get(0);
		Controller killer = GameMaster.gm.controllers.get(0);
		Controller survivor = GameMaster.gm.controllers.get(0);
		Controller bestest = GameMaster.gm.controllers.get(0);
		
		for(Controller controller : GameMaster.gm.controllers) {
			if(controller.deaths < survivor.deaths)
				survivor = controller;

			if(controller.kills > killer.kills)
				killer = controller;
			
			if(controller.deaths > bestest.deaths)
				bestest = controller;
			
			if(controller.roundsWon > bestest.roundsWon)
				winner = controller;
		}
		
		///Winner
		float x = -16.6666667f;
		//Portrait
		UiImage image = new UiImage(0, new Vec2f(23), winner.getPortrait());
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, -22));
		//Number
		Texture tex = Texture.loadTexture("data/sprites/interface/Player_"+(winner.playerNumber+1)+"_Arrow.png");
		tex.setFilter(false);
		image = new UiImage(0, new Vec2f(15), tex);
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, -38));
		//Medal
		tex = Texture.loadTexture("data/sprites/interface/medals/medal_winner.png");
		tex.setFilter(false);
		image = new UiImage(0, new Vec2f(24), tex);
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, 0));
		
		///Killer
		x = -50;
		//Portrait
		image = new UiImage(0, new Vec2f(23), killer.getPortrait());
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, -22));
		//Number
		tex = Texture.loadTexture("data/sprites/interface/Player_"+(killer.playerNumber+1)+"_Arrow.png");
		tex.setFilter(false);
		image = new UiImage(0, new Vec2f(15), tex);
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, -38));
		//Medal
		tex = Texture.loadTexture("data/sprites/interface/medals/medal_kills.png");
		tex.setFilter(false);
		image = new UiImage(0, new Vec2f(24), tex);
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, 0));

		///Killer
		x = 16.6666667f;
		//Portrait
		image = new UiImage(0, new Vec2f(23), survivor.getPortrait());
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, -22));
		//Number
		tex = Texture.loadTexture("data/sprites/interface/Player_"+(survivor.playerNumber+1)+"_Arrow.png");
		tex.setFilter(false);
		image = new UiImage(0, new Vec2f(15), tex);
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, -38));
		//Medal
		tex = Texture.loadTexture("data/sprites/interface/medals/medal_survivor.png");
		tex.setFilter(false);
		image = new UiImage(0, new Vec2f(24), tex);
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, 0));
		
		///Bestest player
		x = 50;
		//Portrait
		image = new UiImage(0, new Vec2f(23), bestest.getPortrait());
		image.rotation = .028;
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, -22));
		//Number
		tex = Texture.loadTexture("data/sprites/interface/Player_"+(bestest.playerNumber+1)+"_Arrow.png");
		tex.setFilter(false);
		image = new UiImage(0, new Vec2f(15), tex);
		image.rotation = -.035;
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, -38));
		//Medal
		tex = Texture.loadTexture("data/sprites/interface/medals/medal_congration.png");
		tex.setFilter(false);
		image = new UiImage(0, new Vec2f(24), tex);
		image.rotation = .056;
		menu.addUiElement(image, 4);
		image.setPosition(new Vec2f(x, 0));

		super.init();
	}
	
	@Override
	public void update(double d) {

		// Detect controls
		for (Controller controller : GameMaster.gm.controllers) {
			if(!(controller instanceof ControllerPlayer)) continue;
			ControllerPlayer pc = (ControllerPlayer) controller;
			
			if (Input.actionJustPressed(pc.getDevice(), Action.JUMP)) {
				Texture tex = Texture.loadTexture("data/sprites/interface/Button_A_Activated.png");
				tex.setFilter(false);
				UiImage but = new UiImage(0, new Vec2f(tex.getWidth() / 2, tex.getHeight() / 2), tex, new Vec4f(1));
				menu.addUiElement(but, 3);
				but.setPosition(new Vec2f(-67, 41));
			} else if (Input.actionJustReleased(pc.getDevice(), Action.JUMP)) {
				Object[] variable = GameParam.maps.toArray();
				String[] chosenMaps = new String[variable.length];
				for (int i = 0; i < variable.length; i++) {
					chosenMaps[i] = (String) variable[i];
				}
				GameMaster.gm.requestNextState(new Game(GameParam.maps.size()), chosenMaps);
			} else if (Input.actionJustPressed(pc.getDevice(), Action.DROP_ITEM)) {
				Texture tex = Texture.loadTexture("data/sprites/interface/Button_Y_Activated.png");
				tex.setFilter(false);
				UiImage but = new UiImage(0, new Vec2f(tex.getWidth() / 2, tex.getHeight() / 2), tex, new Vec4f(1));
				menu.addUiElement(but, 3);
				but.setPosition(new Vec2f(-30, 41));
			} else if (Input.actionJustReleased(pc.getDevice(), Action.DROP_ITEM)) {
				GameMaster.gm.requestNextState(new Config(), GameMaster.mapEmpty);
			} else if (Input.actionJustPressed(pc.getDevice(), Action.UI_BACK)) {
				Texture tex = Texture.loadTexture("data/sprites/interface/Button_B_Activated.png");
				tex.setFilter(false);
				UiImage but = new UiImage(0, new Vec2f(tex.getWidth() / 2, tex.getHeight() / 2), tex, new Vec4f(1));
				menu.addUiElement(but, 3);
				but.setPosition(new Vec2f(7, 41));
			} else if (Input.actionJustReleased(pc.getDevice(), Action.UI_BACK)) {
				GameMaster.gm.requestNextState(new Start(), GameMaster.mapEmpty);
			}
		}

		super.update(d);
		menu.update(d);
	}

	public void draw() {
		super.draw();
		Window.beginUi();
		menu.draw();
		menuscore.draw();
		Window.endUi();
	}
}