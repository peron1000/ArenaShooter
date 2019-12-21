package arenashooter.engine;

public final class OSUtils {
	private OSUtils() {}

	public static final OS os;
	public static final String osString;

	static {
		osString = System.getProperty("os.name");
		String operSys = osString.toLowerCase();
		if (operSys.contains("win")) {
			os = OS.WINDOWS;
		} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
			os = OS.LINUX;
		} else if (operSys.contains("mac")) {
			os = OS.MAC;
		} else {
			os = OS.OTHER;
		}
	}

	public static enum OS {
		LINUX, WINDOWS, MAC, OTHER
	};
}
