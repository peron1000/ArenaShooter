package arenashooter.game.gameStates.editor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import arenashooter.engine.FileUtils;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.graphics.fonts.Text.TextAlignV;
import arenashooter.engine.json.EntityTypes;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.KinematicBody;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.PhysicShape;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.engine.ui.ColorPicker;
import arenashooter.engine.ui.DoubleInput;
import arenashooter.engine.ui.MultiUi;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.TextInput;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiGroup;
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
import arenashooter.game.gameStates.editor.addItem.AddItemEditor;
import arenashooter.game.gameStates.editor.editorEnum.Ui_Input;
import arenashooter.game.gameStates.loading.LoadingConfig;

public class ArenaEditor extends UiElement implements MultiUi {

	// Save & Quit variables
	protected Arena arenaConstruction;
	private String fileName = "NewArena";

	private UiImage background = new UiImage(new Vec4f(.5, .5, .5, .2));

	private boolean menuVisible = true;

	private Label fileNameLabel = new Label("File Name: " + fileName);
	private TabList<UiElement> mainMenu = new TabList<>(), meshChooser = new TabList<>();
	private UiListVertical<Button> setMenu = new UiListVertical<>();
	private UiListVertical<UiElement> saveQuitMenu = new UiListVertical<>();
	private UiListVertical<UiElement> arenaInfo = new UiListVertical<>();
	protected MultiUi current = mainMenu;
	private Button quitButton = null;

	private Editor editor;

	private Map<Entity, Button> entityToButton = new HashMap<>();
	private Map<EntityTypes, Button> typeToButton = new HashMap<>();
	private Entity parent;
	protected Ui_Input ui_InputState = Ui_Input.NOTHING;
	protected UiGroup<UiElement> doubleInputGroup = new UiGroup<UiElement>();
	protected DoubleInput doubleInput = new DoubleInput();
	protected UiGroup<UiElement> textInputGroup = new UiGroup<UiElement>();
	protected TextInput textInput = new TextInput();
	protected ColorPicker colorPicker = new ColorPicker(false);

	protected Trigger colorPickerModification = new Trigger() {

		@Override
		public void make() {
			// Nothing by default
		}
	};

	// default values for buttons
	protected final double scaleText = 5, xRect = 30, yRect = 8, spacing = 1, titleScale = 7, yMenuPosition = -30;

	protected ArenaEditor() {
		// Background
		background.setPosition(Editor.forVisible, 0);
		background.setScale(50, 150);
	}

	public ArenaEditor(Arena toConstruct, Editor editor) {
		this.editor = editor;

		// Background
		background.setPosition(Editor.forVisible, 0);
		background.setScale(50, 150);

		// Settings mainMenu
		mainMenu.setScale(xRect + spacing * 2, 70);
		mainMenu.setPosition(0, yMenuPosition);
		mainMenu.setSpacingForeachList(spacing);
		mainMenu.setTitleScale(titleScale, titleScale);
		mainMenu.setArrowsDistance(18);
		mainMenu.setTitleSpacing(8);
		mainMenu.setScaleArrows(6, 6);
		mainMenu.setScissor(true);

		// Settings meshChooser
		meshChooser.setPosition(0, yMenuPosition);
		meshChooser.setSpacingForeachList(spacing);
		meshChooser.setArrowsDistance(30);
		meshChooser.setTitleScale(6, 6);
		meshChooser.setArrowsDistance(20);
		meshChooser.setTitleSpacing(8);

		setPosition(Editor.forVisible, 0);

		arenaConstruction = toConstruct;
		parent = toConstruct;

		doubleInput.setVisible(false);

		addMenuConstruction();

		setMenuConstruction();

		arenaInfoMenuConstruction();

		saveQuitMenuConstruction();

		meshChooserMenuConstruction();

		UiImage.selector.setPosition(current.getTarget().getPosition().x, current.getTarget().getPosition().y);
	}

	private void setMenuConstruction() {
		loadArenaEntities(arenaConstruction);
		
		mainMenu.addBind("Set", setMenu);
	}

