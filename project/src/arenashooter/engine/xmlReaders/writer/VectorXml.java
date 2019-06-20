package arenashooter.engine.xmlReaders.writer;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class VectorXml extends AbstractElement{
	private Document doc;
	private List<Element> vectors = new ArrayList<>();
	
	public VectorXml(Document doc, Element elementParent) {
		super(doc, elementParent);
		this.doc = doc;
	}
	
	public void addVector(String use , float...fs) {
		Element vec = doc.createElement("vector");
		elementParent.appendChild(vec);
		if(use != "") {
			vec.setAttribute("use", use);
		}
		for (int i = 0; i < fs.length; i++) {
			String f = Float.toString(fs[i]);
			switch (i) {
			case 0:
				vec.setAttribute("x", f);
				break;
			case 1:
				vec.setAttribute("y", f);
				break;
			case 2:
				vec.setAttribute("z", f);
				break;
			case 3:
				vec.setAttribute("w", f);
				break;
			default:
				break;
			}
		}
		vectors.add(vec);
	}
	
	public List<Element> getVectors(){
		return vectors;
	}

	@Override
	public Element getElement() {
		// TODO Auto-generated method stub
		return null;
	}
}
