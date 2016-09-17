package StoneEngine.Rendering.Shading;

import StoneEngine.ResourceLoader.ResourceLoader;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Scene.Transform;
import StoneEngine.Scene.Lighting.BaseLight;
import StoneEngine.Scene.Lighting.DirectionalLight;

public class ForwardDirectional extends Shader
{
	private static ForwardDirectional instance = new ForwardDirectional();
	
	public static ForwardDirectional getInstance()
	{
		return instance;
	}
	
	private ForwardDirectional()
	{
		super(ResourceLoader.loadShader("shaders\\forward-directional.vs"), ResourceLoader.loadShader("shaders\\forward-directional.fs"));
	}
}
