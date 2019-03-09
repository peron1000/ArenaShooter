package arenashooter.engine.graphics;

/**
 * Contains data about a model: models and textures
 *
 */
public class ModelsData {
	public Model[] models;
	public Texture[] textures;
	
	ModelsData(Model[] models, Texture[] textures) {
		this.models = models;
		this.textures = textures;
	}

	public static ModelsData loadModel(String path) {
		if(path.endsWith(".obj"))
			return ModelObjLoader.loadObj(path);
		else {
			System.err.println("Error: "+path+" is not a wavefront obj file!");
			return null;
		}
	}
}
