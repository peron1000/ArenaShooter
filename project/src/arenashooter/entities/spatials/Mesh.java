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
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;
import arenashooter.entities.Entity;

public class Mesh extends Spatial3 implements IAnimated {
	private Animation currentAnim = null;

	private Model[] models;
	private Material[] materials;
	
	private String modelPath = null;

	private int timeMs = 0;

	public Vec3f scale;
	
	private Mat4f modelMatrix = new Mat4f();

	public Mesh(Vec3fi localPosition, String modelPath) {
		this(localPosition, new Quat(), new Vec3f(1), modelPath);
	}

	public Mesh(Vec3fi localPosition, Quat localRotation, String modelPath) {
		this(localPosition, localRotation, new Vec3f(1, 1, 1), modelPath);
	}

	public Mesh(Vec3fi localPosition, Quat localRotation, Vec3f scale, String modelPath) {
		super(localPosition, localRotation);

		this.scale = scale.clone();
		
		this.modelPath = modelPath;

		ModelsData data = ModelsData.loadModel(modelPath);

		models = data.models;
		materials = data.materials;
	}

	private Mesh(Vec3fi position, Quat rotation, Vec3fi scale, Model[] models, Material[] materials) {
		super(position);

		this.localRotation.set(rotation);
		this.scale = new Vec3f(scale);

		this.models = models;

		this.materials = materials;
	}

	/**
	 * @return the modelPath
	 */
	public String getModelPath() {
		return modelPath;
	}

	public static Mesh quad(Vec3fi position, Quat rotation, Vec3fi scale, Material material) {
		return new Mesh(position, rotation, scale, new Model[] { Model.loadQuad() }, new Material[] { material });
	}

	public static Mesh disk(Vec3fi position, Quat rotation, Vec3fi scale, Material material) {
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
			if(materials[i].getTransparency())
				return true;
		}
		return false;
	}
	
	/**
	 * Render opaque/masked entities and add transparent ones to Arena's list
	 */
	public void renderFirstPass() {
		if(drawAsTransparent())
			getArena().transparent.add(this);
		draw(false);

		List<Entity> toDraw = new ArrayList<>(getChildren().values());
		toDraw.sort(comparatorZindex);

		for (Entity e : toDraw)
			e.renderFirstPass();
	}
	
	@Override
	public void draw(boolean transparency) {
		Profiler.startTimer(Profiler.MESHES);

		for (int i = 0; i < models.length; i++)
			if(materials[i].getTransparency() == transparency)
				drawModel(i);

		Profiler.endTimer(Profiler.MESHES);
	}
	
	@Override
	public void editorAddScale(Vec2fi scale) {
		this.scale.x += scale.x();
		this.scale.y += scale.y();
	}
	
	/**
	 * Draw one of the models from this Mesh
	 * @param i
	 */
	private void drawModel(int i) {
		if(getArena() != null) {
			materials[i].setParamVec3f("ambient", getArena().ambientLight);
			materials[i].setParamVec3f("fogColor", getArena().fogColor);
			materials[i].setParamF("fogDistance", getArena().fogDistance);
			materials[i].setLights(getArena().lights);
		}

		materials[i].setParamMat4f("model", Mat4f.transform(getWorldPos(), getWorldRot(), scale, modelMatrix));
		materials[i].setParamMat4f("view", Window.getView());
		materials[i].setParamMat4f("projection", Window.getProj());

		materials[i].setParamI("time", timeMs);

		if(materials[i].bind(models[i])) {
			models[i].bind();
			models[i].draw();
		}
	}
	
	@Override
	public void editorDraw() {
		float editorFilter = 0;
		if (isEditorTarget())
			editorFilter = (float) (Math.sin(System.currentTimeMillis() * 0.006) + 1) / 2f;
		for(int i=0; i<materials.length; i++)
			materials[i].setParamF("editorFilter", editorFilter);
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
