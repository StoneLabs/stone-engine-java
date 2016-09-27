package StoneEngine.Rendering;

import java.util.HashMap;

import StoneEngine.Math.Vector3f;
import StoneEngine.ResourceLoader.Textures.Texture;
import StoneLabs.sutil.Debug;

public class Material
{
	private HashMap<String, Texture> textureHashMap;
	private HashMap<String, Vector3f> vector3fHashMap;
	private HashMap<String, Float> floatHashMap;

	public Material()
	{
		textureHashMap = new HashMap<String, Texture>();
		vector3fHashMap = new HashMap<String, Vector3f>();
		floatHashMap = new HashMap<String, Float>();
	}
	
	public void addTexture	(String name, Texture 	r) {  textureHashMap.put(name, r); }
	public void addVector3f	(String name, Vector3f 	r) { vector3fHashMap.put(name, r); }
	public void addFloat	(String name, Float 	r) { 	floatHashMap.put(name, r); }
	
	public Texture getTexture(String name) 	
	{ 
		Texture ret = textureHashMap.get(name);
		if (ret == null)
			Debug.Error(name + ": Texture not found in renderingEngine!");
		return ret;
	}
	public Vector3f getVector3f (String name)
	{ 
		Vector3f ret = vector3fHashMap.get(name);
		if (ret == null)
			Debug.Error(name + ": Vector3f not found in renderingEngine!");
		return ret;
	}
	public Float getFloat(String name)
	{ 
		Float ret = floatHashMap.get(name);
		if (ret == null)
			Debug.Error(name + ": Float not found in renderingEngine!");
		return ret;
	}
}
