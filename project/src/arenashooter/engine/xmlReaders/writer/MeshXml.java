package arenashooter.engine.xmlReaders.writer;

import java.util.ArrayList;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MeshXml extends AbstractElement {
	private Document doc;
	private ArrayList<Element> meshs = new ArrayList<>();
	
	public MeshXml(Document doc, Element elementParent) {
		super(doc, elementParent);
		this.doc = doc;
	}
	
	public void addMesh(String...path) {
		Element mesh = doc.createElement("mesh");
		elementParent.appendChild(mesh);
		for (int i = 0; i < path.length; i++) {
			Attr src = doc.createAttribute("src");
			src.setValue(path[i]);
			mesh.setAttributeNode(src);
		}
		meshs.add(mesh);
	}
	@Override
	public Element getElement() {
		return (Element) this;
	}
}
