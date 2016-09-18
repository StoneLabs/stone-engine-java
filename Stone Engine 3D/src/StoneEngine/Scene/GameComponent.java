package StoneEngine.Scene;

import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shader;

public abstract class GameComponent
{
	private GameObject parent;
		
	public void update(float delta)
	{
		
	}
	
	public void render(Shader shader, RenderingEngine renderingEngine)
	{
		
	}

	/*default*/ void setParent(GameObject parent)
	{
		this.parent = parent;
	}
	public GameObject getGameObject()
	{
		return parent;
	}
	
	//Temporary solution!
	public void addToRenderingEngine(RenderingEngine renderingEngine)
	{
		
	}
}
