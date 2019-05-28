package arenashooter.engine.xmlReaders;

import org.w3c.dom.Element;

public class XmlVector {
	public double w = 0.0 ,x = 0.0, y = 0.0, z = 0.0;
	public String use = "";
	
	public XmlVector(Element element) {
		if(element.hasAttribute("x")) {
			x = Double.parseDouble(element.getAttribute("x"));
		}
		if(element.hasAttribute("y")) {
			y = Double.parseDouble(element.getAttribute("y"));
		}
		if(element.hasAttribute("w")) {
			w = Double.parseDouble(element.getAttribute("w"));
		}
		if(element.hasAttribute("z")) {
			z = Double.parseDouble(element.getAttribute("z"));
		}
		if(element.hasAttribute("use")) {
			use = element.getAttribute("use");
		}
	}
}
