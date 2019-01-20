package arenashooter.entities.spatials;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;

public class LoadingFloor extends Spatial {
	private static final Texture[] tex;
	private static final Shader shader;
	private static final Model model;
	static private final Vec2f size = new Vec2f(64, 128);
	
	private int currentTex;
	private double timer = 0;
	
	static {
		shader = new Shader("data/shaders/sprite_simple");
		model = Model.loadQuad();
		tex = new Texture[] {
				Texture.loadTexture("data/sprites/loading_floor/floor_01.png"),
				Texture.loadTexture("data/sprites/loading_floor/floor_02.png"),
				Texture.loadTexture("data/sprites/loading_floor/floor_03.png"),
				Texture.loadTexture("data/sprites/loading_floor/floor_04.png"),
				Texture.loadTexture("data/sprites/loading_floor/floor_05.png")
		};
	}
	
	public LoadingFloor(Vec2f position) {
		super(position);
		currentTex = (int)Math.floor( Math.random()*(tex.length-1) );
	}
	
	@Override
	public void step(double d) {
		timer+=d;

		if(timer >= .1) {
			timer = 0;
			
			currentTex++;
			if(currentTex >= tex.length) {
				currentTex = 0;
				size.x = -size.x;
			}
		}
	}
	
	@Override
	public void draw() {
		super.draw();
		
		Profiler.startTimer(Profiler.SPRITES);
		
		shader.bind();
		
		//Create matrices
		Mat4f modelM = Mat4f.transform(position, rotation, size);
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Window.camera.viewMatrix);
		shader.setUniformM4("projection", Window.proj);
		
		model.bindToShader(shader);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		tex[currentTex].bind();
		shader.setUniformI("baseColor", 0);
		
		model.bind();
		model.draw();
		
		Profiler.endTimer(Profiler.SPRITES);
	}
}
