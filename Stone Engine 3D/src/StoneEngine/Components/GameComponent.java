package StoneEngine.Components;

import StoneEngine.Core.Transform;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shading.Shader;

public abstract class GameComponent
{
	public void input(Transform transform, float delta)
	{
		
	}
	
	public void update(Transform transform, float delta)
	{
		
	}
	
	public void render(Transform transform, Shader shader)
	{
		
	}
	
	//Temporary solution!
	public void addToRenderingEngine(RenderingEngine renderingEngine)
	{
		
	}
}
