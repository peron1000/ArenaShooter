package arenashooter.engine.graphics;

import java.nio.ByteBuffer;

public class Image {

	public final String file;
	public final int width, height;
	public final ByteBuffer buffer;
	
	public Image(String path, int width, int height, ByteBuffer buffer) {
		this.file = path;
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}

}
