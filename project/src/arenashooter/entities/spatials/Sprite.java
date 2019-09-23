package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec4f;
import arenashooter.game.Main;

public class Sprite extends Spatial {
	private static final Texture defaultTex = Main.getRenderer().loadTexture("data/white_pixel.png");
	public Material material;
	private static Model model;
	public Vec2f size = new Vec2f(1, 1);
	private Vec2f scale = new Vec2f();
	private Mat4f modelM = new Mat4f();
	
	public boolean flipX = false, flipY = false;
	
	public Sprite(Vec2fi localPosition, Texture texture) {
		super(localPosition);
		if(model == null) model = Main.getRenderer().loadQuad();
		material = Main.getRenderer().loadMaterial("data/materials/sprite_simple.material");
		setTexture(texture);
		material.setParamVec4f("baseColorMod", new Vec4f(1));
		if(texture.isTranslucent())
			material.setTransparency(true);
	}
	
	public Sprite(Vec2fi localPosition, Material material) {
		super(localPosition);
		if(model == null) model = Main.getRenderer().loadQuad();
		this.material = material;
	}
	
	public Sprite(Vec2fi localPosition, String texture) {
		this(localPosition, Main.getRenderer().loadTexture(texture));
	}
	
	public Sprite(Vec2fi localPosition) {
		this(localPosition, defaultTex);
	}
	
	public Texture getTexture() { return material.getParamTex("baseColor"); }
	
	public void setTexture(Texture newTex) { material.setParamTex("baseColor", newTex); };
	
	@Override
	public boolean drawAsTransparent(){ return material.getTransparency(); }
	
	@Override
	public void draw(boolean transparency) {
		Profiler.startTimer(Profiler.SPRITES);
		
		//Create matrices
		scale.set( flipX ? -size.x : size.x, flipY ? -size.y : size.y );

		material.setParamMat4f("model", Mat4f.transform(getWorldPos(), getWorldRot(), scale, modelM));
		material.setParamMat4f("view", Main.getRenderer().getView());
		material.setParamMat4f("projection", Main.getRenderer().getProj());
		
		if(material.bind(model)) {
			model.bind();
			model.draw();
		}
		
		Profiler.endTimer(Profiler.SPRITES);
	}
	
	@Override
	public void editorDraw() {
		if (isEditorTarget())
			material.setParamF("editorFilter", (float) (Math.sin(System.currentTimeMillis() * 0.006) + 1) / 2f);
		else
			material.setParamF("editorFilter", 0);
	}
	
	@Override
	public void editorAddScale(Vec2fi scale) {
		this.size.add(scale);
	}
	
	/**
	 * Create a clone of this Sprite (cloned transform, material, flip and transparency usage)
	 */
	@Override
	public Sprite clone() {
		Sprite res = new Sprite(localPosition, material.clone());
		res.size.set(size);
		res.flipX = flipX;
		res.flipY = flipY;
		
		return res;
	}
}
