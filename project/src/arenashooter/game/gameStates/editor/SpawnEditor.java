package arenashooter.game.gameStates.editor;

import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.items.Item;
import arenashooter.game.gameStates.editor.editorEnum.EntityTypes;

public class SpawnEditor extends EntityEditor {

	private TabList<Button> itemChooser = new TabList<>();

	public SpawnEditor(ArenaEditor mainMenu, Entity entity) {
		super(mainMenu, entity, EntityTypes.SPAWN);

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
				listChoice.addElement(b);
			}
			itemChooser.addBind("Choose an Item", listChoice);
			itemChooser.setPosition(0 , yMenuPosition);
			itemChooser.setTitleScale(titleScale, titleScale);
			
		}
	}
	
	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPositionLerp(x, y, lerp);
		itemChooser.addToPositionLerp(xDif, yDif , lerp);
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPosition(x, y);
		itemChooser.addToPosition(xDif, yDif);
	}

}
