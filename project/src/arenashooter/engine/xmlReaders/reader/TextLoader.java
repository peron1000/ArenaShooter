package arenashooter.engine.xmlReaders.reader;

import java.util.List;

import org.w3c.dom.Element;

import arenashooter.engine.graphics.fonts.Font;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.xmlReaders.XmlVector;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.TextSpatial;

public class TextLoader implements EntitiesLoader<TextSpatial> {

	public static final TextLoader textLoader = new TextLoader();

	private TextLoader() {
		// Only one instance
	}

	@Override
	public TextSpatial loadEntity(Element element, Entity parent) {
		// vectors
		List<Element> vectors = MapXmlReader.getListElementByName("vector", element);
		Vec3f position = new Vec3f();
		Vec3f scale = new Vec3f();
		int nbVec = 2;
		if (vectors.size() != nbVec) {
			MapXmlReader.log.error("Text element needs " + nbVec + " vectors");
		} else {
			for (Element vector : vectors) {
				XmlVector vec = MapXmlReader.loadVector(vector);
				switch (vec.use) {
				case "position":
					position = new Vec3f(vec.x, vec.y, vec.z);
					break;
				case "scale":
					scale = new Vec3f(vec.x, vec.y, vec.z);
					break;

				default:
					MapXmlReader.log.error("Invalid vector in Text element");
					break;
				}
			}
		}

		// Text
		String content, font;
		font = element.getAttribute("font");
		Font f = Font.loadFont(font);
		content = element.getAttribute("content");
		Text t = new Text(f, Text.TextAlignH.CENTER,Text.TextAlignV.CENTER , content);
		TextSpatial spatial = new TextSpatial(position, scale, t);
		if (element.hasAttribute("name")) {
			spatial.attachToParent(parent, element.getAttribute("name"));
		} else {
			spatial.attachToParent(parent, spatial.genName());
		}

		return spatial;
	}

}
