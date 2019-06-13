package arenashooter.game.gameStates.editor;

import java.util.function.Consumer;

import arenashooter.engine.ui.MultiUi;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.TextInput;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.gameStates.editor.editorEnum.SetEditable;
import arenashooter.game.gameStates.editor.editorEnum.TypeEntites;

class EntityEditor extends UiElement implements MultiUi {

	private String entityNameString = "";
	private Label entityNameLabel, parent;
	private TabList<UiElement> menu = new TabList<>();
	private boolean onWritting = false;
	private ScrollerH<TypeEntites> newChild = new ScrollerH<>(TypeEntites.values());
	private ScrollerH<SetEditable> modification = new ScrollerH<>(SetEditable.values());
	private TextInput textInput = new TextInput();
	
	public EntityEditor(MainMenu mainMenu, Entity entity, TypeEntites type) {

		// Make Label for entity name and parent entity name
		makeLabelsForEntity(entity);

		// makeTextInput(mainMenu , entity);

		newChild.setTitle("New Child");
		newChild.setScale(5);
		newChild.setAlwaysScrollable(true);
		newChild.setOnArm(new Trigger() {

			@Override
			public void make() {
				mainMenu.newEntity(entity, newChild.get());
			}
		});
		UiListVertical<UiActionable> vList = new UiListVertical<>();
		vList.addElement(newChild);

		modification.setAlwaysScrollable(true);
		modification.setTitle("Setting");
		modification.setScale(5);
		vList.addElement(modification);

		Button rename = new Button("Rename Entity");
		rename.setScaleText(5);
		rename.setScaleRect(30, 5);
		rename.setOnArm(new Trigger() {

			@Override
			public void make() {
				onWritting = true;
				entityNameLabel.setVisible(false);
				textInput = new TextInput();
				textInput.setPosition(entityNameLabel.getPosition().x , entityNameLabel.getPosition().y);
				textInput.setScale(10);
				textInput.setOnFinish(new Trigger() {

					@Override
					public void make() {
						onWritting = false;
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
		removeEntity.setScaleText(5);
		removeEntity.setScaleRect(30, 5);
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
			Button setText = new Button("set Text");
			vList.addElement(setText);
			setText.setOnArm(new Trigger() {

				@Override
				public void make() {
					onWritting = true;
					setText.setVisible(false);
					textInput = new TextInput();
					textInput.setPosition(setText.getPosition().x , setText.getPosition().y);
					textInput.setScale(10);
					textInput.setOnFinish(new Trigger() {

						@Override
						public void make() {
							onWritting = false;
							((TextSpatial) entity).setText(textInput.getText());
							setText.setVisible(true);
						}
					});
				}
			});
		default:
			break;
		}
		
		menu.addBind(type.name() + " Editor", vList);
		menu.addLabelInfo(vList, entityNameLabel);
		menu.addLabelInfo(vList, parent);
		menu.setSpacing(7);
		menu.setTitleSpacing(1);
		menu.setPosition(0, -35);
		
		UiImage.selector.setPosition(menu.getTarget().getPosition().x, menu.getTarget().getPosition().y);
		
		setPosition(Editor.forVisible, 0);
	}

	private void makeLabelsForEntity(Entity entity) {
		final double scale = 3.5;
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
		entityNameLabel.setScale(scale);

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

		this.parent.setScale(scale);
	}

	@Override
	public boolean upAction() {
		if (onWritting) {
			return textInput.upAction();
		}
		return menu.upAction();
	}

	@Override
	public boolean downAction() {
		if (onWritting) {
			return textInput.downAction();
		}
		return menu.downAction();
	}

	@Override
	public boolean rightAction() {
		if (onWritting) {
			return textInput.rightAction();
		}
		return menu.rightAction();
	}

	@Override
	public boolean leftAction() {
		if (onWritting) {
			return textInput.leftAction();
		}
		return menu.leftAction();
	}

	@Override
	public boolean selectAction() {
		if (onWritting) {
			return textInput.selectAction();
		} else {
			return menu.selectAction();
		}
	}

	@Override
	public boolean continueAction() {
		if (onWritting) {
			return textInput.continueAction();
		}
		return false;
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		textInput.setVisible(onWritting);
		onAll(e -> e.update(delta));
		UiImage.selector.setPositionLerp(getTarget().getPosition().x, getTarget().getPosition().y, 10);
		UiImage.selector.update(delta);
	}

	@Override
	public void draw() {
		onAll(e -> e.draw());
		UiImage.selector.draw();
	}

	@Override
	public void setPositionLerp(double x , double y, double lerp) {
		double xDif = x - getPosition().x , yDif = y - getPosition().y;
		super.setPositionLerp(x, y ,lerp);
		onAll(e -> e.addToPositionLerp(xDif, yDif , lerp));
	}

	@Override
	public void setPosition(double x , double y) {
		double xDif = x - getPosition().x , yDif = y - getPosition().y;
		super.setPosition(x, y);
		onAll(e -> e.addToPosition(xDif, yDif));
	}

	private void onAll(Consumer<UiElement> c) {
		c.accept(menu);
		c.accept(textInput);
	}

	public boolean changeAction() {
		if (onWritting) {
			return textInput.changeAction();
		}
		return false;
	}

	public boolean cancelAction() {
		if (onWritting) {
			return textInput.cancelAction();
		}
		return false;
	}

	public SetEditable getModificationType() {
		return modification.get();
	}
	
	@Override
	public boolean backAction() {
		if (onWritting) {
			return textInput.backAction();
		}
		return false;
	}

	@Override
	public UiElement getTarget() {
		return menu.getTarget();
	}

}
