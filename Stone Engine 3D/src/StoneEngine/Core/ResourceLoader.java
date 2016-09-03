package StoneEngine.Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;

import org.newdawn.slick.opengl.TextureLoader;

import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Mesh;
import StoneEngine.Rendering.Texture;
import StoneEngine.Rendering.Vertex;
import StoneLabs.sutil.Debug;

public class ResourceLoader
{
	public static Texture loadTexture(String fileName)
	{
		Debug.Log("Loading TEXTURE: " + fileName);
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		try
		{
			return new Texture(
					TextureLoader.getTexture(
							ext, new FileInputStream(
									new File("./res/" + fileName)
									)
							).getTextureID()
					);
		}
		catch (Exception ex) { Debug.Error("Unknown error during lead process: " + ex.getMessage()); }
		
		return null;
	}
	
	public static String loadShader(String fileName)
	{
		Debug.Log("Loading SHADER: " + fileName);
		StringBuilder shaderSource = new StringBuilder();
		
		BufferedReader shaderReader = null;
		
		try
		{
			shaderReader = new BufferedReader(new FileReader("./res/" + fileName));
			String line;
			while ((line = shaderReader.readLine()) != null)
				shaderSource.append(line).append("\n");
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return shaderSource.toString();
	}
	
	public static Mesh loadMesh(String fileName)
	{
		Debug.Log("Loading MESH: " + fileName);
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		if (!ext.equals("obj"))
			Debug.Warning("Trying to load obj file from ." + ext + " file!");

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		

		BufferedReader meshReader = null;
		
		try
		{
			meshReader = new BufferedReader(new FileReader("./res/" + fileName));
			String line;
			while ((line = meshReader.readLine()) != null)
			{
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if (tokens.length == 0 || tokens[0].equals("#")) continue;
				if (tokens[0].equals("v"))
					vertices.add(new Vertex(new Vector3f(
							Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2]),
							Float.valueOf(tokens[3])
							)));
				else if (tokens[0].equals("f"))
				{
					indices.add(Integer.valueOf(tokens[1].split("/")[0]) - 1);
					indices.add(Integer.valueOf(tokens[2].split("/")[0]) - 1);
					indices.add(Integer.valueOf(tokens[3].split("/")[0]) - 1);
					
					//Triangulate obj (Change quads to tris)
					if (tokens.length > 4)
					{
						indices.add(Integer.valueOf(tokens[1].split("/")[0]) - 1);
						indices.add(Integer.valueOf(tokens[3].split("/")[0]) - 1);
						indices.add(Integer.valueOf(tokens[4].split("/")[0]) - 1);
					}
				}
			}
			
			meshReader.close();
			
			Mesh res = new Mesh();
			Vertex[] vertexData = vertices.toArray(new Vertex[vertices.size()]);
			Integer[] indexData =  indices.toArray(new Integer[indices.size()]);
			
//			Debug.Log("Loaded obj!");
//			Debug.Log("Loaded " + vertexData.length + " vertices!");
//			Debug.Log("Loaded " + indexData.length + " indices!");
//			Debug.Log("Making " + indexData.length/3 + " triangles!");
			
			res.addVertices(vertexData, Util.toIntArray(indexData), true);
						
			return res;
		}
		catch (Exception e) { e.printStackTrace(); }
		
		
		return null;
	}
}
