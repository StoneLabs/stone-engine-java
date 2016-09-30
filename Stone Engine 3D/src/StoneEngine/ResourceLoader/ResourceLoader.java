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
import StoneEngine.Rendering.Shader;
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


	public static Shader loadShader(String fileName)
	{
		Debug.Log("Loading SHADER GROUP: " + fileName);
		
		BufferedReader shaderReader = null;
		String vertexFile = null, fragmentFile = null, geometryFile = null;
		
		try
		{
			shaderReader = new BufferedReader(new FileReader("./res/" + fileName));
			String line;
			while ((line = shaderReader.readLine()) != null)
			{
				if (line.charAt(0) == '#') continue;
				String[] parts = line.split("\\s+");
				
				if (parts.length == 2)
				{
					switch (parts[0])
					{
						case "vs":
							vertexFile = parts[1];
							break;
						case "fs":
							fragmentFile = parts[1];
							break;
						case "gs":
							geometryFile = parts[1];
							break;
					}
				}
			}
		}
		catch (Exception e) { e.printStackTrace(); }
				
		return loadShader(vertexFile, fragmentFile, geometryFile);
	}
	public static Shader loadShader(String vertexFile, String fragmentFile, String geometryFile)
	{
		String vertexShader = null;		if (vertexFile != null)		vertexShader = readShader(vertexFile);
		String fragmentShader = null; 	if (fragmentFile != null)	fragmentShader = readShader(fragmentFile);
		String geometryShader = null; 	if (geometryFile != null)	geometryShader = readShader(geometryFile);
		
		Shader shader = new Shader();
		
		if (vertexShader != null)	shader.addVertexShader(vertexShader);
		if (fragmentShader != null)	shader.addFragmentShader(fragmentShader);
		if (geometryShader != null)	shader.addGeometryShader(geometryShader);
				
		shader.compileShader();
		
		if (vertexShader != null)	shader.addAllUniforms(vertexShader);
		if (fragmentShader != null)	shader.addAllUniforms(fragmentShader);
		if (geometryShader != null)	shader.addAllUniforms(geometryShader);
		
		return shader;
	}
	
	private static String readShader(String fileName)
	{
		Debug.Log("Loading SHADER: " + fileName);
		StringBuilder shaderSource = new StringBuilder();
		
		BufferedReader shaderReader = null;
		
		final String INCLIDE_DIRECTIVE = "#include";
				
		try
		{
			shaderReader = new BufferedReader(new FileReader("./res/" + fileName));
			String line;
			while ((line = shaderReader.readLine()) != null)
			{
				if  (line.startsWith(INCLIDE_DIRECTIVE))
				{
					String subFile = line.substring(INCLIDE_DIRECTIVE.length() + 2, line.length() - 1);
					shaderSource.append(readShader(subFile)).append("\n");
				}
				else
					shaderSource.append(line).append("\n");
			}
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
			
			String defaultExtension = model.defaultExtension();
			
			if (!ext.equals(defaultExtension))
				Debug.Warning("Trying to load ." + defaultExtension + " file from ." + ext + " file!");
			
			model.load(new FileReader("./res/" + fileName));
			
			IndexedModel indexedModel = model.toIndexedModel();
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
