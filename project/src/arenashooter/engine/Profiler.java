package arenashooter.engine;

public final class Profiler {
	//This class cannot be instantiated
	private Profiler() {}
	
	private static final double NANOTOMILLI = 0.000001;
	
	private static long currentTimer;
	
	//
	//Timers
	//
	
	//Main
	private static long timeSleep;
	
	//Rendering
	private static long timeRender;
	private static long timeParticles;
	private static long timeSprites;
	private static long timeMeshes;
	
	/**
	 * Reset all the timers
	 */
	public static void beginFrame() {
		timeSleep = 0;
		timeRender = 0;
		timeParticles = 0;
		timeSprites = 0;
		timeMeshes = 0;
	}
	
	public static void printTimes() {
		System.out.println("Frame profiling:");

		System.out.println("-Total: "+(float)((timeRender)*NANOTOMILLI)+"ms");
		
		System.out.println(" |-Render: "+(float)(timeRender*NANOTOMILLI)+"ms");
		System.out.println(" | |-Sprites:   "+(float)(timeSprites*NANOTOMILLI)+"ms");
		System.out.println(" | |-Meshes:    "+(float)(timeMeshes*NANOTOMILLI)+"ms");
		System.out.println(" | |-Particles: "+(float)(timeParticles*NANOTOMILLI)+"ms");
		
		System.out.println(" |-Sleep:  "+(float)(timeSleep*NANOTOMILLI)+"ms");
	}
	
	/**
	 * Start the timer for an element
	 */
	public static void startElem() {
		currentTimer = System.nanoTime();
	}
	
	/**
	 * Stop the timer and add its duration to the sleep counter
	 */
	public static void endSleep() {
		timeSleep = System.nanoTime()-currentTimer;
	}
	
	/**
	 * Stop the timer and add its duration to the sprite counter
	 */
	public static void endSprite() {
		long time = System.nanoTime()-currentTimer;
		timeSprites += time;
		timeRender += time;
	}
	
	/**
	 * Stop the timer and add its duration to the mesh counter
	 */
	public static void endMesh() {
		long time = System.nanoTime()-currentTimer;
		timeMeshes += time;
		timeRender += time;
	}
	
	/**
	 * Stop the timer and add its duration to the particle counter
	 */
	public static void endParticle() {
		long time = System.nanoTime()-currentTimer;
		timeParticles += time;
		timeRender += time;
	}
}
