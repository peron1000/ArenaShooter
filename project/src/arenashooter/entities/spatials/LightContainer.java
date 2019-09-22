package arenashooter.entities.spatials;

import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.graphics.Light;
import arenashooter.engine.graphics.MaterialI;
import arenashooter.engine.graphics.Light.LightType;
import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;
import arenashooter.engine.math.Vec4f;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.game.Main;

public class LightContainer extends Spatial3 implements IAnimated {
	private final Light light;
	
	private Mesh editorSprite;
	private MaterialI editorSpriteMat;
	private Mesh editorMesh;
	
	private Animation anim;

	public LightContainer(Vec3fi localPosition, Light light) {
		super(localPosition);
		this.light = light;
	}
	
	public Light getLight() { return light; }
	
	@Override
	protected void recursiveAttach(Entity newParent) {
		super.recursiveAttach(newParent);
		
		if(getArena() != null) getArena().lights.add(light);
	}

	@Override
	protected void recursiveDetach(Arena oldArena) {
		super.recursiveDetach(oldArena);
		if(oldArena != null) oldArena.lights.remove(light);
	}
	
	@Override
	public void updateAttachment() {
		super.updateAttachment();
		updateLight();
	}

	private void updateLight() {
		switch(light.getType()) {
		case POINT:
			light.position.set(getWorldPos());
			break;
		case DIRECTIONAL:
			light.direction.set(getWorldRot().forward());
			break;
		case SPOT:
			light.position.set(getWorldPos());
			light.direction.set(getWorldRot().forward());
			break;
		}
	}
	
	@Override
	public void step(double d) {
		//Animation
		if(anim != null) {
			anim.step(d);
			
			if(anim.hasTrackVec3f("pos"))
				localPosition.set( anim.getTrackVec3f("pos") );
			
			if(anim.hasTrackVec3f("rot"))
				Quat.fromEuler( anim.getTrackVec3f("rot"), localRotation );
			
			if(anim.hasTrackD("radius"))
				light.radius = (float) anim.getTrackD("radius");

			if(anim.hasTrackVec3f("color"))
				light.color.set(anim.getTrackVec3f("color"));
		}
		
		super.step(d);
	}
	
	@Override
	public void draw(boolean transparency) {
		updateLight();
	}
	
	@Override
	public void editorDraw() {
		if(editorSprite == null) {
			editorSpriteMat = Main.getRenderer().loadMaterial("data/materials/sprite_simple.material");
			editorSpriteMat.setParamTex("baseColor", Main.getRenderer().loadTexture("data/sprites/icon_light.png").setFilter(false));
			editorSprite = Mesh.quad(new Vec3f(), new Quat(), new Vec3f(1), editorSpriteMat);
		}
		if(editorMesh == null) {
			editorMesh = new Mesh(new Vec3f(), "data/meshes/arrow.obj");
		}
		
		Vec4f lightColor = new Vec4f(light.color, 1);
		
		editorSpriteMat.setParamVec4f("baseColorMod", lightColor);
		editorSprite.localPosition.set(getWorldPos());
		editorSprite.draw(false);
		
		if(light.getType() != LightType.POINT) {
			editorMesh.localPosition.set(getWorldPos());
			editorMesh.localRotation.set(getWorldRot());
			editorMesh.getMaterial(0).setParamVec4f("baseColor", lightColor);
			editorMesh.draw(false);
		}
//		System.out.println(light.direction);
//		System.out.println(getWorldRot());
	}

	@Override
	public void setAnim(Animation anim) {
		this.anim = anim;
	}

	@Override
	public void playAnim() {
		if(anim != null)
			anim.play();
	}

	@Override
	public void stopAnim() {
		if(anim != null)
			anim.stopPlaying();
	}

	@Override
	public void animJumpToEnd() {
		if(anim != null)
			anim.setTime(anim.getLength());
	}

	@Override
	public Animation getAnim() {
		return anim;
	}

	@Override
	public void setAnimSpeed(double speed) {
		// TODO Auto-generated method stub
	}

	@Override
	public double getAnimSpeed() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	
	/*
	 * JSON
	 */

	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return light.angle;
			}
			@Override
			public String getKey() {
				return "angle";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				light.angle = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return light.radius;
			}
			@Override
			public String getKey() {
				return "radius";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				light.radius = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return light.color;
			}
			@Override
			public String getKey() {
				return "color";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				JsonArray a = json.getCollection(this);
				if (a != null)
					light.color.set(Vec3f.jsonImport(a));
			}
		});
		
		return set;
	}
	
	public static LightContainer fromJson(JsonObject json) throws Exception {
		LightContainer lc = new LightContainer(new Vec3f(), new Light());
		useKeys(lc, json);
		return lc;
	}
	
	
}
