package StoneEngine.Rendering.Shading;

import StoneEngine.ResourceLoader.ResourceLoader;

public class ForwardAmbient extends Shader
{
	private static ForwardAmbient instance = new ForwardAmbient();
	
	public static ForwardAmbient getInstance()
	{
		return instance;
	}
	
	private ForwardAmbient()
	{
		super(ResourceLoader.loadShader("shaders\\forward-ambient.vs"), ResourceLoader.loadShader("shaders\\forward-ambient.fs"));
	}
}
