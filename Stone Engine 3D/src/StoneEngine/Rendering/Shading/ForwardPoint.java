package StoneEngine.Rendering.Shading;

import StoneEngine.ResourceLoader.ResourceLoader;
import StoneEngine.Math.Matrix4f;
import StoneEngine.Rendering.Material;
import StoneEngine.Rendering.RenderingEngine;
import StoneEngine.Scene.Transform;
import StoneEngine.Scene.Lighting.BaseLight;
import StoneEngine.Scene.Lighting.PointLight;

public class ForwardPoint extends Shader
{
	private static ForwardPoint instance = new ForwardPoint();
	
	public static ForwardPoint getInstance()
	{
		return instance;
	}
	
	private ForwardPoint()
	{
		super();
				
		addVertexShader(ResourceLoader.loadShader("shaders\\forward-point.vs"));
		addFragmentShader(ResourceLoader.loadShader("shaders\\forward-point.fs"));
		
		setAttribLocation("position", 0);
		setAttribLocation("texCoord", 1);
		setAttribLocation("normal"	, 2);
		
		compileShader();
				
		addUniform("model");
		addUniform("MVP");
		
		addUniform("specularIntensity");
		addUniform("specularExponent");
		addUniform("eyePos");

		addUniform("pointLight.base.color");
		addUniform("pointLight.base.intensity");
		addUniform("pointLight.atten.constant");
		addUniform("pointLight.atten.linear");
		addUniform("pointLight.atten.exponent");
		addUniform("pointLight.position");
		addUniform("pointLight.range");
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
		
		setUniformPointLight("pointLight", (PointLight)renderingEngine.getActiveLight());
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
}
