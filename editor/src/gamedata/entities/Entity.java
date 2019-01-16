package gamedata.entities;

import java.util.HashMap;

import application.Affichage;
import application.Main;
import application.propertiestabs.EntityProperties;
import application.propertiestabs.PropertiesTab;
import javafx.scene.control.TreeItem;

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
	
	public String getIcon() {
		return "file:editor_data/icons/entity.png";
	}
	
	/**
	 * Change name if possible
	 * @param newName
	 * @return entity name after renaming attempt
	 */
	public String rename(String newName) {
		if(newName.isEmpty())
			return name;
		
		if(parent == null) { //No parent, check map
			if(Main.map.children.get(newName) != null) //Name already in use
				return name;
				
			//Name available, rename
			Main.map.children.remove(name);
			name = newName;
			Main.map.children.put(newName, this);
			treeItem.setValue(name);
			Affichage.refreshSelectionTitle();
			return name;
		}

		if(parent.children.get(newName) != null) //Name already used
			return name;
		
		//Name available, rename
		parent.children.remove(name);
		name = newName;
		parent.children.put(newName, this);
		treeItem.setValue(name);
		Affichage.refreshSelectionTitle();
		return name;
	}
}
