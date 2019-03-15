package arenashooter.game;

import arenashooter.engine.ConfigManager;
import arenashooter.engine.Profiler;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.math.Utils;

public class Main {
	private static final int minFrametime = 8;
	
	public static boolean drawCollisions = true;
	
	private static GameMaster gameMaster;
	
	public static Font font = null;
	
	public static void main(String[] args) {
		ConfigManager.init();
		Audio.init(false);
		Window.init(ConfigManager.getInt("resX"), ConfigManager.getInt("resY"), "Super Blep");
		Window.setVsync(true);
		
		gameMaster = GameMaster.gm;
		
		font = Font.loadFont("data/fonts/ubuntu.fnt");
		
		long currentFrame;
		long lastFrame = System.currentTimeMillis()-8;
		
		int fpsFrames = 0;
		long fpsTime = lastFrame;
		
		while( !Window.requestClose() ) {
			
			currentFrame = System.currentTimeMillis();
			
			//Limit delta to avoid errors
			double delta = Utils.clampD((double)(currentFrame-lastFrame)/1000, .001, .17);
			
			Profiler.beginFrame();
			
			Window.beginFrame();
			
			Profiler.startTimer(Profiler.STEP);
			gameMaster.update(delta);
			Profiler.endTimer(Profiler.STEP);
			
			Profiler.startTimer(Profiler.RENDER);
			gameMaster.draw();
			
			Window.endFrame();
			Profiler.endTimer(Profiler.RENDER);
			
			//FPS counter
			fpsFrames++;
			if(fpsFrames >= 10 && (currentFrame-fpsTime)>=250 ) {
				double time = ((double)(currentFrame-fpsTime))/fpsFrames;
				Window.setTitle( "Super Blep - " + (int)(1/(time/1000)) + "fps" );
				fpsTime = currentFrame;
				fpsFrames = 0;
			}
			
			Profiler.startTimer(Profiler.SLEEP);
			//Limit framerate
			if(currentFrame-lastFrame < minFrametime)
				try {
					Thread.sleep( minFrametime-(currentFrame-lastFrame) );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			lastFrame = currentFrame;
			
			Profiler.endTimer(Profiler.SLEEP);
			//Profiler.printTimes();
		}
		
		Window.destroy();
		Audio.destroy();
	}

}
