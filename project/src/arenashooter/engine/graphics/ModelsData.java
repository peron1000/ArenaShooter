package arenashooter.engine.graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Contains data about a model: models and textures
 *
 */
public class ModelsData {
	static final String default_shader = "data/shaders/mesh_simple";
	
	public Model[] models;
	public Material[] materials;
	
	ModelsData(Model[] models, Material[] materials) {
		this.models = models;
		this.materials = materials;
	}

	public static ModelsData loadModel(String path) {
		if(path.endsWith(".obj"))
			return ModelObjLoader.loadObj(path);
		else {
			System.err.println("Error: "+path+" is not a wavefront obj file!");
			return null;
		}
	}
	
	/**
	 * Find and parse the shader overrides file attached to a mesh file
	 * @param path mesh file (*.obj)
	 * @return a map of material names linked to their shaders path
	 */
	static HashMap<String, String> getShadersOverrides(String path) {
		HashMap<String, String> res = new HashMap<>();
		
		File file = new File( path.substring(0, path.lastIndexOf('.'))+".shaders" );
		
		if(!file.exists()) return res;
		
		try( InputStream in = new FileInputStream(file) ) {
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inReader);
			
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
			
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
