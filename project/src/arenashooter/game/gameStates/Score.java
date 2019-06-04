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
		}
		winner = survivor; //Deathmatch
		
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
		image = new UiImage(0, new Vec2f(23), survivor.getPortrait());
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
		
		
//		/* Shows score */
//		int kill = 0, death = 0;
//		Double ratio = 0.0;
//		int x = 0, y = -40;
//		Controller Winner = null, PlayerKiller = null, TheDeath = null;
//		menuscore.setPosition(new Vec2f(x, y));
//		for (Controller controller : GameMaster.gm.controllers) {
//			if (controller.kills >= kill) {
//				PlayerKiller = controller;
//			}
//			if (controller.deaths >= death) {
//				TheDeath = controller;
//			}
//			if (Winner != null) {
//				if (controller.roundsWon >= Winner.roundsWon) {
//					Winner = controller;
//				}
//			} else {
//				Winner = controller;
//			}
//
//		}
//
//		int killk = PlayerKiller.kills, deathk = PlayerKiller.deaths,
//
//				killd = TheDeath.kills, deathd = TheDeath.deaths;
//
//		kill = Winner.kills;
//		death = Winner.deaths;
//		double ratiod, ratiok;
//
//		if (death > 0) {
//			ratio = (double) (kill / death);
//		} else {
//			ratio = 1.0;
//		}
//		if (deathk > 0) {
//			ratiok = (double) (killk / deathk);
//		} else {
//			ratiok = 1.0;
//		}
//		if (deathd > 0) {
//			ratiod = (double) (killd / deathd);
//		} else {
//			ratiod = 1.0;
//		}
//		//int ww = Winner.playerNumber + 1, kk = +1, dd = -1;
//		/* pl winner */
//		Texture txw1 = Texture.loadTexture("data/sprites/interface/Player_" + (Winner.playerNumber + 1) + "_Arrow.png");
//		UiImage winnerp = new UiImage(0, new Vec2f(txw1.getWidth() * 2, txw1.getHeight() * 2), txw1,
//				new Vec4f(1, 1, 1, 1));
//		menuscore.addUiElement(winnerp, 4);
//		winnerp.setPosition(new Vec2f(0, 0));
//
//		Label p1 = new Label(0, new Vec2f(25, 25), "The winner : Player " + (Winner.playerNumber + 1) + " : kill(s) : "
//				+ kill + " | death(s) : " + death + " |" + " Ratio k/d : " + ratio);
//		p1.setVisible(true);
//		menuscore.addUiElement(p1, 4);
//		p1.setPosition(new Vec2f(x, y));
//		y += 6;
//
//		Label p2 = new Label(0, new Vec2f(25, 25), "The Player_Killer : Player " + (PlayerKiller.playerNumber + 1)
//				+ " : kill(s) : " + killk + " | death(s) : " + deathk + " |" + " Ratio k/d : " + ratiok);
//		p2.setVisible(true);
//		menuscore.addUiElement(p2, 4);
//		p2.setPosition(new Vec2f(x, y));
//		y += 6;
//		Label p3 = new Label(0, new Vec2f(25, 25), "The DEATH : Player " + (TheDeath.playerNumber + 1) + " : kill(s) : "
//				+ killd + " | death(s) : " + deathd + " |" + " Ratio k/d : " + ratiod);
//		p3.setVisible(true);
//		menuscore.addUiElement(p3, 4);
//		p3.setPosition(new Vec2f(x, y));

//		CharacterSprite caracSprite = new CharacterSprite(new Vec2f(0, 0), Winner.info);
//		caracSprite.attachToParent(current, "PlayerSprite_" + Winner.playerNumber+1);
//		Label newNumber = new Label(0,new Vec2f(),
//				"data/sprites/interface/Player_" + (Winner.playerNumber+1) + "_Arrow.png");

//		CharacterSprite caracSprite = new CharacterSprite(new Vec2f(0, 0), Winner.info);
//		caracSprite.attachToParent(getMap(), "aaa");
//		//Texture txw2 = Texture.loadTexture(Winner.info.getSkin());
		// UiImage winnerpsp = new UiImage(0, new
		// Vec2f(txw2.getWidth()*2,txw2.getHeight()*2),txw2 , new Vec4f(1, 1, 1, 1));
		// menuscore.addUiElement(winnerpsp, 4);
		// winnerpsp.setPos(new Vec2f(0, 0));

//		arenashooter.entities.spatials.Character w = Winner.createNewCharacter(new Vec2f(0, -5));
//		w.attachToParent(getMap(), "zzz1");
//
//		arenashooter.entities.spatials.Character k = Winner.createNewCharacter(new Vec2f(-2, -4));
//		k.attachToParent(getMap(), "zzz2");
//
//		arenashooter.entities.spatials.Character d = Winner.createNewCharacter(new Vec2f(2, -3));
//		d.attachToParent(getMap(), "zzz3");

		// CharacterSprite caracSprite = new CharacterSprite(new Vec2f(0, 0),
		// Winner.info);
		// caracSprite.attachToParent(current, "PlayerSprite_" + Winner.playerNumber+1);

//		Texture txw2 = Texture.loadTexture("data/sprites/characters/" + Winner.info.getSkin() + "/head.png");
//		UiImage winnerp2 = new UiImage(0, new Vec2f(txw2.getWidth() / 2, txw2.getHeight() / 2), txw2,
//				new Vec4f(1, 1, 1, 1));
//		menuscore.addUiElement(winnerp2, 4);
//		winnerp2.setPosition(new Vec2f(-49, -32));

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
				but.setPosition(new Vec2f(-67, 41));
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
				but.setPosition(new Vec2f(-30, 41));
			} else if (Input.actionJustReleased(controller.getDevice(), Action.DROP_ITEM)) {
				GameMaster.gm.requestNextState(new Config(), GameMaster.mapEmpty);
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_BACK)) {
				Texture tex = Texture.loadTexture("data/sprites/interface/Button_B_Activated.png");
				tex.setFilter(false);
				UiImage but = new UiImage(0, new Vec2f(tex.getWidth() / 2, tex.getHeight() / 2), tex,
						new Vec4f(1, 1, 1, 1));
				menu.addUiElement(but, 3);
				but.setPosition(new Vec2f(7, 41));
			} else if (Input.actionJustReleased(controller.getDevice(), Action.UI_BACK)) {
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