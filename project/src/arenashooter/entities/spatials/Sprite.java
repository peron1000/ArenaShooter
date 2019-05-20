package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;

public class Sprite extends Spatial {
	private static final Texture defaultTex = Texture.loadTexture("data/white_pixel.png");
	public Material material;
	private static Model model;
	public Vec2f size = new Vec2f(1, 1);
	
	/** Does this sprite require transparency */
	public boolean useTransparency = false;
	
	public boolean flipX = false, flipY = false;
	
	public Sprite(Vec2f position, Texture texture) {
		super(position);
		if(model == null) model = Model.loadQuad();
		material = new Material("data/shaders/sprite_simple");
		setTexture(texture);
		material.setParamVec4f("baseColorMod", new Vec4f(1, 1, 1, 1));
		useTransparency = texture.transparency;
	}
	
	public Sprite(Vec2f position, String texture) {
		this(position, Texture.loadTexture(texture));
	}
	
	public Sprite(Vec2f position) {
		this(position, defaultTex);
	}
	
	public Texture getTexture() { return material.getParamTex("baseColor"); }
	
	public void setTexture(Texture newTex) { material.setParamTex("baseColor", newTex); };
	
	@Override
	public boolean drawAsTransparent(){ return useTransparency; }
	
	@Override
	public void draw() {
		super.draw();
		
		Profiler.startTimer(Profiler.SPRITES);
		
		//Create matrices
		Vec2f scale = new Vec2f( flipX ? -size.x : size.x, flipY ? -size.y : size.y );
		
		material.model = Mat4f.transform(getWorldPos(), rotation, scale);
		material.view = Window.getView();
		material.proj = Window.proj;
		material.bind(model);
		
		model.bind();
		model.draw();
		
		Profiler.endTimer(Profiler.SPRITES);
	}
}
