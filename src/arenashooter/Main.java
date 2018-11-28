package arenashooter;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.entities.Game;

public class Main {
	static Window window;
	
	static int minFrametime = 8;
	
	public static void main(String[] args) {
		window = new Window(1280, 720, "Super Blep");
		window.setVsync(true);
		
		long currentFrame;
		long lastFrame = System.currentTimeMillis()-8;
		
		int fpsFrames = 0;
		long fpsTime = lastFrame;
		
		while( !window.requestClose() ) {
			currentFrame = System.currentTimeMillis();
			
			//Limit delta to avoid errors
			double delta = Utils.clampD((double)(currentFrame-lastFrame)/1000, .001, .5);
			
			//TODO: mettre le jeu ici
			
			window.update( delta );
			
			//TODO: fix this
//			Game.game.update(delta);
			Game.game.draw();
			
			//FPS counter
			fpsFrames++;
			if(fpsFrames >= 10 && (currentFrame-fpsTime)>=250 ) {
				double time = ((double)(currentFrame-fpsTime))/fpsFrames;
				window.setTitle( "Super Blep - " + (int)(1/(time/1000)) + "fps" );
				fpsTime = currentFrame;
				fpsFrames = 0;
			}
			
			//Limit framerate
			if(currentFrame-lastFrame < minFrametime)
				try {
					Thread.sleep( minFrametime-(currentFrame-lastFrame) );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			lastFrame = currentFrame;
		}
		
		window.destroy();
	}

}
