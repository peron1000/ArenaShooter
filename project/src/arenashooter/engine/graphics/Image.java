package arenashooter.engine.graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import arenashooter.engine.FileUtils;

public class Image {

	public final String file;
	public final int width, height, channels;
	public final ByteBuffer buffer;
	public final boolean transparency;
	
	private Image(String path, int width, int height, int channels, ByteBuffer buffer) {
		this.file = path;
		this.width = width;
		this.height = height;
		this.channels = channels;
		this.buffer = buffer;
		
		String pathWithoutExt = path.substring(0, path.lastIndexOf('.'));
		transparency = pathWithoutExt.endsWith("_tr");
	}
	
	/**
	 * Load an image from a file
	 * @param path file
	 * @return Image object containing the loaded image or null if an error occurred
	 */
	public static Image loadImage(String path) {
		Image res = null;
		
		ByteBuffer file = FileUtils.resToByteBuffer(path);
		
		if( file == null )
			return null;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1), height = stack.mallocInt(1), channels = stack.mallocInt(1);
			
			ByteBuffer data = STBImage.stbi_load_from_memory(file, width, height, channels, 0);
			
			res = new Image(path, width.get(0), height.get(0), channels.get(0), data);
		}
		
		return res;
	}
	
}
