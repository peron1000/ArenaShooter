package arenashooter.game.gameStates;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

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
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiImage;
import arenashooter.entities.Controller;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.engineParam.GameMode;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Param extends GameState {

	private GameParam gameParam;
	private MenuSelectionV<Button> menuParam = new MenuSelectionV<Button>(10);
	private MenuSelection<UiImage> menuMap = new MenuSelection<>(10);
	private UiImage selec;
	private Button param1, param2, param3, param4;
	private boolean activated = false;
	private ArrayList<File> maps = new ArrayList<>();

	public Param() {
		super(1);
	}

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
		menuMap.setEcartementX(14);
		menuMap.setEcartementY(14);
		menuMap.setImageSelec(new UiImage(0, new Vec2f(15), texture2, new Vec4f(1, 1, 1, 1)), 3);
		menuMap.setPosition(new Vec2f(-9, -30));
		File mapFolder = new File("data/mapXML");
		File[] folderContent = mapFolder.listFiles();
		for (int i = 0; i < folderContent.length; i++) {
			if (folderContent[i].getName().endsWith(".xml")) {
				maps.add(folderContent[i]);
			}
		}

		Collections.sort(maps);

		int y = 0, i = 0, nbElemH = 7;
		for (File file : maps) {
			Texture image = Texture.loadTexture(
					"data/MAP_VIS/" + file.getName().substring(0, file.getName().lastIndexOf('.')) + ".png");
			int x = i % nbElemH;
			if (x == 0 && i != 0)
				y++;
			UiImage picture = new UiImage(0, new Vec2f(6), image, new Vec4f(1, 1, 1, 1));
			menuMap.put(picture, 1, x, y);
			picture.addAction("selec", new Trigger() {

				@Override
				public void make() {
					if (GameParam.maps.contains(file.getPath())) {
						GameParam.maps.remove(file.getPath());
						picture.setScale(new Vec2f(6));
					} else {
						GameParam.maps.add(file.getPath());
						picture.setScale(new Vec2f(12));
					}
				}
			});
			i++;
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
					menuParam.active.setValue(false);
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
					menuParam.active.setValue(true);
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
		for (Device device : Device.values()) {
			if (Input.actionJustPressed(device, Action.UI_UP)) {
				if (!activated && menuParam.active.getValue()) {
					menuParam.up(delta);
				} else if (menuMap.active.getValue()) {
					menuMap.up();
				}
			}
			if (Input.actionJustPressed(device, Action.UI_DOWN)) {
				if (!activated && menuParam.active.getValue()) {
					menuParam.down(delta);
				} else if (menuMap.active.getValue()) {
					menuMap.down();
				}
			}
			if (Input.actionJustPressed(device, Action.UI_LEFT)) {
				if (activated && menuParam.active.getValue()) {
					menuParam.getTarget().lunchAction("left");
				} else if (menuParam.active.getValue()) {
					menuParam.left();
				} else if (menuMap.active.getValue()) {
					menuMap.left();
				}
			}
			if (Input.actionJustPressed(device, Action.UI_RIGHT)) {
				if (activated && menuParam.active.getValue()) {
					menuParam.getTarget().lunchAction("right");
				} else if (menuParam.active.getValue()) {
					menuParam.right();
				} else if (menuMap.active.getValue()) {
					menuMap.right();
				}
			}
			if (Input.actionJustPressed(device, Action.JUMP)) {
				if (menuParam.active.getValue()) {
					activated = !activated;
				}
				if (menuMap.active.getValue()) {
					menuMap.getElemSelec().lunchAction("selec");
				}
			}
			if (Input.actionJustPressed(device, Action.UI_BACK)) {
				if (activated)
					activated = false;
			}
			if (Input.actionJustPressed(device, Action.UI_OK)) {
				if (!GameParam.maps.isEmpty())
					GameMaster.gm.requestNextState(new CharacterChooser(), GameMaster.mapEmpty);
				else {
					Exception e = new Exception("Choisissez au moins une map");
					e.printStackTrace();
				}
			}
		}

		if (GameParam.getGameMode() == GameMode.Rixe) {
			menuParam.addElementInListOfChoices(param3, 1);
		} else {
			menuParam.removeElementInListOfChoices(param3);
		}

		menuParam.update(delta);
		menuMap.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		menuParam.draw();
		menuMap.draw();
	}

}
