package arenashooter;

import arenashooter.engine.graphics.Window;

public class Main {
	static Window window;
	
	public static void main(String[] args) {
		window = new Window(640, 480, "Super Blep");
		while( !window.requestClose() ) {
			//TODO: mettre le jeu ici

			window.update();
		}
		
		window.destroy();
	}

}
