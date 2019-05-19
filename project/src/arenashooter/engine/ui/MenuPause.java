package arenashooter.engine.ui;

import java.awt.Robot;
import java.util.LinkedList;

import org.apache.logging.log4j.core.util.Log4jThread;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.CharacterChooser;

public class MenuPause extends MenuSelectionV<UiElement> {
	
	private UiImage selec;
	private Label op1, op2, op3, op4;
	private boolean activated = false;	
	
	public MenuPause(int maxLayout) {
		super(maxLayout);
		if(maxLayout < 3) {
			Exception e = new Exception("Max layout trop petit");
			e.printStackTrace();
			
			// Pour eviter le crash
			for (int i = maxLayout; i < 3; i++) {
				elems.put(Integer.valueOf(i), new LinkedList<>());
			}
		}
		final float scale = 27f;
	
		new Rectangle(this, new Vec2f(), 0, new Vec2f(45, 60), new Vec4f(0, 0, 0, .25) , 0);
		new Label(this, new Vec2f(0, -30), 0, new Vec2f(50, 50), "PAUSE" , 1);
		
		op1 = new Label(this, new Vec2f(0, -15), 0, new Vec2f(scale), "Resume" , 1);
				
		op2 = new Label(this, new Vec2f(0, -5), 0, new Vec2f(scale), "Score" , 1);
	
		op3 = new Label(this, new Vec2f(0, 5), 0, new Vec2f(scale), "Option" , 1);

		op4 = new Label(this, new Vec2f(0, 15), 0, new Vec2f(scale), "Quit : Alt+f4" , 1);
	
		op1.addAction("ok", new Trigger() {

			@Override
			public void make() {
				 try { 
					 Robot robot = new Robot();
					 robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
					 System.out.println("op1 : Resume");
			  } catch (Exception ex) {
				  	System.out.println("fail pause");
				  	Log4jThread.getDefaultUncaughtExceptionHandler();
			      
			  }
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
				GameMaster.gm.requestNextState(new CharacterChooser(), GameMaster.mapEmpty);
			}
		});
		
		op3.visible = false;
		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
		texture2.setFilter(false);		
		selec = new UiImage(this, new Vec2f(), 0, new Vec2f(45,18), texture2,
				new Vec4f(1, 1, 1, 1) , 2);
		this.ecartement = 10;
		this.setImageSelec(selec);
		this.setPositionRef(new Vec2f(0, -25));
		this.addElement(op1);
		this.addElement(op2);	
		this.addElement(op3);
		this.addElement(op4);
		//this.focus = op1;
		
	
	}

	@Override
	public void update(double delta) {
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_UP)||Input.actionJustPressed(Device.CONTROLLER01, Action.UI_UP)) {
			if (!activated) {
				this.previous(delta);
			}
		}
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_DOWN)||Input.actionJustPressed(Device.CONTROLLER01, Action.UI_DOWN)) {
			if (!activated) {
				this.next(delta);
			}
		}
		
		
		if (Input.actionJustPressed(Device.KEYBOARD, Action.JUMP)) {
			activated = !activated;
		}
		
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)||Input.actionJustPressed(Device.CONTROLLER01, Action.UI_OK)) {
			this.getTarget().lunchAction("ok");
		}
			
		super.update(delta);
		
	}

}
