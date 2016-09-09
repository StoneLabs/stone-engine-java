package StoneEngine.Components.Lighting;

import StoneEngine.Components.GameComponent;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shading.ForwardSpot;

public class SpotLight extends PointLight
{
	private Vector3f direction;
	private float cutoff;
	
	public SpotLight(Vector3f color, float intensity, float constant, float linear, float exponent, Vector3f position, float range, Vector3f direction, float cutoff)
	{
		super(color, intensity, constant, linear, exponent, position, range);
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
