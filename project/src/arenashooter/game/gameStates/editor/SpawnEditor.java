package arenashooter.game.gameStates.editor;

import java.util.Map.Entry;

import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiGroup;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.UiValuableButton;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.gameStates.editor.editorEnum.Ui_Input;

public class SpawnEditor extends EntityEditor {

	private TabList<Button> itemChooser = new TabList<>();
	private Spawner spawner;
	private UiListVertical<UiElement> itemsList = new UiListVertical<>();

	public SpawnEditor(ArenaEditor mainMenu, Spawner spawner) {
		super(mainMenu, spawner, "SPAWNER");
		this.spawner = spawner;

		if (mainMenu.arenaConstruction.items.isEmpty()) {
			Label info = new Label("No items available");
			info.setScale(labelScale);
			info.setColor(new Vec4f(0.8, 0.2, 0.2, 0.9));
			menu.addLabelInfo(vList, info);
		} else {

			Button addItem = new Button("Add item in list");
			addItem.setScale(buttonXScale, buttonYScale);
			addItem.setOnArm(new Trigger() {

				@Override
				public void make() {
					current = itemChooser;
				}
			});

			vList.addElement(addItem);

			UiListVertical<Button> listChoice = new UiListVertical<>();
			for (Item item : mainMenu.arenaConstruction.items) {
				Button b = new Button(item.name);
				b.setScale(buttonXScale, buttonYScale);
				b.setOnArm(new Trigger() {

					@Override
					public void make() {
						ui_InputState = Ui_Input.DOUBLE;

						UiImage border = new UiImage(0, 0, 0, 1), bg = new UiImage(.8, .8, .8, .5);
						border.setScale(41);
						bg.setScale(39);
						Label title = new Label("Enter item proba");
						title.setScale(scaleText);
						title.setPosition(0, -12);
						doubleInputGroup = new UiGroup<>(border, bg, title);

						doubleInput.setPosition(0, 0);
						doubleInput.reset();
						doubleInput.setOnFinish(new Trigger() {

							@Override
							public void make() {
								ui_InputState = Ui_Input.NOTHING;
								spawner.addItem(item.name, (int) doubleInput.getDouble());
								current = menu;
								updateListItem();
							}

						});
						doubleInput.setOnCancel(new Trigger() {

							@Override
							public void make() {
								ui_InputState = Ui_Input.NOTHING;
								updateListItem();
							}
						});

					}
				});
				listChoice.addElement(b);
			}

			itemChooser.addBind("Choose an Item", listChoice);
			itemChooser.setPosition(menu.getPosition());
			itemChooser.setTitleScale(titleScale, titleScale);

		}

		menu.addBind("Items", itemsList);
		menu.setArrowsDistance(15);
		menu.setScaleArrows(8, 8);
		
		updateListItem();

		initPosition();
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPositionLerp(x, y, lerp);
		itemChooser.addToPositionLerp(xDif, yDif, lerp);
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPosition(x, y);
		itemChooser.addToPosition(xDif, yDif);
	}

	private void updateListItem() {
		itemsList.forEach(e -> itemsList.removeElement(e));
		if (spawner.getAvailableItems().isEmpty()) {
			Label label = new Label("Empty");
			label.setScale(scaleText);
			itemsList.addElement(label);
		} else {
			for (Entry<String, Integer> entry : spawner.getAvailableItems().entrySet()) {
				UiValuableButton<Integer> button = new UiValuableButton<Integer>(entry.getKey(), entry.getValue());
				button.setScale(buttonXScale, buttonYScale);
				button.setRectangleVisible(true);
				button.setOnArm(new Trigger() {
					
					@Override
					public void make() {
						ui_InputState = Ui_Input.DOUBLE;
						doubleInputGroup = getGroupSettingProba();
						doubleInput.reset();
						doubleInput.setOnCancel(new Trigger() {
							
							@Override
							public void make() {
								ui_InputState = Ui_Input.NOTHING;
							}
						});
						doubleInput.setOnFinish(new Trigger() {
							
							@Override
							public void make() {
								ui_InputState = Ui_Input.NOTHING;
								Integer i = Integer.valueOf((int) doubleInput.getDouble());
								if(i.equals(0)) {
									spawner.getAvailableItems().remove(entry.getKey());
								} else {
									button.setValue(i);
									spawner.getAvailableItems().put(entry.getKey(), i);
								}
								updateListItem();
							}
						});
					}
				});
				itemsList.addElement(button);
			}
		}
	}
	
	private UiGroup<UiElement> getGroupSettingProba(){
		UiGroup<UiElement> ret = new UiGroup<>();
		double textScale = 2.5;

		// background
		UiImage bg = new UiImage(.85, .8, .75, .7), border = new UiImage(0, 0, 0, 1);
		bg.setScale(60, 35);
		double toBorder = 1.5;
		border.setScale(bg.getScale().x + toBorder, bg.getScale().y + toBorder);

		// Title
		Label titleLabel = new Label("Setting proba");
		titleLabel.setScale(5.5);
		titleLabel.setPosition(0, -12);
		
		Label instr = new Label("Enter 0 to remove this items");
		instr.setScale(textScale);
		instr.setPosition(0 , -8);
		
		doubleInput.setPosition(-1, 3);
		
		ret.addElements(border , bg , titleLabel , instr , doubleInput);
		return ret;
		
	}

}
