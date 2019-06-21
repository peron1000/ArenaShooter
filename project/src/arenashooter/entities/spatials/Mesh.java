package arenashooter.entities.spatials;

import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.Profiler;
import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.ModelsData;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Entity;
import arenashooter.game.Main;

public class Mesh extends Spatial3 implements IAnimated {
	private Animation currentAnim = null;

	private Model[] models;
	private Material[] materials;
	
	private String modelPath = null;

	private int timeMs = 0;

	public Vec3f scale;

	public Mesh(Vec3f localPosition, String modelPath) {
		this(localPosition, new Quat(), new Vec3f(1), modelPath);
	}

	public Mesh(Vec3f localPosition, Quat localRotation, String modelPath) {
		this(localPosition, localRotation, new Vec3f(1, 1, 1), modelPath);
	}

	/**
	 * @return the modelPath
	 */
	public String getModelPath() {
		return modelPath;
	}

	public Mesh(Vec3f localPosition, Quat localRotation, Vec3f scale, String modelPath) {
		super(localPosition, localRotation);

		this.scale = scale.clone();
		
		this.modelPath = modelPath;

		ModelsData data = ModelsData.loadModel(modelPath);

		models = data.models;
		materials = data.materials;
	}

	private Mesh(Vec3f position, Quat rotation, Vec3f scale, Model[] models, Material[] materials) {
		super(position);

		this.localRotation.set(rotation);
		this.scale = scale.clone();

		this.models = models;

		this.materials = materials;
	}

	public static Mesh quad(Vec3f position, Quat rotation, Vec3f scale, Material material) {
		return new Mesh(position, rotation, scale, new Model[] { Model.loadQuad() }, new Material[] { material });
	}

	public static Mesh disk(Vec3f position, Quat rotation, Vec3f scale, Material material) {
		return new Mesh(position, rotation, scale, new Model[] { Model.loadDisk(16) }, new Material[] { material });
	}

	public Material getMaterial(int id) {
		if (id < 0 || id >= materials.length)
			return null;
		return materials[id];
	}
	
	@Override
	public void step(double d) {
		timeMs += d * 1000;
		
		updateAnim(d);
		
		super.step(d);
	}
	
	protected void updateAnim(double d) {
		if(currentAnim != null) {
			currentAnim.step(d);
			if(currentAnim.hasTrackVec3f("pos"))
				localPosition.set(currentAnim.getTrackVec3f("pos"));
			
			if(currentAnim.hasTrackVec3f("rot"))
				Quat.fromEuler(currentAnim.getTrackVec3f("rot"), localRotation);

			//TODO: Add Quat rotation
		}
	}

	/**
	 * This will return true is at least one material is transparent
	 * @return should this entity be drawn during transparency pass
	 */
	@Override
	public boolean drawAsTransparent() {
		for(int i=0; i<materials.length; i++) {
			if(materials[i].transparency)
				return true;
		}
		return false;
	}
	
	/**
	 * Draw this entity collection<br/>
	 * This will call this function of every children
	 * 
	 */
	@Override
	public void drawSelfAndChildren() {
		draw();

		List<Entity> toDraw = new ArrayList<>(getChildren().values());
		toDraw.sort(comparatorZindex);

		for (Entity e : toDraw)
			e.drawSelfAndChildren();
	}

	@Override
	public void draw() {
		Profiler.startTimer(Profiler.MESHES);

		for (int i = 0; i < models.length; i++)
			drawModel(i);

		Profiler.endTimer(Profiler.MESHES);

		super.draw();
	}
	
	@Override
	public void editorAddScale(Vec2f scale) {
		this.scale.x += scale.x;
		this.scale.y += scale.y;
	}

	@Override
	public void editorDraw() {
		float editorFilter = 0;
		if(isEditorTarget())
			editorFilter = (float) (Math.sin(System.currentTimeMillis() * 0.006) + 1) / 2f;
		
		for (int i = 0; i < models.length; i++) {
			materials[i].setParamF("editorFilter", editorFilter);
			drawModel(i);
		}
	}
	
	/**
	 * Draw one of the models from this Mesh
	 * @param i
	 */
	private void drawModel(int i) {
		if(materials[i].transparency)  {
			if(Main.skipTransparency) return;
			Window.beginTransparency();
		} else
			Window.endTransparency();
		
		if(getArena() != null) {
			materials[i].setParamVec3f("ambient", getArena().ambientLight);
			materials[i].setParamVec3f("fogColor", getArena().fogColor);
			materials[i].setParamF("fogDistance", getArena().fogDistance);
			materials[i].setLights(getArena().lights);
		}

		materials[i].setParamMat4f("model", Mat4f.transform(getWorldPos(), getWorldRot(), scale));
		materials[i].setParamMat4f("view", Window.getView());
		materials[i].setParamMat4f("projection", Window.proj);

		materials[i].setParamI("time", timeMs);

		materials[i].bind(models[i]);

		models[i].bind();
		models[i].draw();
	}

	/**
	 * Creates a copy of this mesh (same geometry, cloned materials and transform)
	 */
	@Override
	public Mesh clone() {
		Material[] cloneMats = new Material[materials.length];
		for(int i=0; i<materials.length; i++)
			cloneMats[i] = materials[i].clone();
		
		Mesh res = new Mesh(localPosition, localRotation, scale, models.clone(), cloneMats);
		return res;
	}
	
	@Override
	public void setAnim(Animation anim) {
		currentAnim = anim;
	}

	@Override
	public void playAnim() {
		if(currentAnim != null) currentAnim.play();
	}

	@Override
	public void stopAnim() {
		if(currentAnim != null) currentAnim.stopPlaying();
	}

	@Override
	public void animJumpToEnd() {
		if(currentAnim != null) currentAnim.setTime(currentAnim.getLength());
	}

	@Override
	public Animation getAnim() {
		return currentAnim;
	}

	@Override
	public void setAnimSpeed(double speed) {
		if(currentAnim != null) currentAnim.setplaySpeed(speed);
	}

	@Override
	public double getAnimSpeed() {
		if(currentAnim == null) return 0;
		return currentAnim.getPlaySpeed();
	}
}
