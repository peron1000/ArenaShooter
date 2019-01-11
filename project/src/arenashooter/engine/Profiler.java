package arenashooter.engine;

public final class Profiler {
	//This class cannot be instantiated
	private Profiler() {}
	
	private static final double NANOTOMILLI = 0.000001;
	
	//
	//Timers
	//

	//Step
	public static final int STEP=0, PHYSIC=7;
	//Rendering
	public static final int RENDER=1, SPRITES=3, MESHES=4, PARTICLES=5, POSTPROCESS=6;
	
	public static final int SLEEP=2;
	
	private static long[] times = new long[8];
	private static long[] counters = new long[8];
	
	/**
	 * Reset all the timers
	 */
	public static void beginFrame() {
		for(int i=0; i<times.length; i++)
			times[i] = 0;
	}
	
	public static void printTimes() {
		System.out.println("Frame profiling:");

		System.out.println("-Frame: "+(float)((times[RENDER]+times[STEP])*NANOTOMILLI)+"ms");
		
		System.out.println(" |-Step:..."+(float)(times[STEP]*NANOTOMILLI)+"ms");
		System.out.println(" | |-Physic:......."+(float)(times[PHYSIC]*NANOTOMILLI)+"ms");
		System.out.println(" |");
		System.out.println(" |-Render:."+(float)(times[RENDER]*NANOTOMILLI)+"ms");
		System.out.println(" | |-Sprites:......"+(float)(times[SPRITES]*NANOTOMILLI)+"ms");
		System.out.println(" | |-Meshes:......."+(float)(times[MESHES]*NANOTOMILLI)+"ms");
		System.out.println(" | |-Particles:...."+(float)(times[PARTICLES]*NANOTOMILLI)+"ms");
		System.out.println(" | |-PostProcess:.."+(float)(times[POSTPROCESS]*NANOTOMILLI)+"ms");
		System.out.println(" |");
		System.out.println(" |-Sleep:.."+(float)(times[SLEEP]*NANOTOMILLI)+"ms");
	}
	
	/**
	 * Start a timer
	 * @param timer constant from this class
	 */
	public static void startTimer(int timer) {
		counters[timer] = System.nanoTime();
	}
	
	/**
	 * End a timer
	 * @param timer constant from this class
	 */
	public static void endTimer(int timer) {
		times[timer] += System.nanoTime()-counters[timer];
	}
}
