package arenashooter.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigManager {
	private static File file = new File("config.cfg");
	private static Properties p = new Properties(), defaults = new Properties();
	
	public static void init() {
		System.out.println("Reading config file");

		try {
			if(!file.exists()) {
				file.createNewFile();
				save();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try( InputStream in = new FileInputStream(file) ) {
			p.load(in);
		} catch(Exception e) {
			
		}
		
		//Fill default values
		defaults.setProperty("resX", "1280");
		defaults.setProperty("resY", "720");
		defaults.setProperty("fullscreen", "false");
	}
	
	public static void save() {
		System.out.println("Saving configuration to file");
		try( OutputStream out = new FileOutputStream(file) ) {
			p.store(out, "SuperBlep Config");
		} catch(Exception e) {
			System.err.println("Error writing configuration file");
		}
	}
	
	public static String getString(String key) {
		return p.getProperty(key, defaults.getProperty(key));
	}
	
	public static int getInt(String key) {
		return Integer.valueOf(getString(key));
	}
	
	public static boolean getBool(String key) {
		return Boolean.valueOf(getString(key));
	}
}
