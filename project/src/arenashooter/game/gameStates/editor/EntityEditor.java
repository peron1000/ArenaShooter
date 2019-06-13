package arenashooter.game.gameStates.editor;

import java.util.function.Consumer;

import arenashooter.engine.graphics.Light;
import arenashooter.engine.ui.ColorPicker;
import arenashooter.engine.ui.DoubleInput;
import arenashooter.engine.ui.MultiUi;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.TextInput;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.LightContainer;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.gameStates.editor.editorEnum.SetEditable;
import arenashooter.game.gameStates.editor.editorEnum.TypeEntites;
import arenashooter.game.gameStates.editor.editorEnum.Ui_Input;

class EntityEditor extends UiElement implements MultiUi {

	private String entityNameString = "";
	private Label entityNameLabel, parent;
	private TabList<UiElement> menu = new TabList<>();
	private Ui_Input uiInputState = Ui_Input.NOTHING;
	private ScrollerH<TypeEntites> newChild = new ScrollerH<>(TypeEntites.values());
	private ScrollerH<SetEditable> modification = new ScrollerH<>(SetEditable.values());
	private TextInput textInput = new TextInput();
	private DoubleInput doubleInput = new DoubleInput();
	private ColorPicker colorPicker = new ColorPicker(false);
	
	private final double labelScale = 3.5;

	private Trigger colorPickerModification = new Trigger() {

		@Override
		public void make() {
			// Nothing by default
		}
	};

	public EntityEditor(MainMenu mainMenu, Entity entity, TypeEntites type) {

		// Make Label for entity name and parent entity name
		makeLabelsForEntity(entity);

		newChild.setTitle("New Child");
		newChild.setScale(5);
		newChild.setAlwaysScrollable(true);
		newChild.setOnArm(new Trigger() {

			@Override
			public void make() {
				mainMenu.newEntity(entity, newChild.get());
			}
		});
		UiListVertical<UiElement> vList = new UiListVertical<>();
		vList.addElement(newChild);

		modification.setAlwaysScrollable(true);
		modification.setTitle("Setting");
		modification.setScale(5);
		vList.addElement(modification);

		Button rename = new Button("Rename Entity");
		rename.setOnArm(new Trigger() {

			@Override
			public void make() {
				uiInputState = Ui_Input.TEXT;
				entityNameLabel.setVisible(false);
				textInput.reset();
				textInput.setPosition(entityNameLabel.getPosition().x, entityNameLabel.getPosition().y);
				textInput.setScale(10);
				textInput.setOnFinish(new Trigger() {

					@Override
					public void make() {
						uiInputState = Ui_Input.NOTHING;
						Entity parent = entity.getParent();
						entityNameString = textInput.getText();
						entity.detach();
						entity.attachToParent(parent, entityNameString);
						entityNameLabel.setText("Name : " + entityNameString);
						entityNameLabel.setVisible(true);
						mainMenu.setButtonName(entity, entityNameString);
					}
				});
			}
		});
		vList.addElement(rename);

		Button removeEntity = new Button("Remove Entity");
		removeEntity.setOnArm(new Trigger() {

			@Override
			public void make() {
				mainMenu.removeEntity(entity);
			}
		});
		vList.addElement(removeEntity);

		switch (type) {
		case RIGID:
		case STATIC:
			break;
		case TEXT:
			Button setText = new Button("Set Text");
			vList.addElement(setText);
			setText.setOnArm(new Trigger() {

				@Override
				public void make() {
					uiInputState = Ui_Input.TEXT;
					setText.setVisible(false);
					textInput = new TextInput();
					textInput.setPosition(setText.getPosition().x, setText.getPosition().y);
					textInput.setScale(10);
					textInput.setOnFinish(new Trigger() {

						@Override
						public void make() {
							uiInputState = Ui_Input.NOTHING;
							((TextSpatial) entity).setText(textInput.getText());
							setText.setVisible(true);
						}
					});
				}
			});
			break;
		case LIGHT:
			LightContainer light = (LightContainer) entity;
			ScrollerH<Light.LightType> lightType = new ScrollerH<>(Light.LightType.values());
			lightType.setAlwaysScrollable(true);
			Label radius = new Label("Radius : "+light.getLight().radius);
			radius.setScale(labelScale);
			menu.addLabelInfo(vList, radius);
			Button setColorPicker = new Button("Change color"), setRadius = new Button("Set radius");
			vList.addElements(setColorPicker , setRadius , lightType);
			setColorPicker.setOnArm(new Trigger() {

				@Override
				public void make() {
					uiInputState = Ui_Input.COLOR_PICKER;
					vList.addElement(colorPicker);
					colorPicker.setOnFinish(new Trigger() {

						@Override
						public void make() {
							uiInputState = Ui_Input.NOTHING;
							vList.removeElement(colorPicker);
						}
					});
					colorPickerModification = new Trigger() {

						@Override
						public void make() {
							light.getLight().color.set(colorPicker.getColorRGB());
						}
					};
				}
			});
			setRadius.setOnArm(new Trigger() {

				@Override
				public void make() {
					uiInputState = Ui_Input.DOUBLE;
					vList.addElement(doubleInput);
					doubleInput.setOnFinish(new Trigger() {

						@Override
						public void make() {
							uiInputState = Ui_Input.NOTHING;
							vList.removeElement(doubleInput);
							light.getLight().radius = (float) doubleInput.getDouble();
							radius.setText("Radius : "+light.getLight().radius);
						}
					});
					doubleInput.setOnCancel(new Trigger() {

						@Override
						public void make() {
							uiInputState = Ui_Input.NOTHING;
							vList.removeElement(doubleInput);
						}
					});
				}
			});
			break;
		default:
			break;
		}

		menu.addBind(type.name() + " Editor", vList);
		menu.addLabelInfo(vList, entityNameLabel);
		menu.addLabelInfo(vList, parent);
		menu.setSpacing(8);
		menu.setTitleSpacing(1);
		menu.setPosition(0, -35);
		for (UiElement e : vList) {
			if (e instanceof Button) {
				Button b = (Button) e;
				b.setScaleText(5);
				b.setScaleRect(30, 5);
			}
		}

		setPosition(Editor.forVisible, 0);
		UiImage.selector.setPosition(menu.getTarget().getPosition().x, menu.getTarget().getPosition().y);
	}

