package StoneEngine.Rendering.Shading;

import StoneEngine.ResourceLoader.ResourceLoader;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Scene.Transform;
import StoneEngine.Scene.Lighting.BaseLight;
import StoneEngine.Scene.Lighting.PointLight;
import StoneEngine.Scene.Lighting.SpotLight;

public class ForwardSpot extends Shader
{
	private static ForwardSpot instance = new ForwardSpot();
	
	public static ForwardSpot getInstance()
	{
		return instance;
	}
	
	private ForwardSpot()
	{
		super(ResourceLoader.loadShader("shaders\\forward-spot.vs"), ResourceLoader.loadShader("shaders\\forward-spot.fs"));
	}
	
	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine)
	{
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);
		
		material.getTexture("diffuse").bind();
				
		setUniform("model", worldMatrix); //transform
		setUniform("MVP", projectedMatrix); //Model view perspective

		setUniformf("specularIntensity", material.getFloat("specularIntensity"));
		setUniformf("specularExponent", material.getFloat("specularExponent"));
		
		setUniform("eyePos", renderingEngine.getMainCamera().getGameObject().getTransformedTranslation());
		
		setUniformSpotLight("spotLight", (SpotLight)renderingEngine.getActiveLight());
	}
	
	public void setUniformBaseLight(String uniformName, BaseLight baseLight)
	{
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	public void setUniformPointLight(String uniformName, PointLight pointLight)
	{
		setUniformBaseLight(uniformName + ".base", pointLight);
		setUniform(uniformName + ".position", pointLight.getGameObject().getTransformedTranslation());
		setUniformf(uniformName + ".range", pointLight.getRange());
		setUniformf(uniformName + ".atten.linear", pointLight.getLinear());
		setUniformf(uniformName + ".atten.constant", pointLight.getConstant());
		setUniformf(uniformName + ".atten.exponent", pointLight.getExponent());
	}
	public void setUniformSpotLight(String uniformName, SpotLight spotLight)
	{
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getGameObject().getTransformedRotation().getForward());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}

}
