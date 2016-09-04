package StoneEngine.Components;

import StoneEngine.Core.RenderingEngine;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.BaseLight;

public class DirectionalLight extends GameComponent
{
	private BaseLight base;
	private Vector3f direction;
	
	public DirectionalLight(BaseLight base, Vector3f direction)
	{
		this.base = base;
		this.direction = direction.normalize();
	}

	//Temporary solution!
	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine)
	{
		renderingEngine.addDirectionalLight(this);
	}

	public BaseLight getBase() {
		return base;
	}

	public void setBase(BaseLight base) {
		this.base = base;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction.normalize();
	}
}
