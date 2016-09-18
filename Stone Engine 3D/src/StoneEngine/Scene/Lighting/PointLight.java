package StoneEngine.Scene.Lighting;

import StoneEngine.Math.Attenuation;
import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.Shader;
import StoneEngine.ResourceLoader.ResourceLoader;

public class PointLight extends BaseLight
{
	private static Shader PointShader = ResourceLoader.loadShader("shaders/forward-point.shader");
	
	private static final int COLOR_DEPTH = 256;
	
	private float range;
	
	private Attenuation attenuation;
	
	public PointLight(Vector3f color, float intensity, Attenuation attenuation) //todo: calc range auto.
	{				
		this(color, intensity, attenuation, 
				calcRange(color, intensity, attenuation));
	}
	public PointLight(Vector3f color, float intensity, Attenuation attenuation, float range) //todo: calc range auto.
	{
		super(color, intensity);
		
		this.attenuation = attenuation;
		this.range = range;
		
		setShader(PointShader);
	}

	private static float calcRange(Vector3f color, float intensity, Attenuation attenuation)
	{

		float a = attenuation.getExponent();
		float b = attenuation.getLinear();
		float c = attenuation.getConstant() - COLOR_DEPTH * intensity * color.max();
		
		return (float)((-b + Math.sqrt(b*b - 4 * a * c)) / (2*a));
	}
	
	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public Attenuation getAttenuation() {
		return attenuation;
	}
}
