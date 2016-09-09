package StoneEngine.Scene.Lighting;

import StoneEngine.Math.Vector3f;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Rendering.Shading.ForwardPoint;
import StoneEngine.Scene.GameComponent;
import StoneLabs.sutil.Debug;

public class PointLight extends BaseLight
{
	private static final int COLOR_DEPTH = 256;
	
	private float range;
	
	private float constant;
	private float linear;
	private float exponent;
	
	public PointLight(Vector3f color, float intensity, float constant, float linear, float exponent) //todo: calc range auto.
	{				
		this(color, intensity, constant, linear, exponent, 
				calcRange(color, intensity, constant, linear, exponent));
	}
	public PointLight(Vector3f color, float intensity, float constant, float linear, float exponent, float range) //todo: calc range auto.
	{
		super(color, intensity);
		
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
		
		this.range = range;
		
		setShader(ForwardPoint.getInstance());
	}

	private static float calcRange(Vector3f color, float intensity, float constant, float linear, float exponent)
	{

		float a = exponent;
		float b = linear;
		float c = constant - COLOR_DEPTH * intensity * color.max();
		
		return (float)((-b + Math.sqrt(b*b - 4 * a * c)) / (2*a));
	}
	
	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getLinear() {
		return linear;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public float getExponent() {
		return exponent;
	}

	public void setExponent(float exponent) {
		this.exponent = exponent;
	}
}
