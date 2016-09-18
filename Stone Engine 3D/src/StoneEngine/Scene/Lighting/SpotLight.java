package StoneEngine.Scene.Lighting;

import StoneEngine.Math.Attenuation;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Shader;
import StoneEngine.ResourceLoader.ResourceLoader;

public class SpotLight extends PointLight
{
	private static Shader SpotShader = ResourceLoader.loadShader("shaders/forward-spot.shader");
	
	private float cutoff;

	public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float cutoff)
	{
		super(color, intensity, attenuation);
		
		this.cutoff = cutoff;
		
		this.setShader(SpotShader);
		
	}
	public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float range, float cutoff)
	{
		super(color, intensity, attenuation, range);
		
		this.cutoff = cutoff;
		
		this.setShader(SpotShader);
	}
	
	public float getCutoff() {
		return cutoff;
	}
	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
}
