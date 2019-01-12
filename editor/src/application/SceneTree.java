package application;

import gamedata.entities.Entity;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class SceneTree extends TreeView<String> {

	public SceneTree() {
		TreeItem<String> rootItem = new TreeItem<String> ("Scene");
        rootItem.setExpanded(true);
        setRoot(rootItem);
	}
	
	/**
	 * Creates a treeItem for an entity and add it to the tree
	 * @param e
	 */
	public void addEntity(Entity e) {
		e.treeItem = new TreeItem<String>(e.name, e.getIcon());
		if(e.parent == null) {
			getRoot().getChildren().add(e.treeItem);
		} else {
			e.parent.treeItem.getChildren().add(e.treeItem);
		}
	}
}
