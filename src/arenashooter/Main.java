package arenashooter;

import arenashooter.engine.graphics.Window;

public class Main {
	static Window window;
	
	public static void main(String[] args) {
		window = new Window(1280, 720, "Super Blep");
		
		long lastFrame = System.currentTimeMillis();
		
		while( !window.requestClose() ) {
			window.setTitle( "Super Blep - " + (System.currentTimeMillis()-lastFrame) + "ms" );
			
			//TODO: mettre le jeu ici
			
			window.update();
			lastFrame = System.currentTimeMillis();
		}
		
		window.destroy();
	}

}
