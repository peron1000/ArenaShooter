package arenashooter.engine.ui;

public abstract class UiActionable extends UiElement implements Actionable {
	private Trigger arm = new Trigger() {
		
		@Override
		public void make() {
			// Nothing by default
		}
	};
	
	public UiActionable(float xPos , float yPos , float xScale , float yScale , double rot) {
		super(xPos , yPos , xScale , yScale , rot);
	}

	public UiActionable() {
		super();
	}
	
	@Override
	public void setOnArm(Trigger t) {
		arm = t;
	}
	
	@Override
	public void arm() {
		arm.make();
	}
}