	private void setMenuVisible(boolean visible) {
		if (visible) {
			background.setPositionLerp(Editor.forVisible, 0, 30);
			setPositionLerp(Editor.forVisible, 0, 40);
		} else {
			background.setPositionLerp(Editor.forNotVisible, 0, 30);
			setPositionLerp(Editor.forNotVisible, 0, 40);
		}
		menuVisible = visible;
	}

	private void arenaInfoMenuConstruction() {
		Button top = new Button("Sky top"), bottom = new Button("Sky bottom"), ambient = new Button("Ambient light"),
				addItem = new Button("Add new Item");
		arenaInfo.addElements(top, bottom, ambient, addItem);

		// Trigger
		top.setOnArm(new Trigger() {
			@Override
			public void make() {
				ui_InputState = Ui_Input.COLOR_PICKER;
				Sky sky = (Sky) arenaConstruction.getChild("sky");
				if (sky != null) {
					colorPicker.setColorRGB(sky.getColorTop());
				}
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
				Sky sky = (Sky) arenaConstruction.getChild("sky");
				if (sky != null) {
					colorPicker.setColorRGB(sky.getColorBot());
				}
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

		ambient.setOnArm(new Trigger() {
			@Override
			public void make() {
				ui_InputState = Ui_Input.COLOR_PICKER;
				colorPicker.setColorRGB(getAmbientLight());
				arenaInfo.addElement(colorPicker);
				colorPickerModification = new Trigger() {
					@Override
					public void make() {
						setAmbientLight(colorPicker.getColorRGB());
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

		addItem.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.setCurrentMenu(new AddItemEditor(arenaConstruction, editor, ArenaEditor.this));
			}
		});

		for (UiElement uiElement : arenaInfo) {
			if (uiElement instanceof Button) {
				Button b = (Button) uiElement;
				b.setScale(xRect, yRect);
			}
		}

		mainMenu.addBind("Arena Info", arenaInfo);

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
			button.setScale(xRect, yRect);
			vList.addElement(button);
			button.setOnArm(new Trigger() {

				@Override
				public void make() {
					Mesh mesh = new Mesh(new Vec3f(), file.getPath().replace('\\', '/'));
					createNewEntity(mesh, EntityTypes.MESH);
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
		Button save = new Button("Save"), rename = new Button("Rename File"), quit = new Button("Quit"),
				load = new Button("Load");
		save.setColorRect(new Vec4f(0.25, 0.25, 1, 1));
		save.setScale(xRect, yRect);
		save.setOnArm(new Trigger() {

			@Override
			public void make() {
				MapXmlWriter.exportArena(arenaConstruction, fileName);
				Main.loadingConfig = new LoadingConfig();
				Main.loadingConfig.start();
			}
		});
		rename.setColorRect(new Vec4f(0.25, 0.25, 1, 1));
		rename.setScale(xRect, yRect);
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
		load.setColorRect(new Vec4f(0.25, 0.25, 1, 1));
		load.setScale(xRect, yRect);
		load.setOnArm(new Trigger() {

			@Override
			public void make() {
				current = getLoadArena();
			}
		});
		quit.setOnArm(new Trigger() {

			@Override
			public void make() {
				Main.getGameMaster().requestNextState(new MenuStart());
			}
		});
		quit.setColorRect(new Vec4f(0.25, 0.25, 1, 1));
		quit.setScale(xRect, yRect);
		quitButton = quit;

		saveQuitMenu.setSpacing(spacing);

		saveQuitMenu.addElements(save, load, rename, quit);

		mainMenu.addBind("Exit", saveQuitMenu);

		mainMenu.addLabelInfo(saveQuitMenu, fileNameLabel);
		fileNameLabel.setScale(scaleText);
	}

	private MultiUi getLoadArena() {
		TabList<Button> list = new TabList<Button>();
		UiListVertical<Button> vList = new UiListVertical<>();
		File mapXML = new File("data/mapXML");
		List<File> maps = FileUtils.listFilesByType(mapXML, ".xml");
		for (File file : maps) {
			String fileName = file.getName().substring(0, file.getName().indexOf('.'));
			Button b = new Button(fileName);
			b.setScale(xRect, yRect);
			b.setOnArm(new Trigger() {

				@Override
				public void make() {
					// Main.getGameMaster().requestNextState(new Editor(), file.getPath()); FIXME
				}
			});
			vList.addElement(b);
		}
		list.setPosition(mainMenu.getPosition());
		list.addBind("Choose Arena", vList);
		return list;
	}

	private void loadArenaEntities(Entity parent) {
		for (Entry<String, Entity> entry : parent.getChildren().entrySet()) {
			if (entry.getValue() instanceof Mesh) {
				createNewEntityToLoad(entry.getValue(), EntityTypes.MESH, entry.getKey());
			} else if (entry.getValue() instanceof RigidBodyContainer) {
				RigidBodyContainer b = (RigidBodyContainer) entry.getValue();
				PhysicShape s = b.getBody().getShape();
				if (s instanceof ShapeBox) {
					createNewEntityToLoad(entry.getValue(), EntityTypes.RIGID_BOX, entry.getKey());
				} else if (s instanceof ShapeDisk) {
					createNewEntityToLoad(entry.getValue(), EntityTypes.RIGID_DISK, entry.getKey());
				}
			} else if (entry.getValue() instanceof StaticBodyContainer) {
				StaticBodyContainer b = (StaticBodyContainer) entry.getValue();
				PhysicShape s = b.getBody().getShape();
				if (s instanceof ShapeBox) {
					createNewEntityToLoad(entry.getValue(), EntityTypes.STATIC_BOX, entry.getKey());
				} else if (s instanceof ShapeDisk) {
					createNewEntityToLoad(entry.getValue(), EntityTypes.STATIC_DISK, entry.getKey());
				}
			} else if (entry.getValue() instanceof KinematicBodyContainer) {
				KinematicBodyContainer b = (KinematicBodyContainer) entry.getValue();
				PhysicShape s = b.getBody().getShape();
				if (s instanceof ShapeBox) {
					createNewEntityToLoad(entry.getValue(), EntityTypes.KINEMATIC_BOX, entry.getKey());
				} else if (s instanceof ShapeDisk) {
					createNewEntityToLoad(entry.getValue(), EntityTypes.KINEMATIC_DISK, entry.getKey());
				}
			} else if (entry.getValue() instanceof Spawner) {
				createNewEntityToLoad(entry.getValue(), EntityTypes.SPAWN, entry.getKey());
			} else if (entry.getValue() instanceof TextSpatial) {
				createNewEntityToLoad(entry.getValue(), EntityTypes.TEXT, entry.getKey());
			} else if (entry.getValue() instanceof LightContainer) {
				createNewEntityToLoad(entry.getValue(), EntityTypes.LIGHT, entry.getKey());
			} else if(!(entry.getValue() instanceof Camera) && !(entry.getValue() instanceof Sky)){
				createNewEntityToLoad(entry.getValue(), EntityTypes.ENTITY, entry.getKey());
			}
			loadArenaEntities(entry.getValue());
		}
	}

	private void addMenuConstruction() {
		UiListVertical<Button> addMenu = new UiListVertical<>();
		for (EntityTypes type : EntityTypes.values()) {
			char first = type.name().charAt(0);
			String name = type.name().substring(1).toLowerCase();

			Button button = new Button(first + name);
			addMenu.addElement(button);
			typeToButton.put(type, button);
			button.setScale(xRect, yRect);
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
						editor.setCurrentMenu(ArenaEditor.this);
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
								new Text(Main.font, TextAlignH.CENTER, TextAlignV.CENTER, "GrosTest"));
						createNewEntity(text, type);
						break;
					case SPAWN:
						Spawner spawner = new Spawner(new Vec2f(), 4);
						createNewEntity(spawner, type);
						arenaConstruction.playerSpawns.add(spawner);
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
		}
		Button animation = new Button("Animation");
		addMenu.addElement(animation);
		animation.setScale(xRect, yRect);
		animation.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.setCurrentMenu(editor.animEditor);
			}
		});

		for (Button button : addMenu) {
			button.setScale(xRect, yRect);
			button.setColorRect(new Vec4f(0, 0, 0, 0.5));
		}

		mainMenu.addBind("Adding", addMenu);
	}

	private void createNewEntity(Entity entity, EntityTypes type) {
		String entityName = entity.genName();
		entity.attachToParent(parent, entityName);
		Button toSetMenu = new Button(entityName);
		toSetMenu.setScale(xRect, yRect);
		setMenu.addElement(toSetMenu);
		editor.allEditable.add(entity);
		entityToButton.put(entity, toSetMenu);
		toSetMenu.setScissorOk(false);
		toSetMenu.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.onSetting = entity;
				switch (type) {
				case SPAWN:
					editor.setCurrentMenu(new SpawnEditor(ArenaEditor.this, (Spawner) entity));
					break;

				default:
					editor.setCurrentMenu(new EntityEditor(ArenaEditor.this, entity, type));
					break;
				}
			}
		});
		current = mainMenu;
		toSetMenu.arm();
		this.parent = arenaConstruction;
	}

	private void createNewEntityToLoad(Entity entity, EntityTypes type, String name) {
		Button toSetMenu = new Button(name);
		toSetMenu.setScale(xRect, yRect);
		setMenu.addElement(toSetMenu);
		editor.allEditable.add(entity);
		entityToButton.put(entity, toSetMenu);
		toSetMenu.setScissorOk(false);
		toSetMenu.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.onSetting = entity;
				switch (type) {
				case SPAWN:
					editor.setCurrentMenu(new SpawnEditor(ArenaEditor.this, (Spawner) entity));
					break;

				default:
					editor.setCurrentMenu(new EntityEditor(ArenaEditor.this, entity, type));
					break;
				}
			}
		});
	}

	void newEntity(Entity parent, EntityTypes type) {
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
		mainMenu.resetCurrentList();
		current = mainMenu;
		toRemove.detach();
		editor.allEditable.remove(toRemove);
		editor.setCurrentMenu(this);
	}

	public void constructCamerabutton(Camera cam) {
		Button toSetMenu = new Button("camera");
		toSetMenu.setScissorOk(false);
		toSetMenu.setScale(xRect, yRect);
		setMenu.addElement(toSetMenu);
		entityToButton.put(cam, toSetMenu);
		toSetMenu.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.onSetting = cam;
				editor.setCurrentMenu(new EntityEditor(ArenaEditor.this, cam, EntityTypes.ENTITY));

				setCamera(arenaConstruction.getArena().cameraBasePos);
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

	private Vec3f getAmbientLight() {
		return arenaConstruction.ambientLight;
	}

	private void setAmbientLight(Vec3f ambient) {
		arenaConstruction.ambientLight.set(ambient);
	}

	private void setCamera(Vec3f pos) {
		Camera cam = (Camera) arenaConstruction.getChild("camera");
		cam.localPosition = pos;
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
		background.update(delta);
		switch (ui_InputState) {
		case TEXT:
			textInputGroup.update(delta);
			textInput.update(delta);
			break;
		case COLOR_PICKER:
			colorPicker.update(delta);
			colorPickerModification.make();
			break;
		case DOUBLE:
			doubleInput.update(delta);
			break;
		default:
			current.update(delta);
			break;
		}
		if (getTarget() != null) {
			UiImage.selector.setVisible(true);
			UiImage.selector.setPositionLerp(getTarget().getPosition().x, getTarget().getPosition().y, 32);
			UiImage.selector.update(delta);
		} else {
			UiImage.selector.setVisible(false);
		}
	}

	@Override
	public void draw() {
		background.draw();
		current.draw();
		switch (ui_InputState) {
		case TEXT:
			textInputGroup.draw();
			textInput.draw();
			break;
		case COLOR_PICKER:
			colorPicker.draw();
			break;
		case DOUBLE:
			doubleInputGroup.draw();
			doubleInput.draw();
			break;
		default:
			UiImage.selector.draw();
			break;
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
			if (!current.continueAction()) {
				setMenuVisible(!menuVisible);
			}
			return true;
		}
	}

	@Override
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

	@Override
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
			} else if (current == mainMenu && quitButton != null) {
				mainMenu.setTarget(quitButton);
			}
			return false;
		}
	}

	@Override
	public UiElement getTarget() {
		return current.getTarget();
	}

}
