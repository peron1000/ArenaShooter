package arenashooter.engine.xmlReaders.reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import arenashooter.engine.graphics.TextureI;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.xmlReaders.XmlReader;
import arenashooter.game.Main;

public class MaterialXmlReader extends XmlReader {	
	public String vertexShader;
	public String fragmentShader;

	public boolean transparency;

	public Map<String, Integer> paramsI = new HashMap<>();
	public Map<String, Float> paramsF = new HashMap<>();
	public Map<String, Vec2f> paramsVec2f = new HashMap<>();
	public Map<String, Vec3f> paramsVec3f = new HashMap<>();
	public Map<String, Vec4f> paramsVec4f = new HashMap<>();
	public Map<String, TextureI> paramsTex = new HashMap<>();

	public MaterialXmlReader(String path) {
		parse(path);

		vertexShader = root.getAttribute("shaderVertex");
		fragmentShader = root.getAttribute("shaderFragment");

		transparency = Boolean.parseBoolean(root.getAttribute("transparency"));

		loadParamsI(getListElementByName("paramI", root));
		loadParamsF(getListElementByName("paramF", root));
		loadParamsVec2f(getListElementByName("paramV2", root));
		loadParamsVec3f(getListElementByName("paramV3", root));
		loadParamsVec4f(getListElementByName("paramV4", root));
		loadParamsTex(getListElementByName("paramTex", root));
	}

	private void loadParamsI(List<Element> elems) {
		for(Element elem : elems) {
			paramsI.put(elem.getAttribute("name"), Integer.parseInt(elem.getAttribute("value")));
		}
	}

	private void loadParamsF(List<Element> elems) {
		for(Element elem : elems) {
			paramsF.put(elem.getAttribute("name"), Float.parseFloat(elem.getAttribute("value")));
		}
	}

	private void loadParamsVec2f(List<Element> elems) {
		for(Element elem : elems) {
			float x = Float.parseFloat(elem.getAttribute("x"));
			float y = Float.parseFloat(elem.getAttribute("y"));
			paramsVec2f.put(elem.getAttribute("name"), new Vec2f(x, y));
		}
	}

	private void loadParamsVec3f(List<Element> elems) {
		for(Element elem : elems) {
			float x = Float.parseFloat(elem.getAttribute("x"));
			float y = Float.parseFloat(elem.getAttribute("y"));
			float z = Float.parseFloat(elem.getAttribute("z"));
			paramsVec3f.put(elem.getAttribute("name"), new Vec3f(x, y, z));
		}
	}

	private void loadParamsVec4f(List<Element> elems) {
		for(Element elem : elems) {
			float x = Float.parseFloat(elem.getAttribute("x"));
			float y = Float.parseFloat(elem.getAttribute("y"));
			float z = Float.parseFloat(elem.getAttribute("z"));
			float w = Float.parseFloat(elem.getAttribute("w"));
			paramsVec4f.put(elem.getAttribute("name"), new Vec4f(x, y, z, w));
		}
	}

	private void loadParamsTex(List<Element> elems) {
		for(Element elem : elems) {
			TextureI tex = Main.getRenderer().loadTexture(elem.getAttribute("path"));

			tex.setFilter( Boolean.parseBoolean(elem.getAttribute("filtered")) );

			paramsTex.put(elem.getAttribute("name"), tex);
		}
	}

}
