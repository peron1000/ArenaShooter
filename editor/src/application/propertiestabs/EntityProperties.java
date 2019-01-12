package application.propertiestabs;

import application.NameField;
import gamedata.entities.Entity;

public class EntityProperties extends PropertiesTab {
	
	public Entity entity;
	
	private NameField nameInput;

	public EntityProperties(Entity e) {
		super();
		entity = e;
		
		nameInput = new NameField("Name ", e);
		
		getChildren().add(nameInput);
	}

	@Override
	public void update() {
		nameInput.setText(entity.name);
	}

}
