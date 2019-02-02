package math;

public class Vec2 {

	public double x, y;
	
	public Vec2() {
		x=0;
		y=0;
	}
	
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2(double a) {
		x=a;
		y=a;
	}
	
	public Vec2(Vec2 v) {
		x = v.x;
		y = v.y;
	}
	
	public Vec2 clone() {
		return new Vec2(x, y);
	}

	public String toString() {
		return "( " + x + ", " + y + " )";
	}

}
