package StoneEngine.Scene.Lighting;

import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shader;
import StoneEngine.Scene.GameComponent;

public class BaseLight extends GameComponent
{
	private Vector3f color;
	private float intensity;
	private Shader shader;
	
	
	public BaseLight(Vector3f color, float intensity)
	{
		this.color = color;
		this.intensity = intensity;
	}
	
	//Temporary solution!
	@Override
	public void addToRenderingEngine(RenderingEngine renderingEngine)
	{
		renderingEngine.addLight(this);
	}

	public void setShader(Shader shader)
	{
		this.shader = shader;
	}
	public Shader getShader()
	{
		return this.shader;
	}
	
	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
