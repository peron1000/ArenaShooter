package arenashooter.game.gameStates.engineParam;

abstract class ParamElement <T> {
	
	private T[] choices;
	private String title;
	private int index = 0;
	
	@SafeVarargs
	public ParamElement(String title , T...choices) {
		this.title = title;
		this.choices = choices;
	}
	
	String getTitle() {
		return title;
	}
	
	T getValue() {
		return choices[index];
	}
	
	void next() {
		index++;
		if(index >= choices.length) {
			index = 0;
		}
	}
	
	void previous() {
		index--;
		if(index < 0) {
			index = choices.length-1;
		}
	}
	
	abstract String getStringValue(); 
	
}
