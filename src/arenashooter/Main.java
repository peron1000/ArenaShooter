package arenashooter;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.entities.Game;

public class Main {
	private static final int minFrametime = 8;
	
	public static void main(String[] args) {
		Audio.init(false);
		Window.init(1280, 720, "Super Blep");
		Window.setVsync(true);
		
		//Initialize game
		Game.newGame();
		
		long currentFrame;
		long lastFrame = System.currentTimeMillis()-8;
		
		int fpsFrames = 0;
		long fpsTime = lastFrame;
		
		while( !Window.requestClose() ) {
			currentFrame = System.currentTimeMillis();
			
			//Limit delta to avoid errors
			double delta = Utils.clampD((double)(currentFrame-lastFrame)/1000, .001, .5);
			
			Window.beginFrame();
			
			//TODO: Use this properly
			Game.game.update(delta);
			Game.game.draw();
			
			Window.endFrame();
			
			//FPS counter
			fpsFrames++;
			if(fpsFrames >= 10 && (currentFrame-fpsTime)>=250 ) {
				double time = ((double)(currentFrame-fpsTime))/fpsFrames;
				Window.setTitle( "Super Blep - " + (int)(1/(time/1000)) + "fps" );
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
		
		Window.destroy();
		Audio.destroy();
	}

}
