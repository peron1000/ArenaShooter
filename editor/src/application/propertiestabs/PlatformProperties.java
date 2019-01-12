package application.propertiestabs;

import application.ListEntite;
import application.NameField;
import application.Vec2Input;
import application.customevents.CustomEvent;
import application.customevents.CustomEventHandler;
import application.movableshapes.MovableRectangle;
import gamedata.entities.Platform;
import javafx.scene.Node;
import math.Vec2;

public class PlatformProperties extends PropertiesTab {

	Platform p;
	
	private Vec2Input sizeInput;
	private NameField nameInput;
	
	public PlatformProperties(Platform p) {
		this.p = p;
		
		nameInput = new NameField("Name ", p);
		
		sizeInput = new Vec2Input("Size", p.extent.x*2, p.extent.y*2);
		sizeInput.addEventHandler(CustomEvent.CUSTOM_EVENT_TYPE, new CustomEventHandler() {

			@Override
			public void onEventVec2Change(double newX, double newY) {
				if(newX < 10)
					sizeInput.setValX(10);
				if(newY < 10)
					sizeInput.setValY(10);
				
				newX = Math.max(10, newX);
				newY = Math.max(10, newY);
				
				p.extent.x = newX/2;
				p.extent.y = newY/2;
				
				Node visual = ListEntite.getVisual(p);
				if( (MovableRectangle)visual != null ) ((MovableRectangle)visual).resize( new Vec2(newX, newY) );
			}
		});
		
		
		getChildren().add(nameInput);
		getChildren().add(sizeInput);
	}

	/**
	 * Get sizeInput value
	 * @return
	 */
	public Vec2 getSize() {
		return new Vec2(sizeInput.getValX(), sizeInput.getValY());
	}
	
	/**
	 * Set sizeInput value and update platform extent
	 * @param size
	 */
	public void setSize(Vec2 size) {
		p.extent.x = size.x/2;
		p.extent.y = size.y/2;
		sizeInput.setValX(size.x);
		sizeInput.setValY(size.y);
	}

	@Override
	public void update() {
		nameInput.setText(p.name);
	}
}
