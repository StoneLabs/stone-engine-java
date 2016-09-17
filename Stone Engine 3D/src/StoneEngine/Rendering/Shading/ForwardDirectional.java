package StoneEngine.Rendering.Shading;

import StoneEngine.ResourceLoader.ResourceLoader;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Scene.Transform;
import StoneEngine.Scene.Lighting.BaseLight;
import StoneEngine.Scene.Lighting.DirectionalLight;

public class ForwardDirectional extends Shader
{
	private static ForwardDirectional instance = new ForwardDirectional();
	
	public static ForwardDirectional getInstance()
	{
		return instance;
	}
	
	private ForwardDirectional()
	{
		super(ResourceLoader.loadShader("shaders\\forward-directional.vs"), ResourceLoader.loadShader("shaders\\forward-directional.fs"));
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
		setUniformDirectionalLight("directionalLight", (DirectionalLight)renderingEngine.getActiveLight());
	}
	
	public void setUniformBaseLight(String uniformName, BaseLight baseLight)
	{
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	
	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight)
	{
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getGameObject().getTransformedRotation().getForward());
	}
}
