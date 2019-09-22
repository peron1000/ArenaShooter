package arenashooter.game;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import arenashooter.engine.ConfigManager;
import arenashooter.engine.Profiler;
import arenashooter.engine.audio.AudioManager;
import arenashooter.engine.audio.NoSound;
import arenashooter.engine.audio.openAL.ALAudio;
import arenashooter.engine.graphics.Renderer;
import arenashooter.engine.graphics.GLRenderer;
import arenashooter.engine.graphics.NoRender;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.game.gameStates.loading.LoadingConfig;
import arenashooter.game.gameStates.loading.PreLoadMainSound;

public class Main {
	private static final int minFrametimeMilli = 8;

	public static final String version = "0.1.0";

	/**
	 * Maximum duration of a step, longer steps will be broken down into sub-steps
	 */
	private static double tickLength = 1 / 150d;

	public static boolean drawCollisions = false, skipTransparency = false;

	private static AudioManager audio;
	private static Renderer renderer;
	private static GameMaster gameMaster;

	public static Font font = null;

	public static final Logger log = LogManager.getLogger("Main");

	private static boolean requestclose = false;
	
	public static LoadingConfig loadingConfig = new LoadingConfig();
	public static PreLoadMainSound preLoadMainSound = new PreLoadMainSound();

	public static void main(String[] args) {
		List<String> argsL = Arrays.asList(args);
		
		log.info("Starting Super Blep version " + version);

		ConfigManager.init();

		//Audio
		if( argsL.contains("-nosound") )
			audio = new NoSound();
		else
			audio = new ALAudio();
		audio.init();

		//Rendering
		if( argsL.contains("-norender") )
			renderer = new NoRender();
		else
			renderer = new GLRenderer();
		
		renderer.init(ConfigManager.getInt("resX"), ConfigManager.getInt("resY"), ConfigManager.getBool("fullscreen"), ConfigManager.getFloat("resScale"), "Super Blep");

		gameMaster = new GameMaster();

		font = Font.loadFont("data/fonts/liberation_sans.fnt");

		ConfigManager.applyAllSettings();

		long currentFrame;
		long lastFrame = System.currentTimeMillis() - 8;

		int fpsFrames = 0;
		long fpsTime = lastFrame;

		double remaining = 0;
		
		loadingConfig.start();
		preLoadMainSound.start();

		while (!renderer.requestedClose() && !requestclose) {

			currentFrame = System.currentTimeMillis();

			// Limit delta to avoid errors
//			double delta = Math.max(tickLength, (double)(currentFrame-lastFrame)/1000d);
//			double delta = Utils.clampD((double)(currentFrame-lastFrame)/1000, tickLength, .17);
			remaining += Math.min((double) (currentFrame - lastFrame) / 1000d, .17);

			Profiler.beginFrame();

			// If delta is too high, break down step into sub-steps
			Profiler.startTimer(Profiler.STEP);

			while (remaining > tickLength) {
				renderer.beginFrame();
				gameMaster.update(tickLength);
				Profiler.subSteps++;
				remaining -= tickLength;
			}

			Profiler.endTimer(Profiler.STEP);

			Profiler.startTimer(Profiler.RENDER);
			gameMaster.draw();
			Profiler.endTimer(Profiler.RENDER);

			audio.update();

			renderer.endFrame();
			
			// FPS counter
			fpsFrames++;
			if (fpsFrames >= 10 && (currentFrame - fpsTime) >= 250) {
				double time = ((double) (currentFrame - fpsTime)) / fpsFrames;
				Main.getRenderer().setTitle("Super Blep - " + (int) (1 / (time / 1000d)) + "fps");
				fpsTime = currentFrame;
				fpsFrames = 0;
			}

			Profiler.startTimer(Profiler.SLEEP);
			// Limit framerate
			if (currentFrame - lastFrame < minFrametimeMilli)
				try {
					Thread.sleep(minFrametimeMilli - (currentFrame - lastFrame));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			lastFrame = currentFrame;

			Profiler.endTimer(Profiler.SLEEP);
			Profiler.printData();
		}

		renderer.destroy();
		audio.destroy();

		log.info("Closing Super Blep...");
	}
	
	/**
	 * @return active GameMaster
	 */
	public static GameMaster getGameMaster() { return gameMaster; }
	
	public static AudioManager getAudioManager() { return audio; }
	
	public static Renderer getRenderer() { return renderer; }

	public static void reqestClose() {
		requestclose = true;
	}

}
