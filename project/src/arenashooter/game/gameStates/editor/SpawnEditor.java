package arenashooter.game.gameStates.editor;

import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiGroup;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.spatials.Spawner;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.gameStates.editor.editorEnum.Ui_Input;

public class SpawnEditor extends EntityEditor {

	private TabList<Button> itemChooser = new TabList<>();

	public SpawnEditor(ArenaEditor mainMenu, Spawner spawner) {
		super(mainMenu, spawner, "SPAWNER");

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
						
						UiImage border = new UiImage(0, 0, 0, 1) , bg = new UiImage(.8, .8, .8, .5);
						border.setScale(41);
						bg.setScale(39);
						Label title = new Label("Enter item proba");
						title.setScale(scaleText);
						title.setPosition(0, -12);
						doubleInputGroup = new UiGroup<>(border , bg , title);
						
						doubleInput.setPosition(0, 0);
						doubleInput.reset();
						doubleInput.setOnFinish(new Trigger() {
							
							@Override
							public void make() {
								ui_InputState = Ui_Input.NOTHING;
								spawner.addItem(item.name, (int) doubleInput.getDouble());
								current = menu;
							}
						});
						doubleInput.setOnCancel(new Trigger() {
							
							@Override
							public void make() {
								ui_InputState = Ui_Input.NOTHING;
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

}
