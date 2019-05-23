package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractElement {
	protected Document doc;
	protected Element elementParent;
	public AbstractElement(Document doc, Element elementParent) {
		this.doc = doc;
		this.elementParent = elementParent;
	}
	
	public abstract Element getElement();
}
