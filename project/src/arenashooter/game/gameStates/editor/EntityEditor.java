package arenashooter.game.gameStates.editor;

import java.util.function.Consumer;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.Navigable;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.TextInput;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.gameStates.editor.editorEnum.SetEditable;
import arenashooter.game.gameStates.editor.editorEnum.TypeEntites;

class EntityEditor implements Navigable {

	private String entityNameString = "";
	private Label entityNameLabel, title, parent;
	private Vec2f position = new Vec2f(Editor.forVisible, 0);
	private MenuSelectionV<UiActionable> menu = new MenuSelectionV<>();
	private boolean onWritting = false;
	private ScrollerH<TypeEntites> newChild = new ScrollerH<>(0, new Vec2f(30), TypeEntites.values());
	private ScrollerH<SetEditable> modification = new ScrollerH<>(0, new Vec2f(30), SetEditable.values());
	private TextInput textInput = new TextInput();

	public EntityEditor(MainMenu mainMenu, Entity entity, TypeEntites type) {

		// Make title
		title = new Label(0, new Vec2f(40), type.name() + " Editor");
		title.setPosition(new Vec2f(Editor.forVisible, -40));

		// Make Label for entity name and parent entity name
		makeLabelsForEntity(entity);

		// makeTextInput(mainMenu , entity);

		menu.setPosition(position);

		newChild.setTitle("New Child");
		newChild.setAlwaysScrollable(true);
		newChild.setOnArm(new Trigger() {

			@Override
			public void make() {
				mainMenu.newEntity(entity, newChild.get());
			}
		});
		menu.addElementInListOfChoices(newChild, 1);

		modification.setAlwaysScrollable(true);
		modification.setTitle("Setting");
		menu.addElementInListOfChoices(modification, 1);

		Button rename = new Button(0, new Vec2f(10), "Rename Entity");
		rename.setOnArm(new Trigger() {

			@Override
			public void make() {
				onWritting = true;
				entityNameLabel.setVisible(false);
				textInput = new TextInput();
				textInput.setPosition(entityNameLabel.getPosition());
				textInput.setScale(new Vec2f(10));
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
		menu.addElementInListOfChoices(rename, 0);
		menu.setEcartement(8);

		switch (type) {
		case RIGID:
		case STATIC:
			break;
		case TEXT:
			Button setText = new Button(0, new Vec2f(30, 10), "set Text");
			menu.addElementInListOfChoices(setText, 1);
			setText.setOnArm(new Trigger() {

				@Override
				public void make() {
					onWritting = true;
					setText.setVisible(false);
					textInput = new TextInput();
					textInput.setPosition(setText.getPosition());
					textInput.setScale(new Vec2f(10));
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

		entityNameLabel = new Label(0, new Vec2f(30), "Name : " + entityNameString);
		entityNameLabel.setPosition(new Vec2f(Editor.forVisible, -30));

		if (parent instanceof Arena) {
			this.parent = new Label(0, new Vec2f(30), "Parent : Arena");
		} else {
			try {
				for (String name : parent.getParent().getChildren().keySet()) {
					if (parent == parent.getParent().getChild(name)) {
						this.parent = new Label(0, new Vec2f(30), "Parent : " + name);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.parent = new Label(0, new Vec2f(30), "Parent : Error");
			}
		}

		this.parent.setPosition(new Vec2f(Editor.forVisible, -20));
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
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unSelec() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(double delta) {
		textInput.setVisible(onWritting);
		onAll(e -> e.update(delta));
	}

	@Override
	public void draw() {
		onAll(e -> e.draw());
	}

	@Override
	public void setPositionLerp(Vec2f position, double lerp) {
		Vec2f dif = Vec2f.subtract(position, this.position);
		this.position.add(dif);
		onAll(e -> e.setPositionLerp(Vec2f.add(e.getPosition(), dif), lerp));
	}

	@Override
	public void setPosition(Vec2f newPosition) {
		Vec2f dif = Vec2f.subtract(position, this.position);
		this.position.add(dif);
		onAll(e -> e.setPosition(Vec2f.add(e.getPosition(), dif)));
	}

	@Override
	public Vec2f getPosition() {
		return position;
	}

	private void onAll(Consumer<Navigable> c) {
		c.accept(entityNameLabel);
		c.accept(title);
		c.accept(parent);
		c.accept(menu);
		c.accept(textInput);
	}

	public void changeAction() {
		if (onWritting) {
			textInput.changeType();
		}
	}

	public void cancelAction() {
		if (onWritting) {
			textInput.cancelChar();
		}
	}

	public SetEditable getModificationType() {
		return modification.get();
	}

}
