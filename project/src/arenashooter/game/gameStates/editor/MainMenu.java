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
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
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

class MainMenu implements Navigable {

	// Save & Quit variables
	private Arena arenaConstruction;
	private String fileName = "NewArena";

	private Vec2f position = new Vec2f(Editor.forVisible, 0);
	private Vec2f buttonScale = new Vec2f(15, 5);

	private MenuSelectionV<UiActionable> addMenu = new MenuSelectionV<>(), setMenu = new MenuSelectionV<>(),
			saveQuitMenu = new MenuSelectionV<>(), arenaInfo = new MenuSelectionV<>(), meshChooserMenu = new MenuSelectionV<>();
	private MultiMenu<Prime> mainMenu = new MultiMenu<>(5, Prime.values(), "data/sprites/interface/Selector.png",
			new Vec2f(30, 10));
	private Navigable current = mainMenu;

	private Editor editor;

	private HashMap<Entity, Button> entityToButton = new HashMap<>();
	private HashMap<TypeEntites, Button> typeToButton = new HashMap<>();
	private Entity parent;
	private boolean onWritting = false;
	private DoubleInput doubleInput = new DoubleInput();

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
		arenaInfo.setEcartement(6);
		arenaInfo.setPositionRef(new Vec2f(Editor.forVisible, -30));
		Button button = new Button(0, buttonScale, "input double");
		arenaInfo.addElementInListOfChoices(button, 1);
		button.setOnArm(new Trigger() {
			
			@Override
			public void make() {
				onWritting = true;
				doubleInput = new DoubleInput();
				button.setVisible(false);
				doubleInput.setPosition(button.getPosition());
				doubleInput.setScale(new Vec2f(10));
				arenaInfo.addUiElement(doubleInput, 1);
				doubleInput.setOnFinish(new Trigger() {
					
					@Override
					public void make() {
						onWritting = false;
						button.setVisible(true);
						System.out.println(doubleInput.getDouble());
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
	
	void newEntity(Entity parent , TypeEntites type) {
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
		if(onWritting) {
			return doubleInput.upAction();
		}
		return current.upAction();
	}

	@Override
	public boolean downAction() {
		if(onWritting) {
			return doubleInput.downAction();
		}
		return current.downAction();
	}

	@Override
	public boolean rightAction() {
		if(onWritting) {
			return doubleInput.rightAction();
		}
		return current.rightAction();
	}

	@Override
	public boolean leftAction() {
		if(onWritting) {
			return doubleInput.leftAction();
		}
		return current.leftAction();
	}

	@Override
	public boolean selectAction() {
		if(onWritting) {
			return doubleInput.selectAction();
		}
		return current.selectAction();
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
		if(onWritting) {
			return doubleInput.continueAction();
		}
		return false;
	}
	
	public boolean changeAction() {
		if (onWritting) {
			return doubleInput.changeAction();
		}
		return false;
	}

	public boolean cancelAction() {
		if (onWritting) {
			return doubleInput.cancelAction();
		}
		return false;
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

}
