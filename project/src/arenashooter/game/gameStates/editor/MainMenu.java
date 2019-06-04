package arenashooter.game.gameStates.editor;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.MultiMenu;
import arenashooter.engine.ui.Navigable;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.xmlReaders.writer.MapXmlWriter;
import arenashooter.entities.Arena;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.MenuStart;
import arenashooter.game.gameStates.editor.editorEnum.Prime;

public class MainMenu implements Navigable {

	// Save & Quit variables
	private Arena arenaConstruction;
	private String fileName = "NewArena";

	private Vec2f position = new Vec2f(Editor.forVisible, 0);
	private MenuSelectionV<UiActionable> addMenu = new MenuSelectionV<>(), setMenu = new MenuSelectionV<>(),
			saveQuitMenu = new MenuSelectionV<>();
	private MultiMenu<Prime> mainMenu = new MultiMenu<>(5, Prime.values(), "data/sprites/interface/Selector.png",
			new Vec2f(30, 10));
	private Vec2f buttonScale = new Vec2f(15, 5);

	public MainMenu(Arena toConstruct) {
		mainMenu.setPosition(new Vec2f(Editor.forVisible, -40));
		mainMenu.addMenu(addMenu, Prime.Add);
		mainMenu.addMenu(setMenu, Prime.Set);
		mainMenu.addMenu(saveQuitMenu, Prime.Exit);

		arenaConstruction = toConstruct;

		saveQuitMenuConstruction();
		
		addMenuConstruction();
	}

	private void saveQuitMenuConstruction() {
		saveQuitMenu.setEcartement(8);
		Button save = new Button(0, buttonScale, "Save"), rename = new Button(0, buttonScale, "Rename File"),
				quit = new Button(0, buttonScale, "Quit");
		save.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		save.setScaleText(new Vec2f(20));
		save.setOnArm(new Trigger() {

			@Override
			public void make() {
				MapXmlWriter.writerMap(arenaConstruction, fileName);
			}
		});
		rename.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		rename.setScaleText(new Vec2f(20));
		rename.setOnArm(new Trigger() {

			@Override
			public void make() {
				// TODO
			}
		});
		quit.setOnArm(new Trigger() {

			@Override
			public void make() {
				GameMaster.gm.requestNextState(new MenuStart(), GameMaster.mapEmpty);
			}
		});
		quit.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		quit.setScaleText(new Vec2f(20));
		saveQuitMenu.addElementInListOfChoices(save, 1);
		saveQuitMenu.addElementInListOfChoices(rename, 1);
		saveQuitMenu.addElementInListOfChoices(quit, 1);
	}

	private void addMenuConstruction() {
		final float scaleText = 20f;
		Button entity = new Button(0, buttonScale, "entity"), rigid = new Button(0, buttonScale, "rigid"),
				mesh = new Button(0, buttonScale, "mesh"), text = new Button(0, buttonScale, "text"),
				statiq = new Button(0, buttonScale, "static"), jointPin = new Button(0, buttonScale, "jointPin");
		entity.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(entity, 1);
		rigid.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(rigid, 1);
		mesh.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(mesh, 1);
		text.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(text, 1);
		statiq.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(statiq, 1);
		jointPin.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(jointPin, 1);
		addMenu.setEcartement(8);

		// Triggers
		mesh.setOnArm(new Trigger() {

			@Override
			public void make() {
				// TODO
			}
		});

		statiq.setOnArm(new Trigger() {

			@Override
			public void make() {
				// TODO
			}
		});

		rigid.setOnArm(new Trigger() {

			@Override
			public void make() {
				// TODO
			}
		});
		text.setOnArm(new Trigger() {

			@Override
			public void make() {
				// TODO
			}
		});
	}

	@Override
	public void upAction() {
		mainMenu.upAction();
	}

	@Override
	public void downAction() {
		mainMenu.downAction();
	}

	@Override
	public void rightAction() {
		mainMenu.rightAction();
	}

	@Override
	public void leftAction() {
		mainMenu.leftAction();
	}

	@Override
	public void selectAction() {
		mainMenu.selectAction();
	}

	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public void unSelec() {
		// Nothing
	}

	@Override
	public void update(double delta) {
		mainMenu.update(delta);
	}

	@Override
	public void draw() {
		mainMenu.draw();
	}

	@Override
	public void setPositionLerp(Vec2f position, double lerp) {
		Vec2f dif = Vec2f.subtract(position, this.position);
		this.position.add(dif);
		mainMenu.setPositionLerp(Vec2f.add(mainMenu.getPosition(), dif), lerp);
	}

}