	private void makeLabelsForEntity(Entity entity) {
		Entity parent = entity.getParent();
		try {
			for (String name : parent.getChildren().keySet()) {
				if (entity == parent.getChild(name)) {
					entityNameString = name;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			entityNameString = "error";
		}

		entityNameLabel = new Label("Name : " + entityNameString);
		entityNameLabel.setScale(labelScale);

		if (parent instanceof Arena) {
			this.parent = new Label("Parent : Arena");
		} else {
			try {
				for (String name : parent.getParent().getChildren().keySet()) {
					if (parent == parent.getParent().getChild(name)) {
						this.parent = new Label("Parent : " + name);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.parent = new Label("Parent : Error");
			}
		}

		this.parent.setScale(labelScale);
	}

	@Override
	public boolean upAction() {
		switch (uiInputState) {
		case TEXT:
			return textInput.upAction();
		case COLOR_PICKER:
			return colorPicker.upAction();
		case DOUBLE:
			return doubleInput.upAction();
		default:
			return menu.upAction();
		}
	}

	@Override
	public boolean downAction() {
		switch (uiInputState) {
		case TEXT:
			return textInput.downAction();
		case COLOR_PICKER:
			return colorPicker.downAction();
		case DOUBLE:
			return doubleInput.downAction();
		default:
			return menu.downAction();
		}
	}

	@Override
	public boolean rightAction() {
		switch (uiInputState) {
		case TEXT:
			return textInput.rightAction();
		case COLOR_PICKER:
			return colorPicker.rightAction();
		case DOUBLE:
			return doubleInput.rightAction();
		default:
			return menu.rightAction();
		}
	}

	@Override
	public boolean leftAction() {
		switch (uiInputState) {
		case TEXT:
			return textInput.leftAction();
		case COLOR_PICKER:
			return colorPicker.leftAction();
		case DOUBLE:
			return doubleInput.leftAction();
		default:
			return menu.leftAction();
		}
	}

	@Override
	public boolean selectAction() {
		switch (uiInputState) {
		case TEXT:
			return textInput.selectAction();
		case COLOR_PICKER:
			return colorPicker.selectAction();
		case DOUBLE:
			return doubleInput.selectAction();
		default:
			return menu.selectAction();
		}
	}

	@Override
	public boolean continueAction() {
		switch (uiInputState) {
		case TEXT:
			return textInput.continueAction();
		case COLOR_PICKER:
			return colorPicker.continueAction();
		case DOUBLE:
			return doubleInput.continueAction();
		default:
			return menu.continueAction();
		}
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		onAll(e -> e.update(delta));
		switch (uiInputState) {
		case TEXT:
			textInput.update(delta);
			break;
		case COLOR_PICKER:
			colorPicker.update(delta);
			colorPickerModification.make();
		case DOUBLE:
			doubleInput.update(delta);
		default:
			break;
		}
		UiImage.selector.setPositionLerp(getTarget().getPosition().x, getTarget().getPosition().y, 10);
		UiImage.selector.update(delta);
	}

	@Override
	public void draw() {
		onAll(e -> e.draw());
		switch (uiInputState) {
		case TEXT:
			textInput.draw();
			break;
		case COLOR_PICKER:
			colorPicker.draw();
			break;
		case DOUBLE:
			doubleInput.draw();
		default:
			UiImage.selector.draw();
			break;
		}
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPositionLerp(x, y, lerp);
		onAll(e -> e.addToPositionLerp(xDif, yDif, lerp));
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPosition(x, y);
		onAll(e -> e.addToPosition(xDif, yDif));
	}

	private void onAll(Consumer<UiElement> c) {
		c.accept(menu);
	}

	public boolean changeAction() {
		switch (uiInputState) {
		case TEXT:
			return textInput.changeAction();
		case COLOR_PICKER:
			return colorPicker.changeAction();
		case DOUBLE:
			return doubleInput.changeAction();
		default:
			return false;
		}
	}

	public boolean cancelAction() {
		switch (uiInputState) {
		case TEXT:
			return textInput.cancelAction();
		case COLOR_PICKER:
			return colorPicker.cancelAction();
		case DOUBLE:
			return doubleInput.cancelAction();
		default:
			return false;
		}
	}

	public SetEditable getModificationType() {
		return modification.get();
	}

	@Override
	public boolean backAction() {
		switch (uiInputState) {
		case TEXT:
			return textInput.backAction();
		case COLOR_PICKER:
			return colorPicker.backAction();
		default:
			return false;
		}
	}

	@Override
	public UiElement getTarget() {
		return menu.getTarget();
	}

}
