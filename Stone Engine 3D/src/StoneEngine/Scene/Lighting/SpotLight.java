package StoneEngine.Scene.Lighting;

import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shading.ForwardSpot;
import StoneEngine.Scene.GameComponent;

public class SpotLight extends PointLight
{
	private float cutoff;

	public SpotLight(Vector3f color, float intensity, float constant, float linear, float exponent, float cutoff)
	{
		super(color, intensity, constant, linear, exponent);
		
		this.cutoff = cutoff;
		
		this.setShader(ForwardSpot.getInstance());
		
	}
	public SpotLight(Vector3f color, float intensity, float constant, float linear, float exponent, float range, float cutoff)
	{
		super(color, intensity, constant, linear, exponent, range);
		
		this.cutoff = cutoff;
		
		this.setShader(ForwardSpot.getInstance());
	}
	
	public Vector3f getDirection() { //Remove?
		return getGameObject().getRotation().getForward();
	}
	public float getCutoff() {
		return cutoff;
	}
	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
}
