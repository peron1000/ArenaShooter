package arenashooter.game.gameStates;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.UiImage;
import arenashooter.entities.spatials.Sprite;
import arenashooter.game.GameMaster;

public class Score extends GameState {
	
	private Menu menu = new Menu(5);
	
	@Override
	public void init(){
		
		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Score.png");
		texture1.setFilter(false);
		UiImage bg = new UiImage(0, new Vec2f(177.78, 100), texture1, new Vec4f(1, 1, 1, 1));
		menu.setBackground(bg);
		

		Texture texButtonA = Texture.loadTexture("data/sprites/interface/Button_A.png");
		texButtonA.setFilter(false);
		UiImage butA = new UiImage(0, new Vec2f(texButtonA.getWidth()/2,texButtonA.getHeight()/2), texButtonA, new Vec4f(1, 1, 1, 1));
		menu.addUiElement(butA, 4);
		butA.setPos(new Vec2f(-67, 41));
		
		Texture texButtonY = Texture.loadTexture("data/sprites/interface/Button_Y.png");
		texButtonY.setFilter(false);
		UiImage butY = new UiImage(0, new Vec2f(texButtonY.getWidth()/2,texButtonY.getHeight()/2), texButtonY, new Vec4f(1, 1, 1, 1));
		menu.addUiElement(butY, 4);
		butY.setPos(new Vec2f(-30, 41));
		
		Texture texButtonB = Texture.loadTexture("data/sprites/interface/Button_B.png");
		texButtonB.setFilter(false);
		UiImage butB = new UiImage(0, new Vec2f(texButtonB.getWidth()/2,texButtonB.getHeight()/2), texButtonB, new Vec4f(1, 1, 1, 1));
		menu.addUiElement(butB, 4);
		butB.setPos(new Vec2f(7, 41));
	
		
		super.init();
	}
	
	@Override
	public void update(double d) {
		if (Input.actionJustReleased(Device.KEYBOARD, Action.UI_OK)) {
			GameMaster.gm.requestNextState(new CharacterChooser(), GameMaster.mapEmpty);
		}
		super.update(d);
		menu.update(d);
	}
	
	public void draw() {
		super.draw();
		menu.draw();
	}
}