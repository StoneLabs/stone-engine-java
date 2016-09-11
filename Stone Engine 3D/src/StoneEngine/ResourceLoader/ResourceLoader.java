package StoneEngine.ResourceLoader;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import StoneEngine.Core.Util;
import StoneEngine.ResourceLoader.Models.IndexedModel;
import StoneEngine.ResourceLoader.Models.Mesh;
import StoneEngine.ResourceLoader.Models.MeshResource;
import StoneEngine.ResourceLoader.Models.ResourceModel;
import StoneEngine.ResourceLoader.Textures.Texture;
import StoneEngine.ResourceLoader.Textures.TextureResource;
import StoneLabs.sutil.Debug;

public class ResourceLoader
{
	private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
	public static Texture loadTexture(String fileName)
	{
		Debug.Log("Loading TEXTURE: " + fileName);
		
		if (loadedTextures.containsKey(fileName))
			return new Texture(loadedTextures.get(fileName));
		
		Debug.Log("Loading file...");
		
//		String[] splitArray = fileName.split("\\.");
//		String ext = splitArray[splitArray.length - 1];
		
		try
		{
			BufferedImage image = ImageIO.read(new File("./res/" + fileName));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
			
			ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 4); // 4 bytes -> rgba
			
			boolean hasAlpha = image.getColorModel().hasAlpha();
			
			for (int y = 0; y < image.getHeight(); y++)
				for (int x = 0; x < image.getWidth(); x++)
				{
					int pixel = pixels[y*image.getWidth() + x];
					
					byte r = (byte)(pixel >> 16 & 0xFF);
					byte g = (byte)(pixel >> 8  & 0xFF); //0xAARRGGBB
					byte b = (byte)(pixel >> 0  & 0xFF); //Shift is level 6 LR precedence, bitwise AND is level 9 LR
					byte a = (byte)(hasAlpha?pixel>>24&0xFF:0xFF);
					
					buffer.put(r); buffer.put(g); buffer.put(b); buffer.put(a); 
				}
			
			buffer.flip();
			
			int id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			TextureResource resource = new TextureResource(id);
			resource.setFileReference(fileName);
			
			loadedTextures.put(fileName, resource);
			
			Texture result = new Texture(resource);
			resource.removeReference(); // Remove ResourceLoader as reference
			
			return result;
		}
		catch (Exception ex) { Debug.Error("Unknown error during load process: " + ex.getMessage()); }
		
		return null;
	}
	public static HashMap<String, TextureResource> getKnownTextures()
	{
		return loadedTextures;
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
	public static <U extends ResourceModel> Mesh loadMesh(String fileName, Class<U> analyser)
	{
		Debug.Log("Loading MESH: " + fileName);
		
		if (loadedModels.containsKey(fileName))
			return new Mesh(loadedModels.get(fileName));
				
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		ResourceModel model;
		try 
		{ 
			model = analyser.newInstance(); 
			
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
