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
	private Mat4f modelM = new Mat4f();
	
	/** Does this sprite require transparency */
	public boolean useTransparency = false;
	
	public boolean flipX = false, flipY = false;
	
	public Sprite(Vec2f localPosition, Texture texture) {
		super(localPosition);
		if(model == null) model = Model.loadQuad();
		material = new Material("data/shaders/sprite_simple");
		setTexture(texture);
		material.setParamVec4f("baseColorMod", new Vec4f(1));
		useTransparency = texture.transparency;
	}
	
	public Sprite(Vec2f localPosition, Material material) {
		super(localPosition);
		if(model == null) model = Model.loadQuad();
		this.material = material;
	}
	
	public Sprite(Vec2f localPosition, String texture) {
		this(localPosition, Texture.loadTexture(texture));
	}
	
	public Sprite(Vec2f localPosition) {
		this(localPosition, defaultTex);
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
		
		material.model = Mat4f.transform(getWorldPos(), getWorldRot(), scale, modelM);
		material.view = Window.getView();
		material.proj = Window.proj;
		material.bind(model);
		
		model.bind();
		model.draw();
		
		Profiler.endTimer(Profiler.SPRITES);
	}
	
	@Override
	public void editorDraw() {
		if (isEditorTarget()) {
			material.setParamF("editorFilter", (float) (Math.sin(System.currentTimeMillis() * 0.006) + 1) / 2f);
		} else {
			material.setParamF("editorFilter", 0);
		}
		draw();
	}
	
	@Override
	public void editorAddScale(Vec2f scale) {
		this.size.add(scale);
	}
}
