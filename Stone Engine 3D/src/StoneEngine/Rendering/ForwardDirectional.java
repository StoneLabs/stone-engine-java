package StoneEngine.Rendering;

import StoneEngine.Core.ResourceLoader;
import StoneEngine.Core.Transform;
import StoneEngine.Math.Matrix4f;

public class ForwardDirectional extends Shader
{
	private static ForwardDirectional instance = new ForwardDirectional();
	
	public static ForwardDirectional getInstance()
	{
		return instance;
	}
	
	private ForwardDirectional()
	{
		super();
				
		addVertexShader(ResourceLoader.loadShader("forward-directional.vs"));
		addFragmentShader(ResourceLoader.loadShader("forward-directional.fs"));
		
		setAttribLocation("position", 0);
		setAttribLocation("texCoord", 1);
		setAttribLocation("normal"	, 2);
		
		compileShader();
				
		addUniform("model");
		addUniform("MVP");
		
		addUniform("specularIntensity");
		addUniform("specularExponent");
		addUniform("eyePos");
		
		addUniform("directionalLight.base.color");
		addUniform("directionalLight.base.intensity");
		addUniform("directionalLight.direction");
	}
	
	public void updateUniforms(Transform transform, Material material)
	{
		Matrix4f worldMatrix = transform.getTanformation();
		Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().mul(worldMatrix);
		
		material.getTexture().bind();
				
		setUniform("model", worldMatrix); //transform
		setUniform("MVP", projectedMatrix); //Model view perspective
		
		setUniformf("specularIntensity", material.getSpecularIntensity());
		setUniformf("specularExponent", material.getSpecularExponent());
		
		setUniform("eyePos", getRenderingEngine().getMainCamera().getPos());
		setUniform("directionalLight", getRenderingEngine().getDirectionalLight());
	}
	
	public void setUniform(String uniformName, BaseLight baseLight)
	{
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}
	
	public void setUniform(String uniformName, DirectionalLight directionalLight)
	{
		setUniform(uniformName + ".base", directionalLight.getBase());
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}
}
