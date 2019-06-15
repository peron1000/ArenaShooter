package arenashooter.game.gameStates.editor;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import arenashooter.engine.FileUtils;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.KinematicBody;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.engine.ui.ColorPicker;
import arenashooter.engine.ui.DoubleInput;
import arenashooter.engine.ui.MultiUi;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.TextInput;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.engine.xmlReaders.writer.MapXmlWriter;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.KinematicBodyContainer;
import arenashooter.entities.spatials.LightContainer;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.StaticBodyContainer;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.MenuStart;
import arenashooter.game.gameStates.editor.editorEnum.TypeEntites;
import arenashooter.game.gameStates.editor.editorEnum.Ui_Input;

class MainMenu extends UiElement implements MultiUi {

	// Save & Quit variables
	private Arena arenaConstruction;
	private String fileName = "NewArena";

	private Label fileNameLabel = new Label("File Name: " + fileName);
	private TabList<UiElement> mainMenu = new TabList<>(), meshChooser = new TabList<>();
	private UiListVertical<Button> setMenu = new UiListVertical<>();
	private UiListVertical<UiElement> saveQuitMenu = new UiListVertical<>();
	private UiListVertical<UiElement> arenaInfo = new UiListVertical<>();
	private TabList<UiElement> current = mainMenu;

	private Editor editor;

	private HashMap<Entity, Button> entityToButton = new HashMap<>();
	private HashMap<TypeEntites, Button> typeToButton = new HashMap<>();
	private Entity parent;
	private Ui_Input ui_InputState = Ui_Input.NOTHING;
	private DoubleInput doubleInput = new DoubleInput();
	private TextInput textInput = new TextInput();
	private ColorPicker colorPicker = new ColorPicker(false);

	private Trigger colorPickerModification = new Trigger() {

		@Override
		public void make() {
			// Nothing by default
		}
	};

	// default values for buttons
	private final double scaleText = 5, xRect = 30, yRect = 8, spacing = 10;

	public MainMenu(Arena toConstruct, Editor editor) {
		this.editor = editor;

		mainMenu.setPosition(0, -30);
		mainMenu.setSpacing(spacing);
		meshChooser.setPosition(0, -30);

		setPosition(Editor.forVisible, 0);

		arenaConstruction = toConstruct;
		parent = toConstruct;

		doubleInput.setVisible(false);

		addMenuConstruction();

		mainMenu.addBind("Set", setMenu);
		setMenu.setSpacing(spacing);

		saveQuitMenuConstruction();

		arenaInfoMenuConstruction();

		meshChooserMenuConstruction();

		UiImage.selector.setPosition(current.getTarget().getPosition().x, current.getTarget().getPosition().y);
	}

	private void arenaInfoMenuConstruction() {
		mainMenu.addBind("Arena Info", arenaInfo);
		Button top = new Button("Change top"), bottom = new Button("Change bottom");
		arenaInfo.addElements(top, bottom);

		// Trigger
		top.setOnArm(new Trigger() {

			@Override
			public void make() {
				ui_InputState = Ui_Input.COLOR_PICKER;
				arenaInfo.addElement(colorPicker);
				colorPickerModification = new Trigger() {

					@Override
					public void make() {
						setSkyColorTop(colorPicker.getColorRGB());
					}
				};
				colorPicker.setOnFinish(new Trigger() {

					@Override
					public void make() {
						arenaInfo.removeElement(colorPicker);
						ui_InputState = Ui_Input.NOTHING;
					}
				});
			}
		});

		bottom.setOnArm(new Trigger() {

			@Override
			public void make() {
				ui_InputState = Ui_Input.COLOR_PICKER;
				arenaInfo.addElement(colorPicker);
				colorPickerModification = new Trigger() {

					@Override
					public void make() {
						setSkyColorBottom(colorPicker.getColorRGB());
					}
				};
				colorPicker.setOnFinish(new Trigger() {

					@Override
					public void make() {
						arenaInfo.removeElement(colorPicker);
						ui_InputState = Ui_Input.NOTHING;
					}
				});
			}
		});

		for (UiElement uiElement : arenaInfo) {
			if (uiElement instanceof Button) {
				Button b = (Button) uiElement;
				b.setScaleRect(xRect, yRect);
				b.setScaleText(scaleText);
			}
		}

	}

	private void meshChooserMenuConstruction() {
		UiListVertical<Button> vList = new UiListVertical<>();
		vList.setSpacing(spacing);

		// mesh buttons
		File root = new File("data");
		List<File> list = FileUtils.listFilesByType(root, ".obj");
		int i = 0;
		for (File file : list) {
			String fileName = file.getName();
			int index = fileName.lastIndexOf('.');
			Button button = new Button(fileName.substring(0, index));
			button.setScaleRect(xRect, yRect);
			button.setScaleText(scaleText);
			vList.addElement(button);
			button.setOnArm(new Trigger() {

				@Override
				public void make() {
					Mesh mesh = new Mesh(new Vec3f(), file.getPath().replace('\\', '/'));
					createNewEntity(mesh, TypeEntites.MESH);
				}
			});
			i++;
			if (i >= 8) {
				meshChooser.addBind("Mesh Chooser", vList);
				vList = new UiListVertical<>();
				vList.setSpacing(spacing);
				i = 0;
			}
		}

		meshChooser.addBind("Mesh Chooser", vList);
	}

