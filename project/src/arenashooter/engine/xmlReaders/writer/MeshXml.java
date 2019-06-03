package arenashooter.engine.xmlReaders.writer;

import java.util.ArrayList;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MeshXml extends AbstractElement {
	Element mesh;
	private ArrayList<Element> meshs = new ArrayList<>();

	// Vecteur 1 position du mesh
	float xPosition = 0f;
	float yPosition = 0f;
	float zPosition = 0f;

	// Vecteur 1 rotation du mesh
	float xRotation = 0f;
	float yRotation = 0f;
	float zRotation = 0f;
	float wRotation = 0f;

	// Vecteur 1 position du mesh
	float xScale = 0f;
	float yScale = 0f;
	float zScale = 0f;

	public MeshXml(Document doc, Element elementParent) {
		super(doc, elementParent);
		this.doc = doc;

		mesh = doc.createElement("mesh");
		elementParent.appendChild(mesh);

		VecteurXml vecteurMesh = new VecteurXml(doc, mesh);
		vecteurMesh.addVecteur("position", xPosition, yPosition, zPosition);
		vecteurMesh.addVecteur("rotation", xRotation, yRotation, zRotation, wRotation);
		vecteurMesh.addVecteur("scale", xScale, yScale, zScale);
		for (Element e : vecteurMesh.getVecteurs()) {
			mesh.appendChild(e);
		}
	}

	public void addMesh(String path) {
		Element mesh = doc.createElement("mesh");
		elementParent.appendChild(mesh);
		Attr src = doc.createAttribute("src");
		src.setValue(path);
		mesh.setAttributeNode(src);
	}

	@Override
	public Element getElement() {
		return (Element) this;
	}

	/**
	 * @param xPosition the xPosition to set
	 */
	public void setxPosition(float xPosition) {
		this.xPosition = xPosition;
	}

	/**
	 * @param yPosition the yPosition to set
	 */
	public void setyPosition(float yPosition) {
		this.yPosition = yPosition;
	}

	/**
	 * @param zPosition the zPosition to set
	 */
	public void setzPosition(float zPosition) {
		this.zPosition = zPosition;
	}

	/**
	 * @param xRotation the xRotation to set
	 */
	public void setxRotation(float xRotation) {
		this.xRotation = xRotation;
	}

	/**
	 * @param yRotation the yRotation to set
	 */
	public void setyRotation(float yRotation) {
		this.yRotation = yRotation;
	}

	/**
	 * @param zRotation the zRotation to set
	 */
	public void setzRotation(float zRotation) {
		this.zRotation = zRotation;
	}

	/**
	 * @param wRotation the wRotation to set
	 */
	public void setwRotation(float wRotation) {
		this.wRotation = wRotation;
	}

	/**
	 * @param xScale the xScale to set
	 */
	public void setxScale(float xScale) {
		this.xScale = xScale;
	}

	/**
	 * @param yScale the yScale to set
	 */
	public void setyScale(float yScale) {
		this.yScale = yScale;
	}

	/**
	 * @param zScale the zScale to set
	 */
	public void setzScale(float zScale) {
		this.zScale = zScale;
	}
}
