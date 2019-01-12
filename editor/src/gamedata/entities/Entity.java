package gamedata.entities;

import java.util.HashMap;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Entity {
	
	public String name;
	public Entity parent;
	public TreeItem<String> treeItem;
	
	public HashMap<String, Entity> children = new HashMap<String, Entity>();

	public Entity() {
	}
	
	public Node getIcon() {
		return new ImageView( new Image("file:icons/entity.png"));
	}

}
