package arenashooter.game.gameStates.editor;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import arenashooter.engine.FileUtils;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.ui.DoubleInput;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.MultiMenu;
import arenashooter.engine.ui.Navigable;
import arenashooter.engine.ui.TextInput;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.UiValuableButton;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.xmlReaders.writer.MapXmlWriter;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.StaticBodyContainer;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.MenuStart;
import arenashooter.game.gameStates.editor.editorEnum.Prime;
import arenashooter.game.gameStates.editor.editorEnum.TypeEntites;
import arenashooter.game.gameStates.editor.editorEnum.Ui_Input;

class MainMenu implements Navigable {

	// Save & Quit variables
	private Arena arenaConstruction;
	private String fileName = "NewArena";

	private Vec2f position = new Vec2f(Editor.forVisible, 0);
	private Vec2f buttonScale = new Vec2f(15, 5);

	private MenuSelectionV<UiActionable> addMenu = new MenuSelectionV<>(), setMenu = new MenuSelectionV<>(),
			saveQuitMenu = new MenuSelectionV<>(), arenaInfo = new MenuSelectionV<>(),
			meshChooserMenu = new MenuSelectionV<>();
	private MultiMenu<Prime> mainMenu = new MultiMenu<>(5, Prime.values(), "data/sprites/interface/Selector.png",
			new Vec2f(30, 10));
	private Navigable current = mainMenu;

	private Editor editor;

	private HashMap<Entity, Button> entityToButton = new HashMap<>();
	private HashMap<TypeEntites, Button> typeToButton = new HashMap<>();
	private Entity parent;
	private Ui_Input ui_InputState = Ui_Input.NOTHING;
	private DoubleInput doubleInput = new DoubleInput();
	private TextInput textInput = new TextInput();

	public MainMenu(Arena toConstruct, Editor editor) {
		this.editor = editor;

		mainMenu.setPosition(new Vec2f(Editor.forVisible, -40));
		mainMenu.addMenu(addMenu, Prime.Add);
		mainMenu.addMenu(setMenu, Prime.Set);
		mainMenu.addMenu(saveQuitMenu, Prime.Exit);
		mainMenu.addMenu(arenaInfo, Prime.ArenaInfo);

		arenaConstruction = toConstruct;
		parent = toConstruct;

		doubleInput.setVisible(false);

		saveQuitMenuConstruction();

		addMenuConstruction();

		arenaInfoMenuConstruction();

		meshChooserMenuConstruction();
	}

	private void arenaInfoMenuConstruction() {
		arenaInfo.setPosition(new Vec2f(Editor.forVisible, -40));
		arenaInfo.setEcartement(8);
		arenaInfo.setPositionRef(new Vec2f(Editor.forVisible, -30));
		UiValuableButton<Double> test = new UiValuableButton<Double>("Test", Double.valueOf(9));
		test.setOnArm(new Trigger() {
			
			@Override
			public void make() {
				ui_InputState = Ui_Input.DOUBLE;
				doubleInput = new DoubleInput();
				test.setVisible(false);
				doubleInput.setPosition(test.getPosition());
				doubleInput.setScale(new Vec2f(10));
				arenaInfo.addUiElement(doubleInput, 1);
				doubleInput.setOnFinish(new Trigger() {
					
					@Override
					public void make() {
						ui_InputState = Ui_Input.NOTHING;
						test.setVisible(true);
						test.setValue(doubleInput.getDouble());
						arenaInfo.removeUiElement(doubleInput);
					}
				});
			}
		});
		arenaInfo.addElementInListOfChoices(test, 1);
		Button button = new Button(0, buttonScale, "Set");
		arenaInfo.addElementInListOfChoices(button, 1);
		button.setOnArm(new Trigger() {

			@Override
			public void make() {
				ui_InputState = Ui_Input.DOUBLE;
				doubleInput = new DoubleInput();
				button.setVisible(false);
				doubleInput.setScale(new Vec2f(10));
				arenaInfo.addUiElement(doubleInput, 1);
				doubleInput.setPosition(button.getPosition());
				doubleInput.setOnFinish(new Trigger() {

					@Override
					public void make() {
						ui_InputState = Ui_Input.NOTHING;
						button.setVisible(true);
						System.out.println(doubleInput.getDouble());
						arenaInfo.removeUiElement(doubleInput);
					}
				});
				doubleInput.setOnCancel(new Trigger() {

					@Override
					public void make() {
						ui_InputState = Ui_Input.NOTHING;
						button.setVisible(true);
						arenaInfo.removeUiElement(doubleInput);
					}
				});
			}
		});
	}

