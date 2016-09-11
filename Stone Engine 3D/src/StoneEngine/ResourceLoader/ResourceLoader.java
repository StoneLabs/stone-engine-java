package StoneEngine.ResourceLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import org.newdawn.slick.opengl.TextureLoader;

import StoneEngine.ResourceLoader.Models.IndexedModel;
import StoneEngine.ResourceLoader.Models.Mesh;
import StoneEngine.ResourceLoader.Models.MeshResource;
import StoneEngine.ResourceLoader.Models.ResourceModel;
import StoneEngine.Rendering.Texture;
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
	
	private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
	public static <U extends ResourceModel> Mesh loadMesh(String fileName, Class<U> T)
	{
		Debug.Log("Loading MESH: " + fileName);
		
		if (loadedModels.containsKey(fileName))
			return new Mesh(loadedModels.get(fileName));
				
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		ResourceModel model;
		try 
		{ 
			model = T.newInstance(); 
			
			String defaultExtension = model.DefaultExtension();
			
			if (!ext.equals(defaultExtension))
				Debug.Warning("Trying to load ." + defaultExtension + " file from ." + ext + " file!");
			
			model.Load(new FileReader("./res/" + fileName));
			
			IndexedModel indexedModel = model.ToIndexedModel();
			Mesh result = indexedModel.ToMesh();
			
			result.getBuffers().setFileReference(fileName);
			loadedModels.put(fileName, result.getBuffers());
			
			return result;
		} 
		catch (InstantiationException | IllegalAccessException e) { Debug.Error("The ResourceLoader has no access to the given class!"); } 
		catch (FileNotFoundException e) { Debug.Error("The requested file could not be found!"); }
		
		return null;
	}
	
	public static HashMap<String, MeshResource> getKnownModels()
	{
		return loadedModels;
	}
}
