package arenashooter.game.gameStates.editor;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import arenashooter.engine.FileUtils;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.MultiMenu;
import arenashooter.engine.ui.Navigable;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.TextInput;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.engine.xmlReaders.writer.MapXmlWriter;
import arenashooter.entities.Editable;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.StaticBodyContainer;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.editor.editorEnum.Prime;
import arenashooter.game.gameStates.editor.editorEnum.SetMesh;
import arenashooter.game.gameStates.editor.editorEnum.TypeEntites;

public class Editor extends GameState {

	static final float forVisible = -64, forNotVisible = -110;
	
	private Rectangle background = new Rectangle(0, new Vec2f(50, 150), new Vec4f(.5, .5, .5, .2));
	
	private boolean menuVisible = true;

	private String fileName = "NewArena";

	/* Save-quit Menu */
	private MenuSelectionV<UiActionable> saveQuitMenu = new MenuSelectionV<>(5, 0, 0, new Vec2f(30, 10),
			"data/sprites/interface/Selector.png");
/*
	private MenuSelectionV<UiActionable> setMenu = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");

	private MenuSelectionV<UiActionable> addMenu = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");
*/
	private InputListener inputs = new InputListener();
	private Camera cam;

	/* Main Menu */
	private MultiMenu<Prime> multiMenu = new MultiMenu<>(10, Prime.values(), "data/sprites/interface/Selector.png",
			new Vec2f(30, 10));

	/* Sub add menu */
	private MenuSelectionV<UiActionable> meshMenu = new MenuSelectionV<>(), staticMenu = new MenuSelectionV<>(),
			rigidMenu = new MenuSelectionV<>(), textMenu = new MenuSelectionV<>();

	/* Menu Set entity */
	private MenuSelectionV<UiActionable> setEntityMenu = new MenuSelectionV<>(10, 0, 0,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");
	private Label setEntityName = new Label(0, new Vec2f(20), "Empty"),
			setEntityParent = new Label(0, new Vec2f(20), "Empty");
	private ScrollerH<SetMesh> setEntityScroller = new ScrollerH<>(0, new Vec2f(35, 25), SetMesh.values());
	private Stack<Entity> setEntityStack = new Stack<>();

	private Stack<Navigable> stackMenu = new Stack<>();
	private HashMap<Entity, Button> mapEntityButton = new HashMap<>();

	private TextInput textInput = new TextInput();

	private LinkedList<Editable> allEditable = new LinkedList<>();
	
	private Navigable currentMenu = new MainMenu(current , this);
	
	AnimEditor animEditor = new AnimEditor();

	private final Vec2f scale = new Vec2f(15, 5);

	/**
	 * 
	 */
	public Editor() {
		super(1);
		
		background.setPos(new Vec2f(forVisible, 0));

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						currentMenu.leftAction();
						break;
					case UI_RIGHT:
						currentMenu.rightAction();
						break;
					case UI_UP:
						currentMenu.upAction();
						break;
					case UI_DOWN:
						currentMenu.downAction();
						break;
					case UI_OK:
						currentMenu.selectAction();
						break;
					case UI_CONTINUE:
						setMenuVisible(!menuVisible);
						break;
					case UI_BACK:
						
						break;
					case UI_CHANGE:
						break;
					case UI_CANCEL:
						break;
					default:
						break;
					}
				} else if (event.getActionState() == ActionState.PRESSED) {
					/*if (stackMenu.peek() == setEntityMenu) {
						final double scaleSpeed = 0.005, positionSpeed = 0.02;
						Entity e = getEntityOnSetting();
						switch (event.getAction()) {
						case UI_DOWN2:
							if (e instanceof Editable) {
								Editable editable = (Editable) e;
								switch (setEntityScroller.get()) {
								case POSITION:
									editable.addPosition(new Vec2f(0, positionSpeed));
									break;
								case SCALE:
									editable.addScale(new Vec2f(0, -scaleSpeed));
									break;
								case ROTATION:
									editable.addRotation(.1);
									break;
								default:
									break;
								}
							}
							break;
						case UI_UP2:
							if (e instanceof Editable) {
								Editable editable = (Editable) e;
								switch (setEntityScroller.get()) {
								case POSITION:
									editable.addPosition(new Vec2f(0, -positionSpeed));
									break;
								case SCALE:
									editable.addScale(new Vec2f(0, scaleSpeed));
									break;
								case ROTATION:
									editable.addRotation(-0.1);
									break;
								default:
									break;
								}
							}
							break;
						case UI_LEFT2:
							if (e instanceof Editable) {
								Editable editable = (Editable) e;
								switch (setEntityScroller.get()) {
								case POSITION:
									editable.addPosition(new Vec2f(-positionSpeed, 0));
									break;
								case SCALE:
									editable.addScale(new Vec2f(-scaleSpeed, 0));
									break;
								default:
									break;
								}
							}
							break;
						case UI_RIGHT2:
							if (e instanceof Editable) {
								Editable editable = (Editable) e;
								switch (setEntityScroller.get()) {
								case POSITION:
									editable.addPosition(new Vec2f(positionSpeed, 0));
									break;
								case SCALE:
									editable.addScale(new Vec2f(scaleSpeed, 0));
									break;
								default:
									break;
								}
							}
							break;
						default:
							break;
						}
					}*/
				}
			}
		});

	}
