package arenashooter.game.gameStates;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Imageinput;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiGridWeak;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Config extends GameState {

	private GameParam gameParam = new GameParam();
	private UiGridWeak menu = new UiGridWeak();
	private UiImage background, selector, enter;
	private InputListener inputs = new InputListener();
	private ScrollerH<Integer> rounds;
	private UiImage escape, space, a, b;
	private Label confirm, back, nextgm = new Label("Character Chooser", 2), arenaName = new Label("Test", 8);
	private Map<UiImage, String> pictureName = new HashMap<>();

	public Config() {
		super(GameMaster.mapEmpty);
		// Button_Start.png
		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent e) {
				if (e.getActionState() == ActionState.JUST_PRESSED) {
					switch (e.getAction()) {
					case UI_RIGHT:
						menu.rightAction();
						break;
					case UI_LEFT:
						menu.leftAction();
						break;
					case UI_UP:
						menu.upAction();
						break;
					case UI_DOWN:
						menu.downAction();
						break;
					case UI_OK:
						if (!menu.selectAction() && menu.getTarget().hasAction("selec")) {
							menu.getTarget().lunchAction("selec");
						}
						break;
					case UI_CONTINUE:
						if (!menu.continueAction()) {
							if (!gameParam.maps.isEmpty()) {
								Main.getGameMaster().requestNextState(new CharacterChooser());
							}
						}
						break;
					case UI_BACK:
						if (!menu.backAction()) {
							Main.getGameMaster().requestPreviousState();
						}
						break;
					case UI_CANCEL:
						menu.cancelAction();
						break;
					case UI_CHANGE:
						menu.changeAction();
						break;
					default:
						break;
					}
				}
				selector.setPositionLerp(menu.getTarget().getPosition().x, menu.getTarget().getPosition().y, 32);
				if (menu.getTarget() == rounds) {
					selector.setScale(47, 12);
				} else {
					selector.setScale(18);
				}

				arenaName.setText(pictureName.getOrDefault(menu.getTarget(), ""));
			}
		});
	}

	private void ui() {
		background = new UiImage(Texture.loadTexture("data/sprites/interface/Fond Menu.png"));
		selector = new UiImage(Texture.loadTexture("data/sprites/interface/Selector_1.png"));
		// boutton_start
		Texture enterTex = Texture.loadTexture("data/sprites/interface/Button_Start.png");
		enter = new UiImage(enterTex);
		enter.setPosition(new Vec2f(72, 39));
		enter.setScale(enterTex.getWidth() / 3, enterTex.getHeight() / 3);

		escape = Imageinput.ESCAPE.getImage();
		space = Imageinput.SPACE.getImage();
		a = Imageinput.A.getImage();
		b = Imageinput.B.getImage();

		background.setScale(180, 100);

		arenaName.setPosition(30, -40);

		UiListVertical<UiElement> vlist = new UiListVertical<>();
		Integer[] roundsValues = { 1, 2, 3, 4, 5, 10, 15, 20, 25, -1 };
		rounds = new ScrollerH<>(roundsValues);
		rounds.setTitle("Round(s)");
		rounds.changeValueView(Integer.valueOf(-1), "\u221E");
		rounds.setBackgroundVisible(true);
		rounds.setBackgroundChange(true);
		UiImage bgU = new UiImage(0, 0, 0, 1), bgS = new UiImage(0.8, 0.8, 0.5, 1);
		rounds.setBackgroundUnselect(bgU);
		rounds.setBackgroundSelect(bgS);
		rounds.setSelectColor(0, 0, 0, 1);
		rounds.setOnValidation(new Trigger() {

			@Override
			public void make() {
				gameParam.setNbRound(rounds.get());
			}
		});
		rounds.setScale(35, 10);
		vlist.addElement(rounds);
		vlist.setPosition(-57.5, -40);

//		bgU.setScale(35, 10);
//		bgS.setScale(35, 10);

		menu.addListVertical(vlist);

		selector.setScale(47, 12);
		// TODO
		space.setScale(space.getScale().x / 2, space.getScale().y / 2);
		space.setPosition(-70, 30);
		space.getMaterial().getParamTex("image").setFilter(false);

		a.setScale(a.getScale().x / 3.142, a.getScale().y / 3.142);
		a.setPosition(-65, 30);
		a.getMaterial().getParamTex("image").setFilter(false);

		confirm = new Label(" : Toggle Selection", 3);
		confirm.setPosition(-50, 30);

		back = new Label(" : Back", 3);
		back.setPosition(-57.5, 38);

		// enter
		nextgm.setPosition(new Vec2f(79, 42));

		escape.setScale(escape.getScale().x / 2, escape.getScale().y / 2);
		escape.setPosition(-70, 38);
		escape.getMaterial().getParamTex("image").setFilter(false);

		b.setScale(b.getScale().x / 3.142, b.getScale().y / 3.142);
		b.setPosition(-65, 38);
		b.getMaterial().getParamTex("image").setFilter(false);
	}

	@Override
	public void init() {

		while (Main.loadingConfig.isAlive()) {
			// wait
		}

		ui();

		List<File> maps = new ArrayList<>(Main.loadingConfig.getFile());
		maps.sort(new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		int i = 0, nbElemH = 7;
		double spacing = 15;
		// init
		List<UiListVertical<UiElement>> everyList = new ArrayList<>(nbElemH);
		for (int j = 0; j < nbElemH; j++) {
			UiListVertical<UiElement> newList = new UiListVertical<>();
			everyList.add(j, newList);
			newList.setPosition(j * spacing - 10, -20);
			newList.setSpacing(spacing);
			menu.addListVertical(newList);
		}

		for (File file : maps) {
			Material thumbnailMat = Material.loadMaterial("data/materials/ui/ui_arena_thumbnail.material");
			thumbnailMat.setParamTex("image", Main.loadingConfig.getTexture(file));
			UiImage picture = new UiImage(thumbnailMat);
			pictureName.put(picture, file.getName().substring(0, file.getName().indexOf('.')));
			double scale = 4.5;
			picture.setScale(14);
			picture.addToScale(-scale);
			Vec4f colorUnselect = new Vec4f(0.8, 0.8, 0.8, 0.75), colorSelect = new Vec4f(1, 1, 1, 1);
			picture.setColor(colorUnselect);
			everyList.get(i % nbElemH).addElement(picture);

			picture.addAction("selec", new Trigger() {
				@Override
				public void make() {
					double lerp = 10;
					if (gameParam.maps.contains(file.getPath())) {
						gameParam.maps.remove(file.getPath());
						picture.addToScaleLerp(-scale, lerp);
						picture.setColor(colorUnselect);
					} else {
						gameParam.maps.add(file.getPath());
						picture.addToScaleLerp(scale, lerp);
						picture.setColor(colorSelect);
					}
				}
			});
			i++;
		}

		selector.setPosition(menu.getTarget().getPosition());

		super.init();
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		menu.update(delta);
		selector.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		// super.draw();
		Window.beginUi();
		background.draw();
		menu.draw();
		selector.draw();
		arenaName.draw();
		a.draw();
		b.draw();
		back.draw();
		enter.draw();
		confirm.draw();
		escape.draw();
		space.draw();
		// nextgm.draw();
		Window.endUi();
	}
	
	public GameParam getGameParam() {
		return gameParam;
	}

}
