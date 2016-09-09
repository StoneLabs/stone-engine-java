package StoneEngine.Scene.Lighting;

import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shading.ForwardSpot;
import StoneEngine.Scene.GameComponent;

public class SpotLight extends PointLight
{
	private Vector3f direction;
	private float cutoff;

	public SpotLight(Vector3f color, float intensity, float constant, float linear, float exponent, Vector3f direction, float cutoff)
	{
		super(color, intensity, constant, linear, exponent);
		
		this.direction = direction.normalize();
		this.cutoff = cutoff;
		
		this.setShader(ForwardSpot.getInstance());
		
	}
	public SpotLight(Vector3f color, float intensity, float constant, float linear, float exponent, float range, Vector3f direction, float cutoff)
	{
		super(color, intensity, constant, linear, exponent, range);
		
		this.direction = direction.normalize();
		this.cutoff = cutoff;
		
		this.setShader(ForwardSpot.getInstance());
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	public void setDirection(Vector3f direction) {
		this.direction = direction.normalize();
	}
	public float getCutoff() {
		return cutoff;
	}
	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
}
