package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiGroup;
import arenashooter.engine.ui.UiImageLabel;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.Controller;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;

public class Score extends GameState {

	private InputListener inputs = new InputListener();

	private UiGroup<UiElement> group = new UiGroup<>();
	
	public Score() {
		super(GameMaster.mapEmpty);
	}

	private void setPositions(UiGroup<UiImage> group, double y, double scale) {
		final double x = 100 * Main.getRenderer().getRatio() / 4;
		double i = -1.5;
		for (UiImage uiImage : group) {
			uiImage.setScale(scale);
			uiImage.setPosition(x * i, y);
			i++;
		}
	}

	@Override
	public void init() {
		UiImage bg = new UiImage(Main.getRenderer().loadTexture("data/sprites/interface/Fond Menu_Score.png"), new Vec4f(1));
		bg.setScale(177.78, 100);
		group.setBackground(bg);

		Texture texButtonA = Main.getRenderer().loadTexture("data/sprites/interface/Button_A.png");
		Texture texButtonY = Main.getRenderer().loadTexture("data/sprites/interface/Button_Y.png");
		Texture texButtonB = Main.getRenderer().loadTexture("data/sprites/interface/Button_B.png");

		UiImageLabel buttonA = new UiImageLabel(new UiImage(texButtonA, new Vec4f(1)), texButtonA.getWidth() / 2,
				texButtonA.getHeight() / 2);
		UiImageLabel buttonY = new UiImageLabel(new UiImage(texButtonY, new Vec4f(1)), texButtonY.getWidth() / 2,
				texButtonY.getHeight() / 2);
		UiImageLabel buttonB = new UiImageLabel(new UiImage(texButtonB, new Vec4f(1)), texButtonB.getWidth() / 2,
				texButtonB.getHeight() / 2);

		final double sizeMain = 3, size2 = 2, pourcent1 = 0.55, pourcent2 = 0.75;

		Label labelA = new Label("Rematch", sizeMain), labelY = new Label("New Game", sizeMain),
				labelB = new Label("Back to Menu", sizeMain), labelAInfo = new Label("(Space)", size2),
				labelYInfo = new Label("(R)", size2), labelBInfo = new Label("(Esc)", size2);
		labelAInfo.setColor(new Vec4f(0, 0, 0, 0.25));
		labelYInfo.setColor(new Vec4f(0, 0, 0, 0.25));
		labelBInfo.setColor(new Vec4f(0, 0, 0, 0.25));

		buttonA.addLabel(labelA, pourcent1);
		buttonA.addLabel(labelAInfo, pourcent2);

		buttonY.addLabel(labelY, pourcent1);
		buttonY.addLabel(labelYInfo, pourcent2);

		buttonB.addLabel(labelB, pourcent1);
		buttonB.addLabel(labelBInfo, pourcent2);

		UiGroup<UiImageLabel> groupButtons = new UiGroup<>();
		groupButtons.addElements(buttonA, buttonY, buttonB);
		float i = -67;
		for (UiImageLabel uiImageLabel : groupButtons) {
			uiImageLabel.setPosition(i, 41);
			i += 37; // spacing between buttons
		}
		group.addElement(groupButtons);

		// Display scores
		Controller winner = Main.getGameMaster().controllers.get(0);
		Controller killer = Main.getGameMaster().controllers.get(0);
		Controller survivor = Main.getGameMaster().controllers.get(0);
		Controller bestest = Main.getGameMaster().controllers.get(0);

		for (Controller controller : Main.getGameMaster().controllers) {
			if (controller.deaths < survivor.deaths)
				survivor = controller;

			if (controller.kills > killer.kills)
				killer = controller;

			if (controller.deaths > bestest.deaths)
				bestest = controller;

			if (controller.roundsWon > bestest.roundsWon)
				winner = controller;
		}
		for (Controller controller : Main.getGameMaster().controllers) {
			controller.resetScore();
		}

		// Medals
		UiGroup<UiImage> medals = new UiGroup<>();
		medals.addElements(new UiImage(Main.getRenderer().loadTexture("data/sprites/interface/medals/medal_winner.png")),
				new UiImage(Main.getRenderer().loadTexture("data/sprites/interface/medals/medal_kills.png")),
				new UiImage(Main.getRenderer().loadTexture("data/sprites/interface/medals/medal_survivor.png")),
				new UiImage(Main.getRenderer().loadTexture("data/sprites/interface/medals/medal_congration.png")));

		setPositions(medals, 0, 24);

		// Portraits
		UiGroup<UiImage> portraits = new UiGroup<>();
		portraits.addElements(new UiImage(winner.getPortrait()), new UiImage(killer.getPortrait()),
				new UiImage(survivor.getPortrait()), new UiImage(bestest.getPortrait()));
		setPositions(portraits, -25, 23);

		// Numbers
		UiGroup<UiImage> numbers = new UiGroup<>();
		numbers.addElements(
				new UiImage(Main.getRenderer()
						.loadTexture("data/sprites/interface/Player_" + (winner.playerNumber + 1) + "_Arrow.png")),
				new UiImage(Main.getRenderer()
						.loadTexture("data/sprites/interface/Player_" + (killer.playerNumber + 1) + "_Arrow.png")),
				new UiImage(Main.getRenderer()
						.loadTexture("data/sprites/interface/Player_" + (survivor.playerNumber + 1) + "_Arrow.png")),
				new UiImage(Main.getRenderer()
						.loadTexture("data/sprites/interface/Player_" + (bestest.playerNumber + 1) + "_Arrow.png")));
		setPositions(numbers, -40, 8);

		group.addElements(medals, portraits, numbers);

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_OK: //TODO: Use UI action
						buttonA.setImage(
								new UiImage(Main.getRenderer().loadTexture("data/sprites/interface/Button_A_Activated.png")));
						break;
					case DROP_ITEM: //TODO: Use UI action
						buttonY.setImage(
								new UiImage(Main.getRenderer().loadTexture("data/sprites/interface/Button_Y_Activated.png")));
						break;
					case UI_BACK:
						buttonB.setImage(
								new UiImage(Main.getRenderer().loadTexture("data/sprites/interface/Button_B_Activated.png")));
						break;

					default:
						break;
					}
				} else if(event.getActionState() == ActionState.JUST_RELEASED) {
					switch (event.getAction()) {
					case UI_OK: //TODO: Use UI action
						// Rematch
//						Main.getGameMaster().requestNewGame(GameParam.mapsString());
//						Main.getGameMaster().requestGame();
						Main.log.info("reviendra plus tard ... fonctionnalit� � refaire");
						break;
					case DROP_ITEM: //TODO: Use UI action
						// New Game
						Main.getGameMaster().requestNextState(new Config());
						break;
					case UI_BACK:
						// Back to menu
						Main.getGameMaster().requestNextState(new MenuStart());
						break;
					default:
						break;
					}
				}
			}
		});

		super.init();
	}

	@Override
	public void update(double d) {
		inputs.step(d);
		for (UiElement element : group) {
			element.update(d);
		}

//		// Detect controls
//		for (Controller controller : GameMaster.gm.controllers) {
//			if (!(controller instanceof ControllerPlayer))
//				continue;
//			ControllerPlayer pc = (ControllerPlayer) controller;
//			if (Input.actionJustPressed(pc.getDevice(), Action.JUMP)) {
//			} else if (Input.actionJustReleased(pc.getDevice(), Action.JUMP)) {
//				Object[] variable = GameParam.maps.toArray();
//				String[] chosenMaps = new String[variable.length];
//				for (int i = 0; i < variable.length; i++) {
//					chosenMaps[i] = (String) variable[i];
//				}
//				GameMaster.gm.requestNextState(new Game(GameParam.maps.size()), chosenMaps);
//			} else if (Input.actionJustPressed(pc.getDevice(), Action.DROP_ITEM)) {
//			} else if (Input.actionJustReleased(pc.getDevice(), Action.DROP_ITEM)) {
//				GameMaster.gm.requestNextState(new Config(), GameMaster.mapEmpty);
//			} else if (Input.actionJustPressed(pc.getDevice(), Action.UI_BACK)) {
//			} else if (Input.actionJustReleased(pc.getDevice(), Action.UI_BACK)) {
//				GameMaster.gm.requestNextState(new Start(), GameMaster.mapEmpty);
//			}
//		}

		super.update(d);
	}

	@Override
	public void draw() {
		super.draw();
		Main.getRenderer().beginUi();
		group.draw();
		Main.getRenderer().endUi();
	}
}