package application;

import java.util.HashMap;

import gamedata.entities.Entity;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class SceneTree extends TreeView<String> {
	
	private HashMap<TreeItem<String>, Entity> entities = new HashMap<TreeItem<String>, Entity>();

	public SceneTree() {
		TreeItem<String> rootItem = new TreeItem<String> ("Scene");
        rootItem.setExpanded(true);
        setRoot(rootItem);
        
        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleMouseClicked(event);
        };
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
	}
	
	/**
	 * Update the currently selected item in the tree view
	 * @param e
	 */
	public void setSelected(Entity e) {
		getSelectionModel().select(e.treeItem);
	}
	
	/**
	 * Creates a treeItem for an entity and add it to the tree
	 * @param e
	 */
	public void addEntity(Entity e) {
		e.treeItem = new TreeItem<String>(e.name, e.getIcon());

		if(e.parent == null) 
			getRoot().getChildren().add(e.treeItem);
		else
			e.parent.treeItem.getChildren().add(e.treeItem);
		
		entities.put(e.treeItem, e);
	}
	
	private void handleMouseClicked(MouseEvent event) {
		Node node = event.getPickResult().getIntersectedNode();
		// Accept clicks only on node cells, and not on empty spaces of the TreeView
		if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
			if( getSelectionModel().getSelectedItem() != getRoot() ) {
				Entity entity = entities.get(getSelectionModel().getSelectedItem());
				if(entity != null )
					Affichage.selectEntity(entity);
			}
		}
	}
}
