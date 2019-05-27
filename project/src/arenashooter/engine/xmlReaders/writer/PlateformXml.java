package arenashooter.engine.xmlReaders.writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arenashooter.entities.Entity;

public class PlateformXml extends AbstractElement {

	private float xpos = 0f;
	private float ypos = 0f;
	private float xext = 0f;
	private float yext = 0f;
	
	public PlateformXml(Document doc, Element elementParent) {
		super(doc, elementParent);
		// Creation de la platform dans le xml
		Element plateforme = doc.createElement("plateform");
		elementParent.appendChild(plateforme);

		VecteurXml vecteurPlateforme = new VecteurXml(doc, plateforme);
		vecteurPlateforme.addVecteur("position", xpos , ypos);
		vecteurPlateforme.addVecteur("extent", xext , yext);
		for (Element e : vecteurPlateforme.getVecteurs()) {
			plateforme.appendChild(e);
		}
	}
	
	protected void loadPlatform(Entity parent, Element parentBalise) {
		
	}

	@Override
	public Element getElement() {
		return (Element) this;
	}

	/**
	 * @param xpos the xpos to set
	 */
	public void setXpos(float xpos) {
		this.xpos = xpos;
	}

	/**
	 * @param ypos the ypos to set
	 */
	public void setYpos(float ypos) {
		this.ypos = ypos;
	}

	/**
	 * @param xext the xext to set
	 */
	public void setXext(float xext) {
		this.xext = xext;
	}

	/**
	 * @param yext the yext to set
	 */
	public void setYext(float yext) {
		this.yext = yext;
	}
}
