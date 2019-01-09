package arenashooter.game;

import arenashooter.engine.Profiler;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;

public class Main {
	private static final int minFrametime = 8;
	
	public static boolean drawCollisions = true;
	
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
			
			Profiler.beginFrame();
			
			Window.beginFrame();
			
			Profiler.startStep();
			
			Game.game.update(delta);
			
			Profiler.endStep();
			Profiler.startRender();
			
			Game.game.draw();
			
			Window.endFrame();
			
			Profiler.endRender();
			
			Profiler.startElem();
			
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
			
			Profiler.endSleep();
			Profiler.printTimes();
		}
		
		Window.destroy();
		Audio.destroy();
	}

}
