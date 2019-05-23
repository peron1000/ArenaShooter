package arenashooter.game.gameStates;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.events.menus.MenuExitEvent;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Button;
import arenashooter.engine.ui.MenuSelection;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiImage;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.engineParam.GameMode;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Config extends GameState {

	private GameParam gameParam;
	private MenuSelectionV<Button> menuParam = new MenuSelectionV<Button>(10);
	private MenuSelection<UiImage> menuMap = new MenuSelection<>(10);
	private UiImage selec;
	private Button param1, param2, param3, param4;
	private boolean activated = false;
	private ArrayList<File> maps = new ArrayList<>();
	private InputListener inputs = new InputListener();

	public Config() {
		super(1);
		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void action(InputActionEvent e) {
				switch (e.getAction()) {
				case UI_RIGHT:
					if (e.getActionState() == ActionState.JUST_PRESSED) {
						if (activated && menuParam.active.getValue()) {
							menuParam.getTarget().lunchAction("right");
						} else if (menuParam.active.getValue()) {
							menuParam.right();
						} else if (menuMap.active.getValue()) {
							menuMap.right();
						}
					}
					break;
				case UI_LEFT:
					if (e.getActionState() == ActionState.JUST_PRESSED) {
						if (activated && menuParam.active.getValue()) {
							menuParam.getTarget().lunchAction("left");
						} else if (menuParam.active.getValue()) {
							menuParam.left();
						} else if (menuMap.active.getValue()) {
							menuMap.left();
						}
					}
					break;
				case UI_UP:
					if (e.getActionState() == ActionState.JUST_PRESSED) {
						if (!activated && menuParam.active.getValue()) {
							menuParam.up();
						} else if (menuMap.active.getValue()) {
							menuMap.up();
						}
					}
					break;
				case UI_DOWN:
					if (e.getActionState() == ActionState.JUST_PRESSED) {
						if (!activated && menuParam.active.getValue()) {
							menuParam.down();
						} else if (menuMap.active.getValue()) {
							menuMap.down();
						}
					}
					break;
				case UI_OK:
					if (e.getActionState() == ActionState.JUST_PRESSED) {
						if (menuParam.active.getValue()) {
							activated = !activated;
							if (activated) {
								menuParam.getTarget().setColorFond(new Vec4f(0.5, 0.5, 0.5, 1));
								menuParam.getTarget().setColorText(new Vec4f(0, 0, 0, 1));
							} else {
								menuParam.getTarget().setColorFond(new Vec4f(0, 0, 0, 1));
								menuParam.getTarget().setColorText(new Vec4f(1, 1, 1, 1));
							}
						} else if (menuMap.active.getValue()) {
							menuMap.getElemSelec().lunchAction("selec");
						}
					}
					break;
				case UI_CONTINUE:
					if (e.getActionState() == ActionState.JUST_PRESSED) {
						if (!GameParam.maps.isEmpty()) {
							GameMaster.gm.requestNextState(new CharacterChooser(), GameMaster.mapEmpty);
						} else {
							Exception exc = new Exception("Choisissez au moins une map");
							exc.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
			}
		});
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
			if (folderContent[i].getName().endsWith(".xml") && !folderContent[i].getName().startsWith("menu_")) {
				maps.add(folderContent[i]);
			}
		}

		Collections.sort(maps);
		maps.sort(new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

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

		menuParam.exit = new EventListener<MenuExitEvent>() {

			@Override
			public void action(MenuExitEvent e) {
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
		menuMap.exit = new EventListener<MenuExitEvent>() {

			@Override
			public void action(MenuExitEvent e) {
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
		if (GameParam.getGameMode() == GameMode.Rixe) {
			menuParam.addElementInListOfChoices(param3, 1);
		} else {
			menuParam.removeElementInListOfChoices(param3);
		}

		inputs.step(delta);
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
