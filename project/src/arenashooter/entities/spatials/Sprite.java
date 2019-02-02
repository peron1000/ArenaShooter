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
import arenashooter.engine.math.Vec4f;

public class Sprite extends Spatial {
	private static final Texture defaultTex = Texture.loadTexture("data/white_pixel.png");
	public Texture tex;
	private static Shader shader;
	private static Model model;
	public Vec4f colorMod = new Vec4f(1,1,1,1);
	public Vec2f size = new Vec2f(100, 100);
	
	/** Does this sprite require transparency */
	public boolean useTransparency = false;
	
	public boolean flipX = false, flipY = false;
	
	public Sprite(Vec2f position, Texture texture) {
		super(position);
		if(shader == null) shader = Shader.loadShader("data/shaders/sprite_simple");
		if(model == null) model = Model.loadQuad();
		this.tex = texture;
	}
	
	public Sprite(Vec2f position, String texture) {
		this(position, Texture.loadTexture(texture));
	}
	
	public Sprite(Vec2f position) {
		this(position, defaultTex);
	}
	
	@Override
	public boolean drawAsTransparent(){ return useTransparency; }
	
	@Override
	public void draw() {
		super.draw();
		
		Profiler.startTimer(Profiler.SPRITES);
		
		shader.bind();
		
		//Create matrices
		Vec2f scale = new Vec2f( flipX ? -size.x : size.x, flipY ? -size.y : size.y );
		Mat4f modelM = Mat4f.transform(position, rotation, scale);
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Window.camera.viewMatrix);
		shader.setUniformM4("projection", Window.proj);
		
		model.bindToShader(shader);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		shader.setUniformI("baseColor", 0);
		
		//Color change
		shader.setUniformV4("baseColorMod", colorMod);
		
		model.bind();
		model.draw();
		
		Profiler.endTimer(Profiler.SPRITES);
	}
}
