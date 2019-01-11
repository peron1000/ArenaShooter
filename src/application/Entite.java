package application;

public class Entite {
	double xPosition;
	double yPosition;
	double xExtent;
	double yExtent;
	double xDiffChar;
	double yDiffChar;
	private Type type;
	enum Type {
		Plateforme , Spawn;
	}
	public Entite(Type type , double xPosition , double yPosition , double xExtent , double yExtent) {
		this.type = type;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.xExtent = xExtent;
		this.yExtent = yExtent;
		xDiffChar = xPosition - ListEntite.getRecChar().getX();
		yDiffChar = xPosition - ListEntite.getRecChar().getY();
	}
	public Type getType() {
		return type;
	}
	
	
}
