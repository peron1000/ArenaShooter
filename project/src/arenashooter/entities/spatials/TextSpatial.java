package arenashooter.entities.spatials;

import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.graphics.fonts.Text.TextAlignV;
import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;
import arenashooter.engine.math.Vec4f;
import arenashooter.game.Main;

public class TextSpatial extends Spatial3 {
	private Text text;
	public Material material;

	public Vec3f scale;
	
	private Mat4f modelMatrix = new Mat4f();

	public TextSpatial(Vec3fi localPosition, Vec3fi scale, Text text) {
		super(localPosition);
		this.scale = new Vec3f(scale);
		this.text = text;
		this.material = Main.getRenderer().loadMaterial("data/materials/text_distance_field.material");

		setThickness(.3f);
		setColor(new Vec4f(1, 1, .5, 1));
	}

	private TextSpatial() {
		this(new Vec3f(), new Vec3f(), new Text(Main.font, TextAlignH.CENTER, TextAlignV.CENTER, "default text"));
	}

	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	public void setThickness(float value) {
		material.setParamF("thickness", value);
	}

	public void setColor(Vec4f value) {
		material.setParamVec4f("baseColor", value);
	}

	public void setText(String str) {
		text = new Text(text.getFont(), text.getAlignH(), text.getAlignV(), str);
	}

	public void setAlignH(TextAlignH alignH) {
		text = new Text(text.getFont(), alignH, text.getAlignV(), text.getText());
	}

	public void setAlignV(TextAlignV alignV) {
		text = new Text(text.getFont(), text.getAlignH(), alignV, text.getText());
	}

	public void setFont(Font font) {
		text = new Text(font, text.getAlignH(), text.getAlignV(), text.getText());
	}

	@Override
	public void draw(boolean transparency) {
		Profiler.startTimer(Profiler.MESHES);

		material.setParamTex("distanceField", text.getFont().getTexture());
		material.setParamMat4f("model", Mat4f.transform(getWorldPos(), getWorldRot(), scale, modelMatrix));
		material.setParamMat4f("view", Main.getRenderer().getView());
		material.setParamMat4f("projection", Main.getRenderer().getProj());
		
		if(material.bind(text.getModel())) {
			text.getModel().bind();
			text.getModel().draw();
		}

		Profiler.endTimer(Profiler.MESHES);
	}

	@Override
	public void editorDraw() {
		if (isEditorTarget()) {
			material.setParamF("editorFilter", (float) (Math.sin(System.currentTimeMillis() * 0.006) + 1) / 2f);
		} else {
			material.setParamF("editorFilter", 0);
		}
	}

	@Override
	public void editorAddScale(Vec2fi scale) {
		this.scale.x += scale.x();
		this.scale.y += scale.y();
	}

	/**
	 * Create a copy of this TextSpatial (cloned transform, text and material)
	 */
	@Override
	public TextSpatial clone() {
		TextSpatial res = new TextSpatial(localPosition, scale, text.clone());
		res.material = material.clone();
		return res;
	}

	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return text.getText();
			}

			@Override
			public String getKey() {
				return "text to show";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				String str = json.getString(this);
				text = new Text(text.getFont(), text.getAlignH(), text.getAlignV(), str);
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return text.getAlignH().name();
			}

			@Override
			public String getKey() {
				return "align H";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				TextAlignH align = TextAlignH.valueOf(json.getString(this));
				text = new Text(text.getFont(), align, text.getAlignV(), text.getText());
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return text.getAlignV().name();
			}

			@Override
			public String getKey() {
				return "align V";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				TextAlignV align = TextAlignV.valueOf(json.getString(this));
				text = new Text(text.getFont(), text.getAlignH(), align, text.getText());
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return text.getFont().getPath();
			}

			@Override
			public String getKey() {
				return "font";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				String path = json.getString(this);
				Font font = Font.loadFont(path);
				text = new Text(font , text.getAlignH(), text.getAlignV(), text.getText());
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return scale;
			}

			@Override
			public String getKey() {
				return "scale";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				scale = Vec3f.jsonImport(json.getCollection(this));
			}
		});
		return set;
	}
	
	public static TextSpatial fromJson(JsonObject json) {
		TextSpatial t = new TextSpatial();
		useKeys(t, json);
		return t;
	}
}
