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
	public static final int RENDER=1, SPRITES=3, MESHES=4, PARTICLES=5, POSTPROCESS=6, TRANSPARENCY=8;
	
	public static final int SLEEP=2;
	
	private static long[] times = new long[9];
	private static long[] counters = new long[9];
	private static boolean[] running = new boolean[9];
	
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
		System.out.println(" | |-Transparency:."+(float)(times[TRANSPARENCY]*NANOTOMILLI)+"ms");
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
		if(!running[timer])
			counters[timer] = System.nanoTime();
		running[timer] = true;
	}
	
	/**
	 * End a timer
	 * @param timer constant from this class
	 */
	public static void endTimer(int timer) {
		if(running[timer])
			times[timer] += System.nanoTime()-counters[timer];
	}
}