/*
	private void textMenuConstruction() {
		// TODO Auto-generated method stub
		textMenu.setPosition(new Vec2f(forVisible.x, -40));
	}

	private void rigidMenuConstruction() {
		rigidMenu.setPosition(new Vec2f(forVisible.x, -40));
		Label title = new Label(0, new Vec2f(30), "Rigid Options");
		rigidMenu.addUiElement(title, 1);
		rigidMenu.setPositionRef(new Vec2f(forVisible.x, -10));
		rigidMenu.setEcartement(10);

		Button box = new Button(0, new Vec2f(15, 5), "Box");
		rigidMenu.addElementInListOfChoices(box, 1);
		box.setOnArm(new Trigger() {

			@Override
			public void make() {
				RigidBody body = new RigidBody(new ShapeBox(new Vec2f(1)), new Vec2f(), 0, CollisionFlags.RIGIDBODY, 1,
						0.8f);
				RigidBodyContainer rigidBody = new RigidBodyContainer(new Vec2f(), body);
				String name = rigidBody.genName();
				rigidBody.attachToParent(getCurrentParent(), name);
				allEditable.add(rigidBody);
				Button entity = new Button(0, new Vec2f(10), name);
				mapEntityButton.put(rigidBody, entity);
				entity.setScale(new Vec2f(15));
				setMenu.addElementInListOfChoices(entity, 1);
				entity.setOnArm(new Trigger() {

					@Override
					public void make() {
						bindEntity(rigidBody);
						stackMenu.push(setEntityMenu);
					}
				});
				entity.arm();
			}
		});

		Button disk = new Button(0, new Vec2f(15, 5), "Disk");
		rigidMenu.addElementInListOfChoices(disk, 1);
		disk.setOnArm(new Trigger() {

			@Override
			public void make() {
				RigidBody body = new RigidBody(new ShapeDisk(1), new Vec2f(), 0, CollisionFlags.RIGIDBODY, 1, 0.8f);
				RigidBodyContainer rigidBody = new RigidBodyContainer(new Vec2f(), body);
				String name = rigidBody.genName();
				rigidBody.attachToParent(getCurrentParent(), name);
				allEditable.add(rigidBody);
				Button entity = new Button(0, new Vec2f(10), name);
				mapEntityButton.put(rigidBody, entity);
				entity.setScale(new Vec2f(15));
				setMenu.addElementInListOfChoices(entity, 1);
				entity.setOnArm(new Trigger() {

					@Override
					public void make() {
						bindEntity(rigidBody);
						stackMenu.push(setEntityMenu);
					}
				});
				entity.arm();
			}
		});
	}

	private void mainMenuConstruction() {
		Rectangle bg = new Rectangle(0, new Vec2f(50, 150), new Vec4f(0.5, 0.5, 0.5, 0.2));
		this.bg.setBackground(bg);
		this.bg.setPosition(forVisible);
		multiMenu.addMenu(setMenu, Prime.Set);
		multiMenu.addMenu(addMenu, Prime.Add);
		multiMenu.addMenu(saveQuitMenu, Prime.Exit);
		multiMenu.setPosition(forVisible);
		multiMenu.setPositionRef(new Vec2f(forVisible.x, -30));
	}

	private void setEntityMenuConstruction() {
		setEntityMenu.addUiElement(setEntityName, 1);
		setEntityMenu.addUiElement(setEntityParent, 1);
		setEntityMenu.setPosition(forVisible);
		setEntityMenu.addElementInListOfChoices(setEntityScroller, 1);
		setEntityMenu.setPositionRef(new Vec2f(forVisible.x, -21));
		setEntityMenu.setEcartement(10);
		setEntityName.setPos(new Vec2f(forVisible.x, -43));
		setEntityParent.setPos(new Vec2f(forVisible.x, -32));
		setEntityScroller.setAlwaysScrollable(true);
		ScrollerH<TypeEntites> newChild = new ScrollerH<>(0, new Vec2f(30), TypeEntites.values());
		newChild.setAlwaysScrollable(true);
		newChild.setTitle("New Child");
		newChild.setOnArm(new Trigger() {

			@Override
			public void make() {
				Entity newParent = getCurrentParent().getChild(setEntityName.getText());
				setEntityStack.push(newParent);
				stackMenu.push(meshMenu);
			}
		});
		setEntityMenu.addElementInListOfChoices(newChild, 1);
		setEntityStack.push(current);
		Button renameEntity = new Button(0, scale, "Rename Entity");
		renameEntity.setOnArm(new Trigger() {

			@Override
			public void make() {
				textInput.reset();
				textInput.setPos(renameEntity.getPos());
				stackMenu.push(textInput);
			}
		});
		setEntityMenu.addElementInListOfChoices(renameEntity, 1);

	}

	private void saveQuitMenuConstruction() {
		saveQuitMenu.setEcartement(8);
		Button save = new Button(0, scale, "Save"), rename = new Button(0, scale, "Rename File");
		save.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		save.setScaleText(new Vec2f(20));
		save.setOnArm(new Trigger() {

			@Override
			public void make() {
				MapXmlWriter.writerMap(current, fileName);
			}
		});
		rename.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		rename.setScaleText(new Vec2f(20));
		rename.setOnArm(new Trigger() {

			@Override
			public void make() {
				stackMenu.push(textInput);
			}
		});
		Button quit = new Button(0, scale, "Quit");
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

	private void staticMenuConstruction() {
		Label titleStaticMenu = new Label(0, new Vec2f(30), "Choose the shape");
		Button shapeBox = new Button(0, scale, "Box"), shapeDisk = new Button(0, scale, "Disk");
		staticMenu.addUiElement(titleStaticMenu, 1);
		staticMenu.setPosition(new Vec2f(forVisible.x, -40));
		staticMenu.setPositionRef(new Vec2f(forVisible.x, -10));
		staticMenu.setEcartement(10);
		staticMenu.addElementInListOfChoices(shapeBox, 1);
		staticMenu.addElementInListOfChoices(shapeDisk, 1);

		// Triggers
		shapeBox.setOnArm(new Trigger() {

			@Override
			public void make() {
				StaticBodyContainer staticBody = new StaticBodyContainer(new Vec2f(), new Vec2f(1), 0);
				String name = staticBody.genName();
				staticBody.attachToParent(getCurrentParent(), name);
				staticBody.getBody().setUserData(staticBody);
				staticBody.getBody().create();
				allEditable.add(staticBody);
				Button entity = new Button(0, scale, name);
				mapEntityButton.put(staticBody, entity);
				setMenu.addElementInListOfChoices(entity, 1);
				entity.setOnArm(new Trigger() {

					@Override
					public void make() {
						bindEntity(staticBody);
						stackMenu.push(setEntityMenu);
					}
				});
				entity.arm();
			}
		});
		shapeDisk.setOnArm(new Trigger() {

			@Override
			public void make() {
				StaticBody body = new StaticBody(new ShapeDisk(1), new Vec2f(), 0, CollisionFlags.LANDSCAPE);
				StaticBodyContainer staticBody = new StaticBodyContainer(new Vec2f(), body);
				String name = staticBody.genName();
				staticBody.attachToParent(getCurrentParent(), name);
				staticBody.getBody().setUserData(staticBody);
				staticBody.getBody().create();
				allEditable.add(staticBody);
				Button entity = new Button(0, scale, name);
				mapEntityButton.put(staticBody, entity);
				setMenu.addElementInListOfChoices(entity, 1);
				entity.setOnArm(new Trigger() {

					@Override
					public void make() {
						bindEntity(staticBody);
						stackMenu.push(setEntityMenu);
					}
				});
				entity.arm();
			}
		});
	}

	private void meshMenuConstruction() {
		meshMenu.setPositionRef(new Vec2f(forVisible.x, -40));
		meshMenu.setEcartement(6);

		File root = new File("data");
		LinkedList<File> m = new LinkedList<>();
		m.addAll(allMesh(root));

		for (File file : m) {
			String name = file.getName();
			Button b = new Button(0, new Vec2f(30, 5.5), name.substring(0, name.length() - 4));
			meshMenu.addElementInListOfChoices(b, 1);
			b.setScaleText(new Vec2f(20));
			b.setOnArm(new Trigger() {

				@Override
				public void make() {
					String replace = file.getPath().replace('\\', '/');
					Mesh mesh = new Mesh(new Vec3f(), replace);
					String name = mesh.genName();
					mesh.attachToParent(getCurrentParent(), name);
					allEditable.add(mesh);
					Button b = new Button(0, new Vec2f(30, 5.5), name);
					mapEntityButton.put(mesh, b);
					b.setScaleText(new Vec2f(15));
					setMenu.addElementInListOfChoices(b, 1);
					b.setOnArm(new Trigger() {

						@Override
						public void make() {
							bindEntity(mesh);
							stackMenu.push(setEntityMenu);
						}
					});
					b.arm();
				}
			});
		}
	}*/

	private void bindEntity(Entity entity) {
		Entity parent = entity.getParent();
		for (String name : parent.getChildren().keySet()) {
			if (parent.getChild(name) == entity) {
				setEntityName.setText(name);
			}
		}
		if (parent == current) {
			setEntityParent.setText("Parent : Arena");
		} else {
			Entity superParent = parent.getParent();
			for (String name : superParent.getChildren().keySet()) {
				if (superParent.getChild(name) == parent) {
					setEntityParent.setText("Parent : " + name);
				}
			}
		}
		if (getCurrentParent() != entity.getParent()) {
			setEntityStack.push(entity.getParent());
		}
	}

	private List<File> allMesh(File parent) {
		return FileUtils.listFilesByType(parent, ".obj");
	}

	private Entity getEntityOnSetting() {
		return getCurrentParent().getChild(setEntityName.getText());
	}

	private Entity getCurrentParent() {
		return setEntityStack.peek();
	}
	
	void setCurrentMenu(Navigable currentMenu) {
		this.currentMenu = currentMenu;
	}
	
	void setVisibleBackground(boolean visible) {
		background.setVisible(visible);
	}

	@Override
	public void init() {
		super.init();
		cam = (Camera) current.getChild("camera");
	}

	private void setMenuVisible(boolean visible) {
		if (visible) {
			background.setPositionLerp(new Vec2f(forVisible, 0), 30);
			currentMenu.setPositionLerp(new Vec2f(forVisible, 0), 40);
		} else {
			background.setPositionLerp(new Vec2f(forNotVisible, 0), 30);
			currentMenu.setPositionLerp(new Vec2f(forNotVisible, 0), 40);
		}
		menuVisible = visible;
	}
	
	@Override
	public void update(double delta) {
		inputs.step(delta);
		background.update(delta);
		currentMenu.update(delta);
		for (Editable editable : allEditable) {
			if (editable == getEntityOnSetting() && stackMenu.peek() == setEntityMenu) {
				editable.setEditorTarget(true);
			} else {
				editable.setEditorTarget(false);
			}
		}
	}

	@Override
	public void draw() {
		for (Editable editable : allEditable) {
			editable.drawEditor();
		}
		Window.beginUi();
		background.draw();
		currentMenu.draw();
		Window.endUi();
	}

}
