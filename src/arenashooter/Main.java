package arenashooter;

import arenashooter.engine.graphics.Window;

public class Main {
	static Window window;
	
	public static void main(String[] args) {
		window = new Window(1280, 720, "Super Blep");
		
		long currentFrame;
		long lastFrame = System.currentTimeMillis();
		
		while( !window.requestClose() ) {
			currentFrame = System.currentTimeMillis();
			
			double delta = (double)(currentFrame-lastFrame)/1000;
			
			window.setTitle( "Super Blep - " + (int)(1/delta) + "fps" );
			
			//TODO: mettre le jeu ici
			
			window.update( delta );
			try {
				Thread.sleep( Math.max(lastFrame-currentFrame, 15) );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastFrame = currentFrame;
		}
		
		window.destroy();
	}

}