	private void meshChooserMenuConstruction() {
		meshChooserMenu.setPosition(new Vec2f(Editor.forVisible, -40));
		meshChooserMenu.setEcartement(6);

		// mesh buttons
		File root = new File("data");
		List<File> list = FileUtils.listFilesByType(root, ".obj");
		for (File file : list) {
			Button button = new Button(0, buttonScale, file.getName());
			meshChooserMenu.addElementInListOfChoices(button, 1);
			button.setOnArm(new Trigger() {

				@Override
				public void make() {
					Mesh mesh = new Mesh(new Vec3f(), file.getPath().replace('\\', '/'));
					createNewEntity(mesh, TypeEntites.MESH);
				}
			});
		}
	}

	private void saveQuitMenuConstruction() {
		final float spacing = 8;
		saveQuitMenu.setEcartement(spacing);
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
				ui_InputState = Ui_Input.TEXT;
				textInput = new TextInput();
				textInput.setScale(new Vec2f(10));
				saveQuitMenu.addUiElement(textInput, 1);
				textInput.setPosition(Vec2f.add(rename.getPosition(), new Vec2f(0, buttonScale.y + spacing * 2)));
				textInput.setOnFinish(new Trigger() {

					@Override
					public void make() {
						ui_InputState = Ui_Input.NOTHING;
						textInput.setVisible(false);
						fileName = textInput.getText();
						saveQuitMenu.removeUiElement(textInput);
					}
				});
				textInput.setOnCancel(new Trigger() {

					@Override
					public void make() {
						ui_InputState = Ui_Input.NOTHING;
						textInput.setVisible(false);
						saveQuitMenu.removeUiElement(textInput);
					}
				});
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
		for (TypeEntites type : TypeEntites.values()) {
			char first = type.name().charAt(0);
			String name = type.name().substring(1).toLowerCase();

			Button button = new Button(0, buttonScale, first + name);
			addMenu.addElementInListOfChoices(button, 1);
			typeToButton.put(type, button);
			button.setScaleText(new Vec2f(scaleText));
			button.setOnArm(new Trigger() {

				@Override
				public void make() {
					switch (type) {
					case ENTITY:
						Entity entity = new Entity();
						createNewEntity(entity, type);
						break;
					case MESH:
						current = meshChooserMenu;
						editor.setCurrentMenu(current);
						break;
					case RIGID:
						RigidBodyContainer rigid = new RigidBodyContainer(new RigidBody(new ShapeBox(new Vec2f(1)),
								new Vec2f(), 0, CollisionFlags.RIGIDBODY, 1, 0.8f));
						createNewEntity(rigid, type);
						break;
					case STATIC:
						StaticBodyContainer staticq = new StaticBodyContainer(new Vec2f(), new Vec2f(1), 0);
						createNewEntity(staticq, type);
						break;
					case TEXT:
						TextSpatial text = new TextSpatial(new Vec3f(), new Vec3f(1f),
								new Text(Main.font, TextAlignH.CENTER, "GrosTest"));
						createNewEntity(text, type);
						break;
					case SPAWN:
						Spawner spawner = new Spawner(new Vec2f(), 4);
						createNewEntity(spawner, type);
						break;
					default:
						break;
					}
				}
			});
		}

		Button joinpin = new Button(0, buttonScale, "Joinpin");
		addMenu.addElementInListOfChoices(joinpin, 1);
		joinpin.setScaleText(new Vec2f(scaleText));

		Button animation = new Button(0, buttonScale, "Animation");
		addMenu.addElementInListOfChoices(animation, 1);
		animation.setScaleText(new Vec2f(scaleText));
		animation.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.setCurrentMenu(editor.animEditor);
			}
		});

		addMenu.setEcartement(8);

	}

	private void createNewEntity(Entity entity, TypeEntites type) {
		String entityName = entity.genName();
		entity.attachToParent(parent, entityName);
		Button toSetMenu = new Button(0, buttonScale, entityName);
		setMenu.addElementInListOfChoices(toSetMenu, 1);
		editor.allEditable.add(entity);
		entityToButton.put(entity, toSetMenu);
		toSetMenu.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.onSetting = entity;
				editor.setCurrentMenu(new EntityEditor(MainMenu.this, entity, type));
			}
		});
		current = mainMenu;
		toSetMenu.arm();
		this.parent = arenaConstruction;
	}

	void newEntity(Entity parent, TypeEntites type) {
		this.parent = parent;
		typeToButton.get(type).arm();
	}

	void setButtonName(Entity entity, String name) {
		Button button = entityToButton.get(entity);
		if (button != null) {
			button.setText(name);
		}
	}

	@Override
	public boolean upAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.upAction();
		case TEXT:
			return textInput.upAction();
		default:
			return current.upAction();
		}
	}

	@Override
	public boolean downAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.downAction();
		case TEXT:
			return textInput.downAction();
		default:
			return current.downAction();
		}
	}

	@Override
	public boolean rightAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.rightAction();
		case TEXT:
			return textInput.rightAction();
		default:
			return current.rightAction();
		}
	}

	@Override
	public boolean leftAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.leftAction();
		case TEXT:
			return textInput.leftAction();
		default:
			return current.leftAction();
		}
	}

	@Override
	public boolean selectAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.selectAction();
		case TEXT:
			return textInput.selectAction();
		default:
			return current.selectAction();
		}
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
		current.update(delta);
	}

	@Override
	public void draw() {
		current.draw();
	}

	@Override
	public void setPositionLerp(Vec2f position, double lerp) {
		Vec2f dif = Vec2f.subtract(position, this.position);
		this.position.add(dif);
		mainMenu.setPositionLerp(Vec2f.add(mainMenu.getPosition(), dif), lerp);
		meshChooserMenu.setPositionLerp(Vec2f.add(meshChooserMenu.getPosition(), dif), lerp);
	}

	@Override
	public void setPosition(Vec2f newPosition) {
		Vec2f dif = Vec2f.subtract(position, this.position);
		this.position.add(dif);
		mainMenu.setPosition(Vec2f.add(mainMenu.getPosition(), dif));
		meshChooserMenu.setPosition(Vec2f.add(meshChooserMenu.getPosition(), dif));
	}

	@Override
	public Vec2f getPosition() {
		return position;
	}

	@Override
	public boolean continueAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.continueAction();
		case TEXT:
			return textInput.continueAction();
		default:
			return current.continueAction();
		}
	}

	public boolean changeAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.changeAction();
		case TEXT:
			return textInput.changeAction();
		default:
			return current.changeAction();
		}
	}

	public boolean cancelAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.cancelAction();
		case TEXT:
			return textInput.cancelAction();
		default:
			return current.cancelAction();
		}
	}

	public void constructCamerabutton(Camera cam) {
		Button toSetMenu = new Button(0, buttonScale, "camera");
		setMenu.addElementInListOfChoices(toSetMenu, 1);
		entityToButton.put(cam, toSetMenu);
		toSetMenu.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.onSetting = cam;
				editor.setCurrentMenu(new EntityEditor(MainMenu.this, cam, TypeEntites.ENTITY));
			}
		});
	}

	@Override
	public boolean backAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.backAction();
		case TEXT:
			return textInput.backAction();
		default:
			return false;
		}
	}

}
