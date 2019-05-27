package arenashooter.engine.xmlReaders.writer;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class VecteurXml extends AbstractElement{
	private Document doc;
	private ArrayList<Element> vecteurs = new ArrayList<>();
	
	public VecteurXml(Document doc, Element elementParent) {
		super(doc, elementParent);
		this.doc = doc;
	}
	
	public void addVecteur(String use , float...fs) {
		Element vec = doc.createElement("vecteur");
		elementParent.appendChild(vec);
		if(use != "") {
			vec.setAttribute("use", use);
		} else {
			//vec.setAttribute("", null);
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
		vecteurs.add(vec);
	}
	
	public ArrayList<Element> getVecteurs(){
		return vecteurs;
	}

	@Override
	public Element getElement() {
		// TODO Auto-generated method stub
		return null;
	}
}
