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

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Window;

public class ConfigManager {
	private static File file = new File("config.cfg");
	private static Properties p = new Properties(), defaults = new Properties();
	
	private static Logger log = LogManager.getLogger("Config");
	
	/**
	 * Load default configuration and configuration file (creating a new file if needed)
	 */
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
		defaults.setProperty("vsync", "true");
		defaults.setProperty("fullscreen", "false");
		
		defaults.setProperty("volumeMain", "1");
		defaults.setProperty("volumeSFX", "1");
		defaults.setProperty("volumeUI", "1");
		defaults.setProperty("volumeMusic", "1");
	}
	
	/**
	 * Save current configuration to configuration file
	 */
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
	
	public static void setString(String key, String value) {
		if(!defaults.contains(key))
			log.warn("Trying to save a setting with the name \""+key+"\", which is not present in default configuration");
		p.setProperty(key, value);
	}
	
	public static int getInt(String key) {
		return Integer.valueOf(getString(key));
	}
	
	public static void setInt(String key, int value) {
		if(!defaults.contains(key))
			log.warn("Trying to save a setting with the name \""+key+"\", which is not present in default configuration");
		p.setProperty(key, String.valueOf(value));
	}
	
	public static float getFloat(String key) {
		return Float.valueOf(getString(key));
	}
	
	public static void setFloat(String key, float value) {
		if(!defaults.contains(key))
			log.warn("Trying to save a setting with the name \""+key+"\", which is not present in default configuration");
		p.setProperty(key, String.valueOf(value));
	}
	
	public static boolean getBool(String key) {
		return Boolean.valueOf(getString(key));
	}
	
	public static void setInt(String key, boolean value) {
		if(!defaults.contains(key))
			log.warn("Trying to save a setting with the name \""+key+"\", which is not present in default configuration");
		p.setProperty(key, String.valueOf(value));
	}
	
	/**
	 * Apply all settings from current configuration
	 */
	public static void applyAllSettings() {
		//Video
		Window.resize(getInt("resX"), getInt("resY"));
		Window.setResScale(getFloat("resScale"));
		Window.setVsync(getBool("vsync"));
		Window.setVsync(getBool("fullscreen"));
		
		//Audio
		Audio.setMainVolume(getFloat("volumeMain"));
		Audio.setChannelVolume(AudioChannel.SFX, getFloat("volumeSFX"));
		Audio.setChannelVolume(AudioChannel.UI, getFloat("volumeUI"));
		Audio.setChannelVolume(AudioChannel.MUSIC, getFloat("volumeMusic"));
	}
}
