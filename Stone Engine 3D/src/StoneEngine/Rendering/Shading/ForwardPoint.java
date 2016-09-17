package StoneEngine.Rendering.Shading;

import StoneEngine.ResourceLoader.ResourceLoader;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Scene.Transform;
import StoneEngine.Scene.Lighting.BaseLight;
import StoneEngine.Scene.Lighting.PointLight;

public class ForwardPoint extends Shader
{
	private static ForwardPoint instance = new ForwardPoint();
	
	public static ForwardPoint getInstance()
	{
		return instance;
	}
	
	private ForwardPoint()
	{
		super(ResourceLoader.loadShader("shaders\\forward-point.vs"), ResourceLoader.loadShader("shaders\\forward-point.fs"));
	}
}
