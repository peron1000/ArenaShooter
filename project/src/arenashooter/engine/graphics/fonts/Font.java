package arenashooter.engine.graphics.fonts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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
	public Texture tex;
	HashMap<Character, FontChar> chars = new HashMap<>();
	int[] padding = new int[4];

	private Font(String path) {
		this.path = path;
	}
	
	public Model genModel(String text) { //TODO: Support multi-lines
		ArrayList<Float> data = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();
		
		int current=0;
		float currentX = 0;
		
		for( int i=0; i<text.length(); i++ ) {
			char c = text.charAt(i);
			FontChar fontChar = chars.get(c);
			
			if(fontChar != null) {
				if(fontChar.width != 0 && fontChar.height != 0) { //Don't generate geometry for empty glyphs
					float[] charData = genQuad(fontChar, currentX, 0);
					
					int[] charIndices = genIndices(current);

					for(float f : charData)
						data.add(f);
					for(int index : charIndices)
						indices.add(index);

					current++;
				}
				
				//Advance in text
				currentX+=fontChar.xAdvance;
			}
		}
		
		float[] dataArray = new float[data.size()];
		for(int i=0; i<data.size(); i++)
			dataArray[i] = data.get(i);
		
		int[] indicesArray = new int[indices.size()];
		for(int i=0; i<indices.size(); i++)
			indicesArray[i] = indices.get(i);
		
		return new Model(dataArray, indicesArray);
	}
	
	public static Font loadFont(String path) {
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
				readLine(font, line);
			}
			
			reader.close();
		} catch(Exception e) {
			System.err.println("Render - Could not load font : "+path);
			e.printStackTrace();
			return null;
		}
		
		return font;
	}
	
	private static void readLine(Font font, String line) {
		String[] parts = line.split(" ");
		
		if(parts.length>0) {
			switch(parts[0]) {
			case "info":
				readInfo(font, parts);
				break;
			case "common":
				readCommon(font, parts);
				break;
			case "page":
				readPageInfo(font, parts);
				break;
			case "chars": //No need to read this because HashMap doesn't have a fixed size
				break;
			case "char":
				readChar(font, parts);
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
	
	private static void readInfo(Font font, String[] line) {
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "padding":
				String[] sides = parts[1].split(","); //Split int array
				for( int side = 0; side<4; side++ )
					font.padding[side] = Integer.parseInt(sides[side]);
				break;
				
			default:
				break;
			}
		}
	}
	
	private static void readCommon(Font font, String[] line) {
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "scaleW":
				font.scaleW = Integer.parseInt(parts[1]);
				break;
				
			case "scaleH":
				font.scaleH = Integer.parseInt(parts[1]);
				break;
				
			default:
				break;
			}
		}
	}
	
	private static void readPageInfo(Font font, String[] line) {
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "file":
				int lastIndex = font.path.lastIndexOf("/");
				String texturePath = parts[1].substring(1, parts[1].length()-1);
				if(lastIndex >= 0)
					texturePath = font.path.substring(0, lastIndex)+'/'+texturePath;
				
				font.tex = Texture.loadTexture(texturePath);
				break;
				
			default:
				break;
			}
		}
	}
	
	private static void readChar(Font font, String[] line) {
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
		
		FontChar newChar = font.new FontChar((float)x/font.scaleW, (float)y/font.scaleH, 
				(float)width/font.scaleW, (float)height/font.scaleH, 
				(float)xOffset/font.scaleW, (float)yOffset/font.scaleH, 
				(float)xAdvance/font.scaleW);
		font.chars.put((char)id, newChar);
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
	
	/**
	 * 
	 * @param character
	 * @param startX
	 * @param startY
	 * @return
	 */
	private float[] genQuad(FontChar character, float startX, float startY) {
		float x1 = startX+character.xOffset;
		float y1 = startY+character.yOffset;
		float x2 = x1+character.width;
		float y2 = y1+character.height;
		
		float u1 = character.x;
		float v1 = character.y;
		float u2 = character.x+character.width;
		float v2 = character.y+character.height;
		
		//Vertices positions, texture coordinates and normals
		//		x,   y,  z,    u,  v,   nx, ny, nz
		return new float[] {
				x1, y1, 0f,   u1, v1,   0f, 0f, 1f, //0
				x2, y1, 0f,   u2, v1,   0f, 0f, 1f, //1
				x2, y2, 0f,   u2, v2,   0f, 0f, 1f, //2
				x1, y2, 0f,   u1, v2,   0f, 0f, 1f  //3
		};
	}
	
	/**
	 * 
	 * @param character number of characters before this one
	 * @return vertex indices array
	 */
	private int[] genIndices(int character) {
		int offset = 4*character;
		return new int[] {
				offset+0, offset+1, offset+2, //Top triangle
				offset+2, offset+3, offset+0  //Bot triangle
		};
	}
}
