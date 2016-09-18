package StoneEngine.Scene.Lighting;

import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Shader;
import StoneEngine.ResourceLoader.ResourceLoader;

public class DirectionalLight extends BaseLight
{
	private static Shader DirectionalShader = ResourceLoader.loadShader("shaders/forward-directional.shader");
	
	public DirectionalLight(Vector3f color, float intensity)
	{
		super(color, intensity);
		
		setShader(DirectionalShader);
	}
}
