package arenashooter.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import arenashooter.engine.ConfigManager;
import arenashooter.engine.Profiler;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.math.Utils;

public class Main {
	private static final int minFrametimeMilli = 8;
	
	public static final String version = "0.0.1";
	
	/** Maximum duration of a step, longer steps will be broken down into sub-steps */
	private static double tickLength = 1/150d;
	
	public static boolean drawCollisions = false;
	
	private static GameMaster gameMaster;
	
	public static Font font = null;
	
	public static final Logger log = LogManager.getLogger("Main");
	
	private static boolean requestclose = false;
	public static void Reqclose(){
		requestclose = true;
	}
	
	public static void main(String[] args) {
		log.info("Starting Super Blep version "+version);
		
		ConfigManager.init();
		Audio.init();
		Window.resolutionScale = ConfigManager.getFloat("resScale");
		Window.init(ConfigManager.getInt("resX"), ConfigManager.getInt("resY"), "Super Blep");
		Window.setVsync(true);
		
		gameMaster = GameMaster.gm;
		
		font = Font.loadFont("data/fonts/liberation_sans.fnt");
		
		long currentFrame;
		long lastFrame = System.currentTimeMillis()-8;
		
		int fpsFrames = 0;
		long fpsTime = lastFrame;
		
		while( !Window.requestClose() && !requestclose) {
			
			currentFrame = System.currentTimeMillis();
			
			//Limit delta to avoid errors
//			double delta = Math.max(tickLength, (double)(currentFrame-lastFrame)/1000d);
			double delta = Utils.clampD((double)(currentFrame-lastFrame)/1000, tickLength, .17);
			
			Profiler.beginFrame();
			
			//If delta is too high, break down step into sub-steps
			Profiler.startTimer(Profiler.STEP);
			double remaining = delta;
			while(remaining > tickLength) {
				Window.beginFrame();
				gameMaster.update(tickLength);
				Profiler.subSteps++;
				remaining -= tickLength;
			}
			if(remaining > 0) {
				Window.beginFrame();
				gameMaster.update(remaining);
				Profiler.subSteps++;
			}
			Profiler.endTimer(Profiler.STEP);
			
			Profiler.startTimer(Profiler.RENDER);
			gameMaster.draw();
			
			Window.endFrame();
			Profiler.endTimer(Profiler.RENDER);
			
			Audio.update();
			
			//FPS counter
			fpsFrames++;
			if(fpsFrames >= 10 && (currentFrame-fpsTime)>=250 ) {
				double time = ((double)(currentFrame-fpsTime))/fpsFrames;
				Window.setTitle( "Super Blep - " + (int)(1/(time/1000d)) + "fps" );
				fpsTime = currentFrame;
				fpsFrames = 0;
			}
			
			Profiler.startTimer(Profiler.SLEEP);
			//Limit framerate
			if(currentFrame-lastFrame < minFrametimeMilli)
				try {
					Thread.sleep( minFrametimeMilli-(currentFrame-lastFrame) );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			lastFrame = currentFrame;
			
			Profiler.endTimer(Profiler.SLEEP);
			Profiler.printData();
		}
		
		Window.destroy();
		Audio.destroy();
		
		log.info("Closing Super Blep...");
	}

}
