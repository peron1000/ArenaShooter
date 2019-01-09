package arenashooter.engine;

public final class Profiler {
	//This class cannot be instantiated
	private Profiler() {}
	
	private static final double NANOTOMILLI = 0.000001;
	
	private static long currentTimer;
	
	static long timeSleep;
	
	//Rendering
	static long timeRender;
	static long timeParticles;
	static long timeSprites;
	static long timeMeshes;
	
	public static void beginFrame() {
		timeSleep = 0;
		timeRender = 0;
		timeParticles = 0;
		timeSprites = 0;
		timeMeshes = 0;
	}
	
	public static void printTimes() {
		System.out.println("Frame profiling:");
		
		System.out.println("  Render: "+(float)(timeRender*NANOTOMILLI)+"ms");
		System.out.println("     Sprites:   "+(float)(timeSprites*NANOTOMILLI)+"ms");
		System.out.println("     Meshes:    "+(float)(timeMeshes*NANOTOMILLI)+"ms");
		System.out.println("     Particles: "+(float)(timeParticles*NANOTOMILLI)+"ms");
		
		System.out.println("  Total:  "+(float)((timeRender+timeSprites+timeParticles)*NANOTOMILLI)+"ms");
		System.out.println("  Sleep:  "+(float)(timeSleep*NANOTOMILLI)+"ms");
	}
	
	public static void startElem() {
		currentTimer = System.nanoTime();
	}
	
	public static void endSleep() {
		timeSleep = System.nanoTime()-currentTimer;
	}
	
	public static void endSprite() {
		long time = System.nanoTime()-currentTimer;
		timeSprites += time;
		timeRender += time;
	}
	
	public static void endMesh() {
		long time = System.nanoTime()-currentTimer;
		timeMeshes += time;
		timeRender += time;
	}
	
	public static void endParticle() {
		long time = System.nanoTime()-currentTimer;
		timeParticles += time;
		timeRender += time;
	}
}
