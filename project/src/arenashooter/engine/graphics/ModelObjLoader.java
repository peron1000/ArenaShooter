package arenashooter.engine.graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

final class ModelObjLoader {
	
	private ModelObjLoader() {};
	
	static ModelsData loadObj( String path ) {
		List<Model> models = new ArrayList<>(1);
		List<String> shaders = new ArrayList<>(1);
		List<Texture> textures = new ArrayList<>(1);
		
		Map<String, String> shaderOverrides = ModelsData.getShadersOverrides(path);
		
		try {
			InputStream in = new FileInputStream(new File(path));

			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inReader);

			List<Vec3f> vertices = new ArrayList<Vec3f>();
			List<Vec2f> texCoords = new ArrayList<Vec2f>();
			texCoords.add(new Vec2f(0, 0)); //Add default value in case a vertex doesn't have UVs
			List<Vec3f> normals = new ArrayList<Vec3f>();
			List<Vec3f> generatedNormals = new ArrayList<Vec3f>();
			List<int[]> points = new ArrayList<int[]>(); //(VertexID, TexCoordsID, NormalsID)[]
			Map<int[], Integer> pointsID = new HashMap<int[], Integer>(); //To find a point's ID from its value
			List<int[]> faces = new ArrayList<int[]>(); //(Point1, Point2, Point3)[]
			
			//Materials
			HashMap<String, Texture> materials = new HashMap<>();
			String currentMat = "";

			//Read data
			String line = "";
			String[] lineParts;

			while( (line = reader.readLine()) != null ) {
				lineParts = line.split(" ");
				if( lineParts.length < 1 ) break;
				switch(lineParts[0]) {
				case "mtllib": //Materials
					String matLibPath = path.substring(0, path.lastIndexOf('/'))+'/'+lineParts[1];
					materials = loadMaterials(matLibPath);
					break;
				case "o": //Begin object
					if( !faces.isEmpty() ) { //Only create a new model if last isn't empty
						models.add(finishModel(vertices, texCoords, normals, generatedNormals, points, faces));
						textures.add( materials.getOrDefault(currentMat, Texture.default_tex) );
						shaders.add( shaderOverrides.getOrDefault(currentMat, ModelsData.default_shader) );
						
						//Clear faces
						faces.clear();
					}
					break;
				case "usemtl": //End current model and change current material
					if( !faces.isEmpty() ) { //Only create a new model if last isn't empty
						models.add(finishModel(vertices, texCoords, normals, generatedNormals, points, faces));
						textures.add( materials.getOrDefault(currentMat, Texture.default_tex) );
						shaders.add( shaderOverrides.getOrDefault(currentMat, ModelsData.default_shader) );
						
						//Clear faces
						faces.clear();
					}
					currentMat = lineParts[1];
					break;
				case "v": //Vertex
					vertices.add(new Vec3f(Float.valueOf(lineParts[1]), -1*Float.valueOf(lineParts[2]), Float.valueOf(lineParts[3])));
					break;
				case "vt": //Texture coordinates (flipped because openGL)
					texCoords.add(new Vec2f(Float.valueOf(lineParts[1]), 1-Float.valueOf(lineParts[2])));
					break;
				case "vn": //Normal
					normals.add(new Vec3f(Float.valueOf(lineParts[1]), -1*Float.valueOf(lineParts[2]), Float.valueOf(lineParts[3])));
					break;
				case "f":
					//Load the first vertex of the face
					int[] v1 = readPoint(lineParts[1]);

					if( !pointsID.containsKey(v1) ) {
						pointsID.put(v1, points.size());
						points.add(v1);
					}

					//Triangulate the face
					for( int i = 1; i < lineParts.length-2; i++ ) { 
						int[] v2 = readPoint(lineParts[i+1]);
						int[] v3 = readPoint(lineParts[i+2]);

						if( !pointsID.containsKey(v2) ) {
							pointsID.put(v2, points.size());
							points.add(v2);
						}
						if( !pointsID.containsKey(v3) ) {
							pointsID.put(v3, points.size());
							points.add(v3);
						}

						//Generate normals
						if( v1[2] == 0 || v2[2] == 0 || v3[2] == 0 ) {
							Vec3f normal = genNormal(vertices.get(v1[0]), vertices.get(v2[0]), vertices.get(v3[0]));

							generatedNormals.add(normal);

							if(v1[2] == 0)
								v1[2] = -generatedNormals.size()+1;
							if(v2[2] == 0)
								v2[2] = -generatedNormals.size()+1;
							if(v3[2] == 0)
								v3[2] = -generatedNormals.size()+1;
						}

						//Add triangle
						faces.add( new int[] { pointsID.get(v1), pointsID.get(v2), pointsID.get(v3) } );
					}
					break;
				default:
					break;
				}
			}

			models.add(finishModel(vertices, texCoords, normals, generatedNormals, points, faces));
			textures.add( materials.getOrDefault(currentMat, Texture.default_tex) );
			shaders.add( shaderOverrides.getOrDefault(currentMat, ModelsData.default_shader) );

			reader.close();
			inReader.close();
			in.close();
		} catch (Exception e) {
			Window.log.error("Cannot load model: "+path);
		}
		