	private void saveQuitMenuConstruction() {
		Button save = new Button("Save"), rename = new Button("Rename File"), quit = new Button("Quit");
		save.setColorRect(new Vec4f(0.25, 0.25, 1, 1));
		save.setScaleText(scaleText);
		save.setScaleRect(xRect, yRect);
		save.setOnArm(new Trigger() {

			@Override
			public void make() {
				MapXmlWriter.writerMap(arenaConstruction, fileName);
			}
		});
		rename.setColorRect(new Vec4f(0.25, 0.25, 1, 1));
		rename.setScaleText(scaleText);
		rename.setScaleRect(xRect, yRect);
		rename.setOnArm(new Trigger() {

			@Override
			public void make() {
				ui_InputState = Ui_Input.TEXT;
				textInput = new TextInput();
				textInput.setScale(10);
				saveQuitMenu.addElement(textInput);
				textInput.setOnFinish(new Trigger() {

					@Override
					public void make() {
						ui_InputState = Ui_Input.NOTHING;
						textInput.setVisible(false);
						fileName = textInput.getText();
						fileNameLabel.setText("File Name: " + fileName);
						saveQuitMenu.removeElement(textInput);
					}
				});
				textInput.setOnCancel(new Trigger() {

					@Override
					public void make() {
						ui_InputState = Ui_Input.NOTHING;
						textInput.setVisible(false);
						saveQuitMenu.removeElement(textInput);
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
		quit.setColorRect(new Vec4f(0.25, 0.25, 1, 1));
		quit.setScaleText(scaleText);
		quit.setScaleRect(xRect, yRect);

		saveQuitMenu.setSpacing(spacing);

		saveQuitMenu.addElements(save, rename, quit);

		mainMenu.addBind("Exit", saveQuitMenu);

		mainMenu.addLabelInfo(saveQuitMenu, fileNameLabel);
		fileNameLabel.setScale(scaleText);
	}

	private void addMenuConstruction() {
		final int mod = 8;
		int i = 0, j = 1;
		UiListVertical<Button> addMenu = new UiListVertical<>();
		for (TypeEntites type : TypeEntites.values()) {
			char first = type.name().charAt(0);
			String name = type.name().substring(1).toLowerCase();

			Button button = new Button(first + name);
			addMenu.addElement(button);
			typeToButton.put(type, button);
			button.setScaleText(scaleText);
			button.setOnArm(new Trigger() {

				@Override
				public void make() {
					switch (type) {
					case ENTITY:
						Entity entity = new Entity();
						createNewEntity(entity, type);
						break;
					case MESH:
						current = meshChooser;
						editor.setCurrentMenu(MainMenu.this);
						break;
					case RIGID_BOX:
						RigidBodyContainer rigidRect = new RigidBodyContainer(new RigidBody(new ShapeBox(new Vec2f(1)),
								new Vec2f(), 0, CollisionFlags.RIGIDBODY, 1, 0.8f));
						createNewEntity(rigidRect, type);
						break;
					case RIGID_DISK:
						RigidBodyContainer rigidDisk = new RigidBodyContainer(
								new RigidBody(new ShapeDisk(1), new Vec2f(), 0, CollisionFlags.RIGIDBODY, 1, 0.8f));
						createNewEntity(rigidDisk, type);
						break;
					case STATIC_BOX:
						StaticBodyContainer staticRect = new StaticBodyContainer(new Vec2f(), new Vec2f(1), 0);
						createNewEntity(staticRect, type);
						break;
					case STATIC_DISK:
						StaticBodyContainer staticDisk = new StaticBodyContainer(new Vec2f(), 1, 0);
						createNewEntity(staticDisk, type);
						break;
					case KINEMATIC_BOX:
						KinematicBodyContainer kinematicBox = new KinematicBodyContainer(new KinematicBody(
								new ShapeBox(new Vec2f(1)), new Vec2f(), 0, CollisionFlags.ARENA_KINEMATIC, 0.8f));
						createNewEntity(kinematicBox, type);
						break;
					case KINEMATIC_DISK:
						KinematicBodyContainer kinematicDisk = new KinematicBodyContainer(new KinematicBody(
								new ShapeDisk(1), new Vec2f(), 0, CollisionFlags.ARENA_KINEMATIC, 0.8f));
						createNewEntity(kinematicDisk, type);
						break;
					case TEXT:
						TextSpatial text = new TextSpatial(new Vec3f(), new Vec3f(1f),
								new Text(Main.font, TextAlignH.CENTER, "GrosTest"));
						createNewEntity(text, type);
						break;
					case SPAWN:
						Spawner spawner = new Spawner(new Vec2f(), 4);
						createNewEntity(spawner, type);
						arenaConstruction.playerSpawns.add(spawner.localPosition);
						break;
					case LIGHT:
						LightContainer light = new LightContainer(new Vec3f(), new Light());
						light.getLight().color.set(1, 1, 1);
						light.getLight().radius = 5f;
						createNewEntity(light, type);
						break;
					default:
						break;
					}
				}
			});
			i++;
			if (i / mod >= j) {
				for (Button b : addMenu) {
					b.setScaleRect(xRect, yRect);
					b.setScaleText(scaleText);
					b.setColorRect(new Vec4f(0, 0, 0, 0.5));
				}
				mainMenu.addBind("Adding" + j, addMenu);
				addMenu = new UiListVertical<>();
				j++;
			}
		}
		Button animation = new Button("Animation");
		addMenu.addElement(animation);
		animation.setScaleText(scaleText);
		animation.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.setCurrentMenu(editor.animEditor);
			}
		});

		for (Button button : addMenu) {
			button.setScaleRect(xRect, yRect);
			button.setScaleText(scaleText);
			button.setColorRect(new Vec4f(0, 0, 0, 0.5));
		}

		mainMenu.addBind("Adding" + j, addMenu);
	}

	private void createNewEntity(Entity entity, TypeEntites type) {
		String entityName = entity.genName();
		entity.attachToParent(parent, entityName);
		Button toSetMenu = new Button(entityName);
		toSetMenu.setScaleRect(xRect, yRect);
		toSetMenu.setScaleText(scaleText);
		setMenu.addElement(toSetMenu);
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

	void removeEntity(Entity toRemove) {
		Button buttonToRemove = entityToButton.remove(toRemove);
		setMenu.removeElement(buttonToRemove);
		mainMenu.reset();
		current = mainMenu;
		toRemove.detach();
		editor.allEditable.remove(toRemove);
		editor.setCurrentMenu(this);
	}

	public void constructCamerabutton(Camera cam) {
		Button toSetMenu = new Button("camera");
		toSetMenu.setScaleRect(xRect, yRect);
		toSetMenu.setScaleText(scaleText);
		setMenu.addElement(toSetMenu);
		entityToButton.put(cam, toSetMenu);
		toSetMenu.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.onSetting = cam;
				editor.setCurrentMenu(new EntityEditor(MainMenu.this, cam, TypeEntites.ENTITY));
			}
		});
	}

	private void setSkyColorTop(Vec3f color) {
		Sky sky = (Sky) arenaConstruction.getChild("sky");
		sky.setColorTop(color);
	}

	private void setSkyColorBottom(Vec3f color) {
		Sky sky = (Sky) arenaConstruction.getChild("sky");
		sky.setColorBot(color);
	}

	@Override
	public boolean upAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.upAction();
		case TEXT:
			return textInput.upAction();
		case COLOR_PICKER:
			return colorPicker.upAction();
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
		case COLOR_PICKER:
			return colorPicker.downAction();
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
		case COLOR_PICKER:
			return colorPicker.rightAction();
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
		case COLOR_PICKER:
			return colorPicker.leftAction();
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
		case COLOR_PICKER:
			return colorPicker.selectAction();
		default:
			return current.selectAction();
		}
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		current.update(delta);
		if (ui_InputState == Ui_Input.COLOR_PICKER) {
			colorPickerModification.make();
		}
		UiImage.selector.setPositionLerp(getTarget().getPosition().x, getTarget().getPosition().y, 10);
		UiImage.selector.update(delta);
	}

	@Override
	public void draw() {
		current.draw();
		if (ui_InputState == Ui_Input.NOTHING) {
			UiImage.selector.draw();
		}
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPositionLerp(x, y, lerp);
		mainMenu.addToPositionLerp(xDif, yDif, lerp);
		meshChooser.addToPositionLerp(xDif, yDif, lerp);
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPosition(x, y);
		mainMenu.addToPosition(xDif, yDif);
		meshChooser.addToPosition(xDif, yDif);
	}

	@Override
	public boolean continueAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.continueAction();
		case TEXT:
			return textInput.continueAction();
		case COLOR_PICKER:
			return colorPicker.continueAction();
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
		case COLOR_PICKER:
			return colorPicker.changeAction();
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
		case COLOR_PICKER:
			return colorPicker.cancelAction();
		default:
			return current.cancelAction();
		}
	}

	@Override
	public boolean backAction() {
		switch (ui_InputState) {
		case DOUBLE:
			return doubleInput.backAction();
		case TEXT:
			return textInput.backAction();
		case COLOR_PICKER:
			return colorPicker.backAction();
		default:
			if (current == meshChooser) {
				current = mainMenu;
				return true;
			}
			return false;
		}
	}

	@Override
	public UiElement getTarget() {
		return current.getTarget();
	}

}
