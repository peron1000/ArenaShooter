package gamedata.entities;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import math.Vec2;

public class Spatial extends Entity {

	public Vec2 position;
	public double rotation;
	
	public Spatial(Vec2 position, double rotation) {
		this.position = position.clone();
		this.rotation = rotation;
	}
	
	@Override
	public Node getIcon() {
		return new ImageView( new Image("file:icons/spatial.png"));
	}

}
