package StoneEngine.Scene.Lighting;

import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shading.ForwardDirectional;

public class DirectionalLight extends BaseLight
{
	public DirectionalLight(Vector3f color, float intensity)
	{
		super(color, intensity);
		
		setShader(ForwardDirectional.getInstance());
	}
}
