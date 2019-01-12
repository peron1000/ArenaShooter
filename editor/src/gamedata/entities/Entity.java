package gamedata.entities;

import java.util.HashMap;

import application.propertiestabs.EntityProperties;
import application.propertiestabs.PropertiesTab;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Entity {
	
	public String name;
	public Entity parent;
	public TreeItem<String> treeItem;
	
	public HashMap<String, Entity> children = new HashMap<String, Entity>();
	
	public PropertiesTab properties;

	public Entity() {
		
	}
	
	/**
	 * Create the properties tab for this entity
	 */
	public void createProperties() {
		properties = new EntityProperties(this);
	}
	
	public Node getIcon() {
		return new ImageView( new Image("file:icons/entity.png"));
	}
	
	/**
	 * Change name if possible
	 * @param newName
	 * @return entity name after renaming attempt
	 */
	public String rename(String newName) {
		if(newName.isEmpty())
			return name;
		
		if(parent == null) { //No parent, allow rename
			name = newName;
			treeItem.setValue(name);
			return name;
		}

		if(parent.children.get(newName) != null) { //Name already used
			return name;
		}

		//Name available, rename
		parent.children.remove(name);
		name = newName;
		parent.children.put(newName, this);
		treeItem.setValue(name);
		return name;
	}
}
