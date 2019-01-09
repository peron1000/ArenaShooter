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

	private static long timeStep;
	
	//Rendering
	private static long timeRender;
	private static long timeParticles;
	private static long timeSprites;
	private static long timeMeshes;
	private static long timePostProcess;
	
	//Physics
	private static long timePhysics;
	
	/**
	 * Reset all the timers
	 */
	public static void beginFrame() {
		timeSleep = 0;
		
		timeStep = 0;
		
		timeRender = 0;
		timeParticles = 0;
		timeSprites = 0;
		timeMeshes = 0;
		timePostProcess = 0;
		
		timePhysics = 0;
	}
	
	public static void printTimes() {
		System.out.println("Frame profiling:");

		System.out.println("-Frame: "+(float)((timeRender+timeStep)*NANOTOMILLI)+"ms");
		
		System.out.println(" |-Step:..."+(float)(timeStep*NANOTOMILLI)+"ms");
		System.out.println(" | |-Physic:......."+(float)(timePhysics*NANOTOMILLI)+"ms");
		System.out.println(" |");
		System.out.println(" |-Render:."+(float)(timeRender*NANOTOMILLI)+"ms");
		System.out.println(" | |-Sprites:......"+(float)(timeSprites*NANOTOMILLI)+"ms");
		System.out.println(" | |-Meshes:......."+(float)(timeMeshes*NANOTOMILLI)+"ms");
		System.out.println(" | |-Particles:...."+(float)(timeParticles*NANOTOMILLI)+"ms");
		System.out.println(" | |-PostProcess:.."+(float)(timePostProcess*NANOTOMILLI)+"ms");
		System.out.println(" |");
		System.out.println(" |-Sleep:.."+(float)(timeSleep*NANOTOMILLI)+"ms");
	}
	
	/**
	 * Start the timer for step
	 */
	public static void startStep() {
		timeStep = System.nanoTime();
	}
	
	/**
	 * End the timer for step
	 */
	public static void endStep() {
		timeStep = System.nanoTime()-timeStep;
	}
	
	/**
	 * Start the timer for rendering
	 */
	public static void startRender() {
		timeRender = System.nanoTime();
	}
	
	/**
	 * End the timer for rendering
	 */
	public static void endRender() {
		timeRender = System.nanoTime()-timeRender;
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
		timeSprites += System.nanoTime()-currentTimer;
	}
	
	/**
	 * Stop the timer and add its duration to the mesh counter
	 */
	public static void endMesh() {
		timeMeshes += System.nanoTime()-currentTimer;
	}
	
	/**
	 * Stop the timer and add its duration to the particle counter
	 */
	public static void endParticle() {
		timeParticles += System.nanoTime()-currentTimer;
	}
	
	/**
	 * Stop the timer and add its duration to the particle counter
	 */
	public static void endPostProcess() {
		timePostProcess += System.nanoTime()-currentTimer;
	}
	
	/**
	 * Stop the timer and add its duration to the physics counter
	 */
	public static void endPhysics() {
		timePhysics += System.nanoTime()-currentTimer;
	}
}
