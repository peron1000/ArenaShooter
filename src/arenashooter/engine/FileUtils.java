package arenashooter.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
	
	/**
	 * Read a resource file as a string
	 * @param path resource path
	 * @return content of the resource or "" if an error occured
	 */
	public static String resToString(String path) {
		String res = "";
		
		InputStream in = ClassLoader.getSystemResourceAsStream(path);
		
		if( in != null) {
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inReader);
			
			String line = "";
			try {
				while( (line = reader.readLine()) != null ) {
					res += line+"\n"; //Include line breaks
//					res += line; //Ignore line breaks
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				reader.close();
				inReader.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Can't find resource: "+path);
		}
		
		return res;
	}
	
}
