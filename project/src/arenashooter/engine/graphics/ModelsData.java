package arenashooter.engine.graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.ContentManager;
import arenashooter.game.Main;

/**
 * Contains data about a model: models and textures
 *
 */
public class ModelsData {
	private static final Map<String, ModelsData> cache = new HashMap<>();
	
	static final String default_mat = "data/materials/mesh_simple.material";
	
	public Model[] models;
	public Material[] materials;
	
	ModelsData(Model[] models, Material[] materials) {
		this.models = models;
		this.materials = materials;
	}

	/**
	 * Load a model from disk or cache
	 * @param path
	 * @return data representing the model, or an empty object if it couldn't be loaded
	 */
	public static ModelsData loadModel(String path) {
		if(cache.containsKey(path)) //Model is cached
			return cache.get(path).clone();
		
		if(path.endsWith(".obj")) { //Model is an obj
			ModelsData res = ModelObjLoader.loadObj(path);
			cache.put(path, res);
			return res.clone();
		}
		
		//Unsupported format
		Main.getRenderer().getLogger().error(path+" is not a wavefront obj file");
		return new ModelsData(new Model[0], new Material[0]);
	}
	
	/**
	 * Find and parse the shader overrides file attached to a mesh file
	 * @param path mesh file (*.obj)
	 * @return a map of material names linked to their materials path
	 */
	static Map<String, String> getMaterialOverrides(String path) {
		Map<String, String> res = new HashMap<>();
		
		String materialsFilePath = path.substring(0, path.lastIndexOf('.'))+".materials";
		File file = new File( ContentManager.transformPath(materialsFilePath) );
		
		if(!file.exists()) return res;
		
		try( InputStream in = new FileInputStream(file);
				InputStreamReader inReader = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(inReader); ) {
			
			String line = "";
			boolean isMatName = true;
			String matName = "";

			while( (line = reader.readLine()) != null ) {
				if(isMatName)
					matName = line;
				else
					res.put(matName, line);
				
				isMatName = !isMatName;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * Creates a clone of this entry (same geometry data, cloned materials)
	 */
	@Override
	public ModelsData clone() {
		Material[] cloneMats = new Material[materials.length];
		for(int i=0; i<materials.length; i++)
			cloneMats[i] = materials[i].clone();
		
		return new ModelsData(models.clone(), cloneMats);
	}
}
