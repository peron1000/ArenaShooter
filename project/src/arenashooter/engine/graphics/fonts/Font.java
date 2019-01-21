package arenashooter.engine.graphics.fonts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Texture;

/**
 * Represents a distance field font generated using <a href="https://github.com/libgdx/libgdx/wiki/Hiero">Hiero</a><br/>
 * Only one page is supported
 */
public class Font {
	private static HashMap<String, Font> fonts = new HashMap<>();
	
	//Font data
	/** Path to the fnt file */
	final String path;
	int scaleW, scaleH;
	Texture tex;
	HashMap<Character, FontChar> chars = new HashMap<>();
	int[] padding = new int[4];

	private Font(String path) {
		this.path = path;
	}
	
	public Model genModel(String text) { //TODO
		return new Model(null, null);
	}
	
	public Font loadFont(String path) {
		//Check if the font has already been loaded
		Font font = fonts.get(path);
		if(font != null) return font;
		else font = new Font(path);
		
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
				readInfo(parts);
				break;
			case "common":
				readCommon(parts);
				break;
			case "page":
				readPageInfo(parts);
				break;
			case "chars": //No need to read this because HashMap doesn't have a fixed size
				break;
			case "char":
				readChar(parts);
				break;
			case "kernings": //TODO: Implement kernings
				break;
			case "kerning":
				break;
			default:
				break;
			}
		}
	}
	
	private void readInfo(String[] line) {
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "padding":
				String[] sides = parts[1].split(","); //Split int array
				for( int side = 0; side<4; side++ )
					padding[side] = Integer.parseInt(sides[side]);
				break;
				
			default:
				break;
			}
		}
	}
	
	private void readCommon(String[] line) {
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "scaleW":
				scaleW = Integer.parseInt(parts[1]);
				break;
				
			case "scaleH":
				scaleH = Integer.parseInt(parts[1]);
				break;
				
			default:
				break;
			}
		}
	}
	
	private void readPageInfo(String[] line) {
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "file":
				int lastIndex = path.lastIndexOf("/");
				String texturePath = parts[1];
				if(lastIndex >= 0)
					texturePath = path.substring(0, lastIndex)+parts[1];
				tex = Texture.loadTexture(texturePath);
				break;
				
			default:
				break;
			}
		}
	}
	
	private void readChar(String[] line) {
		int id = 0, x = 0, y = 0, width = 0, height = 0, xOffset = 0, yOffset = 0, xAdvance = 0;
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "id":
				id = Integer.parseInt(parts[1]);
				break;
				
			case "x":
				x = Integer.parseInt(parts[1]);
				break;
				
			case"y":
				y = Integer.parseInt(parts[1]);
				break;
				
			case "width":
				width = Integer.parseInt(parts[1]);
				break;
				
			case "height":
				height = Integer.parseInt(parts[1]);
				break;
				
			case "xoffset":
				xOffset = Integer.parseInt(parts[1]);
				break;
				
			case "yoffset":
				yOffset = Integer.parseInt(parts[1]);
				break;
				
			case "xadvance":
				xAdvance = Integer.parseInt(parts[1]);
				break;
				
			default:
				break;
			}
		}
		
		FontChar newChar = new FontChar((float)x/scaleW, (float)y/scaleH, 
				(float)width/scaleW, (float)height/scaleH, 
				(float)xOffset/scaleW, (float)yOffset/scaleH, 
				(float)xAdvance/scaleW);
		chars.put((char)id, newChar);
	}

	private class FontChar {
		final float x, y;
		final float width, height;
		final float xOffset, yOffset;
		final float xAdvance;
		
		FontChar(float x, float y, float width, float height, float xOffset, float yOffset, float xAdvance) {
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
