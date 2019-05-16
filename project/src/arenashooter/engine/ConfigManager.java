package arenashooter.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigManager {
	private static File file = new File("config.cfg");
	private static Properties p = new Properties(), defaults = new Properties();
	
	private static Logger log = LogManager.getLogger("Config");
	
	public static void init() {
		log.info("Reading config file");

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
		defaults.setProperty("resScale", "1");
	}
	
	public static void save() {
		log.info("Saving configuration to file");
		try( OutputStream out = new FileOutputStream(file) ) {
			p.store(out, "SuperBlep Config");
		} catch(Exception e) {
			log.error("Error writing configuration file");
		}
	}
	
	public static String getString(String key) {
		return p.getProperty(key, defaults.getProperty(key));
	}
	
	public static int getInt(String key) {
		return Integer.valueOf(getString(key));
	}
	
	public static float getFloat(String key) {
		return Float.valueOf(getString(key));
	}
	
	public static boolean getBool(String key) {
		return Boolean.valueOf(getString(key));
	}
}
