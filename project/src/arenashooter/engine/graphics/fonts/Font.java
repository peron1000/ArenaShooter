package arenashooter.engine.graphics.fonts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import arenashooter.engine.graphics.Texture;

/**
 * Represents a distance field font generated using <a href="https://github.com/libgdx/libgdx/wiki/Hiero">Hiero</a><br/>
 * Only one page is supported
 */
public class Font {
	private static HashMap<String, Font> fonts = new HashMap<>();
	
	//Font data
	Texture tex;
	HashMap<Character, FontChar> chars;
	int[] padding = new int[4];

	private Font() {
		// TODO Auto-generated constructor stub
	}
	
	public Font loadFont(String path) {
		//Check if the font has already been loaded
		Font font = fonts.get(path);
		if(font != null) return font;
		
		//Attempt to load fnt file
		File fileFnt = new File(path);

		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(fileFnt));
			
			String line = "";
			while( (line = reader.readLine()) != null ) {
				readLine(line);
			}
			
			reader.close();
		} catch(Exception e) {
			System.err.println("Render - Could not load font : "+path);
			e.printStackTrace();
			return null;
		}
		
		return font;
	}
	
	private void readLine(String line) {
		String[] parts = line.split(" ");
		
		if(parts.length>0) {
			switch(parts[0]) {
			case "info":
				break;
			case "common":
				break;
			case "page":
				break;
			case "chars":
				break;
			case "char":
				break;
			case "kernings":
				break;
			case "kerning":
				break;
			default:
				break;
			}
		}
	}

	private class FontChar {
		final int x, y;
		final int width, height;
		final int xOffset, yOffset;
		final int xAdvance;
		
		FontChar(int x, int y, int width, int height, int xOffset, int yOffset, int xAdvance) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.xAdvance = xAdvance;
		}
	}
}