		//If textures are missing, replace them with default texture
		if(textures.size()<models.size())
			Window.log.error("Missing textures for "+path);
		for( int i=textures.size()-1; i<models.size(); i++ )
			textures.add(Texture.default_tex);
		//If shaders are missing, replace them with default shader
		if(shaders.size()<models.size())
			Window.log.error("Missing shaders for "+path);
		for( int i=shaders.size()-1; i<models.size(); i++ )
			shaders.add(ModelsData.default_shader);
		
		Model[] modelsArray = models.toArray(new Model[models.size()]);
		Material[] materialsArray = new Material[textures.size()];
		
		for(int i=0; i<materialsArray.length; i++) {
			materialsArray[i] = new Material(shaders.get(i));
			materialsArray[i].setParamTex("baseColor", textures.get(i));
		}
		
		return new ModelsData(modelsArray, materialsArray);
	}
	
	/**
	 * Read a vertex's data for OBJ loading
	 * @param data vertex data as "position/uv/normal", uv and normal are optional
	 * @return (VertexID, TexCoordsID, NormalsID)
	 */
	private static int[] readPoint( String data ) {
		String[] parts = data.split("/");
		
		//Position has a -1 because obj starts counting at 1
		if( parts.length == 1 ) //Only position is present
			return new int[]{Integer.valueOf(parts[0])-1, 0, 0};
		
		int coords = 0;
		if( !parts[1].isEmpty() )
			coords = Integer.parseInt(parts[1]);
		
		if( parts.length == 2 ) //Only position and uvs are present, mark normals to be generated (value 0)
			return new int[]{Integer.valueOf(parts[0])-1, coords, 0};
		
		return new int[]{Integer.valueOf(parts[0])-1, coords, Integer.valueOf(parts[2])-1};
	}
	
	/**
	 * Generate a normal for a face, input vertices are ordered counter-clockwise
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return ( (v2-v1) x (v3-v1) ) normalized
	 */
	public static Vec3f genNormal( Vec3f v1, Vec3f v2, Vec3f v3 ) {
		Vec3f res = Vec3f.cross( Vec3f.subtract(v2, v1), Vec3f.subtract(v3, v1) );
		return Vec3f.normalize(res);
	}
	
	private static Model finishModel( List<Vec3f> vertices, List<Vec2f> texCoords, List<Vec3f> normals, List<Vec3f> generatedNormals, List<int[]> points, List<int[]> faces ) {
		//Convert data to arrays
		List<Float> dataList = new ArrayList<Float>();
		for(int[] point : points) {
			dataList.add(vertices.get(point[0]).x);
			dataList.add(vertices.get(point[0]).y);
			dataList.add(vertices.get(point[0]).z);
			dataList.add((float) texCoords.get(point[1]).x);
			dataList.add((float) texCoords.get(point[1]).y);
			
			if( point[2] <= 0 ) { //Negative values are generated normals
				dataList.add(generatedNormals.get(-point[2]).x);
				dataList.add(generatedNormals.get(-point[2]).y);
				dataList.add(generatedNormals.get(-point[2]).z);
			} else {
				dataList.add(normals.get(point[2]).x);
				dataList.add(normals.get(point[2]).y);
				dataList.add(normals.get(point[2]).z);
			}
		}
		List<Integer> idsList = new ArrayList<Integer>();
		for(int[] face : faces) {
			idsList.add(face[0]);
			idsList.add(face[1]);
			idsList.add(face[2]);
		}
		
		//Create model
		float[] data = new float[dataList.size()];
		for( int i=0; i<data.length; i++ ) {
			data[i] = dataList.get(i);
		}
		int[] ids = new int[idsList.size()];
		for( int i=0; i<ids.length; i++ )
			ids[i] = idsList.get(i);
		
		return new Model(data, ids);
	}

	private static HashMap<String, Texture> loadMaterials(String path) {
		HashMap<String, Texture> res = new HashMap<>();
		
		try {
			InputStream in = new FileInputStream(new File(path));

			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inReader);

			//Read data
			String line = "";
			String[] lineParts;
			
			String currentMat = "";

			while( (line = reader.readLine()) != null ) {
				lineParts = line.split(" ");
				if( lineParts.length < 1 ) break;
				switch(lineParts[0]) {
				case "newmtl": //Begin a new material
					currentMat = lineParts[1];
					break;
				case "map_Kd": //Base color texture path
					String texPath = lineParts[1];
					
					int dataIndex = texPath.indexOf("/data/");
					if(dataIndex == -1) //Relative path
						texPath = path.substring(0, path.lastIndexOf('/'))+'/'+texPath;
					else
						texPath = texPath.substring(dataIndex+1);
						
					Texture tex = Texture.loadTexture(texPath);
					//Disable texture filtering
					tex.setFilter(false);
					res.put(currentMat, tex);
					break;
				}
			}
			
			reader.close();
			in.close();
		} catch(Exception e) {
			Window.log.error("Error loading materials");
			e.printStackTrace();
		}

		return res;
	}
}
