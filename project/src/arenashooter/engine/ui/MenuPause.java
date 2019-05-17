package arenashooter.engine.ui;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class MenuPause extends MenuSelectionV {
	
	private UiImage selec;
	private Label op1, op2, op3, op4;
	private boolean activated = false;	
	
	public MenuPause() {
		final float scale = 27f;
	
		new Rectangle(this, new Vec2f(), 0, new Vec2f(45, 60), new Vec4f(0, 0, 0, .25));
		new Label(this, new Vec2f(0, -30), 0, new Vec2f(50, 50), "PAUSE");
		
		op1 = new Label(this, new Vec2f(0, -15), 0, new Vec2f(scale), "Resume");
				
		op2 = new Label(this, new Vec2f(0, -5), 0, new Vec2f(scale), "Score");
	
		op3 = new Label(this, new Vec2f(0, 5), 0, new Vec2f(scale), "Option");

		op4 = new Label(this, new Vec2f(0, 15), 0, new Vec2f(scale), "Quit : Alt+f4");
	
		op1.addAction("ok", new Trigger() {

			@Override
			public void make() {
				System.out.println("op1 : Resume");
			}
		});
		op2.addAction("ok", new Trigger() {

			@Override
			public void make() {
				System.out.println("op2 : Score");
			}
		});
		op3.addAction("ok", new Trigger() {

			@Override
			public void make() {
				System.out.println("op3 : Option");
			}
		});
		op4.addAction("ok", new Trigger() {

			@Override
			public void make() {
				System.out.println("op4 : quit");
			}
		});
		
		op3.visible = false;
		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
		texture2.setFilter(false);		
		selec = new UiImage(this, new Vec2f(), 0, new Vec2f(40, 8), texture2,
				new Vec4f(1, 1, 1, 1));
		this.ecartement = 10;
		this.setImageSelec(selec);
		this.setPositionRef(new Vec2f(0, -25));
		this.addElement(op1);
		this.addElement(op2);	
		this.addElement(op3);
		this.addElement(op4);	

	
	}

	@Override
	public void update(double delta) {
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_UP)) {
			if (!activated) {
				this.previous(delta);
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_DOWN)) {
			if (!activated) {
				this.next(delta);
			}
		}
		
		
		if (Input.actionJustPressed(Device.KEYBOARD, Action.JUMP)) {
			activated = !activated;
		}
		
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {
			this.getTarget().lunchAction("ok");
		}
			
		super.update(delta);
		
	}

}
